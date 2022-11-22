package com.artbender.crypto.web.config;


import com.artbender.crypto.service.ServiceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration. To scan spring configuration in web packages
 *
 * @author Artsiom Leuchanka
 */
@Configuration
@ComponentScan("com.artbender.crypto.web")
@Import(value = ServiceConfig.class)
public class WebConfig {
}
