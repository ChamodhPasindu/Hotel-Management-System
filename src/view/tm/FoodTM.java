package view.tm;

import java.math.BigDecimal;

public class FoodTM {
    private String foodId;
    private String description;
    private BigDecimal unitPrice;

    public FoodTM(String description) {
        this.description = description;
    }

    public FoodTM(String foodId, String description, BigDecimal unitPrice) {
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
}
