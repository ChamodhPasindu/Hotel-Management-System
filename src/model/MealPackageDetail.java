package model;

import java.math.BigDecimal;

public class MealPackageDetail {
    private String reservationId;
    private String packageId;
    private int breakfast;
    private int lunch;
    private int dinner;
    private BigDecimal price;

    public MealPackageDetail() {
    }

    public MealPackageDetail(String packageId, BigDecimal price) {
        this.packageId = packageId;
        this.price = price;
    }

    public MealPackageDetail(String reservationId, String packageId, int breakfast, int lunch, int dinner, BigDecimal price) {
        this.reservationId = reservationId;
        this.packageId = packageId;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.price = price;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public int getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(int breakfast) {
        this.breakfast = breakfast;
    }

    public int getLunch() {
        return lunch;
    }

    public void setLunch(int lunch) {
        this.lunch = lunch;
    }

    public int getDinner() {
        return dinner;
    }

    public void setDinner(int dinner) {
        this.dinner = dinner;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "MealPackageDetail{" +
                "reservationId='" + reservationId + '\'' +
                ", packageId='" + packageId + '\'' +
                ", breakfast=" + breakfast +
                ", lunch=" + lunch +
                ", dinner=" + dinner +
                ", price=" + price +
                '}';
    }
}
