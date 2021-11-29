package controller.dbController;

import db.DbConnection;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class HallReservationController {

    public String getReservationId() throws SQLException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT reservationId FROM HallReservation ORDER BY reservationId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()) {

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId = tempId + 1;
            if (tempId <= 9) {
                return "HR-0000" + tempId;
            } else if (tempId <= 99) {
                return "HR-000" + tempId;
            } else if (tempId <= 999) {
                return "HR-00" + tempId;
            } else if (tempId <= 9999) {
                return "HR-0" + tempId;
            } else {
                return "HR-" + tempId;
            }

        } else {
            return "HR-00001";
        }
    }

    public boolean makeReservation(HallReservation reservation) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO HallReservation VALUES (?,?,?,?,?,?,?,?,?,?)");
        stm.setObject(1,reservation.getReservationId());
        stm.setObject(2,reservation.getHallId());
        stm.setObject(3,reservation.getCusId());
        stm.setObject(4,reservation.getReserveDate());
        stm.setObject(5,reservation.getReserveTime());
        stm.setObject(6,reservation.getCheckIn());
        stm.setObject(7,reservation.getCheckOut());
        stm.setObject(8,reservation.getAdvance());
        stm.setObject(9,reservation.getCost());
        stm.setObject(10,reservation.getStatus());

        return stm.executeUpdate()>0;
    }

    public ArrayList<HallReservation> getUpcomingReservation() throws SQLException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT HallReservation.reservationId,Customer.custName,HallReservation.reserveDate,HallReservation.reserveTime " +
                "FROM HallReservation JOIN Customer WHERE HallReservation.custId=Customer.custId AND HallReservation.status='Pending'").executeQuery();

        ArrayList<HallReservation> hallReservations = new ArrayList<>();
        while (resultSet.next()){
            hallReservations.add(new HallReservation(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }
        return hallReservations;
    }

    public ArrayList<HallReservation> SearchUpcomingReservation(String reservationId) throws SQLException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("\n" +
                "SELECT HallReservation.reservationId,Customer.custName,HallReservation.reserveDate,HallReservation.reserveTime\n" +
                "            FROM HallReservation JOIN Customer ON HallReservation.custId=Customer.custId AND HallReservation.status='Pending' AND HallReservation.reservationId LIKE '" + reservationId + "%'").executeQuery();


        ArrayList<HallReservation> hallReservations = new ArrayList<>();
        while (resultSet.next()){
            hallReservations.add(new HallReservation(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }
        return hallReservations;
    }

    public boolean deleteHallReservation(String reservationId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM HallReservation WHERE reservationId=?");
        stm.setObject(1,reservationId);
        return stm.executeUpdate()>0;
    }

    public int getTodayBooking(String date) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT Count(reservationId) AS NoOfCheckOut FROM HallReservation WHERE reserveDate=?");
        stm.setObject(1,date);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int getTodayCheckIn(String date) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT Count(reservationId) AS NoOfCheckOut FROM HallReservation WHERE checkIn=? AND (status='CheckIn' OR status='CheckOut')");
        stm.setObject(1,date);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int getTodayCheckOut(String date) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT Count(reservationId) AS NoOfCheckOut FROM HallReservation WHERE checkOut=? AND status='CheckOut'");
        stm.setObject(1,date);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public HallReservation searchReservationForAction(String reservationId,String status) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM HallReservation WHERE reservationId=? AND status=?");
        stm.setObject(1,reservationId);
        stm.setObject(2,status);


        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()){
            return new HallReservation(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getBigDecimal(8),
                    resultSet.getBigDecimal(9),
                    resultSet.getString(10)
            );
        }
        return null;
    }

    public boolean checkInCustomer(String reservationId,double payment) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE HallReservation SET advance=(advance+"+payment+"),status='CheckIn' WHERE reservationId='"+reservationId+"'");
        return stm.executeUpdate()>0;
    }

    public boolean checkOutCustomer(String reservationId, HotelBill bill) throws SQLException {
        Connection con= null;

        try {
            con = DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement stm = con.prepareStatement("UPDATE HallReservation SET checkOut=?, status='CheckOut' WHERE reservationId=?");
            stm.setObject(1,bill.getDate());
            stm.setObject(2,reservationId);

            if(stm.executeUpdate()>0){
                if(createHallBill(bill)){
                    con.commit();
                    return true;
                }else{
                    con.rollback();
                    return false;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            con.setAutoCommit(true);
        }
        return false;
    }

    public boolean createHallBill(HotelBill bill) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO Bill VALUES (?,?,?,?,?)");
        stm.setObject(1,bill.getBillId());
        stm.setObject(2,bill.getCusId());
        stm.setObject(3,bill.getDate());
        stm.setObject(4,bill.getDescription());
        stm.setObject(5,bill.getAmount());

        if (stm.executeUpdate()>0) {
            return true;
        }
        return false;
    }

    public ArrayList<Customer> getAllCheckInCustomers() throws SQLException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT HallReservation.reservationId,Customer.custTittle,Customer.custName\n" +
                "                ,Customer.custNIC,Customer.custNumber,Customer.custEmail,Customer.custAddress,Customer.custProvince FROM HallReservation JOIN Customer ON\n" +
                "                HallReservation.custId=Customer.custId WHERE status='CheckIn'").executeQuery();

        ArrayList<Customer> customers = new ArrayList<>();

        while (resultSet.next()){
            customers.add(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)
            ));
        }
        return customers;
    }

    public int getTotalHallReservationPendingEarnings() throws SQLException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(cost-advance) FROM HallReservation WHERE status='CheckIn' OR status='Pending'").executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int getHallReservationPendingEarnings(LocalDate first, LocalDate last) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(cost-advance) FROM HallReservation WHERE (status='CheckIn' OR status='Pending') AND (reserveDate<=? AND reserveDate>=?)");
        stm.setObject(1,last);
        stm.setObject(2,first);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public HallReservation getUpcomingReservationFullRecode(String reservationId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT reservationId,custId,checkIn,checkOut FROM HallReservation " +
                "WHERE reservationId=?");
        stm.setObject(1, reservationId);
        ResultSet resultSet = stm.executeQuery();

        while (resultSet.next()) {
            return new HallReservation(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
        }
        return null;
    }
}
