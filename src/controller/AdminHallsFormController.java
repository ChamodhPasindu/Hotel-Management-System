package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.HallController;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Hall;
import model.LoadDate;
import model.Room;
import util.LoadTableEvent;
import util.Validation;
import view.tm.HallTM;
import view.tm.RoomTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class AdminHallsFormController implements LoadTableEvent {

    public Label lblDate;
    public Label lblTime;
    public JFXButton btnUpdate;
    public TableView tblHallList;
    public TableColumn colHallId;
    public TableColumn colHallType;
    public TableColumn colFloor;
    public TableColumn colDiscount;
    public TableColumn colPrice;
    public TableColumn colStatus;
    public JFXButton btnDelete;
    public JFXButton btnView;
    public TextField txtSearchHall;
    public Label lblHallId;
    public JFXTextField txtHallType;
    public JFXTextField txtFloor;
    public JFXTextField txtDiscount;
    public JFXTextField txtPrice;
    public JFXTextField txtDescription;
    public JFXComboBox<String> cmbStatus;

    int selectRow=-1;

    public void initialize(){

        loadDateAndTime();
        disableAllComponents();

        colHallId.setCellValueFactory(new PropertyValueFactory<>("hallId"));
        colFloor.setCellValueFactory(new PropertyValueFactory<>("hallFloor"));
        colHallType.setCellValueFactory(new PropertyValueFactory<>("hallType"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tblHallList.setItems(hallTMS);


        txtSearchHall.textProperty().addListener((observable, oldValue, newValue) -> {
            tblHallList.getItems().clear();
            try {
                ArrayList<Hall> halls = new HallController().searchHallDetails(newValue);
                for (Hall hall:halls) {
                    hallTMS.add(new HallTM(
                            hall.getHallId(),
                            hall.getHallFloor(),
                            hall.getHallType(),
                            hall.getPrice(),
                            hall.getDiscount(),
                            hall.getStatus()
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

        txtHallType.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[A-z ]{3,20}$")){
                txtHallType.setStyle("-fx-border-color:blue");
                btnUpdate.setDisable(false);
            }else {
                txtHallType.setStyle("-fx-border-color: red");
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
            loadHallListTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        tblHallList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->{
            selectRow= (int) newValue;
            if(selectRow!=-1){
                btnView.setDisable(false);
                btnDelete.setDisable(false);
            }
        } );
    }

    ObservableList<HallTM>hallTMS = FXCollections.observableArrayList();
    private void loadHallListTable() throws SQLException {
        tblHallList.getItems().clear();

        ArrayList<Hall> hallDetails = new HallController().getHallDetails();
        for (Hall hall :hallDetails) {
            hallTMS.add(new HallTM(
                    hall.getHallId(),
                    hall.getHallFloor(),
                    hall.getHallType(),
                    hall.getPrice(),
                    hall.getDiscount(),
                    hall.getStatus()
            ));
        }
    }

    //Disable btn and some textFields
    private void disableAllComponents() {
        lblHallId.setDisable(true);
        txtFloor.setDisable(true);
        txtHallType.setDisable(true);
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
        lblHallId.setDisable(false);
        txtHallType.setDisable(false);
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

    public void openAddHallOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AdminAddHallForm.fxml"));
        Parent load = loader.load();
        AdminAddHallFormController controller = loader.getController();
        controller.setEvent(this);
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("view/assets/images/hotel-icon.png"));
        stage.setTitle("Add New Hall");
        stage.setResizable(false);
        stage.show();
    }

    //Update employee details
    public void updateOnAction(ActionEvent actionEvent) throws SQLException {
        Hall updateHall = new Hall(
                lblHallId.getText(),
                Integer.parseInt(txtFloor.getText()),
                txtHallType.getText(),
                txtDescription.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtPrice.getText())),
                BigDecimal.valueOf(Float.parseFloat(txtDiscount.getText())),
                cmbStatus.getValue()
        );

        if(new HallController().updateHallDetails(updateHall)){
            new Alert(Alert.AlertType.INFORMATION,"Room Updated Successfully").show();
            loadHallListTable();
            disableAllComponents();
            getAction();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try Again Latter").show();
        }
    }

    //After update,clear window
    private void getAction() {
        JFXTextField[] fields = new JFXTextField[]{txtDescription,txtDiscount,txtFloor,txtHallType,txtPrice};
        Validation.clearText(fields);

        cmbStatus.getSelectionModel().clearSelection();
        lblHallId.setText("Hall ID");
    }

    //Delete hall
    public void deleteOnAction(ActionEvent actionEvent) throws SQLException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION
                , "Do you want to Delete this Hall recode?", yes, no);
        alert.setTitle("Delete Confirmation");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if(new HallController().deleteHallDetails(hallTMS.get(selectRow).getHallId())){
                hallTMS.remove(selectRow);
                btnDelete.setDisable(true);
                btnView.setDisable(true);

            }else{
                new Alert(Alert.AlertType.WARNING,"Error,Try Again").show();
            }
        }
    }

    //View hall recode in for update
    public void viewOnAction(ActionEvent actionEvent) throws SQLException {
        enableAllComponents();
        btnDelete.setDisable(true);

        Hall hallRecord = new HallController().getHallRecord(hallTMS.get(selectRow).getHallId());

        lblHallId.setText(hallRecord.getHallId());
        txtFloor.setText(String.valueOf(hallRecord.getHallFloor()));
        txtHallType.setText(hallRecord.getHallType());
        txtDescription.setText(hallRecord.getDescription());
        txtPrice.setText(String.valueOf(hallRecord.getPrice()));
        txtDiscount.setText(String.valueOf(hallRecord.getDiscount()));
        cmbStatus.setValue(hallRecord.getStatus());
    }
    @Override
    public void reloadTable() {
        try {
            loadHallListTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
