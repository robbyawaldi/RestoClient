package com.unindra.restoclient.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unindra.restoclient.Rupiah;
import com.unindra.restoclient.models.Item;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.unindra.restoclient.models.DaftarMenu.menu;
import static com.unindra.restoclient.models.Item.getItems;

public class PesananController implements Initializable {

    public JFXTreeTableView<Item> pesananTableView;
    public Label totalLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeTableColumn<Item, String> namaCol = new TreeTableColumn<>("Nama");
        TreeTableColumn<Item, Integer> jumlahCol = new TreeTableColumn<>("Jumlah");
        TreeTableColumn<Item, Integer> hargaCol = new TreeTableColumn<>("Harga");
        TreeTableColumn<Item, Integer> totalCol = new TreeTableColumn<>("Total");
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
                            button.setOnAction(event -> {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Pesanan");
                                alert.setHeaderText("Hapus pesanan");
                                alert.setContentText("Anda yakin ingin menghapus pesanan ini?");

                                Stage primaryStage = (Stage) pesananTableView.getScene().getWindow();
                                alert.setX(primaryStage.getX() + 100);
                                alert.setY(primaryStage.getY() + 100);

                                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                                stage.getIcons().add(new Image("/icons/logo-ramen-bulet-merah-copy20x20.png"));

                                Optional<ButtonType> optional = alert.showAndWait();
                                if (optional.isPresent() && optional.get() == ButtonType.OK) {
                                    getItems().remove(getIndex());
                                    setTotalLabel();
                                }
                            });
                            setGraphic(button);
                            setText(null);
                        }
                    }
                };
            }
        });

        TreeItem<Item> root = new RecursiveTreeItem<>(getItems(), RecursiveTreeObject::getChildren);
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
        totalLabel.setText(Rupiah.format(Item.getGrandTotal()));
    }
}
