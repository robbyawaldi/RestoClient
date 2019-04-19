package com.unindra.restoclient;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Dialog {

    private JFXAlert<String> dialog;

    public Dialog(Stage stage) {
        dialog = new JFXAlert<>(stage);
        dialog.setOverlayClose(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setAnimation(JFXAlertAnimation.TOP_ANIMATION);
    }

    public static JFXDialogLayout getDialogLayout(Node heading, Node body, JFXButton... buttons) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setActions(buttons);
        dialogLayout.setHeading(heading);
        dialogLayout.setBody(body);
        return dialogLayout;
    }

    public JFXAlert<String> getDialog() {
        return dialog;
    }

    public void information(String header, String body) {
        JFXButton okButton = new JFXButton("Ok");
        okButton.setOnAction(event -> dialog.hide());
        dialog.setContent(getDialogLayout(
                new Label(header),
                new Label(body),
                okButton
        ));
        dialog.show();
    }

    public void confirmation(String body, EventHandler<ActionEvent> eventConfirm) {
        JFXButton yaButton = new JFXButton("Ya");
        JFXButton batalButton = new JFXButton("Batal");
        yaButton.setOnAction(eventConfirm);
        batalButton.setOnAction(event -> dialog.hide());
        dialog.setContent(getDialogLayout(
                new Label("Konfirmasi"),
                new Label(body),
                yaButton,
                batalButton
        ));
        dialog.show();
    }
}
