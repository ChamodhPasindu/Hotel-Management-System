package model;

import java.math.BigDecimal;

public class ServiceDetail {
    private String reservationId;
    private String serviceId;
    private int noOfDay;
    private BigDecimal price;

    public ServiceDetail() {
    }

    public ServiceDetail(String serviceId, int noOfDay, BigDecimal price) {
        this.serviceId = serviceId;
        this.noOfDay = noOfDay;
        this.price = price;
    }

    public ServiceDetail(String reservationId, String serviceId, int noOfDay, BigDecimal price) {
        this.setReservationId(reservationId);
        this.setServiceId(serviceId);
        this.setNoOfDay(noOfDay);
        this.setPrice(price);
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getNoOfDay() {
        return noOfDay;
    }

    public void setNoOfDay(int noOfDay) {
        this.noOfDay = noOfDay;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ServiceDetail{" +
                "reservationId='" + reservationId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", noOfDay=" + noOfDay +
                ", price=" + price +
                '}';
    }
}
