package mx.sharkit.web;

import javax.faces.application.ViewExpiredException;
import mx.sharkit.web.dao.impl.BaseRepositoryImpl;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author Adrián Salgado D.
 */
@Configuration
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class MvcConfig extends WebMvcConfigurerAdapter {

    /////******ESTO SE AGREGA PARA EVITAR EL PROBLEMA:******/////
    //   with key 'dataSource'; nested exception is javax.management.InstanceAlreadyExistsException: spring-boot-template-jsf:name=dataSource,type=HikariDataSource
    @Bean
    public MBeanExporter exporter() {
        final MBeanExporter exporter = new MBeanExporter();
        exporter.setAutodetect(true);
        exporter.setExcludedBeans("dataSource");
        return exporter;
    }
    /////////////////////
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/home").setViewName("home");
//        registry.addViewController("/").setViewName("home");
//        registry.addViewController("/hello").setViewName("hello");
//        registry.addViewController("/inicio").setViewName("inicio");
        registry.addViewController("/401").setViewName("/error/401.xhtml");
        registry.addViewController("/404").setViewName("/error/404.xhtml");
        registry.addViewController("/500").setViewName("/error/500.xhtml");
        registry.addViewController("/500.html").setViewName("/error/500.xhtml");
        registry.addViewController("/customError").setViewName("/error/customError.xhtml");
        registry.addViewController("/viewExpired").setViewName("/error/viewExpired.xhtml");
        registry.addViewController("/error").setViewName("/error/customError.xhtml");

    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {

                ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401");
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
                ErrorPage viewExpired = new ErrorPage(ViewExpiredException.class, "/viewExpired");
                ErrorPage npe = new ErrorPage(NullPointerException.class, "/customError");
                ErrorPage errorThrowable = new ErrorPage("/customError");

                container.addErrorPages(error401Page, error404Page, error500Page, errorThrowable, viewExpired, npe);
            }
        };
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new MyErrorPageRegistrar();
    }

    private static class MyErrorPageRegistrar implements ErrorPageRegistrar {

        @Override
        public void registerErrorPages(ErrorPageRegistry registry) {
                ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401");
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
                ErrorPage viewExpired = new ErrorPage(ViewExpiredException.class, "/viewExpired");
                ErrorPage npe = new ErrorPage(NullPointerException.class, "/customError");
                ErrorPage errorThrowable = new ErrorPage("/customError");

            registry.addErrorPages(error401Page, error404Page, error500Page, errorThrowable, viewExpired, npe);
        }
        
    }

}
