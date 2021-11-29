package controller;

import com.jfoenix.controls.JFXButton;
import com.sun.deploy.panel.IProperty;
import controller.dbController.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import model.*;
import sun.security.pkcs.SigningCertificateInfo;
import view.tm.HallTM;
import view.tm.MealPackageTM;
import view.tm.RoomTM;
import view.tm.ServiceTM;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class RecDashboardFomController {
    public Pane pane;
    public Pane pane1;
    public AnchorPane subContext;
    public AnchorPane mainContext;
    public Label lblTime;
    public Label lblDate;
    public TableView tblMeal;
    public TableColumn colPackageName;
    public TableColumn colMealDescription;
    public TableView tblRoom;
    public TableColumn colRoomType;
    public TableColumn colRoomDescription;
    public TableView tblHall;
    public TableColumn colHallType;
    public TableColumn colHallDescription;
    public TableView tblService;
    public TableColumn colServiceName;
    public TableColumn colServiceDescription;
    public Label lblCheckIn;
    public Label lblTodayBooking;
    public Label lblTodayCheckOut;

    public JFXButton btnDashboard;
    public JFXButton btnBookingRoom;
    public JFXButton btnBookingHalls;
    public Label lblUserName;
    public JFXButton btnReservation;
    public JFXButton btnCheckIn;
    public JFXButton btnCheckOut;
    public JFXButton btnActiveGuests;
    public JFXButton btnMaintenance;
    public JFXButton btnComplain;
    public JFXButton btnAboutUs;

    public void initialize(){

        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colRoomDescription.setCellValueFactory(new PropertyValueFactory<>("roomDescription"));
        tblRoom.setItems(roomTMS);

        colHallType.setCellValueFactory(new PropertyValueFactory<>("hallType"));
        colHallDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tblHall.setItems(hallTMS);

        colServiceName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        colServiceDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tblService.setItems(serviceTMS);

        colPackageName.setCellValueFactory(new PropertyValueFactory<>("packageType"));
        colMealDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tblMeal.setItems(mealPackageTMS);

        loadDateAndTime();
        try {
            loadRoomTable();
            loadHallTable();
            loadServiceTable();
            loadMealTable();
            
            setTodayBooking();
            setTodayCheckIn();
            setTodayCheckOut();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setTodayCheckOut() throws SQLException {
        int roomCheckOut = new RoomReservationController().getTodayCheckOut(lblDate.getText());
        int hallCheckOut = new HallReservationController().getTodayCheckOut(lblDate.getText());
        lblTodayCheckOut.setText(String.valueOf(roomCheckOut+hallCheckOut));
    }

    private void setTodayCheckIn() throws SQLException {
        int roomCheckCheckIn = new RoomReservationController().getTodayCheckIn(lblDate.getText());
        int hallCheckIn = new HallReservationController().getTodayCheckIn(lblDate.getText());
        lblCheckIn.setText(String.valueOf(roomCheckCheckIn+hallCheckIn));
    }

    private void setTodayBooking() throws SQLException {
        int roomBooking = new RoomReservationController().getTodayBooking(lblDate.getText());
        int hallBooking = new HallReservationController().getTodayBooking(lblDate.getText());
        lblTodayBooking.setText(String.valueOf(roomBooking+hallBooking));
    }

    ObservableList<MealPackageTM>mealPackageTMS = FXCollections.observableArrayList();
    private void loadMealTable() throws SQLException {
        ArrayList<MealPackage> mealPackageDetails = new MealPackageController().getMealPackageDetails();
        for (MealPackage mealPackage:mealPackageDetails) {
            mealPackageTMS.add(new MealPackageTM(mealPackage.getPackageType(),mealPackage.getDescription()));
        }
    }

    ObservableList<ServiceTM>serviceTMS = FXCollections.observableArrayList();
    private void loadServiceTable() throws SQLException {
        ArrayList<Service> serviceDetails = new ServiceController().getServiceDetails();
        for (Service service:serviceDetails) {
            serviceTMS.add(new ServiceTM(service.getServiceName(),service.getDescription()));
        }
    }

    ObservableList<HallTM>hallTMS = FXCollections.observableArrayList();
    private void loadHallTable() throws SQLException {
        ArrayList<Hall> hallDetails = new HallController().getHallDetails();
        for (Hall hall:hallDetails) {
            hallTMS.add(new HallTM(hall.getHallType(),hall.getDescription()));
        }
    }

    ObservableList<RoomTM>roomTMS = FXCollections.observableArrayList();
    private void loadRoomTable() throws SQLException {
        ArrayList<Room> roomDetails = new RoomController().getRoomDetails();
        for (Room room:roomDetails) {
            roomTMS.add(new RoomTM(room.getRoomType(),room.getRoomDescription()));
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


    // -----------------------UI switching----------------------------------------
    private void loadUiForSubContext(String file) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/" + file + ".fxml"));
        subContext.getChildren().clear();
        subContext.getChildren().add(load);
    }

    public void openRecDashboardOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/RecDashboardFom.fxml"));
        Parent load = loader.load();
        RecDashboardFomController controller = loader.<RecDashboardFomController>getController();
        controller.lblUserName.setText(lblUserName.getText());
        Scene scene = new Scene(load);
        Stage window = (Stage) mainContext.getScene().getWindow();
        window.setScene(scene);
    }

    public void openRecBookingRoomsOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnBookingRoom);
        loadUiForSubContext("RecBookingRoomsForm");
    }

    public void openRecBookingHallsOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnBookingHalls);
        loadUiForSubContext("RecBookingHallsForm");
    }

    public void openRecReservationsOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnReservation);
        loadUiForSubContext("RecReservationsForm");
    }

    public void openRecCheckInOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnCheckIn);
        loadUiForSubContext("RecCheckInForm");
    }

    public void openRecCheckOutOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnCheckOut);
        loadUiForSubContext("RecCheckOutForm");
    }

    public void openRecActiveGuestsOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnActiveGuests);
        loadUiForSubContext("RecActiveGuestsForm");
    }

    public void openRecMaintenanceOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnMaintenance);
        loadUiForSubContext("RecMaintenanceForm");
    }

    public void logOutOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"));
        Scene scene = new Scene(load);
        Stage window = (Stage) mainContext.getScene().getWindow();
        window.setScene(scene);
    }

    public void openRecComplainsOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnComplain);
        loadUiForSubContext("RecComplainsForm");
    }

    public void openAboutUsOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnAboutUs);
        loadUiForSubContext("RecAboutUsForm");
    }

    //Change btn color when in clicked
    private void btnChangeColor(Button btn) {
        btnDashboard.setStyle("-fx-background-color: transparent");
        btnBookingRoom.setStyle("-fx-background-color:  transparent");
        btnBookingHalls.setStyle("-fx-background-color:  transparent");
        btnCheckIn.setStyle("-fx-background-color:  transparent");
        btnCheckOut.setStyle("-fx-background-color:  transparent");
        btnComplain.setStyle("-fx-background-color:  transparent");
        btnActiveGuests.setStyle("-fx-background-color:  transparent");
        btnReservation.setStyle("-fx-background-color:  transparent");
        btnMaintenance.setStyle("-fx-background-color:  transparent");
        btnAboutUs.setStyle("-fx-background-color:  transparent");


        btn.setStyle("-fx-background-color: #F24937;");

    }
    //------------------------------------------------------

}
