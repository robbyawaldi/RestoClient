package com.unindra.restoclient.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

import static com.unindra.restoclient.Client.*;
import static com.unindra.restoclient.Rupiah.rupiah;
import static com.unindra.restoclient.models.DaftarMenu.menu;

public class Item extends RecursiveTreeObject<Item> {
    @SuppressWarnings("unused")
    private int id_item;
    private int id_menu;
    private int jumlah_item;
    private int no_meja;
    private String status_item;
    @Expose
    private static final String paramUrl = "/items";

    private Item(int id_menu, int jumlah_item, int no_meja, String status_item) {
        this.id_menu = id_menu;
        this.jumlah_item = jumlah_item;
        this.no_meja = no_meja;
        this.status_item = status_item;
    }

    public static ObservableList<Item> items() {
        StandardResponse standardResponse = get(paramUrl + "/1");
        if (standardResponse.getStatus() == StatusResponse.SUCCESS) {
            Item[] items = new Gson().fromJson(standardResponse.getData(), Item[].class);
            for (Item item : items) item.setChildren(FXCollections.observableArrayList());
            return FXCollections.observableArrayList(items);
        }
        return FXCollections.observableArrayList();
    }

    public static List<Item> items(String status_item) {
        return items()
                .stream()
                .filter(item -> item.getStatus_item().equals("belum dipesan"))
                .collect(Collectors.toList());
    }

    public static Item item(DaftarMenu menu, int jumlah_item) {
        return new Item(menu.getId_menu(), jumlah_item, 1, "belum dipesan");
    }

    public void pesan() {
        this.status_item = "dipesan";
    }

    public StandardResponse post() {
        return send(paramUrl, "POST", gson().toJson(this));
    }

    public StandardResponse put() {
        return send(paramUrl, "PUT", gson().toJson(this));
    }

    private int getTotal() {
        return menu(this).getHarga_menu() * jumlah_item;
    }

    public static int getGrandTotal() {
        return items().stream().mapToInt(Item::getTotal).sum();
    }

    public int getId_item() {
        return id_item;
    }

    int getId_menu() {
        return id_menu;
    }

    public String getStatus_item() {
        return status_item;
    }

    public ObjectProperty<Integer> jumlahProperty() {
        return new SimpleObjectProperty<>(jumlah_item);
    }

    public StringProperty totalProperty() {
        return new SimpleStringProperty(rupiah(getTotal()));
    }

    @Override
    public String toString() {
        return "Item{" +
                "id_item=" + id_item +
                ", id_menu=" + id_menu +
                ", jumlah_item=" + jumlah_item +
                ", no_meja=" + no_meja +
                ", status_item='" + status_item + '\'' +
                '}';
    }
}
