package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.CustomerController;
import controller.dbController.HallReservationController;
import controller.dbController.HotelBillController;
import controller.dbController.RoomReservationController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RecCheckOutFormController {

    public Label lblTime;
    public Label lblDate;
    public Label lblCusId;
    public Label lblCusName;
    public Label lblCusNic;
    public Label lblCusMobile;
    public Label lblAddress;
    public Label lblEmail;
    public Label lblCheckIn;
    public Label lblCheckOut;
    public Label lblProvince;
    public Label lblDinner;
    public Label lblLunch;
    public Label lblBreakfast;
    public Label lblReserveId;
    public Label lblNoOfGuest;
    public Label lblReserveDate;
    public Label lblTotal;
    public Label lblAdvance;
    public Label lblBalance;
    public JFXButton btnCheckOutGuest;
    public JFXButton btnPrintBill;
    public TableView tblReserveRooms;
    public TableColumn colRooms;
    public TableView tblService;
    public TableColumn colService;
    public TextField txtSearchRoomReservationId;
    public Label lblBillId;
    public Label lblCusId1;
    public Label lblCusName1;
    public Label lblCusNic1;
    public Label lblCusMobile1;
    public Label lblAddress1;
    public Label lblEmail1;
    public Label lblCheckIn1;
    public Label lblCheckOut1;
    public Label lblProvince1;
    public Label lblReserveId1;
    public Label lblReserveDate1;
    public TextField txtSearchHallReservationId1;
    public Label lblTotal1;
    public Label lblAdvance1;
    public Label lblBalance1;
    public JFXButton btnCheckOutGuest1;
    public JFXButton btnPrintBill1;
    public Label lblBillId1;
    public Label lblHallId;

    public void initialize(){

        loadDateAndTime();

        colRooms.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colService.setCellValueFactory(new PropertyValueFactory<>("serviceName"));

        tblReserveRooms.setItems(roomTable);
        tblService.setItems(serviceTable);

        try {
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

    public void roomCheckOutOnAction(ActionEvent actionEvent) throws SQLException {
        HotelBill hotelBill = new HotelBill(
                lblBillId.getText(),
                lblCusId.getText(),
                lblDate.getText(),
                "Room Reservation Check Out",
                BigDecimal.valueOf(Double.parseDouble(lblTotal.getText())));

        if(new RoomReservationController().checkOutCustomer(lblReserveId.getText(),hotelBill)){
            new Alert(Alert.AlertType.INFORMATION,"Guest Checked Out Successfully").show();

        }else {
            new Alert(Alert.AlertType.INFORMATION,"Error.Try Again Latter").show();
        }
    }

    ObservableList<RoomTM> roomTable = FXCollections.observableArrayList();
    ObservableList<ServiceTM> serviceTable = FXCollections.observableArrayList();
    public void searchRoomReservationIdOnAction(ActionEvent actionEvent) throws SQLException {

        getActionRoom();
        RoomReservation reservation = new RoomReservationController().searchReservationForAction(txtSearchRoomReservationId.getText(),"CheckIn");

        if (reservation != null){
            Customer customer= new CustomerController().getCustomerDetails(reservation.getCusId());
            ArrayList<MealPackageDetail> mealPackageDetail = new RoomReservationController().getMealPackageDetail(txtSearchRoomReservationId.getText());
            if (!mealPackageDetail.isEmpty()){
                ArrayList<RoomDetail> roomDetail = new RoomReservationController().getRoomDetail(txtSearchRoomReservationId.getText());
                if (!roomDetail.isEmpty()){
                    ArrayList<ServiceDetail> serviceDetail = new RoomReservationController().getServiceDetail(txtSearchRoomReservationId.getText());

                    lblCusId.setText(reservation.getCusId());
                    lblCusName.setText(customer.getCusName());
                    lblCusNic.setText(customer.getCusId());
                    lblCusMobile.setText(customer.getCusNumber());
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

                    btnCheckOutGuest.setDisable(false);
                    btnPrintBill.setDisable(false);

                    setBillId();

                }
            }
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Invalid Room Reservation ID for check Out").show();
        }
    }

    private void setBillId() throws SQLException {
        String billId = new HotelBillController().getBillId();
        lblBillId1.setText(billId);
        lblBillId.setText(billId);
    }

    public void searchHallReservationIdOnAction(ActionEvent actionEvent) throws SQLException {

        getActionHall();
        HallReservation reservation = new HallReservationController().searchReservationForAction(txtSearchHallReservationId1.getText(), "CheckIn");
        if (reservation != null) {
            Customer customer = new CustomerController().getCustomerDetails(reservation.getCusId());

            lblCusId1.setText(reservation.getCusId());
            lblCusName1.setText(customer.getCusName());
            lblCusNic1.setText(customer.getCusId());
            lblCusMobile1.setText(customer.getCusNumber());
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


            lblTotal1.setText(String.valueOf(reservation.getCost()));
            lblAdvance1.setText(String.valueOf(reservation.getAdvance()));

            double total = Double.parseDouble(String.valueOf(reservation.getCost()));
            double advance = Double.parseDouble(String.valueOf(reservation.getAdvance()));
            double balance = total - advance;

            lblBalance1.setText(String.valueOf(String.format("%.2f", balance)));

            btnCheckOutGuest1.setDisable(false);
            btnPrintBill1.setDisable(false);

        } else {
            new Alert(Alert.AlertType.INFORMATION,"Invalid Hall Reservation ID for check Out").show();
        }
    }

    public void HallCheckOutOnAction(ActionEvent actionEvent) throws SQLException {
        HotelBill hotelBill = new HotelBill(
                lblBillId1.getText(),
                lblCusId1.getText(),
                lblDate.getText(),
                "Hall Reservation Check Out",
                BigDecimal.valueOf(Double.parseDouble(lblTotal1.getText())));

        if(new HallReservationController().checkOutCustomer(lblReserveId1.getText(),hotelBill)){
            new Alert(Alert.AlertType.INFORMATION,"Guest Checked Out Successfully").show();

        }else {
            new Alert(Alert.AlertType.INFORMATION,"Error.Try Again Latter").show();
        }
    }

    public void printBillForRoomOnAction(ActionEvent actionEvent) throws SQLException {
        ArrayList<BillModel> models = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkIn = LocalDate.parse(lblCheckIn.getText(), formatter);
        LocalDate checkOut = LocalDate.parse(lblCheckOut.getText(), formatter);

        Period p = Period.between(checkIn,checkOut);

        ArrayList<Room> roomDetailForBill = new RoomReservationController().getRoomDetailForBill(txtSearchRoomReservationId.getText());
        ArrayList<MealPackageDetail> mealDetailsForBill = new RoomReservationController().getMealDetailsForBill(txtSearchRoomReservationId.getText());
        ArrayList<ServiceDetail> serviceDetailForBill = new RoomReservationController().getServiceDetailForBill(txtSearchRoomReservationId.getText());

        for (Room room:roomDetailForBill) {
            double value = (room.getRoomPrice().doubleValue() - room.getRoomDiscount().doubleValue()) * p.getDays();
            models.add(new BillModel(
                    room.getRoomId()+" - "+room.getRoomType(),
                    String.valueOf(p.getDays()),
                    String.valueOf(String.format("%.2f",value))
            ));
        }

        for (MealPackageDetail detail:mealDetailsForBill) {
            double value = detail.getPrice().doubleValue()* p.getDays()*Double.parseDouble(lblNoOfGuest.getText());
            models.add(new BillModel(
                    detail.getPackageId(),
                    String.valueOf(p.getDays()),
                    String.valueOf(String.format("%.2f",value))
            ));
        }

        for (ServiceDetail detail:serviceDetailForBill) {
            double value = detail.getPrice().doubleValue() * detail.getNoOfDay();
            models.add(new BillModel(
                    detail.getServiceId(),
                    String.valueOf(detail.getNoOfDay()),
                    String.valueOf(String.format("%.2f",value))
            ));
        }

        HashMap hashMap = new HashMap();
        hashMap.put("billId",lblBillId.getText());
        hashMap.put("reservationId",lblReserveId.getText());
        hashMap.put("checkInDate",lblCheckIn.getText());
        hashMap.put("checkOutDate",lblCheckOut.getText());
        hashMap.put("cusName",lblCusName.getText());
        hashMap.put("address",lblAddress.getText());
        hashMap.put("number",lblCusMobile.getText());
        hashMap.put("noOfGuest",lblNoOfGuest.getText());
        hashMap.put("total", "Rs "+lblTotal.getText());
        hashMap.put("advance","Rs "+lblAdvance.getText());
        hashMap.put("balance","Rs "+lblBalance.getText());




        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/RoomCheckOutBill.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, hashMap , new JRBeanArrayDataSource(models .toArray()));
            JasperViewer.viewReport(jasperPrint,false);

            getActionRoom();
        } catch (JRException e) {
            e.printStackTrace();
        }

    }

    //After check out in room clear all textFields
    private void getActionRoom() throws SQLException {
        setBillId();

        btnCheckOutGuest.setDisable(true);
        btnPrintBill.setDisable(true);

        Label[] labels = new Label[]{lblAdvance,lblBalance,lblTotal};
        Label[] labels1= new Label[]{lblReserveId,lblCusName,lblCusId,lblCusNic,lblCusMobile,lblAddress,lblEmail,lblProvince,lblCheckIn
                ,lblCheckOut,lblReserveDate,lblNoOfGuest,lblBreakfast,lblLunch,lblDinner};
        Validation.clearLabel(labels1);
        Validation.setZeroToLabel(labels);

        tblReserveRooms.getItems().clear();
        tblService.getItems().clear();
    }

    public void printBillForHallOnAction(ActionEvent actionEvent) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkIn = LocalDate.parse(lblCheckIn1.getText(), formatter);
        LocalDate checkOut = LocalDate.parse(lblCheckOut1.getText(), formatter);

        Period p = Period.between(checkIn,checkOut);


        HashMap hashMap = new HashMap();
        hashMap.put("billId",lblBillId1.getText());
        hashMap.put("reservationId",lblReserveId1.getText());
        hashMap.put("checkInDate",lblCheckIn1.getText());
        hashMap.put("checkOutDate",lblCheckOut1.getText());
        hashMap.put("cusName",lblCusName1.getText());
        hashMap.put("address",lblAddress1.getText());
        hashMap.put("number",lblCusMobile1.getText());
        hashMap.put("particular",lblHallId.getText());
        hashMap.put("Day",String.valueOf(p.getDays()));
        hashMap.put("amount",lblTotal1.getText());
        hashMap.put("total", "Rs "+lblTotal1.getText());
        hashMap.put("advance","Rs "+lblAdvance1.getText());
        hashMap.put("balance","Rs "+lblBalance1.getText());




        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/HallCheckOutBill.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, hashMap , new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint,false);

            getActionHall();
        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }

    //After check out in hall clear all textFields
    private void getActionHall() throws SQLException {
        setBillId();

        btnPrintBill1.setDisable(true);
        btnCheckOutGuest1.setDisable(true);

        Label[] labels = new Label[]{lblAdvance1,lblBalance1,lblTotal1};
        Label[] labels2= new Label[]{lblReserveId1,lblCusName1,lblCusId1,lblCusNic1,lblCusMobile1,lblAddress1,lblEmail1,lblProvince1,lblCheckIn1
                ,lblCheckOut1,lblReserveDate1,lblHallId};
        Validation.clearLabel(labels2);
        Validation.setZeroToLabel(labels);
    }


}
