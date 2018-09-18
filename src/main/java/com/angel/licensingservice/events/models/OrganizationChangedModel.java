package com.angel.licensingservice.events.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationChangedModel {

    private String type;
    private String action;
    private String organizationId;
    private String correlationId;

    public OrganizationChangedModel() {
    }

    public OrganizationChangedModel(String type, String action, String organizationId, String correlationId) {
        this.type = type;
        this.action = action;
        this.organizationId = organizationId;
        this.correlationId = correlationId;
    }


}
