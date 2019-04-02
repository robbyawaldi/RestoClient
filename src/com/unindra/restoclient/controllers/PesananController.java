package com.unindra.restoclient.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unindra.restoclient.Rupiah;
import com.unindra.restoclient.models.Item;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

import static com.unindra.restoclient.models.DaftarMenu.menu;
import static com.unindra.restoclient.models.Item.items;

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

        namaCol.setCellValueFactory(param -> menu(param.getValue().getValue()).namaProperty());
        jumlahCol.setCellValueFactory(param -> param.getValue().getValue().jumlahProperty());
        hargaCol.setCellValueFactory(param -> menu(param.getValue().getValue()).hargaProperty());
        totalCol.setCellValueFactory(param -> param.getValue().getValue().totalProperty());
        hapusCol.setCellValueFactory(param -> new SimpleStringProperty(""));

        hapusCol.setCellFactory(new Callback<TreeTableColumn<Item, String>, TreeTableCell<Item, String>>() {
            @Override
            public TreeTableCell<Item, String> call(TreeTableColumn<Item, String> param) {
                return new TreeTableCell<Item, String>() {
                    final JFXButton button = new JFXButton("hapus");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            button.setStyle("-fx-background-color: #EAEAEA");
                            button.setOnAction(event -> {
                                Stage primaryStage = (Stage) pesananTableView.getScene().getWindow();
                                JFXAlert<String> alert = new JFXAlert<>(primaryStage);
                                alert.initModality(Modality.APPLICATION_MODAL);
                                alert.setOverlayClose(false);

                                JFXDialogLayout layout = new JFXDialogLayout();
                                layout.setHeading(new Label("Hapus Pesanan"));
                                layout.setBody(new Label("Anda yakin ingin menghapus pesanan ini?"));

                                JFXButton iyaButton = new JFXButton("Iya");
                                iyaButton.setDefaultButton(true);
                                iyaButton.setOnAction(evt -> {
                                    alert.hideWithAnimation();
                                    items().remove(getIndex());
                                    setTotalLabel();
                                });

                                JFXButton BatalButton = new JFXButton("Batal");
                                BatalButton.setCancelButton(true);
                                BatalButton.setOnAction(evt -> alert.hideWithAnimation());

                                layout.setActions(iyaButton, BatalButton);
                                alert.setContent(layout);
                                alert.show();
                            });
                            Item i = items().get(getIndex());
                            if (!i.getStatus_item().equals("belum dipesan")) {
                                setGraphic(null);
                                setText(i.getStatus_item());
                            } else {
                                setGraphic(button);
                                setText(null);
                            }
                        }
                    }
                };
            }
        });
        TreeItem<Item> root = new RecursiveTreeItem<>(items(), RecursiveTreeObject::getChildren);
        pesananTableView.setRoot(root);
        pesananTableView.getColumns().add(namaCol);
        pesananTableView.getColumns().add(jumlahCol);
        pesananTableView.getColumns().add(hargaCol);
        pesananTableView.getColumns().add(totalCol);
        pesananTableView.getColumns().add(hapusCol);
        pesananTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        setTotalLabel();
    }

    private void setTotalLabel() {
        totalLabel.setText(Rupiah.rupiah(Item.getGrandTotal()));
    }
}
