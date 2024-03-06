/*
package com.sochina.mvc.config;
import com.sochina.base.constants.Constants;
import com.sochina.base.domain.properties.WebSecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import java.util.List;
import java.util.Optional;
*/
/*
@Configuration
@EnableWebSecurity
public class WebMvcSecurityConfig extends WebSecurityConfigurerAdapter {
    private final WebSecurityProperties webSecurityProperties;
    private static List<String> URL_LIST;
    public WebMvcSecurityConfig(WebSecurityProperties webSecurityProperties) {
        this.webSecurityProperties = webSecurityProperties;
        URL_LIST = Optional.ofNullable(webSecurityProperties.getUrlList())
                .orElse(Constants.EMPTY_LIST);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(URL_LIST.toArray(new String[0]))
                .authenticated()
                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                .permitAll();
    }
}
*/
