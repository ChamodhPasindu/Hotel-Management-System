package controller.dbController;

import db.DbConnection;
import model.Complain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComplainController {

    public String getComplainId() throws SQLException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT complainId FROM complain ORDER BY complainId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()){

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "CM-0000"+tempId;
            }else if(tempId<=99){
                return "CM-000"+tempId;
            }else if(tempId<=999){
                return "CM-00"+tempId;
            }else if(tempId<=9999){
                return "CM-0"+tempId;
            }else{
                return "CM-"+tempId;
            }

        }else{
            return "CM-00001";
        }
    }

    public ArrayList<Complain> getActiveComplain() throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("\n" +
                "\n" +
                "SELECT Complain.complainId, Customer.custName, Complain.date,Complain.time,Complain.description\n" +
                "FROM Complain JOIN Customer\n" +
                "ON Complain.custId = Customer.custId AND status='Active' ORDER BY Complain.complainId;");
        ResultSet resultSet = stm.executeQuery();
        ArrayList<Complain> complains = new ArrayList<>();

        while (resultSet.next()){
            complains.add(new Complain(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return complains;
    }

    public boolean addComplain(Complain complain) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO Complain VALUES (?,?,?,?,?,?)");
        stm.setObject(1,complain.getComplainId());
        stm.setObject(2,complain.getCusId());
        stm.setObject(3,complain.getDescription());
        stm.setObject(4,complain.getTime());
        stm.setObject(5,complain.getDate());
        stm.setObject(6,complain.getStatus());

        return stm.executeUpdate()>0;
    }

    public boolean fixComplain(String complainId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Complain SET status='InActive' WHERE complainId=?");
        stm.setObject(1,complainId);
        return stm.executeUpdate()>0;
    }


}
