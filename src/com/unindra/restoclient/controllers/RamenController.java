package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.unindra.restoclient.Dialog;
import com.unindra.restoclient.models.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.unindra.restoclient.Rupiah.rupiah;
import static com.unindra.restoclient.models.Pesanan.getPesananList;

public class RamenController {
    public VBox rootPane;
    public JFXComboBox<String> levelCombo;
    public Label namaLabel;
    public Label keteranganLabel;
    public Label hargaLabel;
    public Circle circle;
    public JFXButton tambahButton;
    public Label jumlahLabel;

    private AtomicInteger jumlah = new AtomicInteger(1);

    void setMenu(Menu menu) {
        namaLabel.setText(menu.getNama_menu().toUpperCase());
        hargaLabel.setText(rupiah(menu.getHarga_menu()));

        try {
            DetailRamen detailRamen = DetailRamen.detailRamen(menu.getNama_menu());
            if (detailRamen != null) {
                keteranganLabel.setText(detailRamen.getDeskripsi());
                Image image = new Image(new ByteArrayInputStream(detailRamen.getFoto()));
                circle.setFill(new ImagePattern(image));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        levelCombo.setItems(
                FXCollections.observableArrayList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));

        levelCombo.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            try {
                                if (Level.level(Integer.parseInt(item)).getHarga_level() > 0) {
                                    setText(item+" (+"+rupiah(Level.level(Integer.parseInt(item)).getHarga_level())+")");
                                } else {
                                    setText(item);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }
        });

        tambahButton.setOnAction(event -> {
            Dialog alert = new Dialog((Stage) rootPane.getScene().getWindow());

            if (getPesananList("dibayar").isEmpty()) {
                if (!levelCombo.getSelectionModel().isEmpty()) {
                    Pesanan pesanan = new Pesanan(menu, jumlah.get(), Integer.parseInt(levelCombo.getValue()));
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
                        "Level belum dimasukkan");
            } else alert.information(
                    "Gagal",
                    "Proses pembayaran belum selesai");
            reset();
        });
    }

    private void reset() {
        jumlah.set(1);
        jumlahLabel.setText(String.valueOf(jumlah.get()));
        levelCombo.getSelectionModel().clearSelection();
        rootPane.requestFocus();
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
