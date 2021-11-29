package controller.dbController;

import db.DbConnection;
import model.Customer;
import model.Hall;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerController {

    public String getCustomerId() throws SQLException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT CustId FROM Customer ORDER BY custId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()){

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "CT-0000"+tempId;
            }else if(tempId<=99){
                return "CT-000"+tempId;
            }else if(tempId<=999){
                return "CT-00"+tempId;
            }else if(tempId<=9999){
                return "CT-0"+tempId;
            }else{
                return "CT-"+tempId;
            }

        }else{
            return "CT-00001";
        }
    }

    public boolean saveCustomerDetails(Customer customer) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO Customer VALUES (?,?,?,?,?,?,?,?)");
        stm.setObject(1,customer.getCusId());
        stm.setObject(2,customer.getCusTittle());
        stm.setObject(3,customer.getCusName());
        stm.setObject(4,customer.getNic());
        stm.setObject(5,customer.getCusNumber());
        stm.setObject(6,customer.getCusEmail());
        stm.setObject(7,customer.getCusAddress());
        stm.setObject(8,customer.getCusProvince());

        return stm.executeUpdate()>0;
    }

    public Customer getCustomerRecode(String cusNic) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer WHERE CustNIC=?");
        stm.setObject(1,cusNic);
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)
            );
        }else{
            return null;
        }
    }

    public Customer getCustomerDetails(String cusId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer WHERE CustId=?");
        stm.setObject(1,cusId);
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)
            );
        }else{
            return null;
        }
    }
}
