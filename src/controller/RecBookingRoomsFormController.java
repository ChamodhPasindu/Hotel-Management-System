package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;
import controller.dbController.*;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import model.Service;
import util.SendEmail;
import util.Validation;
import view.tm.RoomTM;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RecBookingRoomsFormController {

    public Label lblTime;
    public Label lblDate;
    public TableView tblRoomAvailability;
    public TableColumn colRoomId;
    public TableColumn colType;
    public JFXDatePicker txtCheckDate;
    public Label lblCustomerName;
    public JFXComboBox<String> cmbBreakfast;
    public JFXComboBox<String> cmbLunch;
    public JFXComboBox<String> cmbDinner;
    public JFXComboBox<String> cmbService;
    public JFXTextField txtNoOfDays;
    public JFXButton btnDone;
    public Label lblCustomerId;
    public JFXTextField txtNoOfGuest;
    public JFXDatePicker txtCheckIn;
    public JFXDatePicker txtCheckOut;
    public TextField txtSearchCustomer;
    public JFXButton btnReserve;
    public JFXButton btnSelect;
    public TableView tblBookingRoom;
    public TableColumn colRoomId2;
    public TableColumn colType2;
    public TableColumn colDiscount;
    public TableColumn colPrice;
    public Label lblReservationId;
    public Label lblRoomPrice;
    public Label lblServicePrice;
    public Label lblMealPackagePrice;
    public JFXTextField txtAdvance;
    public Label lblTotal;
    public Label lblBalance;
    public Pane paneReservation;
    public JFXDatePicker txtCheckOutDate;
    public JFXButton btnView;


    int selectRow=-1;
    ArrayList<MealPackage> mealPackageDetails;
    ArrayList<Service> serviceDetails;
    public void initialize() throws SQLException {

        loadDateAndTime();
        setReservationId();
        loadComboBox();

        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("roomType"));

        tblRoomAvailability.setItems(roomTMS);


        colRoomId2.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colType2.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("roomDiscount"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("roomPrice"));
        tblBookingRoom.setItems(getRoomTMS);

        tblRoomAvailability.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectRow = (int) newValue;
            if(selectRow!=-1){
                btnSelect.setDisable(false);
                btnView.setDisable(false);

            }else{
                btnSelect.setDisable(true);
                btnView.setDisable(true);
            }
        });

        txtNoOfGuest.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[0-9]{1,2}$")){
                txtNoOfGuest.setStyle("-fx-border-color:blue");
                isEmpty();
            }else {
                txtNoOfGuest.setStyle("-fx-border-color: red");
                btnDone.setDisable(true);
            }
        });

        txtCheckIn.valueProperty().addListener((observable, oldValue, newValue) -> {
            isEmpty();
        });

        txtCheckOut.valueProperty().addListener((observable, oldValue, newValue) -> {
            isEmpty();
        });

        cmbBreakfast.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            isEmpty();
        });
        cmbLunch.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            isEmpty();
        });
        cmbDinner.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            isEmpty();
        });

        cmbService.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           txtNoOfDays.setDisable(false);
        });
    }

    //Load all combo box data
    private void loadComboBox() throws SQLException {
        cmbService.getItems().clear();
        cmbBreakfast.getItems().clear();
        cmbLunch.getItems().clear();
        cmbDinner.getItems().clear();

        mealPackageDetails = new MealPackageController().getMealPackageDetails();
        for (MealPackage mealPackage:mealPackageDetails) {
            if(mealPackage.getAvailability().equals("Breakfast")){
                cmbBreakfast.getItems().add(mealPackage.getPackageType());
            }
            if(mealPackage.getAvailability().equals("Lunch")){
                cmbLunch.getItems().add(mealPackage.getPackageType());
            }
            if(mealPackage.getAvailability().equals("Dinner")){
                cmbDinner.getItems().add(mealPackage.getPackageType());

            }
        }

        serviceDetails = new ServiceController().getServiceDetails();
        for (Service service:serviceDetails) {
            cmbService.getItems().add(service.getServiceName());
        }
    }

    private void setReservationId() throws SQLException {
        String reservationId = new RoomReservationController().getReservationId();
        lblReservationId.setText(reservationId);
    }

    //Check all other textFields empty or not
    private void isEmpty() {
        if(txtNoOfGuest.getText().isEmpty() || txtCheckIn.getValue()==null || txtCheckOut.getValue()==null ||
        cmbDinner.getValue()==null || cmbLunch.getValue()==null || cmbBreakfast.getValue()==null  || getRoomTMS.isEmpty() || lblCustomerId.getText().isEmpty()){
            btnDone.setDisable(true);
        }else{
            btnDone.setDisable(false);
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

    public void openAddGuestOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/RecAddGuestForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("view/assets/images/hotel-icon.png"));
        stage.setTitle("Add New Guest");
        stage.setResizable(false);
        stage.show();
    }

    public void DoneOnAction(ActionEvent actionEvent) throws SQLException {

       calculateRoomPrice();
       calculateMealPrice();

       txtAdvance.setDisable(false);

       double roomPrice=Double.parseDouble(lblRoomPrice.getText());
       double mealPrice=Double.parseDouble(lblMealPackagePrice.getText());
       double lTotal=Double.parseDouble(lblTotal.getText());

       double total = roomPrice+ mealPrice + lTotal;
       lblTotal.setText(String.valueOf(String.format("%.2f",total)));

        btnDone.setDisable(true);
    }

    ObservableList<RoomTM>getRoomTMS = FXCollections.observableArrayList();
    public void selectRoomOnAction(ActionEvent actionEvent) {
        getRoomTMS.add(new RoomTM(
                roomTMS.get(selectRow).getRoomId(),
                roomTMS.get(selectRow).getRoomType(),
                roomTMS.get(selectRow).getRoomDiscount(),
                roomTMS.get(selectRow).getRoomPrice()
        ));
        roomTMS.remove(selectRow);
        isEmpty();


    }

    ArrayList<ServiceDetail>services=new ArrayList<>();
    public void NoOfDayOnAction(ActionEvent actionEvent) throws SQLException {

        if(txtNoOfDays.getText().matches("^[0-9]{1,2}$")){
            txtNoOfDays.setStyle("-fx-border-color:blue");
        }else {
            txtNoOfDays.setStyle("-fx-border-color: red");
            return;
        }

        for (Service service :serviceDetails) {
            if (cmbService.getValue().equals(service.getServiceName())) {
                services.add(new ServiceDetail(
                        lblReservationId.getText(),
                        service.getServiceId(),
                        Integer.parseInt(txtNoOfDays.getText()),
                        service.getServicePrice()
                ));
            }
            continue;
        }

        double total=Double.parseDouble(lblServicePrice.getText());
        Service servicePrice = new ServiceController().getServicePrice(cmbService.getValue());

        double newServicePrice=servicePrice.getServicePrice().doubleValue()*Integer.parseInt(txtNoOfDays.getText());

        total+=newServicePrice;
        lblServicePrice.setText(String.valueOf(String.format("%.2f",total)));

        double lTotal=Double.parseDouble(lblTotal.getText());
        lTotal+=newServicePrice;
        lblTotal.setText(String.valueOf(String.format("%.2f",lTotal)));


        cmbService.getItems().remove(cmbService.getValue());

    }

    private void calculateMealPrice() throws SQLException {
        Period p = Period.between(txtCheckIn.getValue(), txtCheckOut.getValue());


        MealPackage breakfast = new MealPackageController().getPackagePrice(cmbBreakfast.getValue());
        double breakfastPrice = breakfast.getPackagePrice().doubleValue();

        MealPackage lunch = new MealPackageController().getPackagePrice(cmbLunch.getValue());
        double lunchPrice = lunch.getPackagePrice().doubleValue();

        MealPackage dinner = new MealPackageController().getPackagePrice(cmbDinner.getValue());
        double dinerPrice = dinner.getPackagePrice().doubleValue();

        double total = (breakfastPrice+lunchPrice+dinerPrice)*Integer.parseInt(txtNoOfGuest.getText())*p.getDays();
        lblMealPackagePrice.setText(String.valueOf(String.format("%.2f",total)));

    }

    private void calculateRoomPrice(){
        Period p = Period.between(txtCheckIn.getValue(), txtCheckOut.getValue());

        double total=0;
        double discount=0;
        for (RoomTM room:getRoomTMS) {
            discount+=room.getRoomDiscount().doubleValue();
            total+=room.getRoomPrice().doubleValue();
        }
        total=(total-discount)*p.getDays();
        lblRoomPrice.setText(String.valueOf(String.format("%.2f",total)));
    }

    Customer customerRecode;
    public void searchCustomerNicOnAction(ActionEvent actionEvent) throws SQLException {
        customerRecode = new CustomerController().getCustomerRecode(txtSearchCustomer.getText());
        if(customerRecode==null){
            new Alert(Alert.AlertType.INFORMATION,"Invalid customer NIC").show();
            return;
        }
        lblCustomerName.setText(customerRecode.getCusName());
        lblCustomerId.setText(customerRecode.getCusId());
        isEmpty();

    }

    public void AdvanceOnAction(ActionEvent actionEvent) {
        if(!txtAdvance.getText().matches("^[0-9]+$")) {
            txtAdvance.setStyle("-fx-border-color: red");
            return;
        }
        double total=Double.parseDouble(lblTotal.getText());
        double advance=Double.parseDouble(txtAdvance.getText());
        double balance = total-advance;

        lblBalance.setText(String.valueOf(String.format("%.2f",balance)));

        btnReserve.setDisable(false);
        paneReservation.setDisable(true);
    }

    public void reserveOnAction(ActionEvent actionEvent) throws SQLException {

        ArrayList<RoomDetail> rooms = new ArrayList<>();
        ArrayList<MealPackageDetail> meals = new ArrayList<>();

        for (RoomTM room:getRoomTMS) {
            rooms.add(new RoomDetail(room.getRoomId(),
                    lblReservationId.getText()));
        }

        MealPackageDetail breakfast;
        MealPackageDetail lunch;
        MealPackageDetail dinner;
        for (MealPackage mealPackage:mealPackageDetails) {
            if (cmbBreakfast.getValue().equals(mealPackage.getPackageType())) {
                breakfast = new MealPackageDetail(lblReservationId.getText(),
                        mealPackage.getPackageId(),
                        1,
                        0,
                        0,
                        mealPackage.getPackagePrice());
                meals.add(breakfast);
            }
            if (cmbLunch.getValue().equals(mealPackage.getPackageType())) {
                lunch = new MealPackageDetail(lblReservationId.getText(),
                        mealPackage.getPackageId(),
                        0,
                        1,
                        0,
                        mealPackage.getPackagePrice());
                meals.add(lunch);

            }
            if (cmbDinner.getValue().equals(mealPackage.getPackageType())) {
                dinner = new MealPackageDetail(lblReservationId.getText(),
                        mealPackage.getPackageId(),
                        0,
                        0,
                        1,
                        mealPackage.getPackagePrice());
                meals.add(dinner);

            }
        }

        RoomReservation roomReservation = new RoomReservation(
                lblReservationId.getText(),
                lblCustomerId.getText(),
                lblDate.getText(),
                lblTime.getText(),
                String.valueOf(txtCheckIn.getValue()),
                String.valueOf(txtCheckOut.getValue()),
                BigDecimal.valueOf(Double.parseDouble(txtAdvance.getText())),
                BigDecimal.valueOf(Double.parseDouble(lblTotal.getText())),
                "Pending",
                txtNoOfGuest.getText(),
                rooms,
                meals,
                services);

        if(new RoomReservationController().makeReservation(roomReservation)){
            new Alert(Alert.AlertType.INFORMATION,"Successful").show();
            sendEmail();
            getAction();
        }else {
            new Alert(Alert.AlertType.INFORMATION,"Error.Try Again Latter").show();
        }
    }

    //send email to customer
    private void sendEmail() {

        EmailDetail detail = new EmailDetail(
                lblReservationId.getText(),
                lblCustomerName.getText(),
                String.valueOf(txtCheckIn.getValue()),
                String.valueOf(txtCheckOut.getValue()),
                txtAdvance.getText(),
                lblTotal.getText());

        new SendEmail(customerRecode.getCusEmail(),detail);
    }

    //After make reservation clear all textFields
    private void getAction() throws SQLException {
        setReservationId();

        JFXDatePicker[] pickers = new JFXDatePicker[]{txtCheckDate,txtCheckOutDate,txtCheckIn,txtCheckOut};
        JFXTextField[] fields = new JFXTextField[]{txtNoOfDays,txtNoOfGuest,txtAdvance};
        Label[] label = new Label[]{lblCustomerName,lblCustomerId};
        Label[] labels= new Label[]{lblRoomPrice,lblMealPackagePrice,lblServicePrice,lblTotal,lblBalance};

        Validation.clearText(fields);
        Validation.clearLabel(label);
        Validation.setZeroToLabel(labels);
        Validation.clearDatePicker(pickers);

        roomTMS.clear();
        getRoomTMS.clear();
        btnReserve.setDisable(true);
        paneReservation.setDisable(false);
        loadComboBox();
    }

    ObservableList<RoomTM>roomTMS = FXCollections.observableArrayList();
    public void loadAvailableRoomOnAction(ActionEvent actionEvent) throws SQLException {
        if(txtCheckDate.getValue()==null || txtCheckOutDate.getValue()==null){
            return;
        }
        tblRoomAvailability.getItems().clear();

        ArrayList<Room> availableRoom = new RoomController().getAvailableRoom(String.valueOf(txtCheckDate.getValue()),String.valueOf(txtCheckOutDate.getValue()));
        for (Room room:availableRoom) {
            roomTMS.add(new RoomTM(
                    room.getRoomId(),
                    room.getRoomFloor(),
                    room.getRoomType(),
                    room.getRoomDescription(),
                    room.getRoomPrice(),
                    room.getRoomDiscount(),
                    room.getStatus()
            ));
        }
    }

    //View room recode in for update
    public void viewRoomDetailOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ViewRoomDetailForm.fxml"));
        Parent load = loader.load();
        ViewRoomDetailFormController controller = loader.<ViewRoomDetailFormController>getController();
        controller.lblRoomId.setText(roomTMS.get(selectRow).getRoomId());
        controller.lblFloor.setText(String.valueOf(roomTMS.get(selectRow).getRoomFloor()));
        controller.lblType.setText(roomTMS.get(selectRow).getRoomType());
        controller.lblDescription.setText(roomTMS.get(selectRow).getRoomDescription());
        controller.lblPrice.setText(roomTMS.get(selectRow).getRoomPrice().toString());
        controller.lblDiscount.setText(roomTMS.get(selectRow).getRoomDiscount().toString());
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setTitle("Room Details");
        stage.setScene(scene);
        stage.show();
    }
}
