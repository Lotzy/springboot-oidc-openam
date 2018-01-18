package com.lotzy.sample;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * <pre>
 * Title: SocialApplication class
 * Description: Spring Boot Main class
 * </pre>
 *
 * @author Lotzy
 * @version 1.0
 */
@SpringBootApplication
@RestController
@EnableOAuth2Client
@ComponentScan({"com.lotzy.sample"})
public class SocialApplication extends WebSecurityConfigurerAdapter {

	@Autowired
	OAuth2ClientContext oauth2ClientContext;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.antMatcher("/**").authorizeRequests().antMatchers("/", "/login**", "/webjars/**").permitAll().anyRequest()
				.authenticated().and().exceptionHandling()
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login/openam")).and().logout()
				.logoutSuccessUrl("/").permitAll().and().csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
				.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
		// @formatter:on
	}

	public static void main(String[] args) {
		SpringApplication.run(SocialApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}

	private Filter ssoFilter() {
		OAuth2ClientAuthenticationProcessingFilter openAMFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/openam");
		OAuth2RestTemplate openAMTemplate = new OAuth2RestTemplate(openAM(), oauth2ClientContext);
		openAMFilter.setRestTemplate(openAMTemplate);
		UserInfoTokenServices tokenServices = new UserInfoTokenServices(openAMResource().getUserInfoUri(), openAM().getClientId());
		tokenServices.setRestTemplate(openAMTemplate);
		openAMFilter.setTokenServices(new UserInfoTokenServices(openAMResource().getUserInfoUri(), openAM().getClientId()));
		return openAMFilter;
	}

	@Bean
	@ConfigurationProperties("openam.client")
	public AuthorizationCodeResourceDetails openAM() {
		return new AuthorizationCodeResourceDetails();
	}

	@Bean
	@ConfigurationProperties("openam.resource")
	public ResourceServerProperties openAMResource() {
		return new ResourceServerProperties();
	}

}
