/*
 * Copyright 2014-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package catering;

import org.salespointframework.EnableSalespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNullApi;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableSalespoint
public class Catering {
//	private static final String[] ACCESS_WITHOUT_AUTH = {"/", "/register", "/login", "/catalog", "/offer", "/eventcatering",
//			"/partyservice", "/rentacook", "/mobilebreakfast", "/index","/layout.html", "/static/resources/**"};

	public static void main(String[] args) {
		SpringApplication.run(Catering.class, args);
	}


	@Configuration
	static class MvcConfig implements WebMvcConfigurer {

		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController("/login").setViewName("login");
			registry.addViewController("/").setViewName("welcome");
		}

	}

	@Configuration
	static class WebSecurityConfiguration extends SalespointSecurityConfiguration {
		@Autowired
		PasswordEncoder passwordEncoder;
		@Autowired
		@Qualifier("springSecurityAuthenticationManagement")
		UserDetailsService detailsService;
		@Override
		protected void configure(AuthenticationManagerBuilder amBuilder) throws Exception {
			super.configure(amBuilder);
			DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
			provider.setPasswordEncoder(passwordEncoder);
			provider.setUserDetailsService(detailsService);
			amBuilder.authenticationProvider(provider);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();  // for lab purposes, that's ok!
			http.authorizeRequests().antMatchers("/", "/css/**", "/register").permitAll()
					.and()
					.formLogin().loginPage("/login").loginProcessingUrl("/login").permitAll()
					.failureUrl("/error").and()
					.logout().logoutUrl("/logout").logoutSuccessUrl("/");
		}
	}
}
