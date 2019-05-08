package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unindra.restoclient.Dialog;
import com.unindra.restoclient.models.Pesanan;
import com.unindra.restoclient.models.StatusResponse;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

import static com.unindra.restoclient.Rupiah.rupiah;
import static com.unindra.restoclient.models.Menu.menu;

public class PesananController implements Initializable {

    public JFXTreeTableView<Pesanan> pesananTableView;
    public Label totalLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeTableColumn<Pesanan, String> namaCol = new TreeTableColumn<>("Nama");
        TreeTableColumn<Pesanan, Integer> jumlahCol = new TreeTableColumn<>("Jumlah");
        TreeTableColumn<Pesanan, String> hargaCol = new TreeTableColumn<>("Harga");
        TreeTableColumn<Pesanan, String> totalCol = new TreeTableColumn<>("Total");
        TreeTableColumn<Pesanan, String> hapusCol = new TreeTableColumn<>("Status");

        namaCol.setCellValueFactory(param -> menu(param.getValue().getValue()).namaProperty());
        jumlahCol.setCellValueFactory(param -> param.getValue().getValue().jumlahProperty());
        hargaCol.setCellValueFactory(param -> menu(param.getValue().getValue()).hargaProperty());
        totalCol.setCellValueFactory(param -> param.getValue().getValue().totalProperty());
        hapusCol.setCellValueFactory(param -> new SimpleStringProperty(""));

        namaCol.setCellFactory(new Callback<TreeTableColumn<Pesanan, String>, TreeTableCell<Pesanan, String>>() {
            @Override
            public TreeTableCell<Pesanan, String> call(TreeTableColumn<Pesanan, String> param) {
                return new TreeTableCell<Pesanan, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText(null);
                        } else {
                            Pesanan i = Pesanan.getPesananList().get(getIndex());
                            if (menu(i).getTipe().equals("ramen"))
                                setText(item + " lv." + i.getLevel());
                            else setText(item);
                        }
                    }
                };
            }
        });

        hapusCol.setCellFactory(new Callback<TreeTableColumn<Pesanan, String>, TreeTableCell<Pesanan, String>>() {
            @Override
            public TreeTableCell<Pesanan, String> call(TreeTableColumn<Pesanan, String> param) {
                return new TreeTableCell<Pesanan, String>() {
                    final JFXButton button = new JFXButton("hapus");
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Pesanan thisPesanan = Pesanan.getPesananList().get(getIndex());

                            button.setFocusTraversable(false);
                            button.getStyleClass().add("hapus");
                            button.setOnAction(event -> {
                                Dialog alert = new Dialog((Stage) pesananTableView.getScene().getWindow());

                                alert.confirmation(
                                        "Anda yakin ingin menghapus pesanan ini?",
                                        e -> {
                                            if (thisPesanan.delete().getStatus() == StatusResponse.SUCCESS)
                                                alert.getDialog().hide();
                                        });
                            });

                            if (!thisPesanan.getStatus_item().equals("belum dipesan")) {
                                setText(thisPesanan.getStatus_item());
                                setGraphic(null);
                            } else {
                                setText(null);
                                setGraphic(button);
                            }
                        }
                    }
                };
            }
        });

        Pesanan.getPesananList().addListener((ListChangeListener<Pesanan>) c ->
                Platform.runLater(() -> totalLabel.setText(rupiah(Pesanan.getGrandTotal()))));

        pesananTableView.setRoot(new RecursiveTreeItem<>(Pesanan.getPesananList(), RecursiveTreeObject::getChildren));
        pesananTableView.getColumns().add(namaCol);
        pesananTableView.getColumns().add(jumlahCol);
        pesananTableView.getColumns().add(hargaCol);
        pesananTableView.getColumns().add(totalCol);
        pesananTableView.getColumns().add(hapusCol);
        pesananTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }
}
