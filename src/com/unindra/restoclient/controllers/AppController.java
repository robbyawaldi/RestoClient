package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.unindra.restoclient.Dialog;
import com.unindra.restoclient.models.Item;
import com.unindra.restoclient.models.Menu;
import com.unindra.restoclient.models.Setting;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

    private FlowPane ramenPane;
    private FlowPane minumanPane;
    private FlowPane cemilanPane;
    private FlowPane lainnyaPane;
    private VBox pesananPane;

    private Dialog pesananDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Daftar Menu
        ramenPane = new FlowPane();
        minumanPane = new FlowPane();
        cemilanPane = new FlowPane();
        lainnyaPane = new FlowPane();

        setRamenPane(Menu.menus("ramen"));
        setAllMenuPane(Menu.menus("minuman"), minumanPane, "body-minuman-pane");
        setAllMenuPane(Menu.menus("cemilan"), cemilanPane, "body-cemilan-pane");
        setAllMenuPane(Menu.menus("lainnya"), lainnyaPane, "body-lainnya-pane");
        mainPane.setContent(ramenPane);

        try {
            pesananPane = FXMLLoader.load(getClass().getResource("/fxml/pesanan.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Meminta data item ke server setiap 1 detik
        Thread thread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    Item.updateItems();
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException e) {
                    break;
                }
            }
            ramenButton.setDisable(true);
            minumanButton.setDisable(true);
            cemilanButton.setDisable(true);
            lainnyaButton.setDisable(true);
            pesananButton.setDisable(true);
            Platform.runLater(() ->
                    getDialog().information(
                            "Koneksi Terputus",
                            "Buka setting untuk mengubah alamat host atau port"));
        });
        thread.start();

        // Daftar Pesanan
        Platform.runLater(() -> {
            pesananDialog = getDialog();
            JFXButton pesanButton = new JFXButton("Pesan");
            JFXButton bayarButton = new JFXButton("Bayar");
            JFXButton keluarButton = new JFXButton("Keluar");

            pesananDialog.getDialog()
                    .setContent(
                            getDialogLayout(
                                    new Label("Daftar Pesanan"),
                                    pesananPane,
                                    pesanButton,
                                    bayarButton,
                                    keluarButton));

            pesanButton.setOnAction(event -> {
                if (!getItems("belum dipesan").isEmpty()) {
                    Dialog dialog = getDialog();
                    dialog.confirmation(
                            "Pesanan tidak dapat dibatalkan setelah proses pemesanan berhasil",
                            e -> {
                                if (Item.pesan()) {
                                    getDialog().information(
                                            "Berhasil",
                                            "Pesanan anda berhasil! mohon tunggu pesanan disajikan");
                                    dialog.getDialog().hide();
                                }
                            });
                }
            });

            bayarButton.setOnAction(event -> {
                if (getItems("diproses").size() == getItems().size())
                    if (getItems().size() != 0) try {
                        if (Item.bayar())
                            getDialog().information(
                                    "Mohon tunggu",
                                    "Kasir akan mengantarkan bill ke meja anda");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            });

            keluarButton.setOnAction(event -> pesananDialog.getDialog().hide());
        });

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
        pesananDialog.getDialog().show();
    }

    public void settingHandle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 3) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/setting.fxml"));

            JFXButton keluarButton = new JFXButton("Keluar");
            JFXButton simpanButton = new JFXButton("Simpan");

            Dialog settingDialog = getDialog();
            try {
                settingDialog.getDialog().setContent(
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
            keluarButton.setOnAction(event -> settingDialog.getDialog().hide());
            settingDialog.getDialog().show();
        }
    }

    private void setRamenPane(List<Menu> menuList) {
        ramenPane.setPrefWidth(800);
        ramenPane.setPrefHeight(500);

        menuList.forEach(menu -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ramen.fxml"));
                Parent root = fxmlLoader.load();

                RamenController c = fxmlLoader.getController();
                c.setMenu(menu);

                ramenPane.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setAllMenuPane(List<Menu> menuList, FlowPane allMenuPane, String style) {
        allMenuPane.setPrefWidth(800);
        allMenuPane.setPrefHeight(500);
        allMenuPane.setHgap(30);
        allMenuPane.setVgap(30);
        allMenuPane.setPadding(new Insets(30,20,30,36));
        allMenuPane.getStyleClass().add(style);

        menuList.forEach(menu -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/allmenu.fxml"));
                Parent root = fxmlLoader.load();

                AllMenuController c = fxmlLoader.getController();
                c.setMenu(menu);

                allMenuPane.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Dialog getDialog() {
        return new Dialog((Stage) mainPane.getScene().getWindow());
    }
}
