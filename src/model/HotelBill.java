package model;

import java.math.BigDecimal;

public class HotelBill {
    private String billId;
    private String cusId;
    private String date;
    private String description;
    private BigDecimal amount;

    public HotelBill( BigDecimal amount,String date) {
        this.date = date;
        this.amount = amount;
    }

    public HotelBill(String billId, String cusId, String date, String description, BigDecimal amount) {
        this.setBillId(billId);
        this.setCusId(cusId);
        this.setDate(date);
        this.setDescription(description);
        this.setAmount(amount);
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
