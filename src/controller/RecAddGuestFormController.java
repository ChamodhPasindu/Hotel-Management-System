package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.CustomerController;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Customer;
import util.Validation;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class RecAddGuestFormController {


    public JFXComboBox<String> cmbTitle;
    public JFXTextField txtCustomerName;
    public JFXTextField txtNic;
    public JFXTextField txtMobile;
    public JFXTextField txtEmail;
    public JFXButton btnAddGuest;
    public Label lblCustomerId;
    public JFXTextField txtAddress;
    public JFXComboBox<String> cmbProvince;


    LinkedHashMap<TextField, Pattern> hashMap=new LinkedHashMap();
    public void initialize(){
        btnAddGuest.setDisable(true);

        storeValidations();
        try {
            setCustomerId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        cmbTitle.getItems().addAll("Mr","Miss","Mrs","Sir","Dr");
        cmbTitle.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            isEmpty();
        });

        cmbProvince.getItems().addAll("Western","Central","Southern","Uva","Sabaragamuwa","North","North Central","Nothern","Eastern");
    }

    private void setCustomerId() throws SQLException {
        String customerId = new CustomerController().getCustomerId();
        lblCustomerId.setText(customerId);
    }

    //Store validation Reg-ex in hashmap for validate all textFields
    private void storeValidations() {
        hashMap.put(txtCustomerName,Pattern.compile("^[A-z\\.\\s]{3,50}$"));
        hashMap.put(txtNic,Pattern.compile("^[0-9]{12}\\b|[0-9]{10}[V]$"));
        hashMap.put(txtMobile,Pattern.compile("^(07)(1|2|5|6|7|8)(-?)[0-9]{7}$"));
        hashMap.put(txtEmail,Pattern.compile("^[a-z0-9]{4,20}[@][a-z]{3,6}(.com|.lk)$"));
        hashMap.put(txtAddress,Pattern.compile("^[A-z0-9\\,\\/\\-\\s\\.]{5,}$"));
    }

    public void addGuestOnAction(ActionEvent actionEvent) throws SQLException {

        Customer customer = new Customer(
                lblCustomerId.getText(),
                cmbTitle.getValue(),
                txtCustomerName.getText(),
                txtNic.getText(),
                txtMobile.getText(),
                txtEmail.getText(),
                txtAddress.getText(),
                cmbProvince.getValue()
        );

        if(new CustomerController().saveCustomerDetails(customer)){
            new Alert(Alert.AlertType.INFORMATION,"Customer Added Successfully").show();
            getAction();
            setCustomerId();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try Again latter").show();
        }
    }

    //After update,clear window
    private void getAction() {
        JFXTextField []fields = new JFXTextField[]{txtNic,txtCustomerName,txtAddress,txtEmail,txtMobile};
        Validation.clearText(fields);

        cmbTitle.getSelectionModel().clearSelection();
        cmbProvince.getSelectionModel().clearSelection();
    }

    //Check all other textFields empty or not
    private void isEmpty(){
        if(txtCustomerName.getText().isEmpty() ||  txtNic.getText().isEmpty()|| txtMobile.getText().isEmpty() ||
                txtAddress.getText().isEmpty() || txtEmail.getText().isEmpty()){
            btnAddGuest.setDisable(true);
        }else {
            btnAddGuest.setDisable(false);
        }
    }

    public void TextFieldKeyReleased(KeyEvent keyEvent) {
        Object validate = Validation.validate(hashMap, btnAddGuest);

        if(cmbTitle.getValue()==null ) {
            btnAddGuest.setDisable(true);
        }

        if(keyEvent.getCode()== KeyCode.ENTER){
            if(validate instanceof TextField){
                TextField errorText= (TextField) validate;
                errorText.requestFocus();
            }else if(validate instanceof Boolean){
                cmbProvince.requestFocus();
            }
        }
    }
}
