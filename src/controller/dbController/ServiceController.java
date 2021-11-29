package controller.dbController;

import db.DbConnection;
import model.Hall;
import model.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceController {

    public String createServiceId() throws SQLException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT serviceId FROM Service ORDER BY serviceId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()){

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "S-00"+tempId;
            }else if(tempId<=99){
                return "S-0"+tempId;
            }else{
                return "S-"+tempId;
            }

        }else{
            return "S-001";
        }
    }

    public boolean saveServiceDetails(Service service) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO Service VALUES (?,?,?,?)");
        stm.setObject(1,service.getServiceId());
        stm.setObject(2,service.getServiceName());
        stm.setObject(3,service.getDescription());
        stm.setObject(4,service.getServicePrice());

        return stm.executeUpdate()>0;
    }

    public ArrayList<Service> getServiceDetails() throws SQLException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Service").executeQuery();

        ArrayList<Service> serviceList = new ArrayList();
        while(resultSet.next()){
            serviceList.add(new Service(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getBigDecimal(4)
            ));
        }
        return serviceList;
    }

    public Service getServiceRecord(String serviceId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Service WHERE serviceId=?");
        stm.setObject(1,serviceId);
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return new Service(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getBigDecimal(4)
            );
        }else{
            return null;
        }
    }

    public boolean deleteServiceDetails(String serviceId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Service WHERE serviceId=?");
        stm.setObject(1,serviceId);
        return stm.executeUpdate()>0;
    }

    public boolean updateServiceDetails(Service service) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Service SET serviceName=?,description=?,servicePrice=? WHERE serviceId=?");
        stm.setObject(1,service.getServiceName());
        stm.setObject(2,service.getDescription());
        stm.setObject(3,service.getServicePrice());
        stm.setObject(4,service.getServiceId());

        return stm.executeUpdate()>0;
    }

    public ArrayList<Service> searchServiceDetails(String serviceName) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Service WHERE serviceName LIKE '"+serviceName+"%'");
        ResultSet resultSet = stm.executeQuery();

        ArrayList<Service> serviceList = new ArrayList();
        while(resultSet.next()){
            serviceList.add(new Service(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getBigDecimal(4)
            ));
        }
        return serviceList;
    }

    public Service getServicePrice(String serviceName) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Service WHERE serviceName=?");
        stm.setObject(1,serviceName);
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return new Service(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getBigDecimal(4)
            );
        }else{
            return null;
        }
    }
}
