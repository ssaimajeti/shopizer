--- sm-shop/src/main/java/com/salesmanager/shop/application/config/ShopApplicationConfiguration.java ---
package com.salesmanager.shop.application.config;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.IMAGE_GIF;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_PNG;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.salesmanager.core.business.configuration.CoreApplicationConfiguration;
import com.salesmanager.shop.filter.CorsFilter;
import com.salesmanager.shop.filter.XssFilter;
import com.salesmanager.shop.utils.LabelUtils;

@Configuration
@ComponentScan({"com.salesmanager.shop"})
@ServletComponentScan
@Import({CoreApplicationConfiguration.class}) // import sm-core configurations
@EnableWebSecurity
public class ShopApplicationConfiguration implements WebMvcConfigurer {

  protected final Log logger = LogFactory.getLog(getClass());

  @EventListener(ApplicationReadyEvent.class)
  public void applicationReadyCode() {
    String workingDir = System.getProperty("user.dir");
    logger.info("Current working directory : " + workingDir);
  }

  @Bean
  public FilterRegistrationBean<XssFilter> croseSiteFilter(){
      FilterRegistrationBean<XssFilter> registrationBean 
        = new FilterRegistrationBean<>();
          
      registrationBean.setFilter(new XssFilter());
      registrationBean.addUrlPatterns("/shop/**");
      registrationBean.addUrlPatterns("/api/**");
      registrationBean.addUrlPatterns("/customer/**");
          
      return registrationBean;    
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(new MappingJackson2HttpMessageConverter());
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("shop");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // Changes the locale when a 'locale' request parameter is sent; e.g. /?locale=de
    registry.addInterceptor(localeChangeInterceptor());

    /**
    registry
        .addInterceptor(storeFilter())
        // store web front filter
        .addPathPatterns("/shop/**")
        // customer section filter
        .addPathPatterns("/customer/**");
     **/

    registry
        .addInterceptor(corsFilter())
        // public services cors filter
        .addPathPatterns("/services/**")
        // REST api
        .addPathPatterns("/api/**");

  }

  @Bean
  public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
    List<MediaType> supportedMediaTypes = Arrays.asList(IMAGE_JPEG, IMAGE_GIF, IMAGE_PNG, APPLICATION_OCTET_STREAM);

    ByteArrayHttpMessageConverter byteArrayHttpMessageConverter =
        new ByteArrayHttpMessageConverter();
    byteArrayHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
    return byteArrayHttpMessageConverter;
  }

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    return new LocaleChangeInterceptor();
  }

	/*
	 * @Bean public StoreFilter storeFilter() { return new StoreFilter(); }
	 */

  @Bean
  public CorsFilter corsFilter() {
    return new CorsFilter();
  }


  @Bean
  public SessionLocaleResolver localeResolver() {
    SessionLocaleResolver slr = new SessionLocaleResolver();
    slr.setDefaultLocale(Locale.getDefault());
    return slr;
  }

  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames(
        "classpath:bundles/shopizer",
        "classpath:bundles/messages",
        "classpath:bundles/shipping",
        "classpath:bundles/payment");

    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean
  public LabelUtils messages() {
    return new LabelUtils();
  }

}


--- sm-shop/src/test/resources/application-test.properties ---
#Spring boot configurations
#logging.config=log4j-shopizer.properties
server.port=8080


#Turn Statistics on
#spring.jpa.properties.hibernate.generate_statistics=true
#default database schema
spring.jpa.properties.hibernate.default_schema=SALESMANAGER
#logging.level.org.hibernate.stat=debug

# Enable logging to verify that HikariCP is used, the second entry is specific to HikariCP
#INFO | DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.com.zaxxer.hikari.HikariConfig=INFO
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=DEBUG

#when running from IDE (eclipse) or from springboot:run command
#server.contextPath=/shopizer

logging.level.org.springframework=INFO
logging.level.com.shopizer=INFO
logging.level.org.hibernate=INFO
logging.level.org.apache.http=ERROR

#logging.file=shopizer.log

#Default Spring white label error page
server.error.whitelabel.enabled=false

spring.servlet.multipart.max-file-size=4MB
spring.servlet.multipart.max-request-size=10MB


#hibernate 5
#spring.jpa.hibernate.use-new-id-generator-mappings=true

#Spring boot 2.X
spring.main.allow-bean-definition-overriding=true

