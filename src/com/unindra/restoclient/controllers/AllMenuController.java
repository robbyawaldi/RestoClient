package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.unindra.restoclient.Dialog;
import com.unindra.restoclient.Rupiah;
import com.unindra.restoclient.models.DaftarMenu;
import com.unindra.restoclient.models.Item;
import com.unindra.restoclient.models.StandardResponse;
import com.unindra.restoclient.models.StatusResponse;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AllMenuController {

    public Label namaLabel;
    public Label hargaLabel;
    public JFXButton tambahButton;
    public JFXTextField jumlahField;

    void setMenu(DaftarMenu menu) {
        namaLabel.setText(menu.getNama_menu());
        hargaLabel.setText(Rupiah.rupiah(menu.getHarga_menu()));

        jumlahField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                jumlahField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tambahButton.setOnAction(event -> {
            Dialog alert = new Dialog((Stage) tambahButton.getScene().getWindow());

            if (validasi()) {
                Item item = new Item(menu, Integer.valueOf(jumlahField.getText()));
                StandardResponse standardResponse = item.post();
                if (standardResponse.getStatus() == StatusResponse.SUCCESS) {
                    alert.information(
                            "Berhasil",
                            "Pesanan anda akan disimpan ke daftar pesanan, " +
                                    "masuk ke daftar pesanan untuk melanjutkan proses pemesanan");
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
                        "jumlah belum dimasukkan");
                reset();
            }
        });
    }

    private void reset() {
        jumlahField.setText("");
        namaLabel.requestFocus();
    }

    private boolean validasi() {
        return !jumlahField.getText().isEmpty();
    }
}
