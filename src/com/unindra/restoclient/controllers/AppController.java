package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    public JFXButton ramenButton;
    public JFXButton minumanButton;
    public JFXButton cemilanButton;
    public JFXButton lainnyaButton;
    public ScrollPane mainPane;
    public JFXButton pesananButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ramen.fxml"));
            mainPane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuHandle(ActionEvent actionEvent) throws IOException {
        ramenButton.getStyleClass().set(2, "button-header");
        minumanButton.getStyleClass().set(2, "button-header");
        cemilanButton.getStyleClass().set(2, "button-header");
        lainnyaButton.getStyleClass().set(2, "button-header");

        String fxmlName;
        if (actionEvent.getSource() == ramenButton) {
            ramenButton.getStyleClass().set(2, "button-header-pressed");
            fxmlName = "ramen";
        } else if (actionEvent.getSource() == minumanButton) {
            minumanButton.getStyleClass().set(2, "button-header-pressed");
            fxmlName = "minuman";
        } else if (actionEvent.getSource() == cemilanButton) {
            cemilanButton.getStyleClass().set(2, "button-header-pressed");
            fxmlName = "cemilan";
        } else if (actionEvent.getSource() == lainnyaButton) {
            lainnyaButton.getStyleClass().set(2, "button-header-pressed");
            fxmlName = "lainnya";
        } else {
            fxmlName = "ramen";
        }

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlName + ".fxml"));
        mainPane.setContent(root);
    }

    public void pesananHandle(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == pesananButton) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/pesanan.fxml"));
            Stage primaryStage = (Stage) pesananButton.getScene().getWindow();

            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Pesanan");
            alert.setDialogPane((DialogPane) root);
            alert.setX(primaryStage.getX() + pesananButton.getLayoutX() - 410);
            alert.setY(primaryStage.getY() + pesananButton.getLayoutY() + 60);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/icons/logo-ramen-bulet-merah-copy20x20.png"));

            ButtonType pesanButton = new ButtonType("Pesan");
            ButtonType bayarButton = new ButtonType("Bayar");
            ButtonType keluarButton = new ButtonType("Keluar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(pesanButton, bayarButton, keluarButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == pesanButton) {
                System.out.println("Pesan");
            } else if (result.isPresent() && result.get() == bayarButton) {
                System.out.println("Bayar");
            } else {
                System.out.println("Keluar");
            }
        }
    }

    public void pengaturanHandle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 4) {
            System.out.println("masuk");
        }
    }
}
