package com.angel.licensingservice.controller;

import com.angel.licensingservice.config.ServiceConfig;
import com.angel.licensingservice.model.License;
import com.angel.licensingservice.services.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="v1/organizations/{organizationId}/licenses")
public class LicenceServiceController {

    final LicenseService licenseService;

    final ServiceConfig serviceConfig;

    @Autowired
    public LicenceServiceController(LicenseService licenseService, ServiceConfig serviceConfig) {
        this.licenseService = licenseService;
        this.serviceConfig = serviceConfig;
    }

    @RequestMapping(value="/",method = RequestMethod.GET)
    public List<License> getLicenses(@PathVariable("organizationId") String organizationId) {

        return licenseService.getLicensesByOrg(organizationId);
    }

    @RequestMapping(value="/{licenseId}",method = RequestMethod.GET)
    public License getLicenses( @PathVariable("organizationId") String organizationId,
                                @PathVariable("licenseId") String licenseId) {

        return licenseService.getLicense(organizationId,licenseId);
    }

    @RequestMapping(value="{licenseId}",method = RequestMethod.PUT)
    public String updateLicenses( @PathVariable("licenseId") String licenseId) {
        return "This is the put";
    }

    @RequestMapping(value="/",method = RequestMethod.POST)
    public void saveLicenses(@RequestBody License license) {
        licenseService.saveLicense(license);
    }

    @RequestMapping(value="{licenseId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteLicenses( @PathVariable("licenseId") String licenseId) {
        return "This is the Delete";
    }
}
