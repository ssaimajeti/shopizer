package com.salesmanager.shop.upgrade;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.salesmanager.shop.application.ShopApplication;
import com.salesmanager.shop.utils.LabelUtils;
import com.salesmanager.shop.constants.ApplicationConstants;

@SpringBootTest(classes = ShopApplication.class)
@ActiveProfiles("test")
public class SpringBootUpgradeTest {

    private static final String TARGET_VERSION = "3.1.3";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;

    @Autowired
    private BuildProperties buildProperties;

    @BeforeEach
    void setUp() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    void contextLoads() {
        assertThat(applicationContext.getBean(ShopApplication.class)).isNotNull();
    }

    @Test
    void verifySpringBootVersion() {
        assertThat(buildProperties.getVersion()).isEqualTo(TARGET_VERSION);
        assertThat(buildProperties.getArtifact()).isEqualTo("sm-shop");
    }

    @Test
    void testCriticalApplicationPath() {
        assertThat(applicationContext.getBean(LabelUtils.class)).isNotNull();
        assertThat(environment.getProperty(ApplicationConstants.POPULATE_TEST_DATA)).isNull();
    }

    @Test
    void verifyDeprecatedApis() {
        // Confirm deprecated security configuration does not exist
        assertThat(applicationContext.getBeansOfType(org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter.class))
                .isEmpty();

        // Ensure new security configuration works as expected
        assertThat(applicationContext.getBean(org.springframework.security.web.SecurityFilterChain.class)).isNotNull();
    }

    @Test
    void validateNewConfigurationKeys() {
        assertThat(environment.getProperty("management.endpoint.health.show-components")).isEqualTo("always");
        assertThat(environment.getProperty("spring.main.allow-bean-definition-overriding")).isEqualTo("true");
    }
}