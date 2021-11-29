package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.UserController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import model.LoadDate;
import model.User;
import util.SecurePassword;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AdminUsersFormController {
    public Label lblDate;
    public Label lblTime;
    public JFXTextField txtUserName1;
    public JFXPasswordField txtPassword1;
    public JFXPasswordField txtReEnterPassword1;
    public JFXComboBox<String> cmbUserRole1;
    public JFXButton btnCreateAccount;
    public JFXTextField txtUserName2;
    public JFXComboBox<String> cmbUserRole2;
    public JFXButton btnSearchAccount;
    public JFXPasswordField txtPassword2;
    public JFXPasswordField txtReEnterPassword2;
    public JFXButton btnUpdate;

    public void initialize(){
        loadDateAndTime();

        cmbUserRole1.getItems().addAll("Reception","Cashier");
        cmbUserRole2.getItems().addAll("Reception","Cashier");

        cmbUserRole1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            isEmptyOne();
        });
        cmbUserRole1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            isEmptyTwo();
        });


        txtUserName1.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[A-z]{3,10}$")){
                txtUserName1.setStyle("-fx-border-color: blue");
                isEmptyOne();
            }else{
                txtUserName1.setStyle("-fx-border-color: red");
                btnCreateAccount.setDisable(true);
            }
        });
        txtUserName2.textProperty().addListener((observable, oldValue, newValue) -> {
            isEmptyTwo();
        });

        txtPassword1.textProperty().addListener((observable, oldValue, newValue) -> {
            if(String.valueOf(newValue).matches("^[A-z0-9\\!\\@\\#\\$\\%\\&\\*\\-\\+]{5,}$")){
                txtPassword1.setStyle("-fx-border-color: blue");
                txtReEnterPassword1.setDisable(false);
                isEmptyOne();
            }else{
               txtPassword1.setStyle("-fx-border-color: red");
               txtReEnterPassword1.setDisable(true);
               btnCreateAccount.setDisable(true);
           }
        });
        txtPassword2.textProperty().addListener((observable, oldValue, newValue) -> {
            isEmptyTwo();
        });

        txtReEnterPassword1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(txtPassword1.getText())){
                btnCreateAccount.setDisable(false);
                isEmptyOne();
            }else{
                btnCreateAccount.setDisable(true);
            }
        });
        txtReEnterPassword2.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue==null){
                btnUpdate.setDisable(true);
            }else{
                if(newValue.matches("^[A-z0-9\\!\\@\\#\\$\\%\\&\\*\\-\\+]{5,}$")){
                    txtReEnterPassword2.setStyle("-fx-border-color: blue");
                    btnUpdate.setDisable(false);
                }else{
                    txtReEnterPassword2.setStyle("-fx-border-color: red");
                    btnUpdate.setDisable(true);
                }

            }
        });

    }

    //When update user,Check text field empty or not
    private void isEmptyTwo() {
        if(cmbUserRole2.getValue()==null || txtUserName2.getText().isEmpty() || txtPassword2.getText().isEmpty()){
            btnSearchAccount.setDisable(true);
        }else {
            btnSearchAccount.setDisable(false);
        }
    }

    //When add user,Check text field empty or not
    private void isEmptyOne(){
        if(cmbUserRole1.getValue()==null || txtUserName1.getText().isEmpty() || txtPassword1.getText().isEmpty() || txtReEnterPassword1.getText().isEmpty()){
            btnCreateAccount.setDisable(true);
        }else {
            btnCreateAccount.setDisable(false);
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

    public void createAccountOnAction(ActionEvent actionEvent) throws SQLException {
        if(!String.valueOf(txtPassword1.getText()).equals(String.valueOf(txtReEnterPassword1.getText()))){
            new Alert(Alert.AlertType.WARNING,"Password does not match").show();
            return;
        }

        String password = SecurePassword.getPassword(txtReEnterPassword1.getText());
        User user=new User(
                txtUserName1.getText(),
                password,
                cmbUserRole1.getValue()
        );

        if(new UserController().saveUserDetails(user)){
             new Alert(Alert.AlertType.INFORMATION,"New user added Successfully").show();
             cmbUserRole1.getSelectionModel().clearSelection();
             getAction1();

        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try Again Latter").show();
        }
    }

    //After add user,clear window
    private void getAction1() {
       txtUserName1.clear();
       txtUserName1.setStyle("-fx-border-color: transparent");
       txtPassword1.clear();
       txtPassword1.setStyle("-fx-border-color: transparent");
       txtReEnterPassword1.clear();
       cmbUserRole1.getSelectionModel().clearSelection();

       txtReEnterPassword1.setDisable(true);
       btnCreateAccount.setDisable(true);

    }

    int userId;
    public void searchAccountOnAction(ActionEvent actionEvent) throws SQLException {

        String password = SecurePassword.getPassword(txtPassword2.getText());
        User user=new User(
                txtUserName2.getText(),
                password,
                cmbUserRole2.getValue()
        );

        userId=new UserController().searchAccount(user);
        if(userId!=-1){
            txtReEnterPassword2.setDisable(false);
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Invalid Username and Password ").show();
            txtReEnterPassword2.clear();
            txtReEnterPassword2.setDisable(true);
        }
    }

    public void updateAccountOnAction(ActionEvent actionEvent) throws SQLException {

        String password = SecurePassword.getPassword(txtReEnterPassword2.getText());
        User user = new User(
                txtUserName2.getText(),
                password,
                cmbUserRole2.getValue()
        );

        if(new UserController().updateUser(user,userId)){
            new Alert(Alert.AlertType.INFORMATION,"User updated Successfully").show();
            getAction2();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try Again Latter").show();
        }
    }

    //After update user,clear window
    private void getAction2() {
        txtUserName2.clear();
        txtUserName2.setStyle("-fx-border-color: transparent");
        txtPassword2.clear();
        txtPassword2.setStyle("-fx-border-color: transparent");
        txtReEnterPassword2.clear();

        txtReEnterPassword2.setDisable(true);
        btnSearchAccount.setDisable(true);
        btnUpdate.setDisable(true);
    }
}
