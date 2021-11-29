package model;

import java.math.BigDecimal;

public class OrderDetail {
    private String orderId;
    private String foodId;
    private int qty;
    private BigDecimal price;

    public OrderDetail() {
    }

    public OrderDetail(String orderId, String foodId, int qty, BigDecimal price) {
        this.setOrderId(orderId);
        this.setFoodId(foodId);
        this.setQty(qty);
        this.setPrice(price);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderId='" + orderId + '\'' +
                ", foodId='" + foodId + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }
}
