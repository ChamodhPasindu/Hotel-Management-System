package model;

import java.math.BigDecimal;

public class Room {
    private String roomId;
    private int roomFloor;
    private String roomType;
    private String roomDescription;
    private BigDecimal roomPrice;
    private BigDecimal roomDiscount;
    private String status;

    public Room() {
    }

    public Room(String roomId, String roomType,BigDecimal roomPrice, BigDecimal roomDiscount) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.roomDiscount = roomDiscount;
    }

    public Room(String roomId, int roomFloor, String roomType, String roomDescription, BigDecimal roomPrice, BigDecimal roomDiscount, String status) {
        this.roomId = roomId;
        this.roomFloor = roomFloor;
        this.roomType = roomType;
        this.roomDescription = roomDescription;
        this.roomPrice = roomPrice;
        this.roomDiscount = roomDiscount;
        this.status = status;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getRoomFloor() {
        return roomFloor;
    }

    public void setRoomFloor(int roomFloor) {
        this.roomFloor = roomFloor;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public BigDecimal getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(BigDecimal roomPrice) {
        this.roomPrice = roomPrice;
    }

    public BigDecimal getRoomDiscount() {
        return roomDiscount;
    }

    public void setRoomDiscount(BigDecimal roomDiscount) {
        this.roomDiscount = roomDiscount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
