package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.RoomController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.LoadDate;
import model.Room;
import view.tm.RoomTM;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class RecMaintenanceFormController {
    public Label lblDate;
    public Label lblTime;
    public TextField txtSearchRoomFloor;
    public TableView tblRoomStatus;
    public TableColumn colRoomId;
    public TableColumn colType;
    public TableColumn colFloor;
    public TableColumn colDescription;
    public TableColumn colDiscount;
    public TableColumn colPrice;
    public TableColumn colStatus;
    public JFXButton btnActive;
    public JFXButton btnInActive;

    int selectRow=-1;

    public void initialize() throws SQLException {

        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colFloor.setCellValueFactory(new PropertyValueFactory<>("roomFloor"));
        colType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("roomPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("roomDiscount"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("roomDescription"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tblRoomStatus.setItems(roomTMS);

        btnActive.setDisable(true);
        btnInActive.setDisable(true);

        txtSearchRoomFloor.textProperty().addListener((observable, oldValue, newValue) -> {
            tblRoomStatus.getItems().clear();
            try {
                ArrayList<Room> rooms = new RoomController().searchRoomDetailsByRoomFloor(newValue);
                for (Room room:rooms) {
                    roomTMS.add(new RoomTM(
                            room.getRoomId(),
                            room.getRoomFloor(),
                            room.getRoomType(),
                            room.getRoomDescription(),
                            room.getRoomPrice(),
                            room.getRoomDiscount(),
                            room.getStatus()
                    ));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        });

        tblRoomStatus.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectRow= (int) newValue;
            if(selectRow!=-1){
                if(roomTMS.get(selectRow).getStatus().equals("Active")){
                    btnInActive.setDisable(false);
                    btnActive.setDisable(true);
                }else{
                    btnActive.setDisable(false);
                    btnInActive.setDisable(true);
                }
            }else {
                btnActive.setDisable(true);
                btnInActive.setDisable(true);
            }
        });

        loadDateAndTime();
        
        loadRoomStatusTable();
    }

    ObservableList<RoomTM>roomTMS = FXCollections.observableArrayList();
    private void loadRoomStatusTable() throws SQLException {
        tblRoomStatus.getItems().clear();

        ArrayList<Room> roomDetails = new RoomController().getRoomDetails();
        for (Room room :roomDetails) {
            roomTMS.add(new RoomTM(
                    room.getRoomId(),
                    room.getRoomFloor(),
                    room.getRoomType(),
                    room.getRoomDescription(),
                    room.getRoomPrice(),
                    room.getRoomDiscount(),
                    room.getStatus()
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

    //Update room status=active
    public void ActiveRoomOnAction(ActionEvent actionEvent) throws SQLException {

        if(new RoomController().changeRoomStatus(roomTMS.get(selectRow).getRoomId(),"Active")){
            loadRoomStatusTable();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try again latter").show();
        }
    }

    //Update room status=inactive
    public void InActiveRoomOnAction(ActionEvent actionEvent) throws SQLException {

        if(new RoomController().changeRoomStatus(roomTMS.get(selectRow).getRoomId(),"InActive")){
            loadRoomStatusTable();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try again latter").show();
        }
    }
}
