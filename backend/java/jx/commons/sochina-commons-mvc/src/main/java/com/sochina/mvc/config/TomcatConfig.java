package com.sochina.mvc.config;

import com.sochina.mvc.domain.properties.TomcatProperties;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@ConditionalOnProperty(value = "sochina.tomcat-properties.enable", havingValue = "true")
@Configuration
public class TomcatConfig {
    private final TomcatProperties tomcatProperties;

    public TomcatConfig(TomcatProperties tomcatProperties) {
        this.tomcatProperties = tomcatProperties;
    }

    //
    @Bean
    public ConfigurableServletWebServerFactory configurableServletWebServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addContextCustomizers(context -> {
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setUserConstraint(tomcatProperties.getUserConstraint().toUpperCase());
            SecurityCollection securityCollection = new SecurityCollection();
            securityCollection.addPattern(tomcatProperties.getPattern());
            // 排除认证的非安全的http方法，默认GET/POST
            tomcatProperties.getOmittedMethod().stream().filter(Objects::nonNull).forEach(securityCollection::addOmittedMethod);
            securityConstraint.addCollection(securityCollection);
            context.addConstraint(securityConstraint);
            context.setUseHttpOnly(tomcatProperties.isUseHttpOnly());
        });
        // 禁用TRACE请求需要设置setAllowTrace，有优先级影响
        factory.addConnectorCustomizers(connector -> connector.setAllowTrace(tomcatProperties.isAllowTrace()));
        return factory;
    }

    // 通过tomcat设置cookie的samesite属性
    @Bean
    public TomcatContextCustomizer sameSiteCookiesConfig() {
        return context -> {
            final Rfc6265CookieProcessor cookieProcessor = new Rfc6265CookieProcessor();
            settingSameSite(tomcatProperties.getCookieSameSite(), cookieProcessor);
            context.setCookieProcessor(cookieProcessor);
        };
    }

    private void settingSameSite(String type, Rfc6265CookieProcessor cookieProcessor) {
        switch (type.toUpperCase()) {
            case "NONE":
                cookieProcessor.setSameSiteCookies(SameSiteCookies.NONE.getValue());
                break;
            case "LAX":
                cookieProcessor.setSameSiteCookies(SameSiteCookies.LAX.getValue());
                break;
            case "STRICT":
                cookieProcessor.setSameSiteCookies(SameSiteCookies.STRICT.getValue());
                break;
            default:
                cookieProcessor.setSameSiteCookies(SameSiteCookies.UNSET.getValue());
                break;
        }
    }
}