#actuator
management.endpoints.enabled-by-default=false
management.endpoint.info.enabled=true
management.endpoint.health.enabled=true
management.endpoint.health.sensitive=false
management.endpoint.health.show-details=always

management.health.probes.enabled=true

management.endpoint.health.group.global.include=ping
management.security.enabled=false

--- sm-shop/src/main/java/com/salesmanager/shop/application/ShopApplication.java ---
package com.salesmanager.shop.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

}


--- sm-shop/src/main/java/com/salesmanager/shop/application/config/WebConfig.java ---
package com.salesmanager.shop.application.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	
    @Autowired
    private MerchantStoreArgumentResolver merchantStoreArgumentResolver;
    
    @Autowired
    private LanguageArgumentResolver languageArgumentResolver;

	
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(merchantStoreArgumentResolver);
        argumentResolvers.add(languageArgumentResolver);
    }
    

}


--- sm-shop/src/main/java/com/salesmanager/shop/application/config/MerchantStoreArgumentResolver.java ---
package com.salesmanager.shop.application.config;

import static com.salesmanager.core.business.constants.Constants.DEFAULT_STORE;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.shop.store.api.exception.UnauthorizedException;
import com.salesmanager.shop.store.controller.store.facade.StoreFacade;
import com.salesmanager.shop.store.controller.user.facade.UserFacade;

@Component
public class MerchantStoreArgumentResolver implements HandlerMethodArgumentResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(MerchantStoreArgumentResolver.class);
	public static final String REQUEST_PARAMATER_STORE = "store";

	@Autowired
	private StoreFacade storeFacade;

	@Autowired
	private UserFacade userFacade;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(MerchantStore.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String storeValue = Optional.ofNullable(webRequest.getParameter(REQUEST_PARAMATER_STORE))
				.filter(StringUtils::isNotBlank).orElse(DEFAULT_STORE);
		// todo get from cache
		MerchantStore storeModel = storeFacade.get(storeValue);

		HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

		// TODO Move to an api filter
		// authorize request
		boolean authorized = userFacade.authorizeStore(storeModel, httpServletRequest.getRequestURI());
		LOGGER.debug("is request authorized {} for {} and store {}", authorized, httpServletRequest.getRequestURI(),
				storeModel.getCode());
		if(!authorized){
			throw new UnauthorizedException("Cannot authorize user for store " + storeModel.getCode());
		}
		return storeModel;
	}
}


--- sm-shop/src/main/java/com/salesmanager/shop/application/config/ShopServletContextListener.java ---
package com.salesmanager.shop.application.config;

import com.salesmanager.core.business.modules.cms.impl.VendorCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ShopServletContextListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(ShopServletContextListener.class);
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		logger.info("===context init===");
		System.getenv().forEach((k, v) -> {
		  logger.debug(k + ":" + v);
		});
		Properties props = System.getProperties();
		props.forEach((k, v) -> {
		  logger.debug(k + ":" + v);
		});
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		logger.info("===context destroy===");
		VendorCacheManager cacheManager = VendorCacheManager.getInstance();
		cacheManager.getManager().stop();
	}
}


--- sm-shop/src/main/java/com/salesmanager/shop/constants/ApplicationConstants.java ---
package com.salesmanager.shop.constants;

public class ApplicationConstants {
	
	public final static String POPULATE_TEST_DATA = "POPULATE_TEST_DATA";
	public final static String TEST_DATA_LOADED = "TEST_DATA_LOADED";
	public final static String RECAPTCHA_URL = "shopizer.recapatcha_url";
	public final static String SHOP_SCHEME= "SHOP_SCHEME";
	public final static int MAX_DOWNLOAD_DAYS = 30;

}


--- sm-core/src/main/java/com/salesmanager/core/business/configuration/ApplicationSearchConfiguration.java ---
package com.salesmanager.core.business.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import modules.commons.search.configuration.Credentials;
import modules.commons.search.configuration.SearchHost;

/**
 * Reads search related properties required for search starter
 * @author carlsamson
 *
 */


@Configuration
@ConfigurationProperties(prefix = "search")
@PropertySource("classpath:shopizer-core.properties")
public class ApplicationSearchConfiguration {
	
