package br.com.araujo.rastreabilidade.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import br.com.araujo.rastreabilidade.mapper.InetOrgPersonContextAraujoMapper;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${aplication.ldap.domain}")
	private String domain;
	
	@Value("${aplication.ldap.url}")
	private String url;
	
	@Value("${aplication.ldap.rootDn}")
	private String rootDn;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.authorizeRequests()
		 .anyRequest()
			.fullyAuthenticated()
	        .and()
	            .formLogin()
	            .loginPage("/login")
	            .defaultSuccessUrl("/index")
	            .failureUrl("/login?error=true")
	            .permitAll()
	        .and()
	            .logout()
	            .logoutSuccessUrl("/login?logout=true")
	            .deleteCookies("JSESSIONID")
	            .invalidateHttpSession(true)
	            .permitAll()
	        .and()
	            .csrf()
	            .disable();
		http.headers().frameOptions().disable();
	}

	@Bean
	public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(domain,
				url, rootDn);
		provider.setConvertSubErrorCodesToExceptions(true);
		provider.setUseAuthenticationRequestCredentials(true);
		provider.setUserDetailsContextMapper(new InetOrgPersonContextAraujoMapper());
		return provider;
	}
}
