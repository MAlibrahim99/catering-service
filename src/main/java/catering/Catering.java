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
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableSalespoint
public class Catering {
	private static final String LOGIN_ROUTE = "/login";
	private static final String CONTEXT_ROOT = "/";
	private static final String[] ACCESS_WITHOUT_AUTH = {CONTEXT_ROOT, LOGIN_ROUTE, "/register", "/catalog", "/offer",
			"/eventcatering", "/partyservice", "/rentacook", "/mobilebreakfast", "/index" ,"/webjars/**"};

	public static void main(String[] args) {
		SpringApplication.run(Catering.class, args);
	}

	@Configuration
	static class MvcConfig implements WebMvcConfigurer {

		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController(LOGIN_ROUTE).setViewName("login");
			registry.addViewController(CONTEXT_ROOT).setViewName("welcome");
		}
	}

	@Configuration
	static class WebSecurityConfiguration extends SalespointSecurityConfiguration {


		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();  // for lab purposes, that's ok!
			http.authorizeRequests()
					.antMatchers(ACCESS_WITHOUT_AUTH).permitAll()
					.anyRequest().authenticated().and()
					.formLogin().loginPage(LOGIN_ROUTE).usernameParameter("email").loginProcessingUrl(LOGIN_ROUTE).permitAll()
					.defaultSuccessUrl(CONTEXT_ROOT, true).and()
//					.failureUrl("/error").and()
					.logout().logoutUrl("/logout").logoutSuccessUrl(CONTEXT_ROOT);
		}
	}
}
