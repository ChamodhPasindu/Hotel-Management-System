package controller.dbController;

import db.DbConnection;
import model.Hall;
import model.Room;
import view.tm.HallTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HallController {

    public String createHallId() throws SQLException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT hallId FROM Hall ORDER BY hallId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()){

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "H-00"+tempId;
            }else if(tempId<=99){
                return "H-0"+tempId;
            }else{
                return "H-"+tempId;
            }

        }else{
            return "H-001";
        }
    }

    public boolean saveHallDetails(Hall hall) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO Hall VALUES (?,?,?,?,?,?,?)");
        stm.setObject(1,hall.getHallId());
        stm.setObject(2,hall.getHallFloor());
        stm.setObject(3,hall.getHallType());
        stm.setObject(4,hall.getDescription());
        stm.setObject(5,hall.getPrice());
        stm.setObject(6,hall.getDiscount());
        stm.setObject(7,hall.getStatus());

        return stm.executeUpdate()>0;
    }

    public ArrayList<Hall> getHallDetails() throws SQLException {

        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Hall").executeQuery();

        ArrayList<Hall> hallList = new ArrayList();
        while(resultSet.next()){
            hallList.add(new Hall(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getBigDecimal(5),
                    resultSet.getBigDecimal(6),
                    resultSet.getString(7))
            );
        }
        return hallList;
    }

    public Hall getHallRecord(String hallId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Hall WHERE hallId=?");
        stm.setObject(1,hallId);
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return new Hall(
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

    public boolean deleteHallDetails(String hallId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Hall WHERE hallId=?");
        stm.setObject(1,hallId);
        return stm.executeUpdate()>0;
    }

    public boolean updateHallDetails(Hall hall) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Hall SET hallFloor=?,hallType=?,description=?," +
                "hallPrice=?,hallDiscount=?,hallStatus=? WHERE hallId=?");
        stm.setObject(1,hall.getHallFloor());
        stm.setObject(2,hall.getHallType());
        stm.setObject(3,hall.getDescription());
        stm.setObject(4,hall.getPrice());
        stm.setObject(5,hall.getDiscount());
        stm.setObject(6,hall.getStatus());
        stm.setObject(7,hall.getHallId());

        return stm.executeUpdate()>0;
    }

    public ArrayList<Hall> searchHallDetails(String hallId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Hall WHERE hallType LIKE '"+hallId+"%'");
        ResultSet resultSet = stm.executeQuery();

        ArrayList<Hall> hallList = new ArrayList();
        while(resultSet.next()){
            hallList.add(new Hall(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getBigDecimal(5),
                    resultSet.getBigDecimal(6),
                    resultSet.getString(7))
            );
        }
        return hallList;
    }

    public ArrayList<Hall> getAvailableHall(String checkIn,String checkOut) throws SQLException {

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("\n" +
                " SELECT * FROM Hall WHERE HallStatus='Active' AND hallId NOT IN (SELECT HallId FROM " +
                "HallReservation WHERE HallReservation.checkIn<=? AND HallReservation.checkOut>=? OR HallReservation.checkIn<=? AND HallReservation.checkOut>=? );\n");
        stm.setObject(1,checkIn);
        stm.setObject(2,checkIn);
        stm.setObject(3,checkOut);
        stm.setObject(4,checkOut);

        ArrayList<Hall> hallList = new ArrayList<>();
        ResultSet resultSet = stm.executeQuery();

        while (resultSet.next()){
            hallList.add(new Hall(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getBigDecimal(5),
                    resultSet.getBigDecimal(6),
                    resultSet.getString(7)
            ));
        }
        return hallList;


    }


}
