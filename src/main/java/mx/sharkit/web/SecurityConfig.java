package mx.sharkit.web;

import mx.sharkit.web.security.CustomPasswordEncoder;
import mx.sharkit.web.security.LoginSuccessFailureHandler;
import mx.sharkit.web.security.SSUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/**
 *
 * @author Adrián Salgado D.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private LoginSuccessFailureHandler authenticationHandler;

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    @Autowired
    private SSUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) {
        try {
            http.csrf().disable();
            http.sessionManagement().maximumSessions(1);
//            http.addFilterBefore(authenticationSuccessHandler, UsernamePasswordAuthenticationFilter.class);
            http
                    .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/index**").permitAll()
                    .antMatchers("/login**").permitAll() 
//                    .antMatchers("/mantenimiento**").permitAll()
                    .antMatchers("/getCompanias").permitAll()
                    .antMatchers("/getLogin").permitAll()
                    .antMatchers("/getVendedores").permitAll()
                    .antMatchers("/asignacion").permitAll()
                    .antMatchers("/alta").permitAll()
                    .antMatchers("/findChips").permitAll()
                    .antMatchers("/getEstatus").permitAll()
                    .antMatchers("/coordenadas").permitAll()
                    .antMatchers("/asignacionMasiva").permitAll()
                    .antMatchers("/findChipsPaginator").permitAll()
                    .antMatchers("/getEstatusProceso").permitAll()
                    .antMatchers("/getEstatusPortabilidad").permitAll()
                    .antMatchers("/findChipsErroneos").permitAll() 
                    .antMatchers("/editChip").permitAll() 
                    .antMatchers("/javax.faces.resource/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .csrf().disable()
                    .formLogin()
//                    .loginPage("/mantenimiento.html")
                    .loginPage("/login.xhtml")
                    //                    .loginPage("/index.jsp")
                    //                    .loginProcessingUrl("/j_spring_security_check")
                    .permitAll()
                    .successHandler(authenticationHandler)
                    .failureHandler(authenticationHandler)
                    .usernameParameter("j_username")
                    .passwordParameter("j_password")
                    .and()
                    .logout()
                    //                    .logoutSuccessUrl("/index.jsp")
                    .logoutSuccessUrl("/login.xhtml")
//                    .logoutSuccessUrl("/mantenimiento.xhtml")
                    .permitAll()
                    .and()
                    .exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/webjars/**", "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.
                userDetailsService(userDetailsService)
                .passwordEncoder(customPasswordEncoder);
//                .passwordEncoder(bCryptPasswordEncoder);

    }

    @Bean
    public FilterRegistrationBean FileUploadFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new org.primefaces.webapp.filter.FileUploadFilter());
        registration.setName("PrimeFaces FileUpload Filter");
        return registration;
    }

    @Bean
    public FilterRegistrationBean hiddenHttpMethodFilterDisabled(
            @Qualifier("hiddenHttpMethodFilter") HiddenHttpMethodFilter filter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(filter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }
    
}