    private String clusterName;
    private Credentials credentials;
    private List<SearchHost> host;
    private List<String> searchLanguages;
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	public List<SearchHost> getHost() {
		return host;
	}
	public void setHost(List<SearchHost> host) {
		this.host = host;
	}
	public List<String> getSearchLanguages() {
		return searchLanguages;
	}
	public void setSearchLanguages(List<String> searchLanguages) {
		this.searchLanguages = searchLanguages;
	}


}


--- sm-shop/src/main/java/com/salesmanager/shop/application/config/AsyncConfig.java ---
package com.salesmanager.shop.application.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

  private static final int EXECUTOR_SERVICE_NUMBER_THREADS = 5;

  @Override
  public Executor getAsyncExecutor() {
    return Executors.newFixedThreadPool(EXECUTOR_SERVICE_NUMBER_THREADS);
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }
}


--- sm-core/src/test/resources/application.properties ---
spring.jpa.hibernate.use-new-id-generator-mappings=false
#Spring boot 2
spring.main.allow-bean-definition-overriding= true

logging.level.org.springframework.web=ERROR
logging.level.org.springframework.security=ERROR
logging.level.com.shopizer=INFO
logging.level.org.hibernate=ERROR
logging.level.org.apache.http.headers=ERROR
logging.level.org.apache.http.client=ERROR
logging.level.org.apache.http.impl=ERROR
logging.level.org.apache.http.wire=ERROR
logging.level.com.salesmanager=ERROR

#default database schema
spring.jpa.properties.hibernate.default_schema=SALESMANAGER

shopizer.test=true

--- sm-shop/src/main/java/com/salesmanager/shop/application/config/MethodSecurityConfig.java ---
package com.salesmanager.shop.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
  prePostEnabled = true, 
  securedEnabled = true, 
  jsr250Enabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

}


--- sm-shop/src/main/java/com/salesmanager/shop/application/config/LocationImageConfig.java ---
package com.salesmanager.shop.application.config;

import org.drools.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.salesmanager.shop.utils.CloudFilePathUtils;
import com.salesmanager.shop.utils.ImageFilePath;
import com.salesmanager.shop.utils.LocalImageFilePathUtils;

@Configuration
public class LocationImageConfig {
	
  @Value("${config.cms.contentUrl}")
  private String contentUrl;
  
  @Value("${config.cms.method}")
  private String method;
  
  @Value("${config.cms.static.path}")
  private String staticPath;


  @Bean
  public ImageFilePath img() {
	  
	if(!StringUtils.isEmpty(method) && !method.equals("default")) {
	    CloudFilePathUtils cloudFilePathUtils = new CloudFilePathUtils();
	    cloudFilePathUtils.setBasePath(contentUrl);
	    cloudFilePathUtils.setContentUrlPath(contentUrl);
	    return cloudFilePathUtils;

	} else {

		
	    LocalImageFilePathUtils localImageFilePathUtils = new LocalImageFilePathUtils();
	    localImageFilePathUtils.setBasePath(staticPath);
	    localImageFilePathUtils.setContentUrlPath(contentUrl);
	    return localImageFilePathUtils;
	}
	  

  }
  
  
}


--- sm-shop/src/main/java/com/salesmanager/shop/application/config/MultipleEntryPointsSecurityConfig.java ---
package com.salesmanager.shop.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.salesmanager.shop.admin.security.UserAuthenticationSuccessHandler;
import com.salesmanager.shop.admin.security.WebUserServices;
import com.salesmanager.shop.store.controller.customer.facade.CustomerFacade;
import com.salesmanager.shop.store.security.AuthenticationTokenFilter;
import com.salesmanager.shop.store.security.ServicesAuthenticationSuccessHandler;
import com.salesmanager.shop.store.security.admin.JWTAdminAuthenticationProvider;
import com.salesmanager.shop.store.security.admin.JWTAdminServicesImpl;
import com.salesmanager.shop.store.security.customer.JWTCustomerAuthenticationProvider;
import com.salesmanager.shop.store.security.services.CredentialsService;
import com.salesmanager.shop.store.security.services.CredentialsServiceImpl;

/**
 * Main entry point for security - admin - customer - auth - private - services
 * 
 * @author dur9213
 *
 */
@Configuration
@EnableWebSecurity
public class MultipleEntryPointsSecurityConfig {

	private static final String API_VERSION = "/api/v*";

