package com.angel.licensingservice;

import com.angel.licensingservice.events.models.OrganizationChangedModel;
import com.angel.licensingservice.utils.UserContextInterceptor;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;

import java.util.List;

@SpringBootApplication
@RefreshScope
@EnableEurekaClient
@EnableCircuitBreaker
@EnableResourceServer
@EnableBinding(Sink.class)
public class LicensingServiceApplication {

//    @LoadBalanced
//    @Bean
//    public RestTemplate getRestTemplate() {
//        RestTemplate template = new RestTemplate();
//        List interceptors = template.getInterceptors();
//        if(interceptors == null) {
//            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
//        } else {
//            interceptors.add(new UserContextInterceptor());
//            template.setInterceptors(interceptors);
//        }
//
//        return template;
//    }

    private Logger logger = LoggerFactory.getLogger(LicensingServiceApplication.class);

    @StreamListener(Sink.INPUT)
    public void loggerSink(OrganizationChangedModel orgChange) {
        logger.error("Received an event for organization id {}", orgChange.getOrganizationId());
    }


    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate( OAuth2ClientContext oAuth2ClientContext,
            OAuth2ProtectedResourceDetails details) {
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(details, oAuth2ClientContext);
        List interceptors = restTemplate.getInterceptors();
        interceptors.add(new UserContextInterceptor());
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnFactory = new
                JedisConnectionFactory();
        jedisConnFactory.setHostName("localhost");
        jedisConnFactory.setPort(6379);
        return jedisConnFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

	public static void main(String[] args) {
		SpringApplication.run(LicensingServiceApplication.class, args);
	}
}
