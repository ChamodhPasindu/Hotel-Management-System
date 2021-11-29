package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.FoodController;
import controller.dbController.MealPackageController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.Food;
import model.LoadDate;
import util.Validation;
import view.tm.FoodTM;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class CasMealsFormController {

    public Label lblDate;
    public Label lblTime;
    public Label lblAddFoodId;
    public JFXTextField txtAddPrice;
    public JFXButton btnAdd;
    public JFXTextField txtAddDescription;
    public Label lblUpdateFoodId;
    public JFXTextField txtUpdatePrice;
    public JFXButton btnUpdate;
    public TableView tblDish;
    public TableColumn colDescription;
    public TableColumn colPrice;
    public JFXButton btnDelete;
    public JFXButton btnView;
    public JFXTextField txtUpdateDescription;
    public TableColumn colFood;


    int selectRow=-1;
    public void initialize(){

        colFood.setCellValueFactory(new PropertyValueFactory<>("foodId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        tblDish.setItems(foodTMS);

        disableAllComponents();
        loadDateAndTime();
        try {
            loadTableDish();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            setFoodId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        txtAddDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[A-z ]{3,50}$")){
                txtAddDescription.setStyle("-fx-border-color:blue");
                isEmptyOne();
            }else {
                txtAddDescription.setStyle("-fx-border-color: red");
                btnAdd.setDisable(true);

            }

        });

        txtAddPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[1-9][0-9]*([.][0-9]{2})?$")){
                txtAddPrice.setStyle("-fx-border-color:blue");
                isEmptyOne();
            }else {
                txtAddPrice.setStyle("-fx-border-color: red");
                btnAdd.setDisable(true);
            }
        });

        txtUpdateDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[A-z ]{3,50}$")){
                txtUpdateDescription.setStyle("-fx-border-color:blue");
                isEmptyTwo();
            }else {
                txtUpdateDescription.setStyle("-fx-border-color: red");
                btnUpdate.setDisable(true);
            }
        });

        txtUpdatePrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[1-9][0-9]*([.][0-9]{2})?$")){
                txtUpdatePrice.setStyle("-fx-border-color:blue");
                isEmptyTwo();
            }else {
                txtUpdatePrice.setStyle("-fx-border-color: red");
                btnUpdate.setDisable(true);
            }
        });

        tblDish.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectRow= (int) newValue;
            if (selectRow!=-1){
                btnView.setDisable(false);
                btnDelete.setDisable(false);
            }
        });

    }
    private void setFoodId() throws SQLException {
        String foodId = new FoodController().createFoodId();
        lblAddFoodId.setText(foodId);
    }

    ObservableList<FoodTM> foodTMS = FXCollections.observableArrayList();
    private void loadTableDish() throws SQLException {
        tblDish.getItems().clear();

        ArrayList<Food> foods = new FoodController().getFoods();
        for (Food food:foods) {
            foodTMS.add(new FoodTM(
                    food.getFoodId(),
                    food.getDescription(),
                    food.getUnitPrice()
            ));
        }
    }

    //When update meal,Check text field empty or not
    private void isEmptyTwo() {
        if(!txtUpdateDescription.getText().isEmpty() && !txtUpdatePrice.getText().isEmpty()){
            btnUpdate.setDisable(false);
        }
    }

    //When add meal,Check text field empty or not
    private void isEmptyOne() {
        if(!txtAddDescription.getText().isEmpty() && !txtAddPrice.getText().isEmpty()){
            btnAdd.setDisable(false);
        }
    }

    //Disable btn and some textFields
    private void disableAllComponents() {
        txtUpdateDescription.setDisable(true);
        txtUpdatePrice.setDisable(true);
        btnAdd.setDisable(true);
        btnUpdate.setDisable(true);
        btnView.setDisable(true);
        btnDelete.setDisable(true);
    }

    public void addFoodOnAction(ActionEvent actionEvent) throws SQLException {
        Food food = new Food(
                lblAddFoodId.getText(),
                txtAddDescription.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtAddPrice.getText()))
        );

        if(new FoodController().addNewFood(food)){
            new Alert(Alert.AlertType.INFORMATION,"New Food Added Successfully").show();
            getAddAction();
            loadTableDish();
            setFoodId();

        }else{
            new Alert(Alert.AlertType.INFORMATION,"Error.Try Again Latter").show();
        }


    }

    //After add meal,clear window
    private void getAddAction() {

        JFXTextField[] fields = new JFXTextField[]{txtAddPrice,txtAddDescription};
        Validation.clearText(fields);
        btnAdd.setDisable(true);
    }

    public void updateFoodOnAction(ActionEvent actionEvent) throws SQLException {
        Food food = new Food(
                lblUpdateFoodId.getText(),
                txtUpdateDescription.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtUpdatePrice.getText()))
        );


        if(new FoodController().updateFood(food)){
            new Alert(Alert.AlertType.INFORMATION,"Food Updated Successfully").show();
            getUpdateAction();
            loadTableDish();

        }else {
            new Alert(Alert.AlertType.INFORMATION,"Error.try Again Latter").show();
        }
    }

    //After update meal,clear window
    private void getUpdateAction() {
        JFXTextField[] fields = new JFXTextField[]{txtUpdateDescription,txtUpdatePrice};
        Validation.clearText(fields);

        lblUpdateFoodId.setText("Food ID");
        txtUpdatePrice.setDisable(true);
        txtUpdateDescription.setDisable(true);
        btnUpdate.setDisable(true);
    }

    //Delete meal
    public void deleteOnAction(ActionEvent actionEvent) throws SQLException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION
                , "Do you want to Delete this Food recode?", yes, no);
        alert.setTitle("Delete Confirmation");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if(new FoodController().removeFood(foodTMS.get(selectRow).getFoodId())){
                foodTMS.remove(selectRow);
                btnDelete.setDisable(true);
                btnView.setDisable(true);
                setFoodId();

            }else{
                new Alert(Alert.AlertType.WARNING,"Error,Try Again").show();
            }
        }
    }

    //View meal recode in for update
    public void viewOnAction(ActionEvent actionEvent) {
        lblUpdateFoodId.setText(foodTMS.get(selectRow).getFoodId());
        txtUpdateDescription.setText(foodTMS.get(selectRow).getDescription());
        txtUpdatePrice.setText(String.valueOf(foodTMS.get(selectRow).getUnitPrice()));

        txtUpdateDescription.setDisable(false);
        txtUpdatePrice.setDisable(false);
        btnView.setDisable(true);
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

}
