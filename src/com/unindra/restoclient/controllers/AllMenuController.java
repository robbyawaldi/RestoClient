package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.unindra.restoclient.Rupiah;
import com.unindra.restoclient.models.DaftarMenu;
import javafx.scene.control.Label;

public class AllMenuController {

    public Label namaLabel;
    public Label hargaLabel;
    public JFXButton tambahButton;

    void setMenu(DaftarMenu menu) {
        namaLabel.setText(menu.getNama_menu());
        hargaLabel.setText(Rupiah.rupiah(menu.getHarga_menu()));
        tambahButton.setOnAction(event -> System.out.println(menu));
    }
}
