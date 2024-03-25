package proj.task4;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// Объявление репозитория для сущности users
public interface UsersRep extends CrudRepository<Users, Integer> {
    public List<Users> findByUsernameAndFio(String username, String fio);
    public Users findFirstByUsernameAndFio(String username, String fio);
    /*@Query("select to_char('1') ,  u.username , u.fio , " +
            "to_char(l.access_date, 'dd.mm.yyyy HH:MM:SS') , " +
            "l.application from users u, logins l " +
            "where u.id = l.user_id")
    public List<Model> findAllWrites() ; */
}
