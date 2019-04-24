package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.unindra.restoclient.Dialog;
import com.unindra.restoclient.Rupiah;
import com.unindra.restoclient.models.Item;
import com.unindra.restoclient.models.Menu;
import com.unindra.restoclient.models.StandardResponse;
import com.unindra.restoclient.models.StatusResponse;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;

public class RamenController {
    public VBox rootPane;
    public JFXComboBox<Integer> levelCombo;
    public Label namaLabel;
    public Label keteranganLabel;
    public Label hargaLabel;
    public Circle circle;
    public JFXButton tambahButton;
    public Label jumlahLabel;

    private AtomicInteger jumlah = new AtomicInteger(1);

    void setMenu(Menu menu) {
        namaLabel.setText(menu.getNama_menu().toUpperCase());
        keteranganLabel.setText(menu.getDeskripsi());
        hargaLabel.setText(Rupiah.rupiah(menu.getHarga_menu()));
        circle.setFill(new ImagePattern(menu.getImage()));
        levelCombo.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        tambahButton.setOnAction(event -> {
            Dialog alert = new Dialog((Stage) rootPane.getScene().getWindow());

            if (validasi()) {
                Item item = new Item(menu, jumlah.get(), levelCombo.getValue());
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
                        "Level belum dimasukkan");
                reset();
            }
        });
    }

    private void reset() {
        jumlah.set(1);
        jumlahLabel.setText(String.valueOf(jumlah.get()));
        levelCombo.getSelectionModel().clearSelection();
        rootPane.requestFocus();
    }

    private boolean validasi() {
        return !levelCombo.getSelectionModel().isEmpty();
    }

    public void kurangJmlHandle() {
        if (jumlah.decrementAndGet() > 0) {
            jumlahLabel.setText(String.valueOf(jumlah.get()));
        } else jumlah.incrementAndGet();
    }

    public void tambahJmlHandle() {
        jumlahLabel.setText(String.valueOf(jumlah.incrementAndGet()));
    }
}
