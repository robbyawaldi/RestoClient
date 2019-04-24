package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.unindra.restoclient.Dialog;
import com.unindra.restoclient.Rupiah;
import com.unindra.restoclient.models.Item;
import com.unindra.restoclient.models.Menu;
import com.unindra.restoclient.models.StandardResponse;
import com.unindra.restoclient.models.StatusResponse;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;

public class AllMenuController {

    public Label namaLabel;
    public Label hargaLabel;
    public JFXButton tambahButton;
    public Label jumlahLabel;

    private AtomicInteger jumlah = new AtomicInteger(1);

    void setMenu(Menu menu) {
        namaLabel.setText(menu.getNama_menu());
        hargaLabel.setText(Rupiah.rupiah(menu.getHarga_menu()));

        tambahButton.setOnAction(event -> {
            Dialog alert = new Dialog((Stage) tambahButton.getScene().getWindow());

            Item item = new Item(menu, jumlah.get());
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
        });
    }

    private void reset() {
        jumlah.set(1);
        jumlahLabel.setText(String.valueOf(jumlah.get()));
        namaLabel.requestFocus();
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
