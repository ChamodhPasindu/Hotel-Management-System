package controller.dbController;

import db.DbConnection;
import model.Food;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FoodController {

    public String createFoodId() throws SQLException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT foodId FROM Food ORDER BY foodId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()){

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "F-00"+tempId;
            }else if(tempId<=99){
                return "F-0"+tempId;
            }else{
                return "F-"+tempId;
            }

        }else{
            return "F-001";
        }
    }

    public boolean addNewFood(Food food) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO Food VALUES (?,?,?)");
        stm.setObject(1,food.getFoodId());
        stm.setObject(2,food.getDescription());
        stm.setObject(3,food.getUnitPrice());

        return stm.executeUpdate()>0;
    }

    public ArrayList<Food> getFoods() throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Food");
        ResultSet resultSet = stm.executeQuery();
        ArrayList<Food> foods = new ArrayList<>();

        while (resultSet.next()){
            foods.add(new Food(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getBigDecimal(3)

            ));
        }
        return foods;
    }

    public boolean updateFood(Food food) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Food SET description=?,unitPrice=? WHERE foodId=?");
        stm.setObject(1,food.getDescription());
        stm.setObject(2,food.getUnitPrice());
        stm.setObject(3,food.getFoodId());
        return stm.executeUpdate()>0;
    }

    public boolean removeFood(String foodId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Food WHERE foodId=?");
        stm.setObject(1,foodId);
        return stm.executeUpdate()>0;
    }
}
