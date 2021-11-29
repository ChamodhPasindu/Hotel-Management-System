package model;

import java.math.BigDecimal;

public class Hall {
    private String hallId;
    private int hallFloor;
    private String hallType;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private String status;

    public Hall() {
    }

    public Hall(String hallId, int hallFloor, String hallType, String description, BigDecimal price, BigDecimal discount, String status) {
        this.hallId = hallId;
        this.hallFloor = hallFloor;
        this.hallType = hallType;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.status = status;
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

    public int getHallFloor() {
        return hallFloor;
    }

    public void setHallFloor(int hallFloor) {
        this.hallFloor = hallFloor;
    }

    public String getHallType() {
        return hallType;
    }

    public void setHallType(String hallType) {
        this.hallType = hallType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Hall{" +
                "hallId='" + hallId + '\'' +
                ", hallFloor=" + hallFloor +
                ", hallType='" + hallType + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", status='" + status + '\'' +
                '}';
    }
}
