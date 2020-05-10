package com.smoke.xiguazi.config;

import com.smoke.xiguazi.security.MobileAuthenticationSecurityConfig;
import com.smoke.xiguazi.security.SecurityMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = {SecurityMarker.class})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private MobileAuthenticationSecurityConfig mobileAuthenticationSecurityConfig;

    @Autowired
    public SecurityConfiguration(MobileAuthenticationSecurityConfig mobileAuthenticationSecurityConfig){
        this.mobileAuthenticationSecurityConfig = mobileAuthenticationSecurityConfig;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        静态资源请求忽略
        web.ignoring().antMatchers("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.png", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .ignoringAntMatchers("/test/**", "/es/**", "/admin/**")
                .and()
            .apply(mobileAuthenticationSecurityConfig)
                .and()
            .httpBasic()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll()
                .and()
            .authorizeRequests()
                .antMatchers("/es/**", "/test/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/consultant/**").hasRole("CONSULTANT")
                .antMatchers("/", "/register", "/login", "/logout", "/buy/**").permitAll()
                .anyRequest().authenticated();
    }
}
