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

    private Dialog pesananDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Runnable runnable = () -> {
            while (!Thread.interrupted()) {
                try {
                    Item.updateItems();
                    System.out.println("loop");
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        // Daftar Pesanan
        Platform.runLater(() -> {
            pesananDialog = getDialog();
            JFXButton pesanButton = new JFXButton("Pesan");
            JFXButton bayarButton = new JFXButton("Bayar");
            JFXButton keluarButton = new JFXButton("Keluar");

            try {
                pesananDialog.getAlert().setContent(getDialogLayout(
                        new Label("Daftar Pesanan"),
                        FXMLLoader.load(getClass().getResource("/fxml/pesanan.fxml")),
                        pesanButton,
                        bayarButton,
                        keluarButton));
            } catch (IOException e) {
                e.printStackTrace();
            }

            pesanButton.setOnAction(event -> {
                List<Item> items = getItems("belum dipesan");
                if (!items.isEmpty()) {
                    Dialog dialog = getDialog();
                    dialog.confirmation(
                            "Anda yakin pesanan benar? pesanan tidak dapat dibatalkan setelah proses pemesanan berhasil",
                            e -> {
                                pesan(items);
                                dialog.getAlert().hide();
                            });
                }
            });
            bayarButton.setOnAction(event -> {
                List<Item> items = getItems("diproses");
                if (items.size() == getItems().size()) {
                    try {
                        bayar();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            keluarButton.setOnAction(event -> pesananDialog.getAlert().hide());
        });

        // Daftar Menu
        ramenPane = new VBox();
        minumanPane = new VBox();
        cemilanPane = new VBox();
        lainnyaPane = new VBox();

        try {
            setRamenPane(Menu.menus("ramen"));
            setAllMenuPane(Menu.menus("minuman"), minumanPane, "body-minuman-pane");
            setAllMenuPane(Menu.menus("cemilan"), cemilanPane, "body-cemilan-pane");
            setAllMenuPane(Menu.menus("lainnya"), lainnyaPane, "body-lainnya-pane");
            mainPane.setContent(ramenPane);
        } catch (IOException e) {
            ramenButton.setDisable(true);
            minumanButton.setDisable(true);
            cemilanButton.setDisable(true);
            lainnyaButton.setDisable(true);
            pesananButton.setDisable(true);
            Platform.runLater(() -> getDialog().information(
                    "Koneksi Terputus",
                    "Buka setting untuk mengubah alamat host atau port"));
        }
    }

    public void menuHandle(ActionEvent actionEvent) {
        ramenButton.getStyleClass().set(2, "ramen");
        minumanButton.getStyleClass().set(2, "minuman");
        cemilanButton.getStyleClass().set(2, "cemilan");
        lainnyaButton.getStyleClass().set(2, "lainnya");

        Object source = actionEvent.getSource();
        if (ramenButton.equals(source)) {
            ramenButton.getStyleClass().set(2, "ramen-pressed");
            mainPane.setContent(ramenPane);
        } else if (minumanButton.equals(source)) {
            minumanButton.getStyleClass().set(2, "minuman-pressed");
            mainPane.setContent(minumanPane);
        } else if (cemilanButton.equals(source)) {
            cemilanButton.getStyleClass().set(2, "cemilan-pressed");
            mainPane.setContent(cemilanPane);
        } else if (lainnyaButton.equals(source)) {
            lainnyaButton.getStyleClass().set(2, "lainnya-pressed");
            mainPane.setContent(lainnyaPane);
        }
    }

    public void daftarPesananHandle() {
        pesananDialog.getAlert().show();
    }

    public void settingHandle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 3) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/setting.fxml"));

            JFXButton keluarButton = new JFXButton("Keluar");
            JFXButton simpanButton = new JFXButton("Simpan");

            Dialog settingDialog = getDialog();
            try {
                settingDialog.getAlert().setContent(
                        getDialogLayout(
                                new Label("Setting"),
                                fxmlLoader.load(),
                                simpanButton,
                                keluarButton));
            } catch (IOException e) {
                e.printStackTrace();
            }

            SettingController settingController = fxmlLoader.getController();
            simpanButton.setOnAction(event -> {
                Setting setting = setting();
                setting.setNo_meja(settingController.mejaField.getText());
                setting.setHost(settingController.hostField.getText());
                setting.setPort(settingController.portField.getText());
                setting.simpan();
                settingDialog.confirmation(
                        "Aplikasi akan dimatikan",
                        e -> System.exit(0));
            });
            simpanButton.requestFocus();
            keluarButton.setOnAction(event -> settingDialog.getAlert().hide());
        }
    }

    private void setRamenPane(List<Menu> menuList) {
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

    private void setAllMenuPane(List<Menu> menuList, VBox allMenuPane, String style) {
        allMenuPane.setPrefWidth(800);
        allMenuPane.setPrefHeight(500);
        allMenuPane.setPadding(new Insets(50, 60, 0, 129));
        allMenuPane.setSpacing(15);
        allMenuPane.getStyleClass().add(style);

        for (Menu daftarMenu : menuList) {
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

    private void pesan(List<Item> items) {
        items.forEach(Item::pesan);

        boolean success = true;
        for (Item item : items) success &= item.put().getStatus() == StatusResponse.SUCCESS;

        if (success && !items.isEmpty()) {
            getDialog().information(
                    "Berhasil",
                    "Pesanan anda berhasil! mohon tunggu pesanan disajikan");
        }
    }

    private void bayar() throws IOException {
        StandardResponse standardResponse = get("/bayar/" + setting().getNo_meja());
        if (standardResponse.getStatus() == StatusResponse.SUCCESS) {
            getDialog().information(
                    "Mohon tunggu",
                    "Kasir akan mengantarkan bill ke meja anda");
        }
    }

    private Dialog getDialog() {
        return new Dialog((Stage) mainPane.getScene().getWindow());
    }
}
