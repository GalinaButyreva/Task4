package proj.task4;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface DataCheckable {
   // String checkFio(String name);
   // String checkType(String applType);
   // LocalDateTime checkDate(String date);
    List<Model> checkDateL(List<Model> mods) throws IOException;
    List<Model> checkTypeL(List<Model> mods);
    List<Model> checkFioL(List<Model> mods);

}
