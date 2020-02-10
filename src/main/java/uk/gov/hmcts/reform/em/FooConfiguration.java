package uk.gov.hmcts.reform.em;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class FooConfiguration {

    @Bean
    @Scope("prototype")
    public OkHttpClient client() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxyout.reform.hmcts.net", 8080)))
                .build();

        return okHttpClient;
    }
}
