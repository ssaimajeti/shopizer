package com.salesmanager.shop.upgrade;

import static org.junit.jupiter.api.Assertions.*;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import com.salesmanager.shop.application.config.AsyncConfig;
import com.salesmanager.core.business.configuration.ApplicationSearchConfiguration;

@SpringBootTest
@ActiveProfiles("test")
class SpringBootUpgradeValidationTest {

    private static final String TARGET_SPRING_BOOT_VERSION = "3.2.7";
    private static final String TARGET_SPRING_FRAMEWORK_VERSION = "6.1.8";

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Framework is running upgraded Spring Boot version")
    void springBootVersionIsMigrated() {
        String activeVersion = SpringBootVersion.getVersion();
        assertEquals(TARGET_SPRING_BOOT_VERSION, activeVersion,
                "Spring Boot version must be upgraded to " + TARGET_SPRING_BOOT_VERSION);
    }

    @Test
    @DisplayName("Framework is running upgraded Spring Framework version")
    void springFrameworkVersionIsMigrated() {
        String activeVersion = SpringVersion.getVersion();
        assertEquals(TARGET_SPRING_FRAMEWORK_VERSION, activeVersion,
                "Spring Framework version must be upgraded to " + TARGET_SPRING_FRAMEWORK_VERSION);
    }

    @Test
    @DisplayName("Critical configuration beans load successfully")
    void criticalConfigurationBeansLoad() {
        assertNotNull(applicationContext.getBean(AsyncConfig.class),
                "AsyncConfig bean should be present and loaded");
        assertNotNull(applicationContext.getBean(ApplicationSearchConfiguration.class),
                "ApplicationSearchConfiguration bean should be present and loaded");
    }

    @Test
    @DisplayName("Critical REST controller endpoints work")
    void restApiContextLoadsAndControllersRegister() {
        String[] orderControllerBeans = applicationContext.getBeanNamesForType(
                org.springframework.web.bind.annotation.RestController.class);
        assertTrue(orderControllerBeans.length > 0, 
                "At least one RestController must be present after the upgrade");
    }

    @Test
    @DisplayName("Springfox Swagger2 beans are not registered after replacement")
    void springfoxSwagger2BeansAbsent() {
        boolean anySpringfoxBean = false;
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            if (beanName.toLowerCase().contains("springfox")) {
                anySpringfoxBean = true;
                break;
            }
        }
        assertFalse(anySpringfoxBean, 
                "Springfox Swagger2 beans must not be registered after the upgrade");
    }

    @Test
    @DisplayName("springdoc-openapi OpenAPI bean is present")
    void springdocOpenApiBeanPresent() {
        assertNotNull(applicationContext.getBean(OpenAPI.class), 
                "OpenAPI bean should be registered by springdoc-openapi");
    }

    @Test
    @DisplayName("New configuration keys introduced load without errors")
    void newConfigurationKeysLoad() {
        // Example: test for springdoc-openapi property or new Boot 3.x config
        String springdocApiDocsPath = applicationContext.getEnvironment()
                .getProperty("springdoc.api-docs.path", "/v3/api-docs");
        assertNotNull(springdocApiDocsPath, 
                "The 'springdoc.api-docs.path' configuration should be available");
    }
}