package net.pandamaster.sample.spring.amqp.web;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import java.sql.DriverManager;
import java.util.Collections;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter implements ServletContextListener {
    
    @Autowired
    private ConnectionFactory sampleConnectionFactory;
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/js/**")) {
            registry.addResourceHandler("/js/**").addResourceLocations("classpath:/public/js/");
        }
        if (!registry.hasMappingForPattern("/css/**")) {
            registry.addResourceHandler("/css/**").addResourceLocations("classpath:/public/css/");
        }
        if (!registry.hasMappingForPattern("/img/**")) {
            registry.addResourceHandler("/img/**").addResourceLocations("classpath:/public/img/");
        }
    }
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // do nothing
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ((CachingConnectionFactory)sampleConnectionFactory).destroy();
        AbandonedConnectionCleanupThread.checkedShutdown();
        Collections.list(DriverManager.getDrivers()).forEach(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            }
            catch (final Exception e) {
            }
        });
    }
}
