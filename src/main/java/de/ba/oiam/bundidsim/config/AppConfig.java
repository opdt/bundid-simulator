package de.ba.oiam.bundidsim.config;

import de.ba.oiam.bundidsim.utils.TemplateFormatTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

@Configuration
public class AppConfig {

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(@Autowired SpringTemplateEngine templateEngine,
                                                       @Value("${app.basepath}") String basePath) {
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine);
        thymeleafViewResolver.setCharacterEncoding("UTF-8");
        thymeleafViewResolver.addStaticVariable("templateTools", TemplateFormatTools.getInstance());
        thymeleafViewResolver.addStaticVariable("basepath", basePath);
        return thymeleafViewResolver;
    }
}
