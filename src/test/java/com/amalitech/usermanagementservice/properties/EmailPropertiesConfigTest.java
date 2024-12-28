package com.amalitech.usermanagementservice.properties;

import com.amalitech.usermanagementservice.config.properties.EmailPropertiesConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import jakarta.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = EmailPropertiesConfigTest.TestConfig.class)
@ActiveProfiles("test")
@Epic("Application Properties")
@Feature("Email Properties Configuration Test")
class EmailPropertiesConfigTest {

    private final Logger logger = LoggerFactory.getLogger(EmailPropertiesConfigTest.class);

    private Validator validator;

    @Autowired
    private Environment environment;

    @Autowired
    private EmailPropertiesConfig emailPropertiesConfig;

    @BeforeEach
    void setUp() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory();){
            validator = factory.getValidator();
        }catch (Exception e){
            logger.debug(e.getMessage());
        }
    }

    @Test
    @Story("Verify active profiles in the application")
    @Description("This test verifies that the active profile is correctly set to 'test' during the test environment setup.")
    void testActiveProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        assertArrayEquals(new String[]{"test"}, activeProfiles);
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

