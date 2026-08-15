package com.salesmanager.core.upgrade;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SpringBootUpgradeTest {

    private static ApplicationContext applicationContext;

    @BeforeAll
    public static void init() {
        applicationContext = new AnnotationConfigApplicationContext();
    }

    @Test
    public void shouldRunCorrectSpringBootVersion() {
        assertThat(SpringBootVersion.getVersion()).isEqualTo("3.2.3");
    }

    @Test
    void shouldLoadApplicationContext() {
        assertThat(applicationContext).isNotNull();
        assertThat(applicationContext.containsBean("someCriticalBean")).isTrue();
    }

    @Test
    void deprecatedApiShouldNotExist() {
        try {
            Class.forName("com.salesmanager.core.oldapi.DeletedClass");
            assertThat(false).isTrue(); // fail the test if the class still exists
        } catch (ClassNotFoundException e) {
            assertThat(true).isTrue(); // pass the test if the class is not found
        }
    }

    @Test
    void shouldLoadNewConfigurationKeys() {
        String newConfigValue = applicationContext.getEnvironment().getProperty("new.config.key");
        assertThat(newConfigValue).isNotNull();
    }
}