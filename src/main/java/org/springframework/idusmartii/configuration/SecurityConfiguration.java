package org.springframework.idusmartii.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author japarejo
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Bean
	SessionRegistry sessionRegistry() {
	    return new SessionRegistryImpl();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/resources/**","/webjars/**","/h2-console/**").permitAll()
				.antMatchers(HttpMethod.GET, "/","/oups").permitAll()
				.antMatchers("/users/new").permitAll()
				.antMatchers("/play/**").permitAll()
				.antMatchers("play/**").permitAll()
                .antMatchers("/player/edit").authenticated()
				.antMatchers("/admin").hasAnyAuthority("admin")
				.antMatchers("/admin/playersList").hasAnyAuthority("admin")
				.antMatchers("/friendList").hasAnyAuthority("player", "admin")
                .antMatchers("/findPlayer").hasAnyAuthority("player", "admin")
                .antMatchers("/sendRecomendationEmail").authenticated()
				.antMatchers("/admin/playersList/createPlayer/**").hasAnyAuthority("admin")
				.antMatchers("/admin/playersList/update/**").hasAnyAuthority("admin")
				.antMatchers("/admin/playerList/delete/**").hasAnyAuthority("admin")
				.antMatchers("/admin/matchsList").hasAnyAuthority("admin")
				.antMatchers("/globalEstadistics").authenticated()
				.antMatchers("/admin/matchsList/matchinfo/**").hasAnyAuthority("admin")
				.antMatchers("/admin/**").hasAnyAuthority("admin")
				.antMatchers("/playerList**").authenticated()
				.antMatchers("/achivement/**").authenticated()
				.antMatchers("/playerList/details/**").authenticated()
				.antMatchers("/board/**").authenticated()
				.antMatchers("/match/**").authenticated()
				.antMatchers("/playerList/match/**").authenticated()
				.antMatchers("/friendList/**").hasAnyAuthority("admin", "player")
				.antMatchers("/friendList/invite/{friendId}").hasAnyAuthority("admin", "player")
				.antMatchers("/friendList/details/**").hasAnyAuthority("player")
				.antMatchers("/forum/**").hasAnyAuthority("admin", "player")
				.antMatchers("/admin/**").hasAnyAuthority("admin")
				.antMatchers("/details/**").authenticated()
				.anyRequest().denyAll()
				.and()
				 	.formLogin()
				 	/*.loginPage("/login")*/
				 	.failureUrl("/login-error")
				.and()
					.logout()
						.logoutSuccessUrl("/");
                // Configuración para que funcione la consola de administración
                // de la BD H2 (deshabilitar las cabeceras de protección contra
                // ataques de tipo csrf y habilitar los framesets si su contenido
                // se sirve desde esta misma página.
                http.csrf().ignoringAntMatchers("/h2-console/**");
                http.headers().frameOptions().sameOrigin();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
	      .dataSource(dataSource)
	      .usersByUsernameQuery(
	       "select username,password,enabled "
	        + "from users "
	        + "where username = ?")
	      .authoritiesByUsernameQuery(
	       "select username, authority "
	        + "from authorities "
	        + "where username = ?")
	      .passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder =  NoOpPasswordEncoder.getInstance();
	    return encoder;
	}

}


