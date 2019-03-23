package com.unindra.restoclient.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.unindra.restoclient.models.DaftarMenu.menuList;


public class RamenController implements Initializable {

    public VBox ramenPanes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < menuList("ramen").size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ramenpane.fxml"));
                Parent root = fxmlLoader.load();

                RamenPaneController c = fxmlLoader.getController();
                c.rootPane.getStyleClass().add("body-ramen-pane" + i);
                c.setMenu(menuList("ramen").get(i));

                ramenPanes.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
