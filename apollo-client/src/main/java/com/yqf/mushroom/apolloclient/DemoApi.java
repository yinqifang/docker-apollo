package com.yqf.mushroom.apolloclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chris Yin
 * @date 2019/12/18
 */
@RestController
@Configuration
public class DemoApi {

    @Value("${db.url}")
    private String dbUrl;
    @Value("${db.username}")
    private String dbUserName;
    @Value(("${db.password}"))
    private String dbPassword;

    @GetMapping("/test")
    public String test() {
        System.out.println("dbUrl = " + dbUrl);
        System.out.println("dbUserName = " + dbUserName);
        System.out.println("dbPassword = " + dbPassword);
        return "dbUrl = " + dbUrl + ", dbUserName = " + dbUserName + ", dbPassword = " + dbPassword;
    }

}
