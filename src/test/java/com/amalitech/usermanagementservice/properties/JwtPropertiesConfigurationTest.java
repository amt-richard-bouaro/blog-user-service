package com.amalitech.usermanagementservice.properties;

import com.amalitech.usermanagementservice.TestEnvironmentTest;
import com.amalitech.usermanagementservice.config.properties.JwtPropertiesConfig;
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

@SpringBootTest(classes = JwtPropertiesConfigurationTest.TestConfig.class)
@ActiveProfiles("test")
@Epic("Application Properties")
@Feature("JWT Properties Configuration Tests")
final class JwtPropertiesConfigurationTest extends TestEnvironmentTest {

    private static final Logger logger = LoggerFactory.getLogger(JwtPropertiesConfigurationTest.class);

    @Autowired
    private JwtPropertiesConfig jwtPropertiesConfig;

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
    @Story("Verify valid JWT properties binding")
    @Description("This test ensures that JWT properties are correctly bound when valid properties are provided.")
    void whenPropertiesAreValid_thenPropertiesAreBound() {
        assertEquals("secret", jwtPropertiesConfig.tokenSecretKey());
        assertEquals(864000, jwtPropertiesConfig.tokenExpiration());
    }

    @Test
    @Story("Validate JWT properties when invalid")
    @Description("This test ensures that JWT properties fail validation when invalid data is provided.")
    void whenPropertiesAreInvalid_thenValidationFails() {
        JwtPropertiesConfig config = new JwtPropertiesConfig("", 32823L);

        Set<ConstraintViolation<JwtPropertiesConfig>> violations = validator.validate(config);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Jwt token secret key cannot be blank")));
    }

    @EnableConfigurationProperties(JwtPropertiesConfig.class)
    static class TestConfig {
        // Ensures JwtPropertiesConfig is registered as a bean
    }
}
