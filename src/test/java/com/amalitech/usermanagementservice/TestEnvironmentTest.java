package com.amalitech.usermanagementservice;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


@SpringBootTest(classes = TestEnvironmentTest.class)
@ActiveProfiles("test")
@Epic("API Testing")
@Feature("Test Environment Tests")
public class TestEnvironmentTest {

    private static final Logger logger = LoggerFactory.getLogger(TestEnvironmentTest.class);

    @Autowired
    private Environment environment;

    @Test
    @Story("Verify active profiles in the application")
    @Description("This test verifies that the active profile is correctly set to 'test' during the test environment setup.")
    void testActiveProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        logger.debug("Active profiles: {}", Arrays.asList(activeProfiles));
        assertArrayEquals(new String[]{"test"}, activeProfiles);
    }

}
