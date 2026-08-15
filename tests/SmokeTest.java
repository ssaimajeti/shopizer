import static org.junit.jupiter.api.Assertions.*;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.core.MainResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class ElasticsearchUpgradeValidationTests {

    private static final String TARGET_ELASTICSEARCH_VERSION = "8.2.0";

    @Autowired
    private RestHighLevelClient highLevelClient;

    @Autowired
    private ApplicationContext context;

    @BeforeAll
    static void setUp() {
        // Set up any required pre-test configurations, if needed
    }

    @Test
    void testElasticsearchVersion() throws Exception {
        MainResponse response = highLevelClient.info(RequestOptions.DEFAULT);
        String version = response.getVersion().getNumber();
        assertEquals(TARGET_ELASTICSEARCH_VERSION, version, "Elasticsearch version should be " + TARGET_ELASTICSEARCH_VERSION);
    }

    @Test
    void testApplicationSearchConfiguration() {
        ApplicationSearchConfiguration config = context.getBean(ApplicationSearchConfiguration.class);
        assertNotNull(config, "ApplicationSearchConfiguration should be loaded and not null");
        assertEquals("expectedClusterName", config.getClusterName(), "Cluster name should match the expected value");
        assertEquals("expectedHost", config.getHost(), "Host should match the expected value");
    }

    @Test
    void testSearchFunctionality() {
        // Assuming SearchServiceImpl is a service class that handles searches
        SearchServiceImpl searchService = context.getBean(SearchServiceImpl.class);
        assertNotNull(searchService, "SearchServiceImpl should be loaded and not null");

        // Perform a basic search operation to verify functionality
        List<SearchResult> results = searchService.search("test query");
        assertNotNull(results, "Search results should not be null");
        assertFalse(results.isEmpty(), "Search results should not be empty");
    }

    @Test
    void testNoDeprecatedApis() {
        try {
            ApplicationSearchConfiguration.class.getMethod("deprecatedMethod");
            fail("ApplicationSearchConfiguration should not have deprecatedMethod");
        } catch (NoSuchMethodException expected) {
            // Expected exception as deprecatedMethod should no longer exist
        }

        try {
            ApplicationSearchConfiguration.class.getMethod("newMethod");
            // If the new configuration method exists, we need to confirm functioning
            ApplicationSearchConfiguration config = context.getBean(ApplicationSearchConfiguration.class);
            String result = config.newMethod();
            assertNotNull(result, "newMethod() should work properly");
        } catch (NoSuchMethodException e) {
            fail("ApplicationSearchConfiguration should have newMethod", e);
        }
    }
}
```

Note: In this test file, I've included import statements for components assumed to exist based on the provided context, such as `RestHighLevelClient`, `ApplicationSearchConfiguration`, `SearchServiceImpl`, and `SearchResult`. Adjust these according to your actual codebase and spring configurations.