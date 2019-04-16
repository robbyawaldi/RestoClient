package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.unindra.restoclient.Dialog;
import com.unindra.restoclient.Rupiah;
import com.unindra.restoclient.models.Menu;
import com.unindra.restoclient.models.Item;
import com.unindra.restoclient.models.StandardResponse;
import com.unindra.restoclient.models.StatusResponse;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class RamenController {
    public VBox rootPane;
    public JFXComboBox<Integer> levelCombo;
    public Label namaLabel;
    public Label keteranganLabel;
    public Label hargaLabel;
    public Circle circle;
    public JFXButton tambahButton;
    public JFXTextField jumlahField;

    void setMenu(Menu menu) {
        namaLabel.setText(menu.getNama_menu().toUpperCase());
        keteranganLabel.setText(menu.getDeskripsi());
        hargaLabel.setText(Rupiah.rupiah(menu.getHarga_menu()));
        circle.setFill(new ImagePattern(menu.getImage()));
        levelCombo.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        jumlahField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                jumlahField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tambahButton.setOnAction(event -> {
            Dialog alert = new Dialog((Stage) rootPane.getScene().getWindow());

            if (validasi()) {
                Item item = new Item(menu, Integer.valueOf(jumlahField.getText()), levelCombo.getValue());
                StandardResponse standardResponse = item.post();
                if (standardResponse.getStatus() == StatusResponse.SUCCESS) {
                    alert.information(
                            "Berhasil",
                            "Pesanan anda akan disimpan ke daftar pesanan, masuk ke daftar pesanan untuk melanjutkan proses pemesanan");
                    reset();
                } else {
                    alert.information(
                            "Gagal",
                            "Pesanan anda gagal diproses");
                    reset();
                }
            } else {
                alert.information(
                        "Gagal",
                        "Level atau jumlah belum dimasukkan");
                reset();
            }
        });
    }

    private void reset() {
        jumlahField.setText("");
        levelCombo.getSelectionModel().clearSelection();
        rootPane.requestFocus();
    }

    private boolean validasi() {
        return !jumlahField.getText().isEmpty() && !levelCombo.getSelectionModel().isEmpty();
    }
}
