package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.EmployeeController;
import controller.dbController.HallController;
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
import model.Employee;
import model.Hall;
import model.LoadDate;
import util.LoadTableEvent;
import util.Validation;
import view.tm.EmployeeTM;
import view.tm.HallTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class AdminEmployeesFormController implements LoadTableEvent {

    public Label lblDate;
    public Label lblTime;
    public Label lblEmployeeId;
    public JFXTextField txtMobile;
    public JFXTextField txtEmail;
    public JFXTextField txtAddress;
    public JFXButton btnUpdate;
    public JFXTextField txtName;
    public JFXComboBox<String> cmbGender;
    public JFXTextField txtNic;
    public JFXTextField txtDob;
    public JFXDatePicker txtJoinDate;
    public JFXComboBox<String> cmbDepartment;
    public JFXTextField txtSalary;
    public Label lblDepartmentId;
    public TableView tblEmployeeList;
    public TableColumn colEmployeeId;
    public TableColumn colName;
    public TableColumn colDepartment;
    public JFXButton btnDelete;
    public JFXButton btnView;
    public TextField txtSearchName;

    int selectRow=0;

    public void initialize(){

        loadDateAndTime();
        disableAllComponents();

        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("departmentId"));

        tblEmployeeList.setItems(employeeTMS);

        cmbGender.getItems().addAll("Male","Female");

        cmbDepartment.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                lblDepartmentId.setText(new EmployeeController().getDepartmentId(newValue));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });


        try {
            ArrayList<String> departmentName = new EmployeeController().getDepartmentName();
            cmbDepartment.getItems().addAll(departmentName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        tblEmployeeList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->{
            selectRow= (int) newValue;
            if(selectRow!=-1){
                btnView.setDisable(false);
                btnDelete.setDisable(false);
            }
        } );

        txtSearchName.textProperty().addListener((observable, oldValue, newValue) -> {
            tblEmployeeList.getItems().clear();
            try {
                ArrayList<Employee> employees = new EmployeeController().searchEmployeeDetails(newValue);
                for (Employee employee:employees) {
                    employeeTMS.add(new EmployeeTM(
                            employee.getEmployeeId(),
                            employee.getDepartmentId(),
                            employee.getEmployeeName()
                    ));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        });

        txtName.textProperty().addListener((observable, oldValue, newValue) -> {
            validate(txtName,newValue,"^[A-z\\.\\s]{3,50}$");
        });
        txtMobile.textProperty().addListener((observable, oldValue, newValue) -> {
            validate(txtMobile,newValue,"^(07)(1|2|5|6|7|8)(-?)[0-9]{7}$");
        });
        txtNic.textProperty().addListener((observable, oldValue, newValue) -> {
            validate(txtNic,newValue,"^[0-9]{12}\\b|[0-9]{10}[V]$");
        });
        txtDob.textProperty().addListener((observable, oldValue, newValue) -> {
            validate(txtDob,newValue,"^[0-9]{4}[-|/][0-9]{2}[-|/][0-9]{2}$");
        });
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            validate(txtEmail,newValue,"^[a-z0-9]{4,20}[@][a-z]{3,6}(.com|.lk)$");
        });
        txtAddress.textProperty().addListener((observable, oldValue, newValue) -> {
            validate(txtAddress,newValue,"^[A-z0-9\\,\\/\\-\\s\\.]{5,}$");
        });
        txtSalary.textProperty().addListener((observable, oldValue, newValue) -> {
            validate(txtSalary,newValue,"[0-9][0-9]*([.][0-9]{2})?$");
        });
        txtJoinDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue==null){
                btnUpdate.setDisable(true);

            }else {
                btnUpdate.setDisable(false);
            }
        });

        try {
            loadEmployeeListTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    ObservableList<EmployeeTM>employeeTMS = FXCollections.observableArrayList();
    private void loadEmployeeListTable() throws SQLException {
        tblEmployeeList.getItems().clear();

        ArrayList<Employee> employeeDetails = new EmployeeController().getEmployeeDetails();
        for (Employee employee :employeeDetails) {
            employeeTMS.add(new EmployeeTM(
                    employee.getEmployeeId(),
                    employee.getDepartmentId(),
                    employee.getEmployeeName()
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
        lblEmployeeId.setDisable(true);
        lblDepartmentId.setDisable(true);
        txtName.setDisable(true);
        txtMobile.setDisable(true);
        txtNic.setDisable(true);
        txtDob.setDisable(true);
        txtEmail.setDisable(true);
        txtAddress.setDisable(true);
        txtSalary.setDisable(true);
        cmbDepartment.setDisable(true);
        cmbGender.setDisable(true);
        txtJoinDate.setDisable(true);

        btnDelete.setDisable(true);
        btnView.setDisable(true);
        btnUpdate.setDisable(true);
    }

    //Enable btn and some textFields
    private void enableAllComponents() {
        lblEmployeeId.setDisable(false);
        lblDepartmentId.setDisable(false);
        txtName.setDisable(false);
        txtMobile.setDisable(false);
        txtNic.setDisable(false);
        txtDob.setDisable(false);
        txtEmail.setDisable(false);
        txtAddress.setDisable(false);
        txtSalary.setDisable(false);
        cmbDepartment.setDisable(false);
        cmbGender.setDisable(false);
        txtJoinDate.setDisable(false);

        btnUpdate.setDisable(false);
    }

    public void openAddEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AdminAddEmployeeForm.fxml"));
        Parent load = loader.load();
        AdminAddEmployeeFormController controller = loader.getController();
        controller.setEvent(this);
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("view/assets/images/hotel-icon.png"));
        stage.setTitle("Add New Employee");
        stage.setResizable(false);
        stage.show();
    }

    //Update employee details
    public void updateOnAction(ActionEvent actionEvent) throws SQLException {
        Employee updateEmployee = new Employee(
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

        if(new EmployeeController().updateEmployeeDetails(updateEmployee)){
            new Alert(Alert.AlertType.INFORMATION,"Employee Updated Successfully").show();
            loadEmployeeListTable();
            disableAllComponents();
            getAction();

        }else{
            new Alert(Alert.AlertType.INFORMATION,"Try Again Latter").show();
        }
    }

    //After update,clear window
    private void getAction() {
        JFXTextField[] fields = new JFXTextField[]{txtName,txtSalary,txtAddress,txtEmail,txtDob,txtNic,txtMobile};
        Validation.clearText(fields);
        cmbGender.getSelectionModel().clearSelection();
        cmbDepartment.getSelectionModel().clearSelection();
        txtJoinDate.getEditor().clear();

        lblDepartmentId.setText("Department ID");
        lblEmployeeId.setText("Employee ID");
    }

    //Delete Employee
    public void deleteOnAction(ActionEvent actionEvent) throws SQLException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION
                , "Do you want to Delete this Employee recode?", yes, no);
        alert.setTitle("Delete Confirmation");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if(new EmployeeController().deleteEmployeeDetails(employeeTMS.get(selectRow).getEmployeeId())){
                employeeTMS.remove(selectRow);
                btnDelete.setDisable(true);
                btnView.setDisable(true);

            }else{
                new Alert(Alert.AlertType.WARNING,"Error,Try Again").show();
            }
        }
    }

    //View Employee recode in for update
    public void viewOnAction(ActionEvent actionEvent) throws SQLException {
        enableAllComponents();
        btnDelete.setDisable(true);

        Employee employeeRecord = new EmployeeController().getEmployeeRecord(employeeTMS.get(selectRow).getEmployeeId());

        lblEmployeeId.setText(employeeRecord.getEmployeeId());
        lblDepartmentId.setText(employeeRecord.getDepartmentId());
        txtName.setText(employeeRecord.getEmployeeName());
        txtMobile.setText(employeeRecord.getNumber());
        txtNic.setText(employeeRecord.getNic());
        txtDob.setText(employeeRecord.getDob());
        txtEmail.setText(employeeRecord.getEmail());
        txtAddress.setText(employeeRecord.getAddress());
        txtSalary.setText(String.valueOf(employeeRecord.getSalary()));
        cmbGender.setValue(employeeRecord.getGender());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(employeeRecord.getJoinDate(), formatter);
        txtJoinDate.setValue(localDate);

         cmbDepartment.setValue(employeeTMS.get(selectRow).getDepartmentId());
    }

    //Validate text fields
    public void validate(TextField name,String value,String pattern){
        if(value.matches(pattern)){
            name.setStyle("-fx-border-color:blue");
            btnUpdate.setDisable(false);
        }else {
            name.setStyle("-fx-border-color: red");
            btnUpdate.setDisable(true);
        }
        if(txtJoinDate.getValue()==null){
            btnUpdate.setDisable(true);
        }
    }

    @Override
    public void reloadTable() {
        try {
            loadEmployeeListTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
