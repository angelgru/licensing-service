package com.angel.licensingservice.repository;

import com.angel.licensingservice.model.Organization;

public interface OrganizationRedisRepository {

    void saveOrganization(Organization organization);
    void updateOrganization(Organization org);
    void deleteOrganization(String organizationId);
    Organization findOrganization(String organizationId);

}
