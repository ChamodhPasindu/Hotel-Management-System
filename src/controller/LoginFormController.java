package controller;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.dbController.UserController;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.SecurePassword;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {
    public AnchorPane mainContext;
    public JFXTextField txtUserName;
    public JFXPasswordField txtUserPassword;
    public Label lblWarning;
    public Pane paneWarning;
    public AnchorPane subContext;

    public void initialize(){
        lblWarning.setVisible(false);
        txtUserName.requestFocus();
    }

    public void loginOnAction() throws IOException, SQLException {

        String password = SecurePassword.getPassword(txtUserPassword.getText());

        String user = new UserController().userLogin(txtUserName.getText(),password);

        if (user!=null){
            switch (user){
                case "Admin":
                    FXMLLoader loader1 = new FXMLLoader(getClass().getResource("../view/AdminDashboardForm.fxml"));
                    Parent load1 = loader1.load();

                    Scene sceneAdmin = mainContext.getScene();
                    load1.translateXProperty().set(sceneAdmin.getHeight());
                    mainContext.getChildren().add(load1);

                    Timeline timeline1 = new Timeline();
                    KeyValue kv1 = new KeyValue(load1.translateXProperty(),0, Interpolator.EASE_IN);
                    KeyFrame kf1 = new KeyFrame(Duration.seconds(1),kv1);
                    timeline1.getKeyFrames().add(kf1);
                    timeline1.setOnFinished(event -> mainContext.getChildren().remove(subContext));
                    timeline1.play();

                    AdminDashboardFormController controller1 = loader1.getController();
                    controller1.lblUserName.setText(txtUserName.getText());
                    Stage window1 = (Stage) mainContext.getScene().getWindow();
                    window1.setScene(sceneAdmin);
                    break;

                case "Reception":
                    FXMLLoader loader2 = new FXMLLoader(getClass().getResource("../view/RecDashboardFom.fxml"));
                    Parent load2 = loader2.load();

                    Scene sceneRec = mainContext.getScene();
                    load2.translateXProperty().set(sceneRec.getHeight());
                    mainContext.getChildren().add(load2);

                    Timeline timeline2 = new Timeline();
                    KeyValue kv2 = new KeyValue(load2.translateXProperty(),0, Interpolator.EASE_IN);
                    KeyFrame kf2 = new KeyFrame(Duration.seconds(1),kv2);
                    timeline2.getKeyFrames().add(kf2);
                    timeline2.setOnFinished(event -> mainContext.getChildren().remove(mainContext));
                    timeline2.play();

                    RecDashboardFomController controller2 = loader2.<RecDashboardFomController>getController();
                    controller2.lblUserName.setText(txtUserName.getText());
                    Stage window2= (Stage) mainContext.getScene().getWindow();
                    window2.setScene(sceneRec);
                    break;

                case "Cashier":
                    FXMLLoader loader3 = new FXMLLoader(getClass().getResource("../view/CasDashboardForm.fxml"));
                    Parent load3 = loader3.load();

                    Scene sceneCas = mainContext.getScene();
                    load3.translateXProperty().set(sceneCas.getHeight());
                    mainContext.getChildren().add(load3);

                    Timeline timeline3 = new Timeline();
                    KeyValue kv3 = new KeyValue(load3.translateXProperty(),0, Interpolator.EASE_IN);
                    KeyFrame kf3 = new KeyFrame(Duration.seconds(1),kv3);
                    timeline3.getKeyFrames().add(kf3);
                    timeline3.setOnFinished(event -> mainContext.getChildren().remove(mainContext));
                    timeline3.play();

                    CasDashboardFormController controller3 = loader3.getController();
                    controller3.lblUserName.setText(txtUserName.getText());
                    Stage window3= (Stage) mainContext.getScene().getWindow();
                    window3.setScene(sceneCas);
                    break;
            }

        }else {
            lblWarning.setVisible(true);
            paneWarning.setStyle("-fx-border-color: red");

        }
    }

    //When enter pressed focus next
    public void textFieldKeyReleased(KeyEvent keyEvent) throws IOException, SQLException {
        if((keyEvent.getCode() == KeyCode.ENTER)){
            if (!txtUserName.getText().isEmpty()) {
                txtUserPassword.requestFocus();
            }
            if (!txtUserPassword.getText().isEmpty()) {
                loginOnAction();
            }
        }
    }
}
