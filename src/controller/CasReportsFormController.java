package controller;

import controller.dbController.OrderController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import model.LoadDate;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CasReportsFormController {

    public Label lblDate;
    public Label lblTime;
    public Label lblTodayEarnings;
    public Label lblMovableFood;
    public Label lblLeastMovableFood;
    public Label lblTodayOrders;

    public void initialize(){

        loadDateAndTime();
        try {
            setTodayOrders();
            setMostMovableItem();
            setLeastMovableItem();
            setTodayEarnings();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*get today orders,income,most movable item and least movable item*/

    private void setTodayOrders() throws SQLException {
        int orders = new OrderController().todayOrderCount(lblDate.getText());
        if (orders!=0){
            lblTodayOrders.setText(String.valueOf(orders));
        }
    }

    private void setLeastMovableItem() throws SQLException {
        String leastMovableFood = new OrderController().getLeastMovableFood();
        if(!leastMovableFood.isEmpty()){
            lblLeastMovableFood.setText(leastMovableFood);
        }
    }

    private void setMostMovableItem() throws SQLException {
        String mostMovableFood = new OrderController().getMostMovableFood();
        if(!mostMovableFood.isEmpty()){
            lblMovableFood.setText(mostMovableFood);
        }
    }

    private void setTodayEarnings() throws SQLException {
        int earn = new OrderController().todayEarnings(lblDate.getText());
        if (earn!=0){
            lblTodayEarnings.setText("Rs "+String.format("%,.2f",Double.valueOf(earn)));
        }

    }

    private void loadDateAndTime() {
        // load Date
        lblDate.setText(LoadDate.loadDate());

        // load Time
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            lblTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}
