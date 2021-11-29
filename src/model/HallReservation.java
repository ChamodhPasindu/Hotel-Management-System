package model;

import java.math.BigDecimal;

public class HallReservation {
    private String reservationId;
    private String hallId;
    private String cusId;
    private String reserveDate;
    private String reserveTime;
    private String checkIn;
    private String checkOut;
    private BigDecimal advance;
    private BigDecimal cost;
    private String status;

    public HallReservation() {
    }


    public HallReservation(String reservationId, String cusId, String checkIn, String checkOut, BigDecimal advance, BigDecimal cost) {
        this.reservationId = reservationId;
        this.cusId = cusId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.advance = advance;
        this.cost = cost;
    }

    public HallReservation(String reservationId, String cusId, String reserveDate, String reserveTime) {
        this.reservationId = reservationId;
        this.cusId = cusId;
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
    }

    public HallReservation(String reservationId, String hallId, String cusId, String reserveDate, String reserveTime, String checkIn, String checkOut, BigDecimal advance, BigDecimal cost, String status) {
        this.reservationId = reservationId;
        this.hallId = hallId;
        this.cusId = cusId;
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.advance = advance;
        this.cost = cost;
        this.status = status;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
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

    public BigDecimal getAdvance() {
        return advance;
    }

    public void setAdvance(BigDecimal advance) {
        this.advance = advance;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "HallReservation{" +
                "reservationId='" + reservationId + '\'' +
                ", hallId='" + hallId + '\'' +
                ", cusId='" + cusId + '\'' +
                ", reserveDate='" + reserveDate + '\'' +
                ", reserveTime='" + reserveTime + '\'' +
                ", checkIn='" + checkIn + '\'' +
                ", checkOut='" + checkOut + '\'' +
                ", advance=" + advance +
                ", cost=" + cost +
                ", status='" + status + '\'' +
                '}';
    }
}
