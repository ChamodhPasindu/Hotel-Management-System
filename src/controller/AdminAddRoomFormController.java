package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.RoomController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.Room;
import util.LoadTableEvent;
import util.Validation;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminAddRoomFormController {
    public JFXComboBox<String> cmbStatus;
    public Label lblRoomId;
    public JFXTextField txtFloor;
    public JFXTextField txtRoomType;
    public JFXTextField txtPrice;
    public JFXTextField txtDiscount;
    public JFXTextField txtDescription;
    public JFXButton btnAddRoom;

    public LoadTableEvent loadTableEvent;

    LinkedHashMap<TextField, Pattern> hashMap=new LinkedHashMap();

    public void initialize(){

        try {
            setRoomId();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        storeValidations();

        cmbStatus.getItems().addAll("Active","InActive");

        cmbStatus.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue==null || txtDiscount.getText().isEmpty() || txtDescription.getText().isEmpty() || txtFloor.getText().isEmpty() || txtPrice.getText().isEmpty() || txtRoomType.getText().isEmpty()){
                btnAddRoom.setDisable(true);
            }else {
                btnAddRoom.setDisable(false);
            }
        } );

    }
    //Store validation Reg-ex in hashmap for validate all textFields
    private void storeValidations() {
        hashMap.put(txtFloor,Pattern.compile("^[0-9]{1,4}$"));
        hashMap.put(txtRoomType,Pattern.compile("^[A-z ]{3,20}$"));
        hashMap.put(txtDescription,Pattern.compile("^[A-z ]{3,50}$"));
        hashMap.put(txtPrice,Pattern.compile("^[1-9][0-9]*([.][0-9]{2})?$"));
        hashMap.put(txtDiscount,Pattern.compile("[0-9][0-9]*([.][0-9]{2})?$"));
    }

    private void setRoomId() throws SQLException {
        String roomId = new RoomController().createRoomId();
        lblRoomId.setText(roomId);
    }

    //When enter pressed,that textValidate with 'Validation Class's  validate' method
    public void textFieldKeyReleased(KeyEvent keyEvent) {
        Object validate = Validation.validate(hashMap, btnAddRoom);

        if(cmbStatus.getValue()==null){
            btnAddRoom.setDisable(true);
        }

        if(keyEvent.getCode()== KeyCode.ENTER){
            if(validate instanceof TextField){
                TextField errorText= (TextField) validate;
                errorText.requestFocus();
            }else if(validate instanceof Boolean){
                cmbStatus.requestFocus();
            }
        }
    }

    public void addRoomOnAction() throws SQLException, IOException {

        Room room = new Room(
                lblRoomId.getText(),
                Integer.parseInt(txtFloor.getText()),
                txtRoomType.getText(),
                txtDescription.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtPrice.getText())),
                BigDecimal.valueOf(Double.parseDouble(txtDiscount.getText())),
                cmbStatus.getValue()
        );

        if (new RoomController().saveRoomDetails(room)) {
            new Alert(Alert.AlertType.INFORMATION,"Room Added Successfully").show();
            loadTableEvent.reloadTable();
            setRoomId();
            getAction();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try again latter").show();

        }

    }

    //After add an employee clear all textFields
    private void getAction() {
        JFXTextField[] fields = new JFXTextField[]{txtRoomType,txtFloor,txtPrice,txtDescription,txtDiscount};
        Validation.clearText(fields);
        cmbStatus.getSelectionModel().clearSelection();
        btnAddRoom.setDisable(true);
    }

    public void setEvent(LoadTableEvent event ){
        this.loadTableEvent=event;
    }

}
