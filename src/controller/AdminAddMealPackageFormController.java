package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.MealPackageController;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.MealPackage;
import util.LoadTableEvent;
import util.Validation;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminAddMealPackageFormController {

    public Label lblPackageId;
    public JFXTextField txtPackageType;
    public JFXTextField txtPrice;
    public JFXTextField txtDescription;
    public JFXButton btnAddPackage;

    public LoadTableEvent loadTableEvent;
    public JFXComboBox<String> cmbAvailability;

    LinkedHashMap<TextField, Pattern>hashMap = new LinkedHashMap();
    public void initialize() throws SQLException {

        storeValidations();
        setMealPackageId();

        cmbAvailability.getItems().addAll("Breakfast","Lunch","Dinner","InActive");

        cmbAvailability.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                isEmpty();
            });
    }

    private void setMealPackageId() throws SQLException {
        String mealPackageId = new MealPackageController().createMealPackageId();
        lblPackageId.setText(mealPackageId);
    }

    //Store validation Reg-ex in hashmap for validate all textFields
    private void storeValidations() {
        hashMap.put(txtPackageType,Pattern.compile("^[A-z ]{3,20}$"));
        hashMap.put(txtDescription,Pattern.compile("^[A-z ]{3,50}$"));
        hashMap.put(txtPrice,Pattern.compile("^[1-9][0-9]*([.][0-9]{2})?$"));
    }

    //When enter pressed,that textValidate with 'Validation Class's  validate' method
    public void textFieldKeyReleased(KeyEvent keyEvent) throws SQLException {
        Object validate = Validation.validate(hashMap,btnAddPackage);

        isEmpty();


        if(keyEvent.getCode()== KeyCode.ENTER){
            if(validate instanceof TextField){
                TextField errorText= (TextField) validate;
                errorText.requestFocus();
            }else if(validate instanceof Boolean){
                cmbAvailability.requestFocus();
            }
        }
    }

    //Check all other textFields empty or not
    private void isEmpty() {
        if(cmbAvailability.getValue()==null || txtDescription.getText().isEmpty() || txtPackageType.getText().isEmpty() || txtPrice.getText().isEmpty()){
            btnAddPackage.setDisable(true);
        }else{
            btnAddPackage.setDisable(false);
        }
    }

    public void addPackageOnAction() throws SQLException {

        MealPackage mealPackage = new MealPackage(
                lblPackageId.getText(),
                txtPackageType.getText(),
                txtDescription.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtPrice.getText())),
                cmbAvailability.getValue()
        );

        if(new MealPackageController().saveMealPackageDetail(mealPackage)){
            new Alert(Alert.AlertType.INFORMATION,"Meal Package Added Successfully").show();
            loadTableEvent.reloadTable();
            setMealPackageId();
            getAction();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try again latter").show();
        }
    }

    //After add an record clear all textFields
    private void getAction() {
        JFXTextField[] fields = new JFXTextField[]{txtPrice,txtPackageType,txtDescription};
        Validation.clearText(fields);
        cmbAvailability.getSelectionModel().clearSelection();
        btnAddPackage.setDisable(true);

    }

    public void setEvent(LoadTableEvent event ){
        this.loadTableEvent=event;
    }

}
