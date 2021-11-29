package controller.dbController;

import db.DbConnection;
import model.Hall;
import model.MealPackage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MealPackageController {

    public String createMealPackageId() throws SQLException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT packageId FROM MealPackage ORDER BY packageId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()){

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "P-00"+tempId;
            }else if(tempId<=99){
                return "P-0"+tempId;
            }else{
                return "P-"+tempId;
            }

        }else{
            return "P-001";
        }
    }

    public boolean saveMealPackageDetail(MealPackage mealPackage) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO MealPackage VALUES(?,?,?,?,?)");
        stm.setObject(1,mealPackage.getPackageId());
        stm.setObject(2,mealPackage.getPackageType());
        stm.setObject(3,mealPackage.getDescription());
        stm.setObject(4,mealPackage.getPackagePrice());
        stm.setObject(5,mealPackage.getAvailability());

        return stm.executeUpdate()>0;
    }

    public ArrayList<MealPackage> getMealPackageDetails() throws SQLException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM MealPackage").executeQuery();
        ArrayList<MealPackage> packageList =new ArrayList();

        while(resultSet.next()){
            packageList.add(new MealPackage(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getBigDecimal(4),
                    resultSet.getString(5)
            ));
        }
        return packageList;
    }

    public boolean deletePackageDetails(String packageId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM MealPackage WHERE packageId=?");
        stm.setObject(1,packageId);
        return stm.executeUpdate()>0;

    }

    public MealPackage getPackageRecord(String packageId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM MealPackage WHERE packageId=?");
        stm.setObject(1,packageId);
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return new MealPackage(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getBigDecimal(4),
                    resultSet.getString(5)
            );
        }else{
            return null;
        }
    }

    public boolean updateMealPackageDetails(MealPackage mealPackage) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE MealPackage SET packageType=?,description=?," +
                "packagePrice=?,availability=? WHERE packageId=?");
        stm.setObject(1,mealPackage.getPackageType());
        stm.setObject(2,mealPackage.getDescription());
        stm.setObject(3,mealPackage.getPackagePrice());
        stm.setObject(4,mealPackage.getAvailability());
        stm.setObject(5,mealPackage.getPackageId());

        return stm.executeUpdate()>0;
    }

    public ArrayList<MealPackage> searchMealPackage(String packageType) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM MealPackage WHERE packageType LIKE '"+packageType+"%'");
        ResultSet resultSet = stm.executeQuery();

        ArrayList<MealPackage> packageList = new ArrayList();
        while(resultSet.next()){
            packageList.add(new MealPackage(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getBigDecimal(4),
                    resultSet.getString(5)
            ));
        }
        return packageList;
    }

    public MealPackage getPackagePrice(String packageType) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM MealPackage WHERE packageType=?");
        stm.setObject(1,packageType);
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return new MealPackage(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getBigDecimal(4),
                    resultSet.getString(5)
            );
        }else{
            return null;
        }
    }

}
