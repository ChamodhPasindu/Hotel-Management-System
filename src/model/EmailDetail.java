package model;

public class EmailDetail {
    private String id;
    private String name;
    private String checkIn;
    private String checkOut;
    private String advance;
    private String cost;

    public EmailDetail(String id, String name, String checkIn, String checkOut, String advance, String cost) {
        this.id = id;
        this.name = name;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.advance = advance;
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getAdvance() {
        return advance;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
