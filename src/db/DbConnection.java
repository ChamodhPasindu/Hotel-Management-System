package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection dbConnection=null;
    private Connection connection;

    private DbConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/Hotel",
                    "root",
                    "root1234");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static DbConnection getInstance() {
        if (dbConnection==null){
            dbConnection= new DbConnection();
        }
        return dbConnection;
    }

    public Connection getConnection(){
        return connection;
    }

}

