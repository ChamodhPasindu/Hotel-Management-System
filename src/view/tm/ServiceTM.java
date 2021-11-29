package view.tm;

import java.math.BigDecimal;

public class ServiceTM {
    private String serviceId;
    private String serviceName;
    private String description;
    private BigDecimal servicePrice;

    public ServiceTM(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceTM(String serviceName, String description) {
        this.serviceName = serviceName;
        this.description = description;
    }

    public ServiceTM(String serviceId, String serviceName, String description, BigDecimal servicePrice) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.servicePrice = servicePrice;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }

}
