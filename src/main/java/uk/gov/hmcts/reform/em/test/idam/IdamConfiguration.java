package uk.gov.hmcts.reform.em.test.idam;

import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.IdamTestApi;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
@ConditionalOnProperty("idam.api.url")
@ComponentScan(basePackages = "uk.gov.hmcts.reform.idam.client")
@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform.em.test.idam", "uk.gov.hmcts.reform.idam.client"})
public class IdamConfiguration {

    @Bean
    IdamHelper idamHelper(IdamClient idamClient, IdamTestApi idamTestApi, DeleteUserApi deleteUserApi) {
        return new IdamHelper(idamClient, idamTestApi, deleteUserApi);
    }

    @Bean
    @Scope("prototype")
    public OkHttpClient client() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxyout.reform.hmcts.net", 8080)))
                .build();

        return okHttpClient;
    }
}
