package proj.task4;

import org.springframework.data.repository.CrudRepository;
// Объявление репозитория для сущности logins
public interface LoginsRep extends CrudRepository<Logins, Integer> {
}
