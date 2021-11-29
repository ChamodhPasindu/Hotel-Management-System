package model;

import java.util.ArrayList;

public class Order {
    private String orderId;
    private String date;
    private OrderBill orderBill;

    public Order() {
    }

    public Order(String orderId, String date, OrderBill orderBill) {
        this.orderId = orderId;
        this.date = date;
        this.orderBill = orderBill;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public OrderBill getOrderBill() {
        return orderBill;
    }

    public void setOrderBill(OrderBill orderBill) {
        this.orderBill = orderBill;
    }
}
