package model;

public class RoomDetail {
    private String roomId;
    private String reservationId;

    public RoomDetail() {
    }

    public RoomDetail(String roomId, String reservationId) {
        this.roomId = roomId;
        this.reservationId = reservationId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }



    @Override
    public String toString() {
        return "RoomDetail{" +
                "roomId='" + roomId + '\'' +
                ", reservationId='" + reservationId + '\'' +
                '}';
    }
}
