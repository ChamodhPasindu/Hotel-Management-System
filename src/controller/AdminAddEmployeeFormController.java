package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.EmployeeController;
import controller.dbController.HallController;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Employee;
import model.Hall;
import util.LoadTableEvent;
import util.Validation;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminAddEmployeeFormController {
    public JFXButton btnAddEmployee;
    public Label lblEmployeeId;
    public JFXTextField txtMobile;
    public JFXTextField txtEmail;
    public JFXTextField txtAddress;
    public JFXTextField txtName;
    public JFXComboBox<String> cmbGender;
    public JFXTextField txtNic;
    public JFXTextField txtDob;
    public JFXDatePicker txtJoinDate;
    public JFXComboBox<String> cmbDepartment;
    public JFXTextField txtSalary;
    public Label lblDepartmentId;

    public LoadTableEvent loadTableEvent;

    LinkedHashMap<TextField, Pattern> hashMap=new LinkedHashMap();
    public void initialize(){

        storeValidations();

        try {
            setEmployeeId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        cmbGender.getItems().addAll("Male","Female");

        cmbGender.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            isEmpty();
        } );

        try {
            ArrayList<String> departmentName = new EmployeeController().getDepartmentName();
            cmbDepartment.getItems().addAll(departmentName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        cmbDepartment.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                lblDepartmentId.setText(new EmployeeController().getDepartmentId(newValue));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            isEmpty();
        });

        txtJoinDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            isEmpty();
        });
    }

    //Check all other textFields empty or not
    private void isEmpty(){
        if(cmbGender.getValue()==null || txtName.getText().isEmpty() ||  cmbDepartment.getValue()==null || txtMobile.getText().isEmpty() || txtJoinDate.getValue()==null ||
                txtAddress.getText().isEmpty() || txtDob.getText().isEmpty() || txtEmail.getText().isEmpty() || txtNic.getText().isEmpty()
        || txtSalary.getText().isEmpty()){
            btnAddEmployee.setDisable(true);
        }else {
            btnAddEmployee.setDisable(false);
        }
    }

    private void setEmployeeId() throws SQLException {
        String employeeId = new EmployeeController().createEmployeeId();
        lblEmployeeId.setText(employeeId);
    }

    //Store validation Reg-ex in hashmap for validate all textFields
    private void storeValidations() {
        hashMap.put(txtName,Pattern.compile("^[A-z\\.\\s]{3,50}$"));
        hashMap.put(txtMobile,Pattern.compile("^(07)(1|2|5|6|7|8)(-?)[0-9]{7}$"));
        hashMap.put(txtNic,Pattern.compile("^[0-9]{12}\\b|[0-9]{10}[V]$"));
        hashMap.put(txtDob,Pattern.compile("^[0-9]{4}[-|/][0-9]{2}[-|/][0-9]{2}$"));
        hashMap.put(txtEmail,Pattern.compile("^[a-z0-9]{4,20}[@][a-z]{3,6}(.com|.lk)$"));
        hashMap.put(txtAddress,Pattern.compile("^[A-z0-9\\,\\/\\-\\s\\.]{5,}$"));
        hashMap.put(txtSalary,Pattern.compile("[0-9][0-9]*([.][0-9]{2})?$"));
    }

    public void addEmployeeOnAction(ActionEvent actionEvent) throws SQLException {
        Employee employee = new Employee(
                lblEmployeeId.getText(),
                lblDepartmentId.getText(),
                txtName.getText(),
                txtDob.getText(),
                txtNic.getText(),
                txtAddress.getText(),
                txtMobile.getText(),
                txtEmail.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtSalary.getText())),
                String.valueOf(txtJoinDate.getValue()),
                cmbGender.getValue()
        );



        if (new EmployeeController().saveEmployeeDetails(employee)) {
            new Alert(Alert.AlertType.INFORMATION,"Employee Added Successfully").show();
            loadTableEvent.reloadTable();
            setEmployeeId();
            getAction();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try again latter").show();

        }
    }

    //After add an employee clear all textFields
    private void getAction() {
        JFXTextField[] fields = new JFXTextField[]{txtSalary,txtName,txtNic,txtEmail,txtDob,txtAddress,txtMobile};
        Validation.clearText(fields);
        cmbDepartment.getSelectionModel().clearSelection();
        cmbGender.getSelectionModel().clearSelection();
        txtJoinDate.getEditor().clear();
        btnAddEmployee.setDisable(true);
    }

    //When enter pressed,that textValidate with 'Validation Class's  validate' method
    public void textFieldKeyReleased(KeyEvent keyEvent) {
        Object validate = Validation.validate(hashMap, btnAddEmployee);

        if(cmbDepartment.getValue()==null | cmbGender.getValue()==null | txtJoinDate.getValue()==null) {
            btnAddEmployee.setDisable(true);
        }

        if(keyEvent.getCode()== KeyCode.ENTER){
            if(validate instanceof TextField){
                TextField errorText= (TextField) validate;
                errorText.requestFocus();
            }else if(validate instanceof Boolean){
                txtJoinDate.requestFocus();
            }
        }
    }

    public void setEvent(LoadTableEvent event ){
        this.loadTableEvent=event;
    }

}
