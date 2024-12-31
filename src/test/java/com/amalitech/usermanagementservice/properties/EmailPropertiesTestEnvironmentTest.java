package com.amalitech.usermanagementservice.properties;

import com.amalitech.usermanagementservice.TestEnvironmentTest;
import com.amalitech.usermanagementservice.config.properties.EmailPropertiesConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = EmailPropertiesTestEnvironmentTest.TestConfig.class)
@ActiveProfiles("test")
@Epic("Application Properties")
@Feature("Email Properties Configuration Test")
final class EmailPropertiesTestEnvironmentTest extends TestEnvironmentTest {

    private static final Logger logger = LoggerFactory.getLogger(EmailPropertiesTestEnvironmentTest.class);

    @Autowired
    private EmailPropertiesConfig emailPropertiesConfig;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }

    @Test
    @Story("Verify valid email properties binding")
    @Description("This test ensures that the email properties are correctly bound when valid values are provided.")
    void whenPropertiesAreValid_thenPropertiesAreBound() {
        assertEquals("test@example.com", emailPropertiesConfig.username());
    }

    @Test
    @Story("Validate email properties when invalid")
    @Description("This test ensures that email properties fail validation when invalid data is provided.")
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