	@Bean
	public AuthenticationTokenFilter authenticationTokenFilter() {
		return new AuthenticationTokenFilter();
	}
	
	@Bean
	public CredentialsService credentialsService() {
		return new CredentialsServiceImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserAuthenticationSuccessHandler userAuthenticationSuccessHandler() {
		return new UserAuthenticationSuccessHandler();
	}

	@Bean
	public ServicesAuthenticationSuccessHandler servicesAuthenticationSuccessHandler() {
		return new ServicesAuthenticationSuccessHandler();
	}

	@Bean
	public CustomerFacade customerFacade() {
		return new com.salesmanager.shop.store.controller.customer.facade.CustomerFacadeImpl();
	}

	
	
	/**
	 * shop / customer
	 * 
	 * @author dur9213
	 *
	 */
	@Configuration
	@Order(1)
	public static class CustomerConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Bean("customerAuthenticationManager")
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Autowired
		private UserDetailsService customerDetailsService;

		public CustomerConfigurationAdapter() {
			super();
		}
		
		@Override
		public void configure(WebSecurity web) {
			web.ignoring().antMatchers("/");
			web.ignoring().antMatchers("/error");
			web.ignoring().antMatchers("/resources/**");
			web.ignoring().antMatchers("/static/**");
			web.ignoring().antMatchers("/services/public/**");
		}


		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(customerDetailsService);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/shop/**")
			.csrf().disable()			
			.authorizeRequests()
					.antMatchers("/shop/").permitAll()
					.antMatchers("/shop/**").permitAll()
					.antMatchers("/shop/customer/logon*").permitAll()
					.antMatchers("/shop/customer/registration*").permitAll()
					.antMatchers("/shop/customer/logout*").permitAll()
					.antMatchers("/shop/customer/customLogon*").permitAll()
					.antMatchers("/shop/customer/denied*").permitAll()
					.antMatchers("/shop/customer/**").hasRole("AUTH_CUSTOMER")
					.anyRequest().authenticated()
					.and()
					.httpBasic()
					.authenticationEntryPoint(shopAuthenticationEntryPoint())
					.and()
					.logout()
					.logoutUrl("/shop/customer/logout")
					.logoutSuccessUrl("/shop/")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID")

					.invalidateHttpSession(false)
					.and()
					.exceptionHandling().accessDeniedPage("/shop/");

		}

		@Bean
		public AuthenticationEntryPoint shopAuthenticationEntryPoint() {
			BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
			entryPoint.setRealmName("shop-realm");
			return entryPoint;
		}

	}
	
	/**
	 * services api v0
	 * 
	 * @author dur9213
	 * @deprecated
	 *
	 */
	@Configuration
	@Order(2)
	public static class ServicesApiConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private WebUserServices userDetailsService;

		@Autowired
		private ServicesAuthenticationSuccessHandler servicesAuthenticationSuccessHandler;

		public ServicesApiConfigurationAdapter() {
			super();
		}

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/services/**")
			.csrf().disable()
					.authorizeRequests()
					.antMatchers("/services/public/**").permitAll()
					.antMatchers("/services/private/**").hasRole("AUTH")
					.anyRequest().authenticated()
					.and().httpBasic().authenticationEntryPoint(servicesAuthenticationEntryPoint())
					.and().formLogin()
					.successHandler(servicesAuthenticationSuccessHandler);

		}

		@Bean
		public AuthenticationEntryPoint servicesAuthenticationEntryPoint() {
			BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
			entryPoint.setRealmName("rest-customer-realm");
			return entryPoint;
		}

	}

	/**
	 * admin
	 * 
	 * @author dur9213
	 *
	 */
	/**
	@Configuration
	@Order(3)
	public static class AdminConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private WebUserServices userDetailsService;

		@Autowired
		private UserAuthenticationSuccessHandler userAuthenticationSuccessHandler;

		public AdminConfigurationAdapter() {
			super();
		}

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService);
		}
		
		@Override
		public void configure(WebSecurity web) {
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/admin/**")
					.authorizeRequests()
					.antMatchers("/admin/logon*").permitAll()
					.antMatchers("/admin/resources/**").permitAll()
					.antMatchers("/admin/layout/**").permitAll()
					.antMatchers("/admin/denied*").permitAll()
					.antMatchers("/admin/unauthorized*").permitAll()
					.antMatchers("/admin/users/resetPassword*").permitAll()
					.antMatchers("/admin/").hasRole("AUTH")
					.antMatchers("/admin/**").hasRole("AUTH")
					.antMatchers("/admin/**").hasRole("AUTH")
					.antMatchers("/admin/users/resetPasswordSecurityQtn*").permitAll()
					.anyRequest()
					.authenticated()
					.and()
					.httpBasic()
					.authenticationEntryPoint(adminAuthenticationEntryPoint())
					.and()
					.formLogin().usernameParameter("username").passwordParameter("password")
					.loginPage("/admin/logon.html")
					.loginProcessingUrl("/admin/performUserLogin")
					.successHandler(userAuthenticationSuccessHandler)
					.failureUrl("/admin/logon.html?login_error=true")
					.and()
					.csrf().disable()
					.logout().logoutUrl("/admin/logout").logoutSuccessUrl("/admin/home.html")
					.invalidateHttpSession(true).and().exceptionHandling().accessDeniedPage("/admin/denied.html");
			

		}

		@Bean
		public AuthenticationEntryPoint adminAuthenticationEntryPoint() {
			BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
			entryPoint.setRealmName("admin-realm");
			return entryPoint;
		}

	}
	**/

	/**
	 * api - private
	 * 
	 * @author dur9213
	 *
	 */
	@Configuration
	@Order(5)
	public static class UserApiConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private AuthenticationTokenFilter authenticationTokenFilter;

		@Autowired
		JWTAdminServicesImpl jwtUserDetailsService;

		@Bean("jwtAdminAuthenticationManager")
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			AuthenticationManager mgr = super.authenticationManagerBean();
			return mgr;
		}
		
		

