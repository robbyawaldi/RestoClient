package com.unindra.restoclient.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static com.unindra.restoclient.models.DaftarMenu.menu;

public class Item extends RecursiveTreeObject<Item> {
    private int id_menu;
    private int jumlah;
    private final static ObservableList<Item> items = FXCollections.observableArrayList();

    public Item(int id_menu, int jumlah) {
        this.id_menu = id_menu;
        this.jumlah = jumlah;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public static ObservableList<Item> getItems() {
        return items;
    }

    public int getTotal() {
        return menu(this).getHarga_menu() * jumlah;
    }

    public static int getGrandTotal() {
        return items.stream().mapToInt(Item::getTotal).sum();
    }

    public ObjectProperty<Integer> totalProperty() {
        return new SimpleObjectProperty<>(getTotal());
    }

    public ObjectProperty<Integer> jumlahProperty() {
        return new SimpleObjectProperty<>(jumlah);
    }


    @Override
    public String toString() {
        return "Item{" +
                "id_menu=" + id_menu +
                ", jumlah=" + jumlah +
                '}';
    }
}
