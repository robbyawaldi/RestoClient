package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unindra.restoclient.Dialog;
import com.unindra.restoclient.models.Item;
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
import static com.unindra.restoclient.models.Item.getItems;
import static com.unindra.restoclient.models.Menu.menu;
import static java.util.Objects.requireNonNull;

public class PesananController implements Initializable {

    public JFXTreeTableView<Item> pesananTableView;
    public Label totalLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeTableColumn<Item, String> namaCol = new TreeTableColumn<>("Nama");
        TreeTableColumn<Item, Integer> jumlahCol = new TreeTableColumn<>("Jumlah");
        TreeTableColumn<Item, String> hargaCol = new TreeTableColumn<>("Harga");
        TreeTableColumn<Item, String> totalCol = new TreeTableColumn<>("Total");
        TreeTableColumn<Item, String> hapusCol = new TreeTableColumn<>("Hapus");

        namaCol.setCellValueFactory(param -> requireNonNull(menu(param.getValue().getValue())).namaProperty());
        jumlahCol.setCellValueFactory(param -> param.getValue().getValue().jumlahProperty());
        hargaCol.setCellValueFactory(param -> requireNonNull(menu(param.getValue().getValue())).hargaProperty());
        totalCol.setCellValueFactory(param -> param.getValue().getValue().totalProperty());
        hapusCol.setCellValueFactory(param -> new SimpleStringProperty(""));

        namaCol.setCellFactory(new Callback<TreeTableColumn<Item, String>, TreeTableCell<Item, String>>() {
            @Override
            public TreeTableCell<Item, String> call(TreeTableColumn<Item, String> param) {
                return new TreeTableCell<Item, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText(null);
                        } else {
                            Item i = getItems().get(getIndex());
                            if (requireNonNull(menu(i)).getTipe_menu().equals("ramen"))
                                setText(item + " lv." + i.getLevel_item());
                            else setText(item);
                        }
                    }
                };
            }
        });

        hapusCol.setCellFactory(new Callback<TreeTableColumn<Item, String>, TreeTableCell<Item, String>>() {
            @Override
            public TreeTableCell<Item, String> call(TreeTableColumn<Item, String> param) {
                return new TreeTableCell<Item, String>() {
                    final JFXButton button = new JFXButton("hapus");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            button.setStyle("-fx-background-color: #EAEAEA");
                            button.setOnAction(event -> {
                                Dialog alert = new Dialog((Stage) pesananTableView.getScene().getWindow());

                                alert.confirmation(
                                        "Anda yakin ingin menghapus pesanan ini?",
                                        e -> {
                                            if (getItems().get(getIndex()).delete().getStatus() == StatusResponse.SUCCESS)
                                                alert.getAlert().hide();
                                        });
                            });

                            Item i = getItems().get(getIndex());
                            if (!i.getStatus_item().equals("belum dipesan")) {
                                setText(i.getStatus_item());
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

        getItems().addListener((ListChangeListener<Item>) c ->
                Platform.runLater(() -> totalLabel.setText(rupiah(Item.getGrandTotal()))));

        pesananTableView.setRoot(new RecursiveTreeItem<>(getItems(), RecursiveTreeObject::getChildren));
        pesananTableView.getColumns().add(namaCol);
        pesananTableView.getColumns().add(jumlahCol);
        pesananTableView.getColumns().add(hargaCol);
        pesananTableView.getColumns().add(totalCol);
        pesananTableView.getColumns().add(hapusCol);
        pesananTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }
}
