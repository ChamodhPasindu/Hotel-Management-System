package view.tm;

import java.math.BigDecimal;

public class CartTM {
    private String foodId;
    private String description;
    private int qty;
    private BigDecimal unitPrice;

    public CartTM(String foodId, String description, int qty, BigDecimal unitPrice) {
        this.foodId = foodId;
        this.description = description;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
}
