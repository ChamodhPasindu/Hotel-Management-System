package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.RoomController;
import controller.dbController.ServiceController;
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
import model.Room;
import model.Service;
import util.LoadTableEvent;
import util.Validation;
import view.tm.RoomTM;
import view.tm.ServiceTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class AdminServicesFormController implements LoadTableEvent {

    public Label lblDate;
    public Label lblTime;
    public JFXTextField txtServiceType;
    public JFXTextField txtPrice;
    public JFXTextField txtDescription;
    public JFXButton btnUpdate;
    public TableView tblServiceList;
    public TableColumn colServiceId;
    public TableColumn colName;
    public TableColumn colDescription;
    public TableColumn colPrice;
    public JFXButton btnDelete;
    public JFXButton btnView;
    public TextField txtSearchService;
    public Label lblServiceId;


    int selectRow=-1;

    public void initialize(){

        loadDateAndTime();
        disableAllComponents();

        colServiceId.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));

        tblServiceList.setItems(serviceTMS);

        tblServiceList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectRow= (int) newValue;
            if(selectRow!=-1){
                btnView.setDisable(false);
                btnDelete.setDisable(false);
            }
        });

        txtSearchService.textProperty().addListener((observable, oldValue, newValue) -> {
            tblServiceList.getItems().clear();
            try {
                ArrayList<Service> services = new ServiceController().searchServiceDetails(newValue);
                for (Service service:services) {
                    serviceTMS.add(new ServiceTM(
                            service.getServiceId(),
                            service.getServiceName(),
                            service.getDescription(),
                            service.getServicePrice()
                    ));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        });

        txtServiceType.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[A-z ]{3,20}$")){
                txtServiceType.setStyle("-fx-border-color:blue");
                btnUpdate.setDisable(false);
            }else {
                txtServiceType.setStyle("-fx-border-color: red");
                btnUpdate.setDisable(true);
            }
        });

        txtDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[A-z ]{3,50}$")){
                txtDescription.setStyle("-fx-border-color:blue");
                btnUpdate.setDisable(false);
            }else {
                txtDescription.setStyle("-fx-border-color: red");
                btnUpdate.setDisable(true);
            }
        });

        txtPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^[1-9][0-9]*([.][0-9]{2})?$")){
                txtPrice.setStyle("-fx-border-color:blue");
                btnUpdate.setDisable(false);
            }else {
                txtPrice.setStyle("-fx-border-color: red");
                btnUpdate.setDisable(true);
            }
        });

        try {
            loadServiceListTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    ObservableList<ServiceTM>serviceTMS = FXCollections.observableArrayList();
    private void loadServiceListTable() throws SQLException {
        tblServiceList.getItems().clear();

        ArrayList<Service> serviceDetails = new ServiceController().getServiceDetails();
        for (Service service :serviceDetails) {
            serviceTMS.add(new ServiceTM(
                    service.getServiceId(),
                    service.getServiceName(),
                    service.getDescription(),
                    service.getServicePrice()
            ));
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

    //Disable btn and some textFields
    private void disableAllComponents() {
        lblServiceId.setDisable(true);
        txtServiceType.setDisable(true);
        txtDescription.setDisable(true);
        txtPrice.setDisable(true);

        btnDelete.setDisable(true);
        btnView.setDisable(true);
        btnUpdate.setDisable(true);
    }

    //Enable btn and some textFields
    private void enableAllComponents() {
        lblServiceId.setDisable(false);
        txtServiceType.setDisable(false);
        txtPrice.setDisable(false);
        txtDescription.setDisable(false);
        txtPrice.setDisable(false);
        btnUpdate.setDisable(false);
    }

    public void openAddServiceOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AdminAddServiceForm.fxml"));
        Parent load = loader.load();
        AdminAddServiceFormController controller = loader.getController();
        controller.setEvent(this);
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("view/assets/images/hotel-icon.png"));
        stage.setTitle("Add New Service");
        stage.setResizable(false);
        stage.show();
    }

    //Update room details
    public void updateOnAction(ActionEvent actionEvent) throws SQLException {
        Service updateService = new Service(
                lblServiceId.getText(),
                txtServiceType.getText(),
                txtDescription.getText(),
                BigDecimal.valueOf(Double.parseDouble(txtPrice.getText()))
        );

        if(new ServiceController().updateServiceDetails(updateService)){
            new Alert(Alert.AlertType.INFORMATION,"Service Updated Successfully").show();
            loadServiceListTable();
            disableAllComponents();
            getAction();

        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try Again Latter").show();
        }
    }

    //After update,clear window
    private void getAction() {
        JFXTextField[] fields = new JFXTextField[]{txtServiceType,txtPrice,txtDescription};
        Validation.clearText(fields);
        lblServiceId.setText("Service ID");
    }

    //Delete room
    public void deleteOnAction(ActionEvent actionEvent) throws SQLException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION
                , "Do you want to Delete this Service recode?", yes, no);
        alert.setTitle("Delete Confirmation");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if(new ServiceController().deleteServiceDetails(serviceTMS.get(selectRow).getServiceId())){
                serviceTMS.remove(selectRow);
                btnDelete.setDisable(true);
                btnView.setDisable(true);

            }else{
                new Alert(Alert.AlertType.WARNING,"Error,Try Again").show();
            }
        }
    }

    //View room recode in for update
    public void viewOnAction(ActionEvent actionEvent) throws SQLException {
        enableAllComponents();
        btnDelete.setDisable(true);

        Service serviceRecord = new ServiceController().getServiceRecord(serviceTMS.get(selectRow).getServiceId());

        lblServiceId.setText(serviceRecord.getServiceId());
        txtServiceType.setText(serviceRecord.getServiceName());
        txtDescription.setText(serviceRecord.getDescription());
        txtPrice.setText(String.valueOf(serviceRecord.getServicePrice()));
    }

    @Override
    public void reloadTable() {
        try {
            loadServiceListTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
