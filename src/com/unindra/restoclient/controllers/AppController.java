package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.unindra.restoclient.models.DaftarMenu;
import com.unindra.restoclient.models.Item;
import com.unindra.restoclient.models.StatusResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.unindra.restoclient.models.Item.items;

public class AppController implements Initializable {
    public JFXButton ramenButton;
    public JFXButton minumanButton;
    public JFXButton cemilanButton;
    public JFXButton lainnyaButton;
    public ScrollPane mainPane;
    public JFXButton pesananButton;

    private VBox ramenPane;
    private VBox minumanPane;
    private VBox cemilanPane;
    private VBox lainnyaPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ramenPane = new VBox();
        minumanPane = new VBox();
        cemilanPane = new VBox();
        lainnyaPane = new VBox();

        setRamenPane(DaftarMenu.menus("ramen"));
        setAllMenuPane(DaftarMenu.menus("minuman"), minumanPane, "body-minuman-pane");
        setAllMenuPane(DaftarMenu.menus("cemilan"), cemilanPane, "body-cemilan-pane");
        setAllMenuPane(DaftarMenu.menus("lainnya"), lainnyaPane, "body-lainnya-pane");

        mainPane.setContent(ramenPane);
    }

    public void menuHandle(ActionEvent actionEvent) {
        ramenButton.getStyleClass().set(2, "ramen");
        minumanButton.getStyleClass().set(2, "minuman");
        cemilanButton.getStyleClass().set(2, "cemilan");
        lainnyaButton.getStyleClass().set(2, "lainnya");

        if (actionEvent.getSource() == ramenButton) {
            ramenButton.getStyleClass().set(2, "ramen-pressed");
            mainPane.setContent(ramenPane);
        } else if (actionEvent.getSource() == minumanButton) {
            minumanButton.getStyleClass().set(2, "minuman-pressed");
            mainPane.setContent(minumanPane);
        } else if (actionEvent.getSource() == cemilanButton) {
            cemilanButton.getStyleClass().set(2, "cemilan-pressed");
            mainPane.setContent(cemilanPane);
        } else if (actionEvent.getSource() == lainnyaButton) {
            lainnyaButton.getStyleClass().set(2, "lainnya-pressed");
            mainPane.setContent(lainnyaPane);
        }
    }

    public void pesananHandle() throws IOException {
        Stage primaryStage = (Stage) pesananButton.getScene().getWindow();

        JFXAlert<String> alert = new JFXAlert<>(primaryStage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/pesanan.fxml"));

        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("Pesanan"));
        layout.setBody(root);

        JFXButton pesanButton = new JFXButton("Pesan");
        pesanButton.setDefaultButton(true);
        pesanButton.setOnAction(event -> {
            List<Item> items = items("belum dipesan");
            items.forEach(Item::pesan);

            boolean success = true;
            for (Item item : items) {
                success &= item.put().getStatus() == StatusResponse.SUCCESS;
            }

            JFXDialogLayout layoutInformation = new JFXDialogLayout();
            JFXButton okButton = new JFXButton("Ok");
            okButton.setCancelButton(true);
            okButton.setOnAction(evt -> alert.hideWithAnimation());
            layoutInformation.setActions(okButton);
            layoutInformation.setHeading(new Label("Berhasil"));
            layoutInformation.setBody(new Label("Pesanan anda berhasil! mohon tunggu pesanan disajikan"));

            if (success && !items.isEmpty()) {
                alert.setContent(layoutInformation);
                alert.show();
            }
        });

        JFXButton bayarButton = new JFXButton("Bayar");

        JFXButton keluarButton = new JFXButton("Keluar");
        keluarButton.setCancelButton(true);
        keluarButton.setOnAction(event -> alert.hideWithAnimation());

        layout.setActions(pesanButton, bayarButton, keluarButton);

        alert.setContent(layout);
        alert.show();
    }

    public void pengaturanHandle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 4) {
            System.out.println("masuk");
        }
    }

    private void setRamenPane(List<DaftarMenu> menuList) {
        ramenPane.setPrefWidth(800);
        ramenPane.setPrefHeight(500);

        for (int i = 0; i < menuList.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ramen.fxml"));
                Parent root = fxmlLoader.load();

                RamenController c = fxmlLoader.getController();
                c.rootPane.getStyleClass().add("body-ramen-pane" + i);
                c.setMenu(menuList.get(i));

                ramenPane.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAllMenuPane(List<DaftarMenu> menuList, VBox allMenuPane, String style) {
        allMenuPane.setPrefWidth(800);
        allMenuPane.setPrefHeight(500);
        allMenuPane.setPadding(new Insets(50, 60, 0, 129));
        allMenuPane.setSpacing(15);
        allMenuPane.getStyleClass().add(style);

        for (DaftarMenu daftarMenu : menuList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/allmenu.fxml"));
                Parent root = fxmlLoader.load();

                AllMenuController c = fxmlLoader.getController();
                c.setMenu(daftarMenu);

                allMenuPane.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
