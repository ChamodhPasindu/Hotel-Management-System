package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.FoodController;
import controller.dbController.OrderController;
import controller.dbController.RestaurantBillController;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.Validation;
import view.tm.CartTM;
import view.tm.FoodTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CasDashboardFormController {

    public AnchorPane mainContext;
    public AnchorPane subContext;
    public Label lblDate;
    public Label lblTime;
    public TableView<CartTM> tblCart;
    public TableColumn colItem;
    public TableColumn colQty;
    public TableColumn colPrice;
    public Label txtTotal;
    public JFXButton btnPrintBill;
    public JFXTextField txtQty;
    public Label lblDescription;
    public Label lblFoodId;
    public Label lblUnitPrice;
    public JFXButton btnAddToBill;
    public Label lblOrderId;
    public TableView<FoodTM> tblDish;
    public TableColumn colDish;
    public Label lblBillId;
    public Label lblUserName;
    public JFXButton btnMeals;
    public JFXButton btnReports;
    public JFXButton btnDashboard;

    int selectRow = -1;

    public void initialize() {

        loadDateAndTime();

        colDish.setCellValueFactory(new PropertyValueFactory<>("description"));
        tblDish.setItems(foodTMS);

        colItem.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblCart.setItems(cartTMS);

        try {

            setOrderId();
            setBillId();
            loadMainDishTable();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        txtQty.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[0-9]{1,3}$")) {
                txtQty.setStyle("-fx-border-color: red");
                btnAddToBill.setDisable(true);
            } else {
                txtQty.setStyle("-fx-border-color: blue");
                btnAddToBill.setDisable(false);
            }
        });

        tblDish.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectRow = (int) newValue;
            if (selectRow != -1) {
                lblFoodId.setText(foodTMS.get(selectRow).getFoodId());
                lblDescription.setText(foodTMS.get(selectRow).getDescription());
                lblUnitPrice.setText(String.valueOf(foodTMS.get(selectRow).getUnitPrice()));
            }
        });
    }

    //Load all food to table
    ObservableList<FoodTM> foodTMS = FXCollections.observableArrayList();
    private void loadMainDishTable() throws SQLException {
        ArrayList<Food> foods = new FoodController().getFoods();

        for (Food food : foods) {
            foodTMS.add(new FoodTM(food.getFoodId(),
                    food.getDescription(),
                    food.getUnitPrice()));
        }
    }

    private void setBillId() throws SQLException {
        String bill = new RestaurantBillController().createBill();
        lblBillId.setText(bill);
    }

    private void setOrderId() throws SQLException {
        String orderId = new OrderController().createOrderId();
        lblOrderId.setText(orderId);
    }

    ObservableList<CartTM> cartTMS = FXCollections.observableArrayList();
    public void addToBillOnAction(ActionEvent actionEvent) {

        CartTM tm = new CartTM(
                lblFoodId.getText(),
                lblDescription.getText(),
                Integer.parseInt(txtQty.getText()),
                BigDecimal.valueOf(Double.parseDouble(lblUnitPrice.getText()))
        );

        int exists = isExists(tm);
        if (exists == -1) {
            cartTMS.add(tm);
        } else {
            CartTM temp = cartTMS.get(exists);
            CartTM newTm = new CartTM(
                    temp.getFoodId(),
                    temp.getDescription(),
                    temp.getQty() + Integer.parseInt(txtQty.getText()),
                    temp.getUnitPrice()
            );

            cartTMS.remove(exists);
            cartTMS.add(newTm);
        }
        calculateTotal();
        btnPrintBill.setDisable(false);
        btnAddToBill.setDisable(true);
        txtQty.clear();
        txtQty.setStyle("-fx-border-color: transparent");


    }

    //Check before enter food to cart,it is already in or not..If it is update QTY only
    private int isExists(CartTM tm) {
        for (int i = 0; i < cartTMS.size(); i++) {
            if (tm.getDescription().equals(cartTMS.get(i).getDescription())) {
                return i;
            }
        }
        return -1;
    }

    //When food adding to cart,calculate total price
    private void calculateTotal() {
        double total = 0;
        for (CartTM tm : cartTMS) {
            total += tm.getQty() * Double.parseDouble(String.valueOf(tm.getUnitPrice()));
        }
        txtTotal.setText(String.valueOf(String.format("%.2f", total)));
    }

    public void printBillOnAction(ActionEvent actionEvent) {
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        for (CartTM tm : cartTMS) {
            orderDetails.add(new OrderDetail(
                    lblOrderId.getText(),
                    tm.getFoodId(),
                    tm.getQty(),
                    tm.getUnitPrice()
            ));
        }
        OrderBill orderBill = new OrderBill(
                lblBillId.getText(),
                lblOrderId.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtTotal.getText())),
                orderDetails
        );
        Order order = new Order(
                lblOrderId.getText(),
                lblDate.getText(),
                orderBill
        );

        if (new OrderController().makeOrder(order)) {
            HashMap hashMap = new HashMap();
            hashMap.put("billNo",lblBillId.getText());
            hashMap.put("total",txtTotal.getText());

            try {
                JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/RestaurantBillReport.jrxml"));
                JasperReport compileReport = JasperCompileManager.compileReport(design);
                JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, hashMap , new JRBeanArrayDataSource(cartTMS.toArray()));
                JasperViewer.viewReport(jasperPrint,false);

                getAction();

            } catch (JRException e) {
                e.printStackTrace();
            }

        } else {
            new Alert(Alert.AlertType.INFORMATION, "Error.Try Again Latter").show();
        }

    }

    private void getAction() {
        cartTMS.clear();

        JFXTextField[] fields = new JFXTextField[]{txtQty};
        Label []label = new Label[]{lblDescription,lblUnitPrice,lblFoodId,txtTotal};

        Validation.clearLabel(label);
        Validation.clearText(fields);

        try {
            setBillId();
            setBillId();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

    //UI switching -----------------------------
    private void loadUiForSubContext(String file) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/" + file + ".fxml"));
        subContext.getChildren().clear();
        subContext.getChildren().add(load);
    }

    public void openCasDashboardOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/CasDashboardForm.fxml"));
        Parent load = loader.load();
        CasDashboardFormController controller = loader.<CasDashboardFormController>getController();
        controller.lblUserName.setText(lblUserName.getText());
        Scene scene = new Scene(load);
        Stage window = (Stage) mainContext.getScene().getWindow();
        window.setScene(scene);
    }

    public void openCasMealsOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnMeals);
        loadUiForSubContext("CasMealsForm");
    }

    public void openCasReportsOnAction(ActionEvent actionEvent) throws IOException {
        btnChangeColor(btnReports);
        loadUiForSubContext("CasReportsForm");
    }

    public void logOutOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"));
        Scene scene = new Scene(load);
        Stage window = (Stage) mainContext.getScene().getWindow();
        window.setScene(scene);
    }

    //Change btn color when in clicked
    private void btnChangeColor(Button btn) {
        btnDashboard.setStyle("-fx-background-color: transparent");
        btnMeals.setStyle("-fx-background-color:  transparent");
        btnReports.setStyle("-fx-background-color:  transparent");

        btn.setStyle("-fx-background-color: #F24937;");

    }

    //--------------------------------------------








}
