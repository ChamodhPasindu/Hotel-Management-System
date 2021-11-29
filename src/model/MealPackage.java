package model;

import java.math.BigDecimal;

public class MealPackage {
    private String packageId;
    private String packageType;
    private String description;
    private BigDecimal packagePrice;
    private String availability;

    public MealPackage() {
    }

    public MealPackage(String packageId, String packageType, String description, BigDecimal packagePrice, String availability) {
        this.packageId = packageId;
        this.packageType = packageType;
        this.description = description;
        this.packagePrice = packagePrice;
        this.availability = availability;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(BigDecimal packagePrice) {
        this.packagePrice = packagePrice;
    }

    @Override
    public String toString() {
        return "MealPackage{" +
                "packageId='" + getPackageId() + '\'' +
                ", packageType='" + getPackageType() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", packagePrice=" + getPackagePrice() +
                '}';
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
