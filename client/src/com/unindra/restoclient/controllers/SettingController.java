package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import static com.unindra.restoclient.models.Setting.setting;

public class SettingController implements Initializable {
    public JFXTextField mejaField;
    public JFXTextField hostField;
    public JFXTextField portField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mejaField.setText(setting().getNo_meja());
        hostField.setText(setting().getHost());
        portField.setText(setting().getPort());
    }
}
