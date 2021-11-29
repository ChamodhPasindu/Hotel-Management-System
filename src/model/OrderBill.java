package model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OrderBill {
    private String billId;
    private String orderId;
    private BigDecimal amount;
    private ArrayList<OrderDetail> orderDetails;

    public OrderBill() {
    }

    public OrderBill(String billId, String orderId, BigDecimal amount, ArrayList<OrderDetail> orderDetails) {
        this.billId = billId;
        this.orderId = orderId;
        this.amount = amount;
        this.orderDetails = orderDetails;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrderBill{" +
                "billId='" + billId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", amount=" + amount +
                '}';
    }

    public ArrayList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
