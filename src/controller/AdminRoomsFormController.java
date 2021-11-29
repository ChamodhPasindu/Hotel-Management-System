package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.RoomController;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.LoadDate;
import model.Room;
import util.LoadTableEvent;
import util.Validation;
import view.tm.RoomTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class AdminRoomsFormController implements LoadTableEvent {

    public Label lblDate;
    public Label lblTime;
    public Label lblRoomId;
    public JFXTextField txtRoomType;
    public JFXTextField txtFloor;
    public JFXTextField txtDiscount;
    public JFXTextField txtPrice;
    public JFXTextField txtDescription;
    public JFXComboBox<String> cmbStatus;
    public TableView<RoomTM> tblRoomList;
    public TableColumn colRoomId;
    public TableColumn colType;
    public TableColumn colFloor;
    public TableColumn colDiscount;
    public TableColumn colPrice;
    public TableColumn colStatus;
    public JFXButton btnClear;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXButton btnView;
    public TextField txtSearchRoom;

    int selectRow=-1;

    public void initialize(){

        loadDateAndTime();
        disableAllComponents();

        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colFloor.setCellValueFactory(new PropertyValueFactory<>("roomFloor"));
        colType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("roomPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("roomDiscount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tblRoomList.setItems(roomTMS);


        txtSearchRoom.textProperty().addListener((observable, oldValue, newValue) -> {
            tblRoomList.getItems().clear();
            try {
                ArrayList<Room> rooms = new RoomController().searchRoomDetails(newValue);
                for (Room room:rooms) {
                    roomTMS.add(new RoomTM(
                            room.getRoomId(),
                            room.getRoomFloor(),
                            room.getRoomType(),
                            room.getRoomPrice(),
                            room.getRoomDiscount(),
                            room.getStatus()
                    ));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        });

        txtFloor.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[0-9]{1,4}$")){
                txtFloor.setStyle("-fx-border-color:blue");
                btnUpdate.setDisable(false);
            }else {
                txtFloor.setStyle("-fx-border-color: red");
                btnUpdate.setDisable(true);
            }
        });

        txtRoomType.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[A-z ]{3,20}$")){
                txtRoomType.setStyle("-fx-border-color:blue");
                btnUpdate.setDisable(false);
            }else {
                txtRoomType.setStyle("-fx-border-color: red");
                btnUpdate.setDisable(true);
            }
        });

        txtDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[A-z ]{3,50}$")){
                txtDescription.setStyle("-fx-border-color:blue");
                btnUpdate.setDisable(false);
            }else {
                txtDescription.setStyle("-fx-border-color: red");
                btnUpdate.setDisable(true);
            }
        });

        txtPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[1-9][0-9]*([.][0-9]{2})?$")){
                txtPrice.setStyle("-fx-border-color:blue");
                btnUpdate.setDisable(false);
            }else {
                txtPrice.setStyle("-fx-border-color: red");
                btnUpdate.setDisable(true);
            }
        });

        txtDiscount.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("[0-9][0-9]*([.][0-9]{2})?$")){
                txtDiscount.setStyle("-fx-border-color:blue");
                btnUpdate.setDisable(false);
            }else {
                txtDiscount.setStyle("-fx-border-color: red");
                btnUpdate.setDisable(true);
            }
        });

        cmbStatus.getItems().addAll("Active","InActive");

        try {
            loadRoomListTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        tblRoomList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->{
            selectRow= (int) newValue;
            if(selectRow!=-1){
                btnView.setDisable(false);
                btnDelete.setDisable(false);
            }
        } );
    }

    ObservableList<RoomTM>roomTMS= FXCollections.observableArrayList();
    public void loadRoomListTable() throws SQLException {
        tblRoomList.getItems().clear();

        ArrayList<Room> roomDetails = new RoomController().getRoomDetails();
        for (Room room :roomDetails) {
            roomTMS.add(new RoomTM(
                    room.getRoomId(),
                    room.getRoomFloor(),
                    room.getRoomType(),
                    room.getRoomPrice(),
                    room.getRoomDiscount(),
                    room.getStatus()
            ));
        }

    }

    //Disable btn and some textFields
    private void disableAllComponents() {
        lblRoomId.setDisable(true);
        txtFloor.setDisable(true);
        txtRoomType.setDisable(true);
        txtDescription.setDisable(true);
        txtPrice.setDisable(true);
        txtDiscount.setDisable(true);
        cmbStatus.setDisable(true);

        btnDelete.setDisable(true);
        btnView.setDisable(true);
        btnUpdate.setDisable(true);
    }

    //Enable btn and some textFields
    private void enableAllComponents() {
        lblRoomId.setDisable(false);
        txtRoomType.setDisable(false);
        txtFloor.setDisable(false);
        txtDescription.setDisable(false);
        txtPrice.setDisable(false);
        txtDiscount.setDisable(false);
        cmbStatus.setDisable(false);
        btnUpdate.setDisable(false);
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

    public void openAddRoomOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AdminAddRoomForm.fxml"));
        Parent load = loader.load();
        AdminAddRoomFormController controller = loader.getController();
        controller.setEvent(this);
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("view/assets/images/hotel-icon.png"));
        stage.setTitle("Add New Room");
        stage.setResizable(false);
        stage.show();
    }

    //Update room details
    public void updateRoomOnAction(ActionEvent actionEvent) throws SQLException {

        Room updateRoom = new Room(
                lblRoomId.getText(),
                Integer.parseInt(txtFloor.getText()),
                txtRoomType.getText(),
                txtDescription.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtPrice.getText())),
                BigDecimal.valueOf(Float.parseFloat(txtDiscount.getText())),
                cmbStatus.getValue()
        );

        if(new RoomController().updateRoomDetails(updateRoom)){
            new Alert(Alert.AlertType.INFORMATION,"Room Updated Successfully").show();
            loadRoomListTable();
            disableAllComponents();
            getAction();

        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try Again Latter").show();
        }
    }

    //After update,clear window
    private void getAction() {
        JFXTextField[] fields = new JFXTextField[]{txtFloor,txtDiscount,txtPrice,txtDescription,txtRoomType};
        Validation.clearText(fields);

        lblRoomId.setText("Room ID");
        cmbStatus.getSelectionModel().clearSelection();

    }

    //Delete room
    public void deleteRoomOnAction(ActionEvent actionEvent) throws SQLException {

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION
                , "Do you want to Delete this Room recode?", yes, no);
        alert.setTitle("Delete Confirmation");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if(new RoomController().deleteRoomDetails(roomTMS.get(selectRow).getRoomId())){
                roomTMS.remove(selectRow);
                btnDelete.setDisable(true);
                btnView.setDisable(true);

            }else{
                new Alert(Alert.AlertType.WARNING,"Error,Try Again").show();
            }
        }
    }

    //View room recode in for update
    public void viewRoomDetailsOnAction(ActionEvent actionEvent) throws SQLException {
        enableAllComponents();
        btnDelete.setDisable(true);

        Room roomRecord = new RoomController().getRoomRecord(roomTMS.get(selectRow).getRoomId());

        lblRoomId.setText(roomRecord.getRoomId());
        txtFloor.setText(String.valueOf(roomRecord.getRoomFloor()));
        txtRoomType.setText(roomRecord.getRoomType());
        txtDescription.setText(roomRecord.getRoomDescription());
        txtPrice.setText(String.valueOf(roomRecord.getRoomPrice()));
        txtDiscount.setText(String.valueOf(roomRecord.getRoomDiscount()));
        cmbStatus.setValue(roomRecord.getStatus());
    }

    @Override
    public void reloadTable() {
        try {
            loadRoomListTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
