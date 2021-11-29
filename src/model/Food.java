package model;

import java.math.BigDecimal;

public class Food {
    private String foodId;
    private String description;
    private BigDecimal unitPrice;

    public Food() {
    }

    public Food(String foodId, String description, BigDecimal unitPrice) {
        this.foodId = foodId;
        this.description = description;
        this.unitPrice = unitPrice;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "Food{" +
                "foodId='" + foodId + '\'' +
                ", description='" + description + '\'' +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
