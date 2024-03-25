package proj.task4;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;



// исп-ся testcontainers библиотека, чтобы работать с instance БД Postgres в Docker контейнере
// с тестом было не просто Долго пришлось разбираться, чтобы  не падал
//  Given-when-then попробовала
//@Testcontainers
@SpringBootTest
public class DbWriterTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.name", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    DataReader dataReader;
    @Autowired
    DataWriter dataWriter;
    @Autowired
    DataCheck dataCheck;

    @Autowired
    UsersRep usersRep;


    @BeforeEach
    void setUp() {
        usersRep.deleteAll();
    }


    // Сравним данные прочитанные и записанные в БД
    boolean compareDataWrite(List<Model> md, List<Users> users){
        List<Model> mdDbLst = new ArrayList<>();
        for (Users u: users) {
            for (Logins lg : u.getLogins()){
                Model modDb = new Model();
                modDb.setUsername(u.getUsername());
                modDb.setFio(u.getFio());
                modDb.setDateInput(lg.getAccess_date().format(dataCheck.getDateFormatter()));
                modDb.setApplType(lg.getApplication());
                mdDbLst.add(modDb);
            }
        }
        if (!(mdDbLst.size() == md.size()))
            return false;
        for (Model m1: mdDbLst) {
            String m1Str = m1.getUsername() + m1.getFio() + m1.getDateInput() + m1.getApplType();
            boolean isFind = false;
            for (Model m2 : md) {
                String m2Str = m2.getUsername() + m2.getFio() + m2.getDateInput() + m2.getApplType();
                if (m1Str.equals(m2Str))
                    isFind = true;
            }
            if (!isFind)
                return false;
        }
        return true;

    }


// Тестирование наличия зависимостей и всей цепочки реализации приложения , а также взаимодействия с БД
   @Test
    void TestDb() throws IOException {
        // given
       Assertions.assertNotNull(dataReader);
       Assertions.assertNotNull(dataCheck);
       Assertions.assertNotNull(dataWriter);

       //  Прочитаем данные when
       List<Model>  mods = dataReader.readFiles();

       then(!mods.isEmpty()).isTrue();

      // Выполним проверки
       mods = dataCheck.checkTypeL(mods);
       mods = dataCheck.checkFioL(mods);
       mods = dataCheck.checkDateL(mods);
        // then запишем в БД
       dataWriter.writeDb(mods);

       // Прочитаем из БД , чтобы сравнить с тем, что записали
       List<Users> users = (List<Users>) usersRep.findAll();

       // then Сравним то, что записали с тем что записывали
       then(compareDataWrite(mods, users)).isTrue();
       // Выборочно проверим несколько записей(Найдем по Фио и Логину в БД)
       for (int i = 0; i < 20; i++) {
           //given
           int randomIdx = (int) (Math.random() * mods.size());
           Model mdTest = mods.get(randomIdx);
           then(usersRep.findByUsernameAndFio(mdTest.getUsername(), mdTest.getFio())).isNotEmpty();
       }

    }
}
