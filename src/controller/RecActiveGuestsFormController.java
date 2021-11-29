package controller;

import com.jfoenix.controls.JFXTextField;
import controller.dbController.HallReservationController;
import controller.dbController.RoomReservationController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.Customer;
import model.LoadDate;
import view.tm.CustomerTM;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class RecActiveGuestsFormController {

    public Label lblDate;
    public Label lblTime;
    public TableView tblGuests;
    public TableColumn colReservationId;
    public TableColumn colTitle;
    public TableColumn colName;
    public TableColumn colNic;
    public TableColumn colMobile;
    public TableColumn colEmail;
    public TableColumn colAddress;
    public TableColumn colProvince;

    public void initialize(){

        colReservationId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("cusTittle"));
        colName.setCellValueFactory(new PropertyValueFactory<>("cusName"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colMobile.setCellValueFactory(new PropertyValueFactory<>("cusNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("cusEmail"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("cusAddress"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("cusProvince"));

        tblGuests.setItems(customerTMS);

        loadDateAndTime();
        try {
            LoadGuestTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    ObservableList<CustomerTM>customerTMS = FXCollections.observableArrayList();
    private void LoadGuestTable() throws SQLException {
        ArrayList<Customer> roomCheckin = new RoomReservationController().getAllCheckInCustomers();

        for (Customer c : roomCheckin) {
            customerTMS.add(new CustomerTM(
                    c.getCusId(),
                    c.getCusTittle(),
                    c.getCusName(),
                    c.getNic(),
                    c.getCusNumber(),
                    c.getCusEmail(),
                    c.getCusAddress(),
                    c.getCusProvince()
            ));
        }

        ArrayList<Customer> hallCheckIn = new HallReservationController().getAllCheckInCustomers();
        for (Customer c : hallCheckIn) {
            customerTMS.add(new CustomerTM(
                    c.getCusId(),
                    c.getCusTittle(),
                    c.getCusName(),
                    c.getNic(),
                    c.getCusNumber(),
                    c.getCusEmail(),
                    c.getCusAddress(),
                    c.getCusProvince()
            ));

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
