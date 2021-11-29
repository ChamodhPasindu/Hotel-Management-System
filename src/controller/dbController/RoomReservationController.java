package controller.dbController;

import db.DbConnection;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.DoubleBinaryOperator;

public class RoomReservationController {

    public String getReservationId() throws SQLException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT reservationId FROM RoomReservation ORDER BY reservationId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()) {

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId = tempId + 1;
            if (tempId <= 9) {
                return "RR-0000" + tempId;
            } else if (tempId <= 99) {
                return "RR-000" + tempId;
            } else if (tempId <= 999) {
                return "RR-00" + tempId;
            } else if (tempId <= 9999) {
                return "RR-0" + tempId;
            } else {
                return "RR-" + tempId;
            }

        } else {
            return "RR-00001";
        }
    }

    public boolean makeReservation(RoomReservation reservation) {
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            PreparedStatement stm = con.prepareStatement("INSERT INTO RoomReservation VALUES (?,?,?,?,?,?,?,?,?,?)");

            stm.setObject(1, reservation.getReservationId());
            stm.setObject(2, reservation.getCusId());
            stm.setObject(3, reservation.getReserveDate());
            stm.setObject(4, reservation.getReserveTime());
            stm.setObject(5, reservation.getCheckIn());
            stm.setObject(6, reservation.getCheckOut());
            stm.setObject(7, reservation.getAdvance());
            stm.setObject(8, reservation.getCost());
            stm.setObject(9, reservation.getStatus());
            stm.setObject(10, reservation.getNoOfGuest());

            if (stm.executeUpdate() > 0) {
                if (updateRoomDetail(reservation.getRoomDetails())) {
                    if (updateMealDetail(reservation.getMealPackageDetails())) {
                        if (updateServiceDetail(reservation.getServiceDetails())) {
                            con.commit();
                            return true;
                        } else {
                            con.rollback();
                            return false;
                        }
                    } else {
                        con.rollback();
                        return false;
                    }
                } else {
                    con.rollback();
                    return false;
                }
            } else {
                con.rollback();
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateRoomDetail(ArrayList<RoomDetail> roomDetails) throws SQLException {
        for (RoomDetail detail : roomDetails) {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO RoomDetail VALUES (?,?)");

            stm.setObject(1, detail.getRoomId());
            stm.setObject(2, detail.getReservationId());

            boolean isSave = stm.executeUpdate() > 0;
            if (!isSave) {
                return false;
            }
        }
        return true;
    }

    public boolean updateServiceDetail(ArrayList<ServiceDetail> serviceDetails) throws SQLException {

        for (ServiceDetail serviceDetail : serviceDetails) {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO ServiceDetail VALUES (?,?,?,?)");

            stm.setObject(1, serviceDetail.getReservationId());
            stm.setObject(2, serviceDetail.getServiceId());
            stm.setObject(3, serviceDetail.getNoOfDay());
            stm.setObject(4, serviceDetail.getPrice());

            boolean isSave = stm.executeUpdate() > 0;
            if (!isSave) {
                return false;
            }
        }
        return true;
    }

    public boolean updateMealDetail(ArrayList<MealPackageDetail> mealPackageDetails) throws SQLException {
        for (MealPackageDetail mealPackageDetail : mealPackageDetails) {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO MealPackageDetail VALUES (?,?,?,?,?,?)");

            stm.setObject(1, mealPackageDetail.getReservationId());
            stm.setObject(2, mealPackageDetail.getPackageId());
            stm.setObject(3, mealPackageDetail.getBreakfast());
            stm.setObject(4, mealPackageDetail.getLunch());
            stm.setObject(5, mealPackageDetail.getDinner());
            stm.setObject(6, mealPackageDetail.getPrice());

            boolean isSave = stm.executeUpdate() > 0;
            if (!isSave) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<RoomReservation> getUpcomingReservation() throws SQLException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT RoomReservation.reservationId,Customer.custName,RoomReservation.reserveDate,RoomReservation.reserveTime " +
                "FROM RoomReservation JOIN Customer ON RoomReservation.custId=Customer.custId WHERE RoomReservation.status='Pending'").executeQuery();
        ArrayList<RoomReservation> roomReservations = new ArrayList<>();

        while (resultSet.next()) {
            roomReservations.add(new RoomReservation(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }

        return roomReservations;
    }

    public ArrayList<RoomReservation> searchUpcomingReservation(String reservationId) throws SQLException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT RoomReservation.reservationId,Customer.custName,RoomReservation.reserveDate,RoomReservation.reserveTime\n" +
                "FROM RoomReservation JOIN Customer ON RoomReservation.custId=Customer.custId AND RoomReservation.status='Pending' AND RoomReservation.reservationId LIKE '" + reservationId + "%'").executeQuery();

        ArrayList<RoomReservation> roomReservations = new ArrayList<>();

        while (resultSet.next()) {
            roomReservations.add(new RoomReservation(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }
        return roomReservations;
    }

    public boolean deleteRoomReservation(String reservationId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM RoomReservation WHERE reservationId=?");
        stm.setObject(1, reservationId);
        return stm.executeUpdate() > 0;
    }

    public RoomReservation searchReservationForAction(String reservationId, String status) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM RoomReservation WHERE reservationId=? AND status=?");
        stm.setObject(1, reservationId);
        stm.setObject(2, status);


        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()) {
            return new RoomReservation(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getBigDecimal(7),
                    resultSet.getBigDecimal(8),
                    resultSet.getString(9),
                    resultSet.getString(10)
            );
        }
        return null;
    }

    public ArrayList<RoomDetail> getRoomDetail(String reservationId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM RoomDetail WHERE reservationId=?");
        stm.setObject(1, reservationId);
        ResultSet resultSet = stm.executeQuery();

        ArrayList<RoomDetail> details = new ArrayList<>();
        while (resultSet.next()) {
            details.add(new RoomDetail(
                    resultSet.getString(1),
                    resultSet.getString(2)
            ));
        }
        return details;
    }

    public ArrayList<MealPackageDetail> getMealPackageDetail(String reservationId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT MealPackageDetail.reservationId,MealPackage.packageType,MealPackageDetail.breakfast" +
                ",MealPackageDetail.lunch,MealPackageDetail.dinner,MealPackageDetail.price FROM MealPackageDetail JOIN MealPackage " +
                "ON MealPackageDetail.packageId=MealPackage.packageId WHERE reservationId=? ");
        stm.setObject(1, reservationId);
        ResultSet resultSet = stm.executeQuery();

        ArrayList<MealPackageDetail> details = new ArrayList<>();
        while (resultSet.next()) {
            details.add(new MealPackageDetail(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getInt(4),
                    resultSet.getInt(5),
                    resultSet.getBigDecimal(6)
            ));
        }
        return details;
    }

    public ArrayList<ServiceDetail> getServiceDetail(String reservationId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT ServiceDetail.reservationId,Service.serviceName,ServiceDetail.noOfDay,ServiceDetail.price" +
                " FROM ServiceDetail JOIN Service ON ServiceDetail.serviceId=Service.serviceId WHERE reservationId=?");
        stm.setObject(1, reservationId);
        ResultSet resultSet = stm.executeQuery();

        ArrayList<ServiceDetail> serviceDetails = new ArrayList<>();
        while (resultSet.next()) {
            serviceDetails.add(new ServiceDetail(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getBigDecimal(4)));

        }
        return serviceDetails;
    }

    public boolean checkInCustomer(String reservationId, double payment) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE RoomReservation SET advance=(advance+" + payment + "),status='CheckIn' WHERE reservationId='" + reservationId + "'");
        return stm.executeUpdate() > 0;
    }

    public boolean checkOutCustomer(String reservationId, HotelBill bill) throws SQLException {
        Connection con = null;

        try {
            con = DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement stm = con.prepareStatement("UPDATE RoomReservation SET checkOut=?, status='CheckOut' WHERE reservationId=?");
            stm.setObject(1, bill.getDate());
            stm.setObject(2, reservationId);

            if (stm.executeUpdate() > 0) {
                if (createRoomBill(bill)) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            con.setAutoCommit(true);
        }
        return false;
    }

    public boolean createRoomBill(HotelBill bill) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO Bill VALUES (?,?,?,?,?)");
        stm.setObject(1, bill.getBillId());
        stm.setObject(2, bill.getCusId());
        stm.setObject(3, bill.getDate());
        stm.setObject(4, bill.getDescription());
        stm.setObject(5, bill.getAmount());

        if (stm.executeUpdate() > 0) {
            return true;
        }
        return false;
    }

    public int getTodayBooking(String date) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT Count(reservationId) AS NoOfCheckOut FROM RoomReservation WHERE reserveDate=?");
        stm.setObject(1, date);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public ArrayList<Customer> getAllCheckInCustomers() throws SQLException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT RoomReservation.reservationId,Customer.custTittle,Customer.custName\n" +
                "                ,Customer.custNIC,Customer.custNumber,Customer.custEmail,Customer.custAddress,Customer.custProvince FROM RoomReservation JOIN Customer ON\n" +
                "                RoomReservation.custId=Customer.custId WHERE status='CheckIn'").executeQuery();

        ArrayList<Customer> customers = new ArrayList<>();

        while (resultSet.next()) {
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

    public int getTodayCheckIn(String date) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT Count(reservationId) AS NoOfCheckOut FROM RoomReservation WHERE checkIn=? AND (status='CheckIn' OR status='CheckOut')");
        stm.setObject(1, date);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int getTodayCheckOut(String date) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT Count(reservationId) AS NoOfCheckOut FROM RoomReservation WHERE checkOut=? AND status='CheckOut'");
        stm.setObject(1, date);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int getTotalRoomReservationPendingEarnings() throws SQLException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(cost-advance) FROM RoomReservation WHERE status='CheckIn' OR status='Pending'").executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int getRoomReservationPendingEarnings(LocalDate first, LocalDate last) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(cost-advance) FROM RoomReservation WHERE (status='CheckIn' OR status='Pending') AND (reserveDate<=? AND reserveDate>=?)");
        stm.setObject(1, last);
        stm.setObject(2, first);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public ArrayList<Room> getRoomDetailForBill(String reservationId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("\n" +
                "                    SELECT RoomDetail.roomId,Room.roomType,Room.roomPrice,Room.roomDiscount FROM RoomDetail JOIN Room ON RoomDetail.roomId=room.RoomId\n" +
                "                    WHERE RoomDetail.reservationId=?;");
        stm.setObject(1, reservationId);
        ResultSet resultSet = stm.executeQuery();
        ArrayList<Room> rooms = new ArrayList<>();
        while (resultSet.next()) {
            rooms.add(new Room(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getBigDecimal(3),
                    resultSet.getBigDecimal(4)
            ));
        }
        return rooms;
    }

    public ArrayList<MealPackageDetail> getMealDetailsForBill(String reservationId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("\n" +
                "                    SELECT MealPackage.packageType,MealPackageDetail.price FROM MealPackageDetail JOIN MealPackage \n" +
                "                    ON MealPackage.packageId=MealPackageDetail.packageId WHERE MealPackageDetail.reservationId=?;");
        stm.setObject(1,reservationId);
        ResultSet resultSet = stm.executeQuery();
        ArrayList<MealPackageDetail> details = new ArrayList<>();

        while (resultSet.next()){
            details.add(new MealPackageDetail(
                    resultSet.getString(1),
                    resultSet.getBigDecimal(2)
            ));
        }
        return details;
    }

    public ArrayList<ServiceDetail> getServiceDetailForBill(String reservationId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("\n" +
                "                    SELECT Service.serviceName,ServiceDetail.noOfDay,ServiceDetail.price\n" +
                "                FROM ServiceDetail JOIN Service ON ServiceDetail.serviceId=Service.serviceId WHERE ServiceDetail.reservationId=?;");
        stm.setObject(1,reservationId);
        ResultSet resultSet = stm.executeQuery();
        ArrayList<ServiceDetail> serviceDetails = new ArrayList<>();

        while (resultSet.next()){
            serviceDetails.add(new ServiceDetail(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getBigDecimal(3)
            ));
        }
        return serviceDetails;
    }

    public RoomReservation getUpcomingReservationFullRecode(String reservationId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT reservationId,custId,checkIn,checkOut FROM RoomReservation " +
                "WHERE reservationId=?");
        stm.setObject(1,reservationId);
        ResultSet resultSet = stm.executeQuery();

        while (resultSet.next()){
            return new RoomReservation(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
        }
        return null;
    }
}