		public UserApiConfigurationAdapter() {
			super();
		}

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
		       auth.userDetailsService(jwtUserDetailsService)
	            .and()
	            .authenticationProvider(authenticationProvider());
		}
		
		@Override
		public void configure(WebSecurity web) {
			web.ignoring().antMatchers("/swagger-ui.html");
		}

		
		/**
		 * Admin us

--- sm-core/src/main/java/com/salesmanager/core/business/configuration/CoreApplicationConfiguration.java ---
package com.salesmanager.core.business.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan({"com.salesmanager.core.business"})
@EnableAutoConfiguration
@EnableConfigurationProperties(ApplicationSearchConfiguration.class)
@EnableJpaRepositories(basePackages = "com.salesmanager.core.business.repositories")
@EntityScan(basePackages = "com.salesmanager.core.model")
@EnableTransactionManagement
@ImportResource("classpath:/spring/shopizer-core-context.xml")
public class CoreApplicationConfiguration {



}


--- sm-shop/src/main/java/com/salesmanager/shop/application/config/LanguageArgumentResolver.java ---
package com.salesmanager.shop.application.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.utils.LanguageUtils;

@Component
public class LanguageArgumentResolver implements HandlerMethodArgumentResolver {
		

  @Autowired
  private LanguageUtils languageUtils;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(Language.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

    return languageUtils.getRESTLanguage(request, webRequest);
  }

}


--- sm-shop/src/main/java/com/salesmanager/shop/application/config/DocumentationConfiguration.java ---
package com.salesmanager.shop.application.config;

import static io.swagger.models.auth.In.HEADER;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class DocumentationConfiguration {

	public static final Contact DEFAULT_CONTACT = new Contact("Shopizer", "https://www.shopizer.com", "");
	
	private static final String HOST = "localhost:8080";

	/**
	 * http://localhost:8080/swagger-ui.html#/ http://localhost:8080/v2/api-docs
	 */

	@Bean
	public Docket api() {

		final List<ResponseMessage> getMessages = new ArrayList<ResponseMessage>();
		getMessages.add(new ResponseMessageBuilder().code(500).message("500 message")
				.responseModel(new ModelRef("Error")).build());
		getMessages.add(new ResponseMessageBuilder().code(403).message("Forbidden").build());
		getMessages.add(new ResponseMessageBuilder().code(401).message("Unauthorized").build());

		Set<String> produces = new HashSet<>();
		produces.add("application/json");

		Set<String> consumes = new HashSet<>();
		consumes.add("application/json");

		return new Docket(DocumentationType.SWAGGER_2)
				.host(HOST)
				.select()
				.apis(requestHandlers()).build()
				.securitySchemes(Collections.singletonList(new ApiKey("JWT", AUTHORIZATION, HEADER.name())))
		        .securityContexts(singletonList(
		            SecurityContext.builder()
		                .securityReferences(
		                    singletonList(SecurityReference.builder()
		                        .reference("JWT")
		                        .scopes(new AuthorizationScope[0])
		                        .build()
		                    )
		                )
		                .build())
		        )
				.produces(produces).consumes(consumes).globalResponseMessage(RequestMethod.GET, getMessages)
	            .globalResponseMessage(RequestMethod.GET, getMessages);

	}
	
	final Predicate<RequestHandler> requestHandlers() {
		
		   Set<Predicate<RequestHandler>> matchers = new HashSet<Predicate<RequestHandler>>();
		   matchers.add(RequestHandlerSelectors.basePackage("com.salesmanager.shop.store.api.v1"));
		   matchers.add(RequestHandlerSelectors.basePackage("com.salesmanager.shop.store.api.v2"));
		   
		   return Predicates.or(matchers);

	}

	@SuppressWarnings("rawtypes")
	private ApiInfo apiInfo() {
		return new ApiInfo("Shopizer REST API",
				"API for Shopizer e-commerce. Contains public end points as well as private end points requiring basic authentication and remote authentication based on jwt bearer token. URL patterns containing /private/** use bearer token; those are authorized customer and administrators administration actions.",
				"1.0", "urn:tos", DEFAULT_CONTACT, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0",
				new ArrayList<VendorExtension>());

	}

	private static ArrayList<? extends SecurityScheme> securitySchemes() {
		return (ArrayList<? extends SecurityScheme>) Stream.of(new ApiKey("Bearer", "Authorization", "header"))
				.collect(Collectors.toList());
	}

}


