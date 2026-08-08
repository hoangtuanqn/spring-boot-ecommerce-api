package mst.local.mstsoftware.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientConfig {
    @Bean
    public SepayApiClient sepayApiClient(SepayConfig config) {
        RestClient restClient = RestClient.builder()
                .baseUrl(config.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + config.getApiToken())
                .build();
        return HttpServiceProxyFactory.builderFor(
                        RestClientAdapter.create(restClient)
                ).build()
                .createClient(SepayApiClient.class);
    }
}
