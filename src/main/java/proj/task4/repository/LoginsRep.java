package proj.task4.repository;

import org.springframework.data.repository.CrudRepository;
import proj.task4.model.Logins;

// Объявление репозитория для сущности logins
public interface LoginsRep extends CrudRepository<Logins, Integer> {
}
