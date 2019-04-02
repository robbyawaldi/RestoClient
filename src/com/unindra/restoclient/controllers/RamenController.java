package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.unindra.restoclient.Rupiah;
import com.unindra.restoclient.models.DaftarMenu;
import com.unindra.restoclient.models.Item;
import com.unindra.restoclient.models.StandardResponse;
import com.unindra.restoclient.models.StatusResponse;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import static com.unindra.restoclient.models.Item.item;

public class RamenController {
    public VBox rootPane;
    public JFXComboBox<Integer> levelCombo;
    public Label namaLabel;
    public Label keteranganLabel;
    public Label hargaLabel;
    public Circle circle;
    public JFXButton tambahButton;
    public JFXTextField jumlahField;

    void setMenu(DaftarMenu menu) {
        namaLabel.setText(menu.getNama_menu().toUpperCase());
        keteranganLabel.setText(menu.getDeskripsi());
        hargaLabel.setText(Rupiah.rupiah(menu.getHarga_menu()));
        circle.setFill(new ImagePattern(menu.getImage()));
        levelCombo.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5));
        tambahButton.setOnAction(event -> {
            Item item = item(menu, Integer.valueOf(jumlahField.getText()));
            StandardResponse standardResponse = item.post();
            if (standardResponse.getStatus() == StatusResponse.SUCCESS) {
                System.out.println("Berhasil");
            } else {
                System.out.println("Gagal");
            }
        });
    }
}
