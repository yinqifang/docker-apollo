package com.yqf.mushroom.apolloclient;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableApolloConfig
@SpringBootApplication
public class ApolloClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApolloClientApplication.class, args);
    }

}
