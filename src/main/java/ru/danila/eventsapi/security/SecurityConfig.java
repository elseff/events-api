package ru.danila.eventsapi.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    JwtFilter jwtFilter;

    UserDetailsServiceImpl userDetailsService;

    @Qualifier("customAuthenticationEntryPoint")
    AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable();

        http.cors();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                // создание и удаление событий - только организатор
                .antMatchers(HttpMethod.POST, "/api/v1/events/**").hasRole("ORGANIZER")
                .antMatchers(HttpMethod.DELETE, "/api/v1/events/**").hasRole("ORGANIZER")
                // список всех событий - любой желающий
                .antMatchers(HttpMethod.GET, "/api/v1/events/**").permitAll()

                // добавление и удаление мест проведения - только модератор
                .antMatchers(HttpMethod.POST, "/api/v1/places/**").hasRole("MODERATOR")
                .antMatchers(HttpMethod.DELETE, "/api/v1/places/**").hasRole("MODERATOR")
                // список всех мест проведения - любой желающий
                .antMatchers(HttpMethod.GET, "/api/v1/places/**").permitAll()

                // покупка билетов - только обычный пользователь
                .antMatchers(HttpMethod.POST, "/api/v1/tickets/**").hasRole("USER")

                // для администраторов
                .antMatchers("/admin/**").hasRole("ADMIN")

                // зарегистрироваться и получить токен - любой пользователь
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("/logout").permitAll();

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);


        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v3/api-docs/**")
                .antMatchers("/swagger-ui/**")
                .antMatchers("/swagger-ui.html/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
