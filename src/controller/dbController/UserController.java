package controller.dbController;

import db.DbConnection;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    public boolean saveUserDetails(User user) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO Users(userName,userPassword,userRole) VALUES (?,?,?)");
        stm.setObject(1,user.getUserName());
        stm.setObject(2,user.getUserPassword());
        stm.setObject(3,user.getUserRole());

        return stm.executeUpdate()>0;
    }

    public int searchAccount(User user) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT userId FROM Users WHERE userName=? AND userPassword=? AND userRole=?");
        stm.setObject(1,user.getUserName());
        stm.setObject(2,user.getUserPassword());
        stm.setObject(3,user.getUserRole());
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return resultSet.getInt(1);
        }
        return -1;
    }

    public boolean updateUser(User user,int userId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Users SET UserName=?,userPassword=?,userRole=? WHERE userId=?");
        stm.setObject(1,user.getUserName());
        stm.setObject(2,user.getUserPassword());
        stm.setObject(3,user.getUserRole());
        stm.setObject(4,userId);

        return stm.executeUpdate()>0;
    }

    public String userLogin(String name,String password) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT userRole FROM Users WHERE userName=? AND userPassword=?");
        stm.setObject(1,name);
        stm.setObject(2,password);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
}

