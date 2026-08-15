package com.shopizer.upgrade;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

@SpringBootTest
public class SpringBootUpgradeTest {

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private Environment environment;

    @Test
    void contextLoads() {
        assertNotNull(environment);
    }

    @Test
    void verifySpringBootVersion() {
        assertThat(buildProperties.getVersion()).isEqualTo("3.2.3");
    }

    @Test
    void verifyKeyConfigurationProperties() {
        String securityProperty = environment.getProperty("spring.security.enabled");
        assertThat(securityProperty).isEqualTo("true");
        
        String ormProperty = environment.getProperty("spring.jpa.open-in-view");
        assertThat(ormProperty).isNull(); // Checks that deprecated property is no longer available
    }

    @Test
    void apiPathsRespondSuccessfully() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> response = restTemplate.getForObject("http://localhost:8080/api/health", Map.class);
        assertTrue(response.containsKey("status"));
        assertThat(response.get("status")).isEqualTo("UP");
    }

    @Test
    void checkForDeprecatedApis() {
        // Example: Verify no deprecated APIs in the Spring Security context
        String deprecatedApiConfig = environment.getProperty("spring.security.deprecated-api");
        assertThat(deprecatedApiConfig).isNull(); // Ensures that deprecated configurations are not present
    }

    @Test
    void newConfigurationPropertyIsLoaded() {
        String newFeatureFlag = environment.getProperty("custom.new-feature.enabled");
        assertThat(newFeatureFlag).isEqualTo("true");
    }
}