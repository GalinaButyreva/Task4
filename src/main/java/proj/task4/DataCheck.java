package proj.task4;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Component
public class DataCheck implements DataCheckable {

    public  DateTimeFormatter getDateFormatter(){
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    }
    // Проверяем ФИО
    public String checkFio(String fio) {
        // В ФИО может быть несколько пробелов
        String[] strArr = fio.split(" ");
        // Итоговая строка
        StringBuilder strRes = new StringBuilder();
        for (String s : strArr) {
            String strAdd = s.trim();
            // Делаем первую букву заглавной
            if (!strAdd.isEmpty())
                strRes.append(" ").append(strAdd.substring(0, 1).toUpperCase()).append(strAdd.substring(1).toLowerCase());
        }
        // удаляем добавленный первый пробел
        return strRes.toString().trim();
    }

    // Проверяем тип приложения
    public String checkType(String applType) {
        if (applType == null)
            applType = "";
        if (!applType.equals("web") && !applType.equals("mobile"))
            return "other:" + applType;
        return applType;
    }

    // Проверяем дату
    public LocalDateTime checkDate(String date) {
        LocalDateTime dateloc = null;
        try {
            if (!(date == null)) {
                dateloc = LocalDateTime.parse(date, getDateFormatter()); //!!
            }
        } catch (Exception ex) {
            System.out.println("Ошибка форматирования даты");
        }
        return dateloc;
    }

    // Запись лога в файл
    private void writeFile(List<String> linesToWrite, String fullFileName) throws IOException {
        Path textFile = Paths.get(fullFileName); // "/Users/LearnJAVA/logFile"
        Files.write(textFile, linesToWrite, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }
    // Формируем строку для записи в файл
    private String getStrWrite(Model model) {
        return model.getFileName() + ": " +
                model.getUsername() + " " +
                model.getFio() + " ";
    }
    // Имя файла для логирования записей с незаполненной датой из application.properties
    // Сначала сделала из файла ресурсов - ппоказалось лучше через  application.properties
    @Value("${file.name.check.date}")
    private String fileNameLog;

    public String getFileNameLog() {
       return (fileNameLog == null) ? "LogBlankDate.log" : fileNameLog;
    }

    // ==================Промежуточные компоненты ==============
   // Проверяем дату (компонента подлежит логированию)
    @LogTransformation("LogComponentReader.log")
    @Override
    public List<Model> checkDateL(List<Model> mods) throws IOException {
        // Сформированные строки логирования
        List<String> linesToFile = new ArrayList<>();
        // Отфильтруем(удалим с пустой датой)
        List<Model> modelsOut = mods.stream().filter(x-> (!x.getDateInput().isEmpty())).collect(Collectors.toList());
        // Отфильтруем(оставим с пустой датой, для записи в файл)
        List<Model> linesToFileMod = mods.stream().filter(x-> (x.getDateInput().isEmpty())).toList();
        // Если нашли, что записать, запишем
        if (!linesToFileMod.isEmpty()) {
            for (Model mod : linesToFileMod){
                linesToFile.add(getStrWrite(mod));
             }

            writeFile(linesToFile, getFileNameLog() );
        }
        return modelsOut;
    }

    // Проверяем тип (компонента логируется )
    @LogTransformation("LogComponentReader.log")
    @Override
    public List<Model> checkTypeL(List<Model> mods) {
        mods.stream().peek(x-> x.setApplType(checkType(x.getApplType()))).toList();//.collect(Collectors.toList());
        return mods;
    }

    // Проверяем ФИО  (компонента логируется )
    @LogTransformation("LogComponentReader.log")
    @Override
    public List<Model> checkFioL(List<Model> mods) {
        mods.stream().peek(x-> x.setFio(checkFio(x.getFio()))).collect(Collectors.toList());
        return mods;
    }


}
