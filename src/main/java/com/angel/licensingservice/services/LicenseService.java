package com.angel.licensingservice.services;

import com.angel.licensingservice.clients.OrganizationRestTemplateClient;
import com.angel.licensingservice.config.ServiceConfig;
import com.angel.licensingservice.model.License;
import com.angel.licensingservice.model.Organization;
import com.angel.licensingservice.repository.LicenseRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LicenseService {

    private final LicenseRepository licenseRepository;

    private final ServiceConfig serviceConfig;

    private final OrganizationRestTemplateClient organizationRestTemplateClient;

    @HystrixCommand(fallbackMethod = "buildFallbackOrganization")
    private Organization retrieveOrgInfo(String organizationId) {
        return organizationRestTemplateClient.getOrganization(organizationId);
    }

    Organization buildFallbackOrganization(String organizationId) {
        Organization organization = new Organization();
        organization.setId("");
        organization.setContactEmail("");
        organization.setContactName("");
        organization.setContactPhone("");
        organization.setName("Sorry, we are not able to retrieve the data");
        return organization;
    }

    @Autowired
    public LicenseService(LicenseRepository licenseRepository, ServiceConfig serviceConfig, OrganizationRestTemplateClient organizationRestTemplateClient) {
        this.licenseRepository = licenseRepository;
        this.serviceConfig = serviceConfig;
        this.organizationRestTemplateClient = organizationRestTemplateClient;
    }

    public License getLicense(String organizationId, String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        Organization organization = retrieveOrgInfo(organizationId);
        license.setOrganizationName(organization.getName());
        license.setContactName(organization.getContactName());
        license.setContactEmail(organization.getContactEmail());
        license.setContactPhone(organization.getContactPhone());
        license.setComment(serviceConfig.getExampleProperty());
        return license;
    }

    @HystrixCommand()
    public List<License> getLicensesByOrg(String organizationId) {
        return licenseRepository.findByOrganizationId(organizationId);
    }

    public void saveLicense(License license) {
        license.withId(UUID.randomUUID().toString());
        licenseRepository.save(license);
    }

    public void updateLicense(License license) {
        licenseRepository.save(license);
    }

    public void deleteLicense(License license) {
        licenseRepository.delete(license);
    }
}
