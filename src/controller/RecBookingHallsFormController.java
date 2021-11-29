package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.CustomerController;
import controller.dbController.HallController;
import controller.dbController.HallReservationController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import util.SendEmail;
import util.Validation;
import view.tm.HallTM;

import javax.xml.soap.Detail;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RecBookingHallsFormController {

    public Label lblDate;
    public Label lblTime;
    public TableView tblAvailableHall;
    public TableColumn colRoomId;
    public TableColumn colRoomType;
    public JFXDatePicker txtCheckDate;
    public JFXButton btnSelect;
    public Label lblHallId;
    public Label lblHallType;
    public Label lblDiscount;
    public Label lblPrice;
    public JFXTextField txtAdvance;
    public JFXButton btnReserve;
    public JFXDatePicker txtCheckIn;
    public JFXDatePicker txtCheckOut;
    public Label lblBalance;
    public Label lblCustomerId;
    public Label lblCustomerName;
    public TextField txtSearchCustomer;
    public Label lblReservationId;
    public JFXDatePicker txtCheckOutDate;
    public JFXButton btnView;

    int selectRow=-1;

    public void initialize(){
        loadDateAndTime();

        colRoomId.setCellValueFactory(new PropertyValueFactory<>("hallId"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("hallType"));

        tblAvailableHall.setItems(hallTMS);

        try {
            setReservationId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        tblAvailableHall.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectRow= (int) newValue;
            if(selectRow!=-1){
                btnSelect.setDisable(false);
                btnView.setDisable(false);
            }else{
                btnView.setDisable(true);
                btnSelect.setDisable(true);
            }
        });

        txtCheckIn.valueProperty().addListener((observable, oldValue, newValue) -> {
            isEmpty();
        });

        txtCheckOut.valueProperty().addListener((observable, oldValue, newValue) -> {
            isEmpty();
        });
    }

    private void setReservationId() throws SQLException {
        String reservationId = new HallReservationController().getReservationId();
        lblReservationId.setText(reservationId);
    }

    //Check all other textFields empty or not
    private void isEmpty() {
        if (txtCheckIn.getValue()==null || txtCheckOut.getValue()==null || txtAdvance.getText().isEmpty()
                ||  lblCustomerId.getText().isEmpty()){
            btnReserve.setDisable(true);
        }else{
            btnReserve.setDisable(false);
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

    public void openAddGuestOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/RecAddGuestForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("view/assets/images/hotel-icon.png"));
        stage.setTitle("Add New Guest");
        stage.setResizable(false);
        stage.show();
    }

    public void reserveOnAction(ActionEvent actionEvent) throws SQLException {

        HallReservation hallReservation = new HallReservation(
                lblReservationId.getText(),
                lblHallId.getText(),
                lblCustomerId.getText(),
                lblDate.getText(),
                lblTime.getText(),
                String.valueOf(txtCheckIn.getValue()),
                String.valueOf(txtCheckOut.getValue()),
                BigDecimal.valueOf(Double.parseDouble(txtAdvance.getText())),
                BigDecimal.valueOf(Double.parseDouble(lblPrice.getText())),
                "Pending"
        );

        if (new HallReservationController().makeReservation(hallReservation)){
            new Alert(Alert.AlertType.INFORMATION,"Successful").show();
            sendEmail();
            getAction();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Error.Try Again Latter").show();

        }
    }

    //send email to customer
    private void sendEmail() {

        EmailDetail detail = new EmailDetail(
                lblReservationId.getText(),
                lblCustomerName.getText(),
                String.valueOf(txtCheckIn.getValue()),
                String.valueOf(txtCheckOut.getValue()),
                txtAdvance.getText(),
                lblPrice.getText());
        new SendEmail(customerRecode.getCusEmail(),detail);
    }

    //After make reservation clear all textFields
    private void getAction() throws SQLException {
        setReservationId();

        JFXDatePicker[] pickers = new JFXDatePicker[]{txtCheckDate,txtCheckOutDate,txtCheckIn,txtCheckOut};
        Label[] label = new Label[]{lblCustomerId,lblCustomerName};
        Validation.clearLabel(label);
        Validation.clearDatePicker(pickers);

        hallTMS.clear();
        lblHallId.setText("Hall ID");
        lblHallType.setText("Hall Type");
        lblDiscount.setText("Hall Discount");
        lblPrice.setText("Price");
        lblBalance.setText("0.00");

        btnReserve.setDisable(true);
    }

    public void selectOnAction(ActionEvent actionEvent) {
        lblHallId.setText(hallTMS.get(selectRow).getHallId());
        lblHallType.setText(hallTMS.get(selectRow).getHallType());
        lblDiscount.setText(String.valueOf(hallTMS.get(selectRow).getDiscount()));
        lblPrice.setText(String.valueOf(hallTMS.get(selectRow).getPrice()));

        txtAdvance.setDisable(false);
        hallTMS.remove(selectRow);

        isEmpty();
    }

    Customer customerRecode;
    public void searchCustomerNicOnAction(ActionEvent actionEvent) throws SQLException {
        customerRecode = new CustomerController().getCustomerRecode(txtSearchCustomer.getText());
        if(customerRecode==null){
            new Alert(Alert.AlertType.INFORMATION,"Invalid customer NIC").show();
            return;
        }
        lblCustomerName.setText(customerRecode.getCusName());
        lblCustomerId.setText(customerRecode.getCusId());
        isEmpty();
    }

    public void advanceOnAction(ActionEvent actionEvent) {
        if(!txtAdvance.getText().matches("^[0-9]+$")) {
            txtAdvance.setStyle("-fx-border-color: red");
            return;
        }
        btnReserve.setDisable(false);
        double balance = Double.parseDouble(lblPrice.getText())-Double.parseDouble(txtAdvance.getText());
        lblBalance.setText(String.valueOf(String.format("%.2f",balance)));

        isEmpty();

    }

    ObservableList<HallTM>hallTMS = FXCollections.observableArrayList();
    public void loadAvailableHallOnAction(ActionEvent actionEvent) throws SQLException {
        if(txtCheckDate.getValue()==null || txtCheckOutDate.getValue()==null){
            return;
        }
        tblAvailableHall.getItems().clear();

        ArrayList<Hall> availableHall = new HallController().getAvailableHall(String.valueOf(txtCheckDate.getValue()),String.valueOf(txtCheckOutDate.getValue()));
        for (Hall hall:availableHall) {
            hallTMS.add(new HallTM(
                    hall.getHallId(),
                    hall.getHallFloor(),
                    hall.getHallType(),
                    hall.getDescription(),
                    hall.getPrice(),
                    hall.getDiscount()
            ));
        }
    }

    //View hall recode in for update
    public void viewHallDetailOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ViewHallDetailForm.fxml"));
        Parent load = loader.load();
        ViewHallDetailFormController controller = loader.<ViewHallDetailFormController>getController();
        controller.lblHallId.setText(hallTMS.get(selectRow).getHallId());
        controller.lblFloor.setText(String.valueOf(hallTMS.get(selectRow).getHallFloor()));
        controller.lblDescription.setText(hallTMS.get(selectRow).getDescription());
        controller.lblType.setText(hallTMS.get(selectRow).getHallType());
        controller.lblPrice.setText(hallTMS.get(selectRow).getPrice().toString());
        controller.lblDiscount.setText(hallTMS.get(selectRow).getDiscount().toString());
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setTitle("Hall Details");
        stage.setScene(scene);
        stage.show();
    }
}
