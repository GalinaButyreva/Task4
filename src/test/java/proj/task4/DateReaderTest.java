package proj.task4;

import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import proj.task4.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateReaderTest {

    @BeforeEach
    void setup() {

    }
   @Test
    @Description("Тестирование перечня файлов в папках")
    void readFiles() throws IOException {
        DataReader dataReader = new DataReader();
        List<Model> models = dataReader.readFiles();

       Assertions.assertEquals(7, models.size());
    }



    @Test
    @Description("Тестирование фио")
    void DataCheckFio() throws IOException {
        DataCheck dataCheck = new DataCheck();
        Assertions.assertEquals("Иванов Иван Иванович" , dataCheck.checkFio("иванов    иван    иванович"));
    }
    @Test
    @Description("Тестирование типа")
    void DataCheckType() throws IOException {
        DataCheck dataCheck = new DataCheck();
        Assertions.assertEquals("mobile" , dataCheck.checkType("mobile"));
        Assertions.assertEquals("web" , dataCheck.checkType("web"));
        Assertions.assertEquals("other:something" , dataCheck.checkType("something"));
        Assertions.assertEquals("other:" , dataCheck.checkType(null));
    }

    @Test
    @Description("Тестирование преобразования даты")
    void DataCheckDate() throws IOException {
        DataCheck  dataCheck = new DataCheck();
        LocalDateTime date = dataCheck.checkDate("07.04.2023 12:00:00");
        Assertions.assertEquals(12 , date.getHour());
        Assertions.assertEquals(0 , date.getMinute());
        Assertions.assertEquals(0 , date.getSecond());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Assertions.assertEquals("07.04.2023" , date.format(dateTimeFormatter));

        date = dataCheck.checkDate("07.04.23 12:00:00");
        Assertions.assertEquals(null , date);


    }


    @Test
    @Description("Тестирование проверки файлов")
    void checkFiles() throws IOException {
        DataReader dataReader = new DataReader();
        List<Model> models = dataReader.readFiles();
        DataCheck dataCheck = new DataCheck();

        // Проверяем  компоненту проверки  ФИО
        models = dataCheck.checkFioL(models);
        for (Model md: models) {
            Assertions.assertEquals(md.getFio(), dataCheck.checkFio(md.getFio()));
        }

        // Проверяем  компоненту проверки  типа
        models = dataCheck.checkTypeL(models);

       for (Model md: models) {
           Assertions.assertEquals(md.getApplType().replace("other:","")
                                , dataCheck.checkType(md.getApplType()).replace("other:", ""));
       }

        //  Проверяем  компоненту проверки  даты ( с пустой записью не должно быть)
        models = dataCheck.checkDateL(models);
        Assertions.assertEquals(6, models.size());

    }
}
