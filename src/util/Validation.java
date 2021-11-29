package util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Validation {

    public static Object validate(HashMap<TextField, Pattern> map, JFXButton btn){
        btn.setDisable(true);
        for (TextField textFieldKey : map.keySet()) {
            Pattern patternValue = map.get(textFieldKey);
            if (!patternValue.matcher(textFieldKey.getText()).matches()) {
                if (!textFieldKey.getText().isEmpty()) {
                    textFieldKey.setStyle("-fx-border-color: red");
                }
                return textFieldKey;
            }
            textFieldKey.setStyle("-fx-border-color:blue");
        }
        btn.setDisable(false);
        return true;
    }

    public static void clearText( JFXTextField[] textField){
        for (JFXTextField field:textField) {
            field.clear();
            field.setStyle("-fx-border-color: transparent");

        }

    }

    public static void clearLabel(Label[] label){
        for (Label lbl:label) {
            lbl.setText(null);
        }
    }

    public static void clearDatePicker(JFXDatePicker[] datePickers){
        for (JFXDatePicker picker:datePickers) {
            picker.getEditor().clear();
        }

    }

    public static void setZeroToLabel(Label[] label){
        for (Label lbl:label) {
            lbl.setText("0.00");
        }
    }


}
