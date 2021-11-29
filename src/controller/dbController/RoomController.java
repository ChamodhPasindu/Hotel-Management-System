package controller.dbController;

import db.DbConnection;
import model.Room;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomController {

    public String createRoomId() throws SQLException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT roomId FROM Room ORDER BY roomId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()){

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "R-00"+tempId;
            }else if(tempId<=99){
                return "R-0"+tempId;
            }else{
                return "R-"+tempId;
            }

        }else{
            return "R-001";
        }
    }

    public Boolean saveRoomDetails(Room room) throws SQLException {

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?,?)");
        stm.setObject(1,room.getRoomId());
        stm.setObject(2,room.getRoomFloor());
        stm.setObject(3,room.getRoomType());
        stm.setObject(4,room.getRoomDescription());
        stm.setObject(5,room.getRoomPrice());
        stm.setObject(6,room.getRoomDiscount());
        stm.setObject(7,room.getStatus());

        return stm.executeUpdate()>0;

    }

    public ArrayList<Room> getRoomDetails() throws SQLException {

        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Room").executeQuery();

        ArrayList<Room> roomList = new ArrayList();
        while(resultSet.next()){
            roomList.add(new Room(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getBigDecimal(5),
                    resultSet.getBigDecimal(6),
                    resultSet.getString(7))
            );
        }
        return roomList;
    }

    public boolean deleteRoomDetails(String roomId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Room WHERE roomId=?");
        stm.setObject(1,roomId);
        return stm.executeUpdate()>0;
    }

    public boolean updateRoomDetails(Room room) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Room SET roomFloor=?,roomType=?,roomDescription=?," +
                "roomPrice=?,roomDiscount=?,roomStatus=? WHERE roomId=?");
        stm.setObject(1,room.getRoomFloor());
        stm.setObject(2,room.getRoomType());
        stm.setObject(3,room.getRoomDescription());
        stm.setObject(4,room.getRoomPrice());
        stm.setObject(5,room.getRoomDiscount());
        stm.setObject(6,room.getStatus());
        stm.setObject(7,room.getRoomId());

        return stm.executeUpdate()>0;
    }

    public ArrayList<Room> searchRoomDetails(String roomType) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Room WHERE roomType LIKE '"+roomType+"%'");
        ResultSet resultSet = stm.executeQuery();

        ArrayList<Room> roomList = new ArrayList();
        while(resultSet.next()){
            roomList.add(new Room(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getBigDecimal(5),
                    resultSet.getBigDecimal(6),
                    resultSet.getString(7))
            );
        }
        return roomList;
    }

    public Room getRoomRecord(String roomId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Room WHERE roomId=?");
        stm.setObject(1,roomId);
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return new Room(
                resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getBigDecimal(5),
                    resultSet.getBigDecimal(6),
                    resultSet.getString(7)
            );
        }else{
            return null;
        }
    }

    public ArrayList<Room> searchRoomDetailsByRoomFloor(String roomFloor) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Room WHERE roomFloor LIKE '"+roomFloor+"%'");
        ResultSet resultSet = stm.executeQuery();

        ArrayList<Room> roomList = new ArrayList();
        while(resultSet.next()){
            roomList.add(new Room(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getBigDecimal(5),
                    resultSet.getBigDecimal(6),
                    resultSet.getString(7))
            );
        }
        return roomList;
    }

    public boolean changeRoomStatus(String roomId,String status) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Room SET roomStatus=? WHERE roomId=?");
        stm.setObject(1,status);
        stm.setObject(2,roomId);
        return stm.executeUpdate()>0;
    }

    public ArrayList<Room> getAvailableRoom(String checkIn,String checkOut) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("\n" +
                "    SELECT * FROM Room WHERE roomStatus='Active' AND Room.roomId NOT IN\n" +
                "                    (SELECT RoomDetail.roomId FROM RoomDetail JOIN RoomReservation\n" +
                "                    ON RoomDetail.reservationId=RoomReservation.reservationId\n" +
                "                    WHERE (RoomReservation.checkIn<=? AND RoomReservation.checkOut>=?\n" +
                "                    OR RoomReservation.checkIn<=? AND RoomReservation.checkOut>=?))");
        stm.setObject(1,checkIn);
        stm.setObject(2,checkIn);
        stm.setObject(3,checkOut);
        stm.setObject(4,checkOut);
        ResultSet resultSet = stm.executeQuery();

        ArrayList<Room> roomList = new ArrayList();
        while(resultSet.next()){
            roomList.add(new Room(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getBigDecimal(5),
                    resultSet.getBigDecimal(6),
                    resultSet.getString(7))
            );
        }
        return roomList;
    }
}
