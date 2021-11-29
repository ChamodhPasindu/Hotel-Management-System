package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.ComplainController;
import controller.dbController.CustomerController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.Complain;
import model.Customer;
import model.LoadDate;
import view.tm.ComplainTM;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class RecComplainsFormController {

    public Label lblTime;
    public Label lblDate;
    public Label lblCustomerId;
    public Label lblCustomerName;
    public JFXButton btnSubmit;
    public Label lblComplainId;
    public JFXTextField txtDescription;
    public TableView<ComplainTM> tblComplainList;
    public TableColumn colComplainId;
    public TableColumn colCustomerName;
    public TableColumn colDescription;
    public TextField txtSearchNic;
    public TableColumn colDate;
    public TableColumn colTime;
    public Label lblEmail;

    public void initialize() {

        loadDateAndTime();

        colComplainId.setCellValueFactory(new PropertyValueFactory<>("complainId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        tblComplainList.setItems(complainTMS);

        try {
            loadComplainTable();
            setComplainId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        txtDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[A-z0-9\\,-. ]{3,50}$")){
                txtDescription.setStyle("-fx-border-color:blue");
                btnSubmit.setDisable(false);
            }else {
                txtDescription.setStyle("-fx-border-color: red");
                btnSubmit.setDisable(true);
            }
        });

    }

    private void setComplainId() throws SQLException {
        String complainId = new ComplainController().getComplainId();
        lblComplainId.setText(complainId);
    }

    ObservableList<ComplainTM>complainTMS = FXCollections.observableArrayList();
    private void loadComplainTable() throws SQLException {
        tblComplainList.getItems().clear();

        ArrayList<Complain> activeComplain = new ComplainController().getActiveComplain();
        for (Complain complain:activeComplain) {
            complainTMS.add(new ComplainTM(complain.getComplainId(),
                    complain.getCusId(),
                    complain.getTime(),
                    complain.getDate(),
                    complain.getDescription()));
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

    public void submitComplainOnAction(ActionEvent actionEvent) throws SQLException {

        Complain complain = new Complain(
                lblComplainId.getText(),
                lblCustomerId.getText(),
                txtDescription.getText(),
                lblTime.getText(),
                lblDate.getText(),
                "Active"
        );

        if(new ComplainController().addComplain(complain)){
            loadComplainTable();
            setComplainId();
            getAction();
        }else {
            new Alert(Alert.AlertType.INFORMATION,"Try again latter").show();
        }

    }

    //After add complain clear all textFields
    private void getAction() {
        lblCustomerId.setText("Customer ID");
        lblCustomerName.setText("Customer Name");
        lblEmail.setText("Email");

        txtDescription.clear();
        btnSubmit.setDisable(true);
        txtDescription.setDisable(true);
    }

    public void searchCustomerOnAction(ActionEvent actionEvent) throws SQLException {
        Customer customerRecode = new CustomerController().getCustomerRecode(txtSearchNic.getText());
        if(customerRecode==null){
            new Alert(Alert.AlertType.INFORMATION,"Invalid Customer NIC").show();
            return;
        }
        txtDescription.setDisable(false);
        lblCustomerId.setText(customerRecode.getCusId());
        lblCustomerName.setText(customerRecode.getCusName());
        lblEmail.setText(customerRecode.getCusEmail());
    }
}
