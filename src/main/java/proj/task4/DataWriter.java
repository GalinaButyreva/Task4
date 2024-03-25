package proj.task4;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Collections;

import java.util.List;


@Component
public class DataWriter implements DataWriteable {
    @Autowired
    UsersRep usersRep;
    @Autowired
    LoginsRep loginsRep;

    @Override
    @Transactional
    public void writeDb(List<Model> mods) {
        // Отсортируем, чтобы можно было проще обработать при проходе
        mods.sort((o1, o2) -> (o1.getUsername() + o2.getFio()).compareTo(o2.getUsername() + o2.getFio()));
        String keyCmp = ""; //  для сравнения в цикле
        Users usr = new Users();
        // Начитываем для добавления в БД(можно было сделать чере saveAll - все начитать и все записать)
        // Проходим по отсортированному множеству данных
        for (Model m : mods) {
            if (!keyCmp.equals(m.getUsername() + m.getFio())){
                keyCmp = m.getUsername() + m.getFio();
                // записываем данные  user
                usr = new Users();
                usr.setUsername(m.getUsername());
                usr.setFio(m.getFio());
                usersRep.save(usr);
            }
            // Добавляем login
            Logins log = new Logins();
            log.setApplication(m.getApplType());
            log.setAccessDate(m.getDateInput());
            log.setUsers(usr);
            loginsRep.save(log);
        }

    }

}
