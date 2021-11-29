package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.CustomerController;
import controller.dbController.HallReservationController;
import controller.dbController.RoomReservationController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.Validation;
import view.tm.RoomTM;
import view.tm.ServiceTM;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class RecCheckInFormController {

    public Label lblDate;
    public Label lblTime;
    public Label lblCusId;
    public Label lblCusName;
    public Label lblNic;
    public Label lblMobile;
    public Label lblAddress;
    public Label lblEmail;
    public Label lblCheckIn;
    public Label lblCheckOut;
    public Label lblProvince;
    public Label lblNoOfGuest;
    public Label lblDinner;
    public Label lblLunch;
    public Label lblBreakfast;
    public Label lblReserveDate;
    public Label lblReserveId;
    public TableView tblReserveRooms;
    public TableColumn colRooms;
    public TableView tblService;
    public TableColumn colService;
    public Label lblTotal;
    public Label lblAdvance;
    public JFXButton btnRoomCheckIn;
    public JFXButton btnRoomPrintToken;
    public JFXTextField txtExtraPayment;
    public TextField txtSearchRoomReservationId;
    public Label lblBalance;
    public Label lblCusId1;
    public Label lblNic1;
    public Label lblCusName1;
    public Label lblMobile1;
    public Label lblAddress1;
    public Label lblEmail1;
    public Label lblCheckIn1;
    public Label lblCheckOut1;
    public Label lblProvince1;
    public Label lblReserveDate1;
    public Label lblReserveId1;
    public TextField txtSearchHallReservationId1;
    public Label lblTotal1;
    public Label lblAdvance1;
    public Label lblBalance1;
    public JFXButton btnHallCheckIn;
    public JFXButton btnHallPrintToken;
    public JFXTextField txtExtraPayment1;
    public Label lblHallId;

    public void initialize() {
        loadDateAndTime();

        colRooms.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colService.setCellValueFactory(new PropertyValueFactory<>("serviceName"));

        tblReserveRooms.setItems(roomTable);
        tblService.setItems(serviceTable);


        txtExtraPayment1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[0-9]+$")) {
                txtExtraPayment1.setStyle("-fx-border-color:blue");
                btnHallCheckIn.setDisable(false);
                btnHallPrintToken.setDisable(false);
            } else {
                txtExtraPayment1.setStyle("-fx-border-color: red");
                btnHallCheckIn.setDisable(true);
                btnHallPrintToken.setDisable(true);
            }

        });

        txtExtraPayment.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[0-9]+$")) {
                txtExtraPayment.setStyle("-fx-border-color:blue");
                btnRoomCheckIn.setDisable(false);
                btnRoomPrintToken.setDisable(false);
            } else {
                txtExtraPayment.setStyle("-fx-border-color: red");
                btnRoomCheckIn.setDisable(true);
                btnRoomPrintToken.setDisable(true);
            }

        });
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

    public void checkInRoomOnAction(ActionEvent actionEvent) throws SQLException {

        if(new RoomReservationController().checkInCustomer(lblReserveId.getText(),Double.parseDouble(txtExtraPayment.getText()))){
            new Alert(Alert.AlertType.INFORMATION,"Guest Checked In Successfully").show();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Error.Try Again Latter").show();
        }
    }

    //After make room reservation clear all textFields
    private void getActionRoom() {
        btnRoomPrintToken.setDisable(true);
        btnRoomCheckIn.setDisable(true);

        Label[] labels = new Label[]{lblAdvance,lblBalance,lblTotal};
        Label[] labels1= new Label[]{lblReserveId,lblCusName,lblCusId,lblNic,lblMobile,lblAddress,lblEmail,lblProvince,lblCheckIn
        ,lblCheckOut,lblReserveDate,lblNoOfGuest,lblBreakfast,lblLunch,lblDinner};
        Validation.clearLabel(labels1);
        Validation.setZeroToLabel(labels);

        txtExtraPayment.clear();
        txtExtraPayment.setStyle("-fx-border-color: transparent");

        tblReserveRooms.getItems().clear();
        tblService.getItems().clear();
    }

    ObservableList<RoomTM>roomTable = FXCollections.observableArrayList();
    ObservableList<ServiceTM> serviceTable = FXCollections.observableArrayList();
    public void searchRoomReservationIdOnAction(ActionEvent actionEvent) throws SQLException {

        getActionRoom();
        RoomReservation reservation = new RoomReservationController().searchReservationForAction(txtSearchRoomReservationId.getText(),"Pending");

        if (reservation != null){
            Customer customer= new CustomerController().getCustomerDetails(reservation.getCusId());
            ArrayList<MealPackageDetail> mealPackageDetail = new RoomReservationController().getMealPackageDetail(txtSearchRoomReservationId.getText());
            if (!mealPackageDetail.isEmpty()){
                ArrayList<RoomDetail> roomDetail = new RoomReservationController().getRoomDetail(txtSearchRoomReservationId.getText());
                if (!roomDetail.isEmpty()){
                    ArrayList<ServiceDetail> serviceDetail = new RoomReservationController().getServiceDetail(txtSearchRoomReservationId.getText());

                    lblCusId.setText(reservation.getCusId());
                    lblCusName.setText(customer.getCusName());
                    lblNic.setText(customer.getNic());
                    lblMobile.setText(customer.getCusNumber());
                    lblAddress.setText(customer.getCusAddress());
                    lblEmail.setText(customer.getCusEmail());
                    lblProvince.setText(customer.getCusProvince());
                    lblCheckIn.setText(reservation.getCheckIn());
                    lblCheckOut.setText(reservation.getCheckOut());
                    lblReserveId.setText(reservation.getReservationId());
                    lblReserveDate.setText(reservation.getReserveDate());
                    lblNoOfGuest.setText(reservation.getNoOfGuest());
                    lblBreakfast.setText(mealPackageDetail.get(0).getPackageId());
                    lblLunch.setText(mealPackageDetail.get(1).getPackageId());
                    lblDinner.setText(mealPackageDetail.get(2).getPackageId());

                    for (RoomDetail detail: roomDetail) {
                        roomTable.add(new RoomTM(
                                detail.getRoomId()
                        ));
                    }
                    for (ServiceDetail detail:serviceDetail) {
                        serviceTable.add(new ServiceTM(
                                detail.getServiceId()
                        ));
                    }

                    lblTotal.setText(String.valueOf(reservation.getCost()));
                    lblAdvance.setText(String.valueOf(reservation.getAdvance()));

                    double total=Double.parseDouble(String.valueOf(reservation.getCost()));
                    double advance =Double.parseDouble(String.valueOf(reservation.getAdvance()));
                    double balance = total-advance;

                    lblBalance.setText(String.valueOf(String.format("%.2f",balance)));

                    txtExtraPayment.setDisable(false);

                }
            }
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Invalid Room Reservation ID for check In").show();
        }


    }

    public void extraPaymentForRoomOnAction(ActionEvent actionEvent) {
        if(!txtExtraPayment.getText().matches("^[0-9]+$")) {
            txtExtraPayment.setStyle("-fx-border-color: red");
            return;
        }
        double balance=Double.parseDouble(lblBalance.getText());
        double payment=Double.parseDouble(txtExtraPayment.getText());
        double newBalance = balance-payment;

        lblBalance.setText(String.valueOf(String.format("%.2f",newBalance)));
    }

    public void checkInHallOnAction(ActionEvent actionEvent) throws SQLException {
        if(new HallReservationController().checkInCustomer(lblReserveId1.getText(),Double.parseDouble(txtExtraPayment1.getText()))){
            new Alert(Alert.AlertType.INFORMATION,"Guest Checked In Successfully").show();
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Error.Try Again Latter").show();
        }

    }

    //After make hall reservation clear all textFields
    private void getActionHall() {

        btnHallCheckIn.setDisable(true);
        btnHallPrintToken.setDisable(true);

        Label[] labels = new Label[]{lblAdvance1,lblBalance1,lblTotal1};
        Label[] labels2= new Label[]{lblReserveId1,lblCusName1,lblCusId1,lblNic1,lblMobile1,lblAddress1,lblEmail1,lblProvince1,lblCheckIn1
                ,lblCheckOut1,lblReserveDate1,lblHallId};
        txtExtraPayment1.clear();
        txtExtraPayment1.setStyle("-fx-border-color: transparent");
        Validation.clearLabel(labels2);
        Validation.setZeroToLabel(labels);
    }

    public void extraPaymentForHallOnAction(ActionEvent actionEvent) {
        if(!txtExtraPayment1.getText().matches("^[0-9]+$")) {
            txtExtraPayment1.setStyle("-fx-border-color: red");
            return;
        }
        double balance=Double.parseDouble(lblBalance1.getText());
        double payment=Double.parseDouble(txtExtraPayment1.getText());
        double newBalance = balance-payment;

        lblBalance1.setText(String.valueOf(String.format("%.2f",newBalance)));
    }

    public void searchHallReservationIdOnAction(ActionEvent actionEvent) throws SQLException {

        getActionHall();
        HallReservation reservation = new HallReservationController().searchReservationForAction(txtSearchHallReservationId1.getText(), "Pending");
        if (reservation!=null){
            Customer customer = new CustomerController().getCustomerDetails(reservation.getCusId());

            lblCusId1.setText(reservation.getCusId());
            lblCusName1.setText(customer.getCusName());
            lblNic1.setText(customer.getNic());
            lblMobile1.setText(customer.getCusNumber());
            lblAddress1.setText(customer.getCusAddress());
            lblEmail1.setText(customer.getCusEmail());
            lblProvince1.setText(customer.getCusProvince());
            lblCheckIn1.setText(reservation.getCheckIn());
            lblCheckOut1.setText(reservation.getCheckOut());
            lblReserveId1.setText(reservation.getReservationId());
            lblReserveDate1.setText(reservation.getReserveDate());
            lblHallId.setText(reservation.getHallId());

            lblTotal1.setText(String.valueOf(reservation.getCost()));
            lblAdvance1.setText(String.valueOf(reservation.getAdvance()));

            double total=Double.parseDouble(String.valueOf(reservation.getCost()));
            double advance =Double.parseDouble(String.valueOf(reservation.getAdvance()));
            double balance = total-advance;

            lblBalance1.setText(String.valueOf(String.format("%.2f",balance)));

            txtExtraPayment1.setDisable(false);

        }else{
            new Alert(Alert.AlertType.INFORMATION,"Invalid Hall Reservation ID for check In").show();
        }
    }

    public void printTokenForRoomOnAction(ActionEvent actionEvent) {

        double advance = Double.parseDouble(lblAdvance.getText());
        double extra = Double.parseDouble(txtExtraPayment.getText());
        advance=advance+extra;

        HashMap hashMap = new HashMap();
        hashMap.put("reservationId",lblReserveId.getText());
        hashMap.put("checkInDate",lblCheckIn.getText());
        hashMap.put("checkOutDate",lblCheckOut.getText());
        hashMap.put("Name",lblCusName.getText());
        hashMap.put("address",lblAddress.getText());
        hashMap.put("phone",lblMobile.getText());
        hashMap.put("nic",lblNic.getText());
        hashMap.put("email",lblEmail.getText());
        hashMap.put("total", "Rs "+lblTotal.getText());
        hashMap.put("advance","Rs "+String.valueOf(String.format("%.2f",advance)));


        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/RoomReservationCheckInToken.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, hashMap , new JRBeanArrayDataSource(roomTable .toArray()));
            JasperViewer.viewReport(jasperPrint,false);
            getActionRoom();

        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public void printTokenForHallOnAction(ActionEvent actionEvent) throws JRException {
        double advance = Double.parseDouble(lblAdvance1.getText());
        double extra = Double.parseDouble(txtExtraPayment1.getText());
        advance=advance+extra;

        HashMap hashMap = new HashMap();
        hashMap.put("reservationId",lblReserveId1.getText());
        hashMap.put("checkInDate",lblCheckIn1.getText());
        hashMap.put("checkOutDate",lblCheckOut1.getText());
        hashMap.put("Name",lblCusName1.getText());
        hashMap.put("address",lblAddress1.getText());
        hashMap.put("phone",lblMobile1.getText());
        hashMap.put("nic",lblNic1.getText());
        hashMap.put("email",lblEmail1.getText());
        hashMap.put("total", "Rs "+lblTotal1.getText());
        hashMap.put("advance","Rs "+String.valueOf(String.format("%.2f",advance)));
        hashMap.put("hallId",lblHallId.getText());


        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/HallReservationCheckInToken.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, hashMap ,new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint,false);
            getActionHall();
        } catch (JRException e) {
            e.printStackTrace();
        }

    }

}
