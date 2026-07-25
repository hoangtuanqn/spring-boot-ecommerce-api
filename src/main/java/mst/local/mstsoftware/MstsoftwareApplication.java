package mst.local.mstsoftware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
// thêm cái này vào để trả json khi pagination ổn định hơn
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class MstsoftwareApplication {

    public static void main(String[] args) {
        SpringApplication.run(MstsoftwareApplication.class, args);
    }

}