--- sm-shop/src/main/java/com/salesmanager/shop/application/config/ShopizerPropertiesConfig.java ---
package com.salesmanager.shop.application.config;


import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ShopizerPropertiesConfig {

  @Bean(name = "shopizer-properties")
  public PropertiesFactoryBean mapper() {
    PropertiesFactoryBean bean = new PropertiesFactoryBean();
    bean.setLocation(new ClassPathResource("shopizer-properties.properties"));
    return bean;
  }
}


--- sm-shop/src/main/java/com/salesmanager/shop/utils/WebApplicationCacheUtils.java ---
package com.salesmanager.shop.utils;

import com.salesmanager.core.business.utils.CacheUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class WebApplicationCacheUtils {
	
	@Inject
	private CacheUtils cache;
	
	public Object getFromCache(String key) throws Exception {
		return cache.getFromCache(key);
	}
	
	public void putInCache(String key, Object object) throws Exception {
		cache.putInCache(object, key);
	}

}


--- sm-shop/src/main/resources/application.properties ---
#Spring boot configurations
#logging.config=log4j-shopizer.properties
server.port=8080


#Turn Statistics on
#spring.jpa.properties.hibernate.generate_statistics=true
#default database schema
spring.jpa.properties.hibernate.default_schema=SALESMANAGER
#logging.level.org.hibernate.stat=debug

# Enable logging to verify that HikariCP is used, the second entry is specific to HikariCP
logging.level.org.hibernate.SQL=ERROR
logging.level.com.zaxxer.hikari.HikariConfig=INFO
#TRACE will print binding
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=INFO

#when running from IDE (eclipse) or from springboot:run command
#server.contextPath=/shopizer

logging.level.org.springframework=ERROR
logging.level.com.shopizer=INFO
logging.level.org.hibernate=ERROR
logging.level.org.apache.http=ERROR

#logging.file=shopizer.log

#Default Spring white label error page
server.error.whitelabel.enabled=false

spring.servlet.multipart.max-file-size=4MB
spring.servlet.multipart.max-request-size=10MB


#hibernate 5
spring.jpa.hibernate.use-new-id-generator-mappings=true


#Spring boot 2.X
spring.main.allow-bean-definition-overriding: true

#build informations
application-description=@project.description@
application-version=@project.version@
build.timestamp=@maven.build.timestamp@

#actuator
management.endpoints.web.exposure.include=*

management.endpoint.health.show-details=always
management.endpoint.health.show-components=always

management.health.elasticsearch.enabled=false
management.health.mail.enabled=false
management.health.ping.enabled=true