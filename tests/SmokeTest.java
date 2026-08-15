package com.salesmanager.core.upgrade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebMvcTest
public class SpringBootUpgradeValidationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Value("${spring-boot-upgrade.target-version}")
    private String targetSpringBootVersion;

    @BeforeEach
    public void setUp() {
        // Load any necessary configuration for tests
    }

    @Test
    public void validateSpringBootVersion() {
        String currentVersion = SpringBootApplication.class.getPackage().getImplementationVersion();
        assertThat(currentVersion).isEqualTo(targetSpringBootVersion);
    }

    @Test
    public void testCriticalApplicationPathWorks() throws Exception {
        this.mockMvc.perform(get("/api/products"))
                    .andExpect(status().isOk());
    }

    @Test
    public void testDeprecatedAPIsReplaced() {
        // Verify that deprecated APIs are no longer present or their replacements are invoked correctly.
        // e.g., ensuring /api/v1 endpoint does not exist if deprecated.
        assertThat(applicationContext.containsBean("oldBean")).isFalse();
        assertThat(applicationContext.containsBean("newBean")).isTrue();
    }

    @Test
    public void testNewConfigurationKeysLoadWithoutErrors() {
        String newConfig = applicationContext.getEnvironment().getProperty("new.config.key");
        assertThat(newConfig).isNotNull();
    }
}