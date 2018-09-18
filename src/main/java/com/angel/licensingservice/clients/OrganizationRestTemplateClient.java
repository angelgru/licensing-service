package com.angel.licensingservice.clients;

import com.angel.licensingservice.model.Organization;
import com.angel.licensingservice.repository.OrganizationRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrganizationRestTemplateClient {

    private final OAuth2RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    @Autowired
    public OrganizationRestTemplateClient(OAuth2RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    OrganizationRedisRepository organizationRedisRepository;

    public Organization getOrganization(String organizationId) {
//        Replaced http://gateway/api/organization/v1/organizations/{organizationId}

        Organization organization = checkRedisCache(organizationId);

        if(organization != null) {
            logger.error("Successfully retrieved an organization {} from Redis cache: {}", organizationId, organization);
            return organization;
        }

        logger.error("Unable to locate organization from redis cache : {}", organizationId);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange("http://localhost:9090/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null,
                        Organization.class,
                        organizationId);

        organization = restExchange.getBody();

        if(organization != null) {
            cacheOrganizationObject(organization);
        }

        return organization;
    }

    private Organization checkRedisCache (String organizationId) {
        try {
            return organizationRedisRepository.findOrganization(organizationId);
        } catch (Exception e) {
            logger.error("Error  encountered while trying to retrieve organization {} check Redis Cache Exception {}", organizationId, e);
            return null;
        }
    }

    private void cacheOrganizationObject(Organization organization) {
        try {
            organizationRedisRepository.saveOrganization(organization);
        } catch (Exception e) {
            logger.error("Unable to cache organization{} in Redis. Exception {}", organization.getId(), e);
        }
    }
}
