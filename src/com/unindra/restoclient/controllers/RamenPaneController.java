package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.unindra.restoclient.Rupiah;
import com.unindra.restoclient.models.DaftarMenu;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class RamenPaneController {
    public VBox rootPane;
    public JFXButton tambahButton;
    public JFXComboBox<Integer> levelCombo;
    public ImageView imageView;
    public Label namaLabel;
    public Label keteranganLabel;
    public Label hargaLabel;

    public void actionhandle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == tambahButton) {
            System.out.println(levelCombo.getStyleClass());
        }
    }

    public void setMenu(DaftarMenu menu) {
        namaLabel.setText(menu.getNama_menu().toUpperCase());
        keteranganLabel.setText(menu.getDeskripsi());
        hargaLabel.setText(Rupiah.format(menu.getHarga_menu()));
        imageView.setImage(menu.getImage());
        levelCombo.setItems(FXCollections.observableArrayList(0,1,2,3,4,5));
    }
}
