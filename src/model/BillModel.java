package model;

public class BillModel {
    private String particular;
    private String day;
    private String amount;

    public BillModel(String particular, String day, String amount) {
        this.particular = particular;
        this.day = day;
        this.amount = amount;
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
