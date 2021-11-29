package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.RoomController;
import controller.dbController.ServiceController;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Room;
import model.Service;
import util.LoadTableEvent;
import util.Validation;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminAddServiceFormController {
    public JFXButton btnAddService;
    public Label lblServiceId;
    public JFXTextField txtServiceName;
    public JFXTextField txtPrice;
    public JFXTextField txtDescription;

    public LoadTableEvent loadTableEvent;

    LinkedHashMap<TextField, Pattern>hashMap=new LinkedHashMap();

    public void initialize(){

        try {
            setServiceId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        storeValidation();
    }

    //Store validation Reg-ex in hashmap for validate all textFields
    private void storeValidation() {
        hashMap.put(txtServiceName,Pattern.compile("^[A-z ]{3,20}$"));
        hashMap.put(txtDescription,Pattern.compile("^[A-z ]{3,50}$"));
        hashMap.put(txtPrice,Pattern.compile("^[1-9][0-9]*([.][0-9]{2})?$"));
    }

    private void setServiceId() throws SQLException {
        String serviceId = new ServiceController().createServiceId();
        lblServiceId.setText(serviceId);
    }

    public void addServiceOnAction() throws SQLException {
        Service service = new Service(
                lblServiceId.getText(),
                txtServiceName.getText(),
                txtDescription.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtPrice.getText()))
        );

        if (new ServiceController().saveServiceDetails(service)) {
            new Alert(Alert.AlertType.INFORMATION,"Service Added Successfully").show();
            loadTableEvent.reloadTable();
            setServiceId();
            getAction();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try again latter").show();

        }
    }

    //After add an employee clear all textFields
    private void getAction() {
        JFXTextField[] fields = new JFXTextField[]{txtDescription,txtPrice,txtServiceName};
        Validation.clearText(fields);
        btnAddService.setDisable(true);
    }

    //When enter pressed,that textValidate with 'Validation Class's  validate' method
    public void textFieldKeyReleased(KeyEvent keyEvent) throws SQLException {
        Object validate = Validation.validate(hashMap, btnAddService);

        if(keyEvent.getCode()== KeyCode.ENTER){
            if(validate instanceof TextField){
                TextField errorText= (TextField) validate;
                errorText.requestFocus();
            }else if(validate instanceof Boolean){
                addServiceOnAction();
            }
        }
    }

    public void setEvent(LoadTableEvent event ){
        this.loadTableEvent=event;
    }

}
