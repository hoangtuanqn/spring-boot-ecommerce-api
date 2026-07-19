package mst.local.mstsoftware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MstsoftwareApplication {

    public static void main(String[] args) {
        SpringApplication.run(MstsoftwareApplication.class, args);
    }

}
