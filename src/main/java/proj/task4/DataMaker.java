package proj.task4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


@Component
public class DataMaker {
   // Строки прочитанные
   List<Model> models;
   // Компонента чтения данных
   DataReader dataReader;
   //Компонента проверки данных
   DataCheck dataCheck;
   DataWriter dataWriter;

    @Autowired
   public DataMaker(DataReader dataReader, DataCheck dataCheck, DataWriter dateWriter) {

        this.dataReader = dataReader;
        this.dataCheck = dataCheck;
        this.dataWriter = dateWriter;

    }



    public  void make() throws IOException {
        // Получим путь(по условию задачи) для загрузки файлов(его можно разными способами задавать)
        String strPath = dataReader.getPath();
        // Читаем данные из файлов
        models = dataReader.readFromFiles(strPath);
        //models = dataReader.readFiles();

        // Выполняем проверку данных
        // Проверяем ФИО(меняем на первые заглавные буквы)
        models = dataCheck.checkFioL(models);


        // Проверяем тип меняем на "other: ", если не  web, mobile
        models = dataCheck.checkTypeL(models);

        // Проверяем дату Если пустая, записываем в файл
        models = dataCheck.checkDateL(models);


       //    System.out.println("=========Запись в БД =========================");
        dataWriter.writeDb(models);

   }
}
