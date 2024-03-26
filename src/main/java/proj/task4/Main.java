package proj.task4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import proj.task4.service.DataMaker;

import java.io.IOException;



@SpringBootApplication(scanBasePackages = "proj.task4")
public class Main {

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = SpringApplication.run(Main.class);
        DataMaker mk = ctx.getBean(DataMaker.class);
        mk.make();

    }

}
