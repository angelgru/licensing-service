package com.angel.licensingservice.repository;

import com.angel.licensingservice.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class OrganizationRedisRepositoryImpl implements OrganizationRedisRepository {

    private static final String HASH_NAME = "organization";

    private RedisTemplate<String, Organization> redisTemplate;
    private HashOperations hashOperations;

    @Autowired
    public OrganizationRedisRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void saveOrganization(Organization organization) {
        hashOperations.put(HASH_NAME, organization.getId(), organization);
    }

    @Override
    public void updateOrganization(Organization org) {
        hashOperations.put(HASH_NAME, org.getId(), org);
    }

    @Override
    public void deleteOrganization(String organizationId) {
        hashOperations.delete(HASH_NAME, organizationId);
    }

    @Override
    public Organization findOrganization(String organizationId) {
        return (Organization) hashOperations.get(HASH_NAME, organizationId);
    }
}
