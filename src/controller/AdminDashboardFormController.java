package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import controller.dbController.HallReservationController;
import controller.dbController.HotelBillController;
import controller.dbController.RestaurantBillController;
import controller.dbController.RoomReservationController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.HotelBill;
import model.LoadDate;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

public class AdminDashboardFormController {
    public AnchorPane mainContext;
    public AnchorPane subContext;
    public Label lblDate;
    public Label lblTime;
    public Label lblTotalEarning;
    public Label lblPendingEarning;
    public Label lblRestaurantsEarning;
    public LineChart<String,Integer> lineChartHotel;
    public LineChart<String,Integer> lineChartRestaurants;
    public Label lblUserName;
    public JFXButton btnDashboard;
    public JFXButton btnRooms;
    public JFXButton btnHalls;
    public JFXButton btnServices;
    public JFXButton btnMealPackage;
    public JFXButton btnEmployee;
    public JFXButton btnComplain;
    public JFXButton btnUsers;

    LocalDate today = LocalDate.now();
    LocalDate firstDay = today.with(firstDayOfYear());
    LocalDate lastDay = today.with(lastDayOfYear());

    String []months = new String[ 12 ];

    public void initialize(){
        loadDateAndTime();

        try {

            setTotalEarning();
            setPendingEarning();
            setRestaurantEarning();
            loadHotelChart();
            loadRestaurantsChart();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Load restaurant chart with annual income
    private void  loadRestaurantsChart() throws SQLException {
        XYChart.Series<String,Integer> series = new XYChart.Series<>();


        ArrayList<HotelBill> annualIncome = new RestaurantBillController().getAnnualIncome(firstDay, lastDay);
        for (HotelBill bill:annualIncome) {
            for (String m:months) {
                if (bill.getDate().equals(m)){
                    series.getData().add(new XYChart.Data<String,Integer>(bill.getDate(),bill.getAmount().intValue()));
                    if (annualIncome.indexOf(bill)==annualIncome.size()-1){
                        break;
                    }
                }else{
                    series.getData().add(new XYChart.Data<String,Integer>(m,0));
                }
            }
        }
        series.setName("Total Earnings");
        lineChartRestaurants.getData().add(series);
        series.getNode().setStyle("-fx-stroke: blue");

    }

    //Load hotel chart with annual income
    private void  loadHotelChart() throws SQLException {
        months[ 0 ] = "January";
        months[ 1 ] = "February";
        months[ 2 ] = "March";
        months[ 3 ] = "April";
        months[ 4 ] = "May";
        months[ 5 ] = "June";
        months[ 6 ] = "July";
        months[ 7 ] = "August";
        months[ 8 ] = "September";
        months[ 9 ] = "October";
        months[ 10 ] = "November";
        months[ 11 ] = "December";

        XYChart.Series<String,Integer> series = new XYChart.Series<>();

        ArrayList<HotelBill> annualIncome = new HotelBillController().getAnnualIncome(firstDay,lastDay);

            for (HotelBill bill:annualIncome) {
                for (String m:months) {
                    if (bill.getDate().equals(m)){
                        series.getData().add(new XYChart.Data<String,Integer>(bill.getDate(),bill.getAmount().intValue()));
                        if (annualIncome.indexOf(bill)==annualIncome.size()-1){
                            break;
                        }
                    }else{
                        series.getData().add(new XYChart.Data<String,Integer>(m,0));
                    }
                }
            }

        series.setName("Total Earnings");
        lineChartHotel.getData().add(series);
        series.getNode().setStyle("-fx-stroke: blue");
    }

    //Get restaurant income
    private void setRestaurantEarning() throws SQLException {
        int totalIncome = new RestaurantBillController().getTotalIncome();
        if (totalIncome!=0){
            lblRestaurantsEarning.setText("Rs "+String.format("%,.2f",Double.valueOf(totalIncome)));
        }
    }

    //Get room+hall pending income
     private void setPendingEarning() throws SQLException {
        int roomPending = new RoomReservationController().getTotalRoomReservationPendingEarnings();
        int hallPending = new HallReservationController().getTotalHallReservationPendingEarnings();

        lblPendingEarning.setText("Rs "+String.format("%,.2f",Double.valueOf(roomPending+hallPending)));


    }

    //Get all income in hotel
    private void setTotalEarning() throws SQLException {

        int totalReservationIncome = new HotelBillController().getTotalReservationIncome();
        if(totalReservationIncome!=0){
            lblTotalEarning.setText("Rs "+String.format("%,.2f",Double.valueOf(totalReservationIncome)));
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

    //Get monthly income(hotel+restaurant)
    public void monthlyIncomeOnAction(ActionEvent actionEvent) throws SQLException {
        LocalDate today = LocalDate.now();
        LocalDate firstDay = today.withDayOfMonth(1);
        LocalDate lastDay=today.withDayOfMonth(today.lengthOfMonth());

        int monthlyTotal = new HotelBillController().getIncome(firstDay, lastDay);
        lblTotalEarning.setText("Rs "+String.format("%,.2f",Double.valueOf(monthlyTotal)));

        int roomPendingEarnings = new RoomReservationController().getRoomReservationPendingEarnings(firstDay, lastDay);
        int hallPendingEarnings = new HallReservationController().getHallReservationPendingEarnings(firstDay, lastDay);
        lblPendingEarning.setText("Rs "+String.format("%,.2f",Double.valueOf(roomPendingEarnings+hallPendingEarnings)));

        int income = new RestaurantBillController().getIncome(firstDay, lastDay);
        lblRestaurantsEarning.setText("Rs "+String.format("%,.2f",Double.valueOf(income)));

    }

    //Get annual income(hotel+restaurant)
    public void annuallyIncomeOnAction(ActionEvent actionEvent) throws SQLException {

        int annuallyTotal = new HotelBillController().getIncome(firstDay, lastDay);
        lblTotalEarning.setText("Rs "+String.format("%,.2f",Double.valueOf(annuallyTotal)));

        int roomPendingEarnings = new RoomReservationController().getRoomReservationPendingEarnings(firstDay, lastDay);
        int hallPendingEarnings = new HallReservationController().getHallReservationPendingEarnings(firstDay, lastDay);
        lblPendingEarning.setText("Rs "+String.format("%,.2f",Double.valueOf(roomPendingEarnings+hallPendingEarnings)));

        int income = new RestaurantBillController().getIncome(firstDay, lastDay);
        lblRestaurantsEarning.setText("Rs "+String.format("%,.2f", Double.valueOf(income)));
    }

    // -----------------------UI switching----------------------------------------

    private void loadUiForSubContext(String file) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/" + file + ".fxml"));
        subContext.getChildren().clear();
        subContext.getChildren().add(load);
    }

    public void openAdminDashboardOnAction(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AdminDashboardForm.fxml"));
        Parent load = loader.load();
        AdminDashboardFormController controller = loader.<AdminDashboardFormController>getController();
        controller.lblUserName.setText(lblUserName.getText());
        Scene scene = new Scene(load);
        Stage window = (Stage) mainContext.getScene().getWindow();
        window.setScene(scene);
    }

    public void openAdminRoomsOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnRooms);
        loadUiForSubContext("AdminRoomsForm");
    }

    public void openAdminHallsOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnHalls);
        loadUiForSubContext("AdminHallsForm");
    }

    public void openAdminServiceOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnServices);
        loadUiForSubContext("AdminServicesForm");
    }

    public void openAdminMealPackagesOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnMealPackage);
        loadUiForSubContext("AdminMealPackagesForm");
    }

    public void openAdminEmployeesOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnEmployee);
        loadUiForSubContext("AdminEmployeesForm");
    }

    public void openAdminUsersOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnUsers);
        loadUiForSubContext("AdminUsersForm");
    }

    public void openAdminComplainsOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnComplain);
        loadUiForSubContext("AdminComplainForm");
    }

    public void logOutOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"));
        Scene scene = new Scene(load);
        Stage window = (Stage) mainContext.getScene().getWindow();
        window.setScene(scene);
    }

    //Change btn color when in clicked
    private void btnChangeColor(Button btn) {
        btnDashboard.setStyle("-fx-background-color: transparent");
        btnRooms.setStyle("-fx-background-color:  transparent");
        btnHalls.setStyle("-fx-background-color:  transparent");
        btnMealPackage.setStyle("-fx-background-color:  transparent");
        btnServices.setStyle("-fx-background-color:  transparent");
        btnEmployee.setStyle("-fx-background-color:  transparent");
        btnComplain.setStyle("-fx-background-color:  transparent");
        btnUsers.setStyle("-fx-background-color:  transparent");

        btn.setStyle("-fx-background-color: #F24937;");


    }

    //-------------------------------------------------------------------------


}
