package proj.task4.service;

import proj.task4.model.Model;

import java.io.IOException;
import java.util.List;

public interface DataCheckDateable {
    List<Model> checkDateL(List<Model> mods) throws IOException;
}
