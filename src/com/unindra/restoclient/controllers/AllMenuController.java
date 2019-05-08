package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.unindra.restoclient.Dialog;
import com.unindra.restoclient.Rupiah;
import com.unindra.restoclient.models.Pesanan;
import com.unindra.restoclient.models.Menu;
import com.unindra.restoclient.models.StandardResponse;
import com.unindra.restoclient.models.StatusResponse;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;

import static com.unindra.restoclient.models.Pesanan.getPesananList;

public class AllMenuController {

    public Label namaLabel;
    public Label hargaLabel;
    public JFXButton tambahButton;
    public Label jumlahLabel;

    private AtomicInteger jumlah = new AtomicInteger(1);

    void setMenu(Menu menu) {
        namaLabel.setText(menu.getNama());
        hargaLabel.setText(Rupiah.rupiah(menu.getHarga_menu()));

        tambahButton.setOnAction(event -> {
            Dialog alert = new Dialog((Stage) tambahButton.getScene().getWindow());

            if (getPesananList("dibayar").isEmpty()) {
                Pesanan pesanan = new Pesanan(menu, jumlah.get());
                StandardResponse standardResponse = pesanan.post();
                if (standardResponse.getStatus() == StatusResponse.SUCCESS)
                    alert.information(
                            "Berhasil",
                            "Pesanan anda disimpan ke daftar pesanan");
                else alert.information(
                        "Gagal",
                        "Pesanan anda gagal diproses");
            } else alert.information(
                    "Gagal",
                    "Proses pembayaran belum selesai");
            reset();
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
