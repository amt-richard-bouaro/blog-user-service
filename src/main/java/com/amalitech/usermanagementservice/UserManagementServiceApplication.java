package com.amalitech.usermanagementservice;

import com.amalitech.usermanagementservice.config.properties.EmailPropertiesConfig;
import com.amalitech.usermanagementservice.config.properties.JwtPropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtPropertiesConfig.class, EmailPropertiesConfig.class})
public class UserManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementServiceApplication.class, args);
    }

}
