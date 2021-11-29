package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.CustomerController;
import controller.dbController.HallController;
import controller.dbController.HallReservationController;
import controller.dbController.RoomReservationController;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import view.tm.HallReservationTM;
import view.tm.RoomReservationTM;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class RecReservationsFormController {
    public Label lblDate;
    public Label lblTime;
    public TableView tblRoomDetail;
    public TableColumn colRoomReservationId;
    public TableColumn colRoomReserveCusId;
    public TableColumn colRoomReserveDate;
    public TableColumn colRoomReserveTime;
    public JFXButton btnDeleteRoomReservation;
    public TableView tblHallDetail;
    public TableColumn colHallReservationId;
    public TableColumn colHallReserveCusId;
    public TableColumn colHallReserveDate;
    public TableColumn colHallReserveTime;
    public JFXButton btnDeleteHallReservation;
    public TextField txtRoomReservationId;
    public TextField txtHallReservationId;
    public JFXButton btnViewRoomReservation;
    public JFXButton btnViewHallReservation;

    int selectRoomRow=-1;
    int selectHallRow=-1;

    public void initialize(){

        colRoomReservationId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        colRoomReserveCusId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colRoomReserveDate.setCellValueFactory(new PropertyValueFactory<>("reserveDate"));
        colRoomReserveTime.setCellValueFactory(new PropertyValueFactory<>("reserveTime"));

        tblRoomDetail.setItems(roomReservationTMS);

        colHallReservationId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        colHallReserveCusId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colHallReserveDate.setCellValueFactory(new PropertyValueFactory<>("reserveDate"));
        colHallReserveTime.setCellValueFactory(new PropertyValueFactory<>("reserveTime"));

        tblHallDetail.setItems(hallReservationTMS);

        btnDeleteRoomReservation.setDisable(true);
        btnDeleteHallReservation.setDisable(true);

        loadDateAndTime();

        try {
            loadRoomDetailTable();
            loadHallDetailTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        txtRoomReservationId.textProperty().addListener((observable, oldValue, newValue) -> {
            tblRoomDetail.getItems().clear();

            try {
                ArrayList<RoomReservation> roomReservations = new RoomReservationController().searchUpcomingReservation(newValue);
                for (RoomReservation reservation:roomReservations ){
                    roomReservationTMS.add(new RoomReservationTM(reservation.getReservationId(),
                            reservation.getCusId(),
                            reservation.getReserveDate(),
                            reservation.getReserveTime()));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        txtHallReservationId.textProperty().addListener((observable, oldValue, newValue) -> {
            tblHallDetail.getItems().clear();

            try {
                ArrayList<HallReservation> hallReservations = new HallReservationController().SearchUpcomingReservation(newValue);
                for (HallReservation reservation:hallReservations) {
                    hallReservationTMS.add(new HallReservationTM(
                            reservation.getReservationId(),
                            reservation.getCusId(),
                            reservation.getReserveDate(),
                            reservation.getReserveTime()
                    ));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });

        tblRoomDetail.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectRoomRow= (int) newValue;
            btnDeleteRoomReservation.setDisable(false);
            btnViewRoomReservation.setDisable(false);

        });

        tblHallDetail.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectHallRow= (int) newValue;
            btnDeleteHallReservation.setDisable(false);
            btnViewHallReservation.setDisable(false);


        });
    }

    ObservableList<HallReservationTM>hallReservationTMS = FXCollections.observableArrayList();
    private void loadHallDetailTable() throws SQLException {
        ArrayList<HallReservation> hallReservations = new HallReservationController().getUpcomingReservation();

        for (HallReservation reservation:hallReservations) {
            hallReservationTMS.add(new HallReservationTM(
                    reservation.getReservationId(),
                    reservation.getCusId(),
                    reservation.getReserveDate(),
                    reservation.getReserveTime()
            ));
        }
    }

    ObservableList<RoomReservationTM> roomReservationTMS = FXCollections.observableArrayList();
    private void loadRoomDetailTable() throws SQLException {
        ArrayList<RoomReservation> roomReservations = new RoomReservationController().getUpcomingReservation();

        for (RoomReservation reservation:roomReservations) {
            roomReservationTMS.add(new RoomReservationTM(reservation.getReservationId(),
                    reservation.getCusId(),
                    reservation.getReserveDate(),
                    reservation.getReserveTime()));
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

    public void deleteRoomReservationOnAction(ActionEvent actionEvent) throws SQLException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION
                , "Do you want to Delete this Room Reservation recode?", yes, no);
        alert.setTitle("Delete Confirmation");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if(new RoomReservationController().deleteRoomReservation(roomReservationTMS.get(selectRoomRow).getReservationId())){
                roomReservationTMS.remove(selectRoomRow);
                btnDeleteRoomReservation.setDisable(true);

            }else{
                new Alert(Alert.AlertType.WARNING,"Error,Try Again").show();
            }
        }
    }

    public void deleteHallReservationOnAction(ActionEvent actionEvent) throws SQLException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION
                , "Do you want to Delete this Hall Reservation recode?", yes, no);
        alert.setTitle("Delete Confirmation");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if(new HallReservationController().deleteHallReservation(hallReservationTMS.get(selectHallRow).getReservationId())){
                hallReservationTMS.remove(selectHallRow);
                btnDeleteHallReservation.setDisable(true);

            }else{
                new Alert(Alert.AlertType.WARNING,"Error,Try Again").show();
            }
        }
    }

    //View upcoming room reservation in new window
    public void viewRoomReservationOnAction(ActionEvent actionEvent) throws IOException, SQLException {
        RoomReservation recode = new RoomReservationController().getUpcomingReservationFullRecode(roomReservationTMS.get(selectRoomRow).getReservationId());
        Customer customer = new CustomerController().getCustomerDetails(recode.getCusId());



        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ViewRoomReservationForm.fxml"));
        Parent load = loader.load();
        ViewRoomReservationFormController controller = loader.<ViewRoomReservationFormController>getController();

        controller.lblReservationId.setText(recode.getReservationId());
        controller.lblCusId.setText(recode.getCusId());
        controller.lblCheckIn.setText(recode.getReserveDate());
        controller.lblCheckOut.setText(recode.getReserveTime());

        controller.lblName.setText(customer.getCusName());
        controller.lblAddress.setText(customer.getCusAddress());
        controller.lblEmail.setText(customer.getCusEmail());
        controller.lblNic.setText(customer.getNic());
        controller.lblNumber.setText(customer.getCusNumber());
        controller.lblProvince.setText(customer.getCusProvince());

        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setTitle("Room Reservation");
        stage.setScene(scene);
        stage.show();
    }

    //View upcoming hall reservation in new window
    public void viewHallReservationOnAction(ActionEvent actionEvent) throws IOException, SQLException {
        HallReservation recode = new HallReservationController().getUpcomingReservationFullRecode(hallReservationTMS.get(selectHallRow).getReservationId());
        Customer customer = new CustomerController().getCustomerDetails(recode.getCusId());


        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ViewHallReservationForm.fxml"));
        Parent load = loader.load();
        ViewHallReservationFormController controller = loader.<ViewHallReservationFormController>getController();

        controller.lblReservationId.setText(recode.getReservationId());
        controller.lblCusId.setText(recode.getCusId());
        controller.lblCheckIn.setText(recode.getReserveDate());
        controller.lblCheckOut.setText(recode.getReserveTime());

        controller.lblName.setText(customer.getCusName());
        controller.lblAddress.setText(customer.getCusAddress());
        controller.lblEmail.setText(customer.getCusEmail());
        controller.lblNic.setText(customer.getNic());
        controller.lblNumber.setText(customer.getCusNumber());
        controller.lblProvince.setText(customer.getCusProvince());

        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setTitle("Hall Reservation");
        stage.setScene(scene);
        stage.show();
    }
}
