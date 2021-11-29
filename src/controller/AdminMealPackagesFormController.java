package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.MealPackageController;
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
import model.LoadDate;
import model.MealPackage;
import util.LoadTableEvent;
import util.Validation;
import view.tm.MealPackageTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class AdminMealPackagesFormController implements LoadTableEvent {

    public Label lblDate;
    public Label lblTime;
    public Label lblPackageId;
    public JFXTextField txtPackageType;
    public JFXTextField txtPackagePrice;
    public JFXTextField txtPackageDescription;
    public JFXButton btnUpdate;
    public TableView tblPackageList;
    public TableColumn colPackageId;
    public TableColumn colType;
    public TableColumn colDescription;
    public JFXButton btnDelete;
    public JFXButton btnView;
    public TextField txtSearchPackage;
    public TableColumn colPrice;
    public JFXComboBox<String> cmbAvailability;
    public TableColumn colAvailability;

    int selectRow=-1;

    public void initialize(){

        loadDateAndTime();
        disableAllComponents();

        colPackageId.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("packageType"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("packagePrice"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));

        tblPackageList.setItems(mealPackageTMS);

        tblPackageList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->{
            selectRow= (int) newValue;
            if(selectRow!=-1){
                btnView.setDisable(false);
                btnDelete.setDisable(false);
            }
        } );

        txtSearchPackage.textProperty().addListener((observable, oldValue, newValue) -> {
            tblPackageList.getItems().clear();

            try {
                ArrayList<MealPackage> mealPackages = new MealPackageController().searchMealPackage(newValue);

                for (MealPackage meal:mealPackages) {
                    mealPackageTMS.add(new MealPackageTM(
                            meal.getPackageId(),
                            meal.getPackageType(),
                            meal.getDescription(),
                            meal.getPackagePrice(),
                            meal.getAvailability()
                    ));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        });

        txtPackageType.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[A-z ]{3,20}$")){
                txtPackageType.setStyle("-fx-border-color:blue");
                btnUpdate.setDisable(false);
            }else {
                txtPackageType.setStyle("-fx-border-color: red");
                btnUpdate.setDisable(true);
            }
        });

        txtPackageDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[A-z ]{3,50}$")){
                txtPackageDescription.setStyle("-fx-border-color:blue");
                btnUpdate.setDisable(false);
            }else {
                txtPackageDescription.setStyle("-fx-border-color: red");
                btnUpdate.setDisable(true);
            }
        });

        txtPackagePrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[1-9][0-9]*([.][0-9]{2})?$")){
                txtPackagePrice.setStyle("-fx-border-color:blue");
                btnUpdate.setDisable(false);
            }else {
                txtPackagePrice.setStyle("-fx-border-color: red");
                btnUpdate.setDisable(true);
            }
        });

        cmbAvailability.getItems().addAll("Breakfast","Lunch","Dinner","InActive");

        cmbAvailability.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==null){
                btnUpdate.setDisable(true);
            }
        });


        try {
            loadPackageListTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    ObservableList<MealPackageTM> mealPackageTMS = FXCollections.observableArrayList();
    private void loadPackageListTable() throws SQLException {
        tblPackageList.getItems().clear();

        ArrayList<MealPackage> hallDetails = new MealPackageController().getMealPackageDetails();
        for (MealPackage mealPackage :hallDetails) {
            mealPackageTMS.add(new MealPackageTM(
                    mealPackage.getPackageId(),
                    mealPackage.getPackageType(),
                    mealPackage.getDescription(),
                    mealPackage.getPackagePrice(),
                    mealPackage.getAvailability()
            ));
        }
    }

    //Disable btn and some textFields
    private void disableAllComponents() {
        lblPackageId.setDisable(true);
        txtPackageType.setDisable(true);
        txtPackageDescription.setDisable(true);
        txtPackagePrice.setDisable(true);
        cmbAvailability.setDisable(true);

        btnDelete.setDisable(true);
        btnView.setDisable(true);
        btnUpdate.setDisable(true);
    }

    //Enable btn and some textFields
    private void enableAllComponents() {
        lblPackageId.setDisable(false);
        txtPackagePrice.setDisable(false);
        txtPackageDescription.setDisable(false);
        txtPackageType.setDisable(false);
        cmbAvailability.setDisable(false);

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

    public void openAddMealOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AdminAddMealPackageForm.fxml"));
        Parent load = loader.load();
        AdminAddMealPackageFormController controller = loader.getController();
        controller.setEvent(this);
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.getIcons().add(new Image("view/assets/images/hotel-icon.png"));
        stage.setTitle("Add New Meal Package");
        stage.setScene(scene);
        stage.show();
    }

    //Update package details
    public void updateOnAction(ActionEvent actionEvent) throws SQLException {
        MealPackage updatePackage = new MealPackage(
                lblPackageId.getText(),
                txtPackageType.getText(),
                txtPackageDescription.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtPackagePrice.getText())),
                cmbAvailability.getValue()
        );

        if(new MealPackageController().updateMealPackageDetails(updatePackage)){
            new Alert(Alert.AlertType.INFORMATION,"Meal Package Updated Successfully").show();
            loadPackageListTable();
            disableAllComponents();
            getAction();

        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try Again Latter").show();
        }
    }

    //After update,clear window
    private void getAction() {
        JFXTextField[] fields = new JFXTextField[]{txtPackageType,txtPackagePrice,txtPackageDescription};
        Validation.clearText(fields);
        cmbAvailability.getSelectionModel().clearSelection();
        lblPackageId.setText("Package ID");
    }

    //Delete package
    public void deleteOnAction(ActionEvent actionEvent) throws SQLException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION
                , "Do you want to Delete this MealPackage recode?", yes, no);
        alert.setTitle("Delete Confirmation");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if(new MealPackageController().deletePackageDetails(mealPackageTMS.get(selectRow).getPackageId())){
                mealPackageTMS.remove(selectRow);
                btnDelete.setDisable(true);
                btnView.setDisable(true);

            }else{
                new Alert(Alert.AlertType.WARNING,"Error,Try Again").show();
            }
        }
    }

    //View package recode in for update
    public void viewOnAction(ActionEvent actionEvent) throws SQLException {
        enableAllComponents();
        btnDelete.setDisable(true);

        MealPackage packageRecord = new MealPackageController().getPackageRecord(mealPackageTMS.get(selectRow).getPackageId());

        lblPackageId.setText(packageRecord.getPackageId());
        txtPackageType.setText(packageRecord.getPackageType());
        txtPackageDescription.setText(packageRecord.getDescription());
        txtPackagePrice.setText(String.valueOf(packageRecord.getPackagePrice()));
        cmbAvailability.setValue(packageRecord.getAvailability());
    }

    @Override
    public void reloadTable() {
        try {
            loadPackageListTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
