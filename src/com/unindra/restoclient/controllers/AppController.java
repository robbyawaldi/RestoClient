package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.unindra.restoclient.Dialog;
import com.unindra.restoclient.models.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.unindra.restoclient.Client.get;
import static com.unindra.restoclient.Dialog.getDialogLayout;
import static com.unindra.restoclient.models.Item.getItems;
import static com.unindra.restoclient.models.Setting.setting;

public class AppController implements Initializable {
    public JFXButton ramenButton;
    public JFXButton minumanButton;
    public JFXButton cemilanButton;
    public JFXButton lainnyaButton;
    public JFXButton pesananButton;
    public ScrollPane mainPane;

    private VBox ramenPane;
    private VBox minumanPane;
    private VBox cemilanPane;
    private VBox lainnyaPane;

    private Dialog daftarPesanan;
    private Dialog alert;
    private Dialog settingDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Daftar Pesanan & Setting
        Platform.runLater(this::run);

        // Daftar Menu
        ramenPane = new VBox();
        minumanPane = new VBox();
        cemilanPane = new VBox();
        lainnyaPane = new VBox();

        try {
            setRamenPane(DaftarMenu.menus("ramen"));
            setAllMenuPane(DaftarMenu.menus("minuman"), minumanPane, "body-minuman-pane");
            setAllMenuPane(DaftarMenu.menus("cemilan"), cemilanPane, "body-cemilan-pane");
            setAllMenuPane(DaftarMenu.menus("lainnya"), lainnyaPane, "body-lainnya-pane");
            mainPane.setContent(ramenPane);
        } catch (IOException e) {
            ramenButton.setDisable(true);
            minumanButton.setDisable(true);
            cemilanButton.setDisable(true);
            lainnyaButton.setDisable(true);
            pesananButton.setDisable(true);
            Platform.runLater(() -> alert.information(
                    "Koneksi Terputus",
                    "Buka setting untuk mengubah alamat host atau port"));
        }
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

    public void daftarPesananHandle() {
        try {
            Item.updateItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        daftarPesanan.getAlert().show();
    }

    public void settingHandle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 3) {
            settingDialog.getAlert().show();
        }
    }

    private void pesan(List<Item> items) {
        items.forEach(Item::pesan);

        boolean success = true;
        for (Item item : items) success &= item.put().getStatus() == StatusResponse.SUCCESS;

        if (success && !items.isEmpty()) {
            alert.information(
                    "Berhasil",
                    "Pesanan anda berhasil! mohon tunggu pesanan disajikan");
            daftarPesanan.getAlert().hide();
        }
    }

    private void bayar() throws IOException {
        StandardResponse standardResponse = get("/bayar/" + setting().getNo_meja());
        if (standardResponse.getStatus() == StatusResponse.SUCCESS) {
            alert.information(
                    "Mohon tunggu",
                    "Kasir akan mengantarkan bill ke meja anda");
            daftarPesanan.getAlert().hide();
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
                c.rootPane.getStyleClass().add("body-ramen-pane" + i % 4);
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

    private void run() {
        Stage stage = (Stage) mainPane.getScene().getWindow();

        alert = new Dialog(stage);
        daftarPesanan = new Dialog(stage);
        settingDialog = new Dialog(stage);

        JFXButton pesanButton = new JFXButton("Pesan");
        JFXButton bayarButton = new JFXButton("Bayar");
        JFXButton keluarPesananButton = new JFXButton("Keluar");
        JFXButton keluarSettingButton = new JFXButton("Keluar");
        JFXButton simpanSettingButton = new JFXButton("Simpan");

        try {
            daftarPesanan.getAlert().setContent(getDialogLayout(
                    new Label("Daftar Pesanan"),
                    FXMLLoader.load(getClass().getResource("/fxml/pesanan.fxml")),
                    pesanButton,
                    bayarButton,
                    keluarPesananButton));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/setting.fxml"));

        try {
            settingDialog.getAlert().setContent(getDialogLayout(
                    new Label("Setting"),
                    fxmlLoader.load(),
                    simpanSettingButton,
                    keluarSettingButton));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pesanButton.setOnAction(event -> {
            List<Item> items = getItems("belum dipesan");
            if (!items.isEmpty())
                alert.confirmation(
                        "Anda yakin pesanan benar? pesanan tidak dapat dibatalkan setelah proses pemesanan berhasil",
                        e -> pesan(items));
        });
        bayarButton.setOnAction(event -> {
            try {
                bayar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        keluarPesananButton.setOnAction(event -> daftarPesanan.getAlert().hide());

        SettingController settingController = fxmlLoader.getController();
        simpanSettingButton.setOnAction(event -> {
            Setting setting = setting();
            setting.setNo_meja(settingController.mejaField.getText());
            setting.setHost(settingController.hostField.getText());
            setting.setPort(settingController.portField.getText());
            setting.simpan();
            alert.confirmation(
                    "Aplikasi akan dimatikan",
                    e -> System.exit(0));
        });
        simpanSettingButton.requestFocus();
        keluarSettingButton.setOnAction(event -> settingDialog.getAlert().hide());
    }
}
