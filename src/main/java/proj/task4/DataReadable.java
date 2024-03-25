package proj.task4;



import java.io.IOException;
import java.util.List;


public interface DataReadable {
    //  Получить данные для записи в БД
    List<Model> readFromFiles(String strPath) throws IOException;
}
