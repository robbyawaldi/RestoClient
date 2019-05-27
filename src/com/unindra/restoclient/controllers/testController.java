package com.unindra.restoclient.controllers;

import com.unindra.restoclient.models.DetailRamen;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class testController implements Initializable {

    public Label deskripsi;
    public ImageView foto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            DetailRamen detailRamen = DetailRamen.detailRamen("miso");
            System.out.println(detailRamen);
            if (detailRamen != null) {
                deskripsi.setText(detailRamen.getDeskripsi());
                Image image = new Image(new ByteArrayInputStream(detailRamen.getFoto()));
                foto.setImage(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
