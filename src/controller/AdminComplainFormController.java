package controller;

import com.jfoenix.controls.JFXButton;
import controller.dbController.ComplainController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.Complain;
import model.LoadDate;
import view.tm.ComplainTM;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class AdminComplainFormController {

    public Label lblDate;
    public Label lblTime;
    public TableColumn colDate;
    public TableColumn colTime;
    public TableColumn colDescription;
    public JFXButton btnFix;
    public TableColumn colComplainId;
    public TableColumn colName;
    public TableView tblComplainList;

    int selectRow=-1;

    public void initialize(){
        colComplainId.setCellValueFactory(new PropertyValueFactory<>("complainId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        tblComplainList.setItems(complainTMS);
        btnFix.setDisable(true);

        tblComplainList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->{
            selectRow= (int) newValue;
            if(selectRow!=-1){
                btnFix.setDisable(false);
            }
        } );

        loadDateAndTime();

        try {
            loadComplainTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    ObservableList<ComplainTM> complainTMS = FXCollections.observableArrayList();
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

    //Change complain status
    public void fixOnaAction(ActionEvent actionEvent) throws SQLException {

        if(new ComplainController().fixComplain(complainTMS.get(selectRow).getComplainId())){
            new Alert(Alert.AlertType.INFORMATION,"Successfully").show();
            tblComplainList.getItems().remove(selectRow);
        }else{
            new Alert(Alert.AlertType.WARNING,"Try again latter").show();
        }


    }
}
