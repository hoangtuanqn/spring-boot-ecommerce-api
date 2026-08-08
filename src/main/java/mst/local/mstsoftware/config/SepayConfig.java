package mst.local.mstsoftware.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//@ConfigurationProperties(prefix = "sepay")
@Configuration
@Data
public class SepayConfig {
    //    @Value("${sepay.base-url}")
    private String baseUrl = "https://my.sepay.vn/userapi";

    @Value("${sepay.api-token}")
    private String apiToken;
}
