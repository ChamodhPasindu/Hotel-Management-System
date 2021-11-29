package model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class RoomReservation {
    private String reservationId;
    private String cusId;
    private String reserveDate;
    private String reserveTime;
    private String checkIn;
    private String checkOut;
    private BigDecimal advance;
    private BigDecimal cost;
    private String status;
    private String noOfGuest;

    private ArrayList<RoomDetail> roomDetails;
    private ArrayList<MealPackageDetail> mealPackageDetails;
    private ArrayList<ServiceDetail> serviceDetails;


    public RoomReservation(String reservationId, String cusId, String reserveDate, String reserveTime) {
        this.reservationId = reservationId;
        this.cusId = cusId;
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
    }

    public RoomReservation(String reservationId, String cusId, String checkIn, String checkOut, BigDecimal advance, BigDecimal cost) {
        this.reservationId = reservationId;
        this.cusId = cusId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.advance = advance;
        this.cost = cost;
    }

    public RoomReservation(String reservationId, String cusId, String reserveDate, String reserveTime, String checkIn, String checkOut, BigDecimal advance, BigDecimal cost, String status, String noOfGuest) {
        this.reservationId = reservationId;
        this.cusId = cusId;
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.advance = advance;
        this.cost = cost;
        this.status = status;
        this.noOfGuest = noOfGuest;
    }

    public RoomReservation(String reservationId, String cusId, String reserveDate, String reserveTime, String checkIn, String checkOut, BigDecimal advance, BigDecimal cost, String status, String noOfGuest, ArrayList<RoomDetail> roomDetails, ArrayList<MealPackageDetail> mealPackageDetails, ArrayList<ServiceDetail> serviceDetails) {
        this.reservationId = reservationId;
        this.cusId = cusId;
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.advance = advance;
        this.cost = cost;
        this.status = status;
        this.noOfGuest = noOfGuest;
        this.roomDetails = roomDetails;
        this.mealPackageDetails = mealPackageDetails;
        this.serviceDetails = serviceDetails;
    }


    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
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

    public ArrayList<RoomDetail> getRoomDetails() {
        return roomDetails;
    }

    public void setRoomDetails(ArrayList<RoomDetail> roomDetails) {
        this.roomDetails = roomDetails;
    }

    public ArrayList<MealPackageDetail> getMealPackageDetails() {
        return mealPackageDetails;
    }

    public void setMealPackageDetails(ArrayList<MealPackageDetail> mealPackageDetails) {
        this.mealPackageDetails = mealPackageDetails;
    }

    public ArrayList<ServiceDetail> getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(ArrayList<ServiceDetail> serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public String getNoOfGuest() {
        return noOfGuest;
    }

    public void setNoOfGuest(String noOfGuest) {
        this.noOfGuest = noOfGuest;
    }
}
