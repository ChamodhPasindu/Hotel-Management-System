package controller.dbController;

import db.DbConnection;
import model.HotelBill;

import javax.security.auth.callback.LanguageCallback;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RestaurantBillController {

    public String createBill() throws SQLException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT billId FROM orderBill ORDER BY billId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()) {

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId = tempId + 1;
            if (tempId <= 9) {
                return "B-0000" + tempId;
            } else if (tempId <= 99) {
                return "B-000" + tempId;
            } else if (tempId <= 999) {
                return "B-00" + tempId;
            } else if (tempId <= 9999) {
                return "B-0" + tempId;
            } else {
                return "B-" + tempId;
            }

        } else {
            return "B-00001";
        }
    }

    public int getTotalIncome() throws SQLException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(amount) FROM OrderBill").executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int getIncome(LocalDate first,LocalDate last) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(amount) FROM OrderBill JOIN Orders ON OrderBill.orderId=Orders.orderId WHERE date<=? AND date>=?");
        stm.setObject(1,last);
        stm.setObject(2,first);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public ArrayList<HotelBill> getAnnualIncome(LocalDate first,LocalDate last) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(amount),MONTHNAME(date) FROM OrderBill JOIN Orders ON OrderBill.orderId=Orders.orderId WHERE date<=? AND date>=? GROUP By MONTH(date)");
        stm.setObject(1,last);
        stm.setObject(2,first);
        ResultSet resultSet = stm.executeQuery();
        ArrayList<HotelBill>bills=new ArrayList<>();
        if (resultSet.next()){

            bills.add(new HotelBill(resultSet.getBigDecimal(1),
                    resultSet.getString(2)));
        }
        return bills;
    }

}
