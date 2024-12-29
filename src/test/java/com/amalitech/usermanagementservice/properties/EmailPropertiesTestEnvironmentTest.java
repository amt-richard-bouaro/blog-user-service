package com.amalitech.usermanagementservice.properties;


import com.amalitech.usermanagementservice.ConfigTest;
import com.amalitech.usermanagementservice.config.properties.EmailPropertiesConfig;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import jakarta.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = EmailPropertiesConfigTest.TestConfig.class)
@ActiveProfiles("test")
@Epic("Application Properties")
@Feature("Email Properties Configuration Test")
final class EmailPropertiesConfigTest extends ConfigTest {

    private static final Logger logger = LoggerFactory.getLogger(EmailPropertiesConfigTest.class);

    @Autowired
    private EmailPropertiesConfig emailPropertiesConfig;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory();){
            validator = factory.getValidator();
        }catch (Exception e){
            logger.debug(e.getMessage());
        }
    }

    @Test
    void whenPropertiesAreValid_thenPropertiesAreBound() {
        assertEquals("test@example.com", emailPropertiesConfig.username());
    }

    @Test
    void whenPropertiesAreInvalid_thenValidationFails() {
        EmailPropertiesConfig config = new EmailPropertiesConfig("");

        Set<ConstraintViolation<EmailPropertiesConfig>> violations = validator.validate(config);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Mail username cannot be blank")));
    }

    @EnableConfigurationProperties(EmailPropertiesConfig.class)
    static class TestConfig {
        // Ensures EmailPropertiesConfig is registered as a bean
    }
}

