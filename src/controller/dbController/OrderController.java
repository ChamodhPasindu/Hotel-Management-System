package controller.dbController;

import db.DbConnection;
import model.Order;
import model.OrderBill;
import model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderController {

    public String createOrderId() throws SQLException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT orderId FROM Orders ORDER BY orderId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()) {

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId = tempId + 1;
            if (tempId <= 9) {
                return "O-0000" + tempId;
            } else if (tempId <= 99) {
                return "O-000" + tempId;
            } else if (tempId <= 999) {
                return "O-00" + tempId;
            } else if (tempId <= 9999) {
                return "O-0" + tempId;
            } else {
                return "O-" + tempId;
            }

        } else {
            return "O-00001";
        }

    }

    public boolean makeOrder(Order order) {
        Connection con= null;
        try {
            con = DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement stm = con.prepareStatement("INSERT INTO Orders VALUES (?,?)");
            stm.setObject(1, order.getOrderId());
            stm.setObject(2, order.getDate());

            if (stm.executeUpdate() > 0) {
                if (createOrderBill(order.getOrderBill())) {
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    public boolean createOrderBill(OrderBill bill) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO OrderBill VALUES (?,?,?)");
        stm.setObject(1, bill.getBillId());
        stm.setObject(2, bill.getOrderId());
        stm.setObject(3, bill.getAmount());

        if (stm.executeUpdate() > 0) {
            if (saveOrderDetail(bill.getOrderDetails())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean saveOrderDetail(ArrayList<OrderDetail> orderDetails) throws SQLException {
        for (OrderDetail detail : orderDetails) {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO OrderDetail VALUES(?,?,?,?)");
            stm.setObject(1, detail.getOrderId());
            stm.setObject(2, detail.getFoodId());
            stm.setObject(3, detail.getQty());
            stm.setObject(4, detail.getPrice());

            boolean isSave = stm.executeUpdate() > 0;
            if (!isSave) {
                return false;
            }
        }
        return true;
    }

    public int todayOrderCount(String date) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT COUNT(orderId) AS TodayOrders FROM Orders WHERE date=?");
        stm.setObject(1,date);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int todayEarnings(String date) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(amount) AS todayEarn FROM OrderBill JOIN Orders ON " +
                "OrderBill.orderId=Orders.orderId WHERE Orders.date=? ");
        stm.setObject(1,date);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public String getLeastMovableFood() throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(" SELECT Food.description, SUM(quantity) AS \"Total sales\"\n" +
                "FROM orderDetail JOIN Food ON OrderDetail.foodId=Food.foodId\n" +
                "GROUP BY food.description ORDER BY food.description ASC LIMIT 1");
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }

    public String getMostMovableFood() throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("\n" +
                "    SELECT Food.description, SUM(quantity) AS \"Total sales\"\n" +
                "FROM orderDetail JOIN Food ON OrderDetail.foodId=Food.foodId\n" +
                "GROUP BY food.description ORDER BY food.description DESC LIMIT 1 ;");
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
}
