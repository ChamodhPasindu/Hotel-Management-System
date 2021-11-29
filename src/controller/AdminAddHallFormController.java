package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.HallController;
import controller.dbController.RoomController;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Hall;
import model.Room;
import util.LoadTableEvent;
import util.Validation;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminAddHallFormController{
    public JFXComboBox<String> cmbStatus;
    public Label lblHallId;
    public JFXTextField txtFloor;
    public JFXTextField txtHallType;
    public JFXTextField txtPrice;
    public JFXTextField txtDiscount;
    public JFXTextField txtDescription;
    public JFXButton btnAddHall;


    LinkedHashMap<TextField, Pattern>hashMap=new LinkedHashMap();
    private LoadTableEvent loadTableEvent;

    public void initialize(){

        try {
            setHallId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        storeValidations();

        cmbStatus.getItems().addAll("Active","InActive");

        cmbStatus.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue==null || txtDiscount.getText().isEmpty() || txtDescription.getText().isEmpty() || txtFloor.getText().isEmpty() || txtPrice.getText().isEmpty() || txtHallType.getText().isEmpty()){
                btnAddHall.setDisable(true);
            }else {
                btnAddHall.setDisable(false);
            }
        } );
    }

    //Store validation Reg-ex in hashmap for validate all textFields
    private void storeValidations() {
        hashMap.put(txtFloor,Pattern.compile("^[0-9]{1,4}$"));
        hashMap.put(txtHallType,Pattern.compile("^[A-z ]{3,20}$"));
        hashMap.put(txtDescription,Pattern.compile("^[A-z ]{3,50}$"));
        hashMap.put(txtPrice,Pattern.compile("^[1-9][0-9]*([.][0-9]{2})?$"));
        hashMap.put(txtDiscount,Pattern.compile("[0-9][0-9]*([.][0-9]{2})?$"));
    }

    private void setHallId() throws SQLException {
        String hallId = new HallController().createHallId();
        lblHallId.setText(hallId);
    }

    public void addHallOnAction(ActionEvent actionEvent) throws SQLException {
        Hall hall = new Hall(
                lblHallId.getText(),
                Integer.parseInt(txtFloor.getText()),
                txtHallType.getText(),
                txtDescription.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtPrice.getText())),
                BigDecimal.valueOf(Double.parseDouble(txtDiscount.getText())),
                cmbStatus.getValue()
        );

        if (new HallController().saveHallDetails(hall)) {
            new Alert(Alert.AlertType.INFORMATION,"Hall Added Successfully").show();
            loadTableEvent.reloadTable();
            setHallId();
            getAction();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try again latter").show();

        }
    }

    //After add an record clear all textFields
    private void getAction() {
        JFXTextField []field = new JFXTextField[]{txtDescription,txtDiscount,txtFloor,txtHallType,txtPrice};
        Validation.clearText(field);
        cmbStatus.getSelectionModel().clearSelection();
        btnAddHall.setDisable(true);

    }

    //When enter pressed,that textValidate with 'Validation Class's  validate' method
    public void txtFieldsKeyReleased(KeyEvent keyEvent) {
        Object validate = Validation.validate(hashMap, btnAddHall);

        if(cmbStatus.getValue()==null){
            btnAddHall.setDisable(true);
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

    public void setEvent(LoadTableEvent event ){
        this.loadTableEvent=event;
    }

}
