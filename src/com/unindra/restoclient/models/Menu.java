package com.unindra.restoclient.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.unindra.restoclient.Client.get;
import static com.unindra.restoclient.Client.gson;
import static com.unindra.restoclient.Rupiah.rupiah;

public class Menu {
    private String nama_menu;
    private int harga_menu;
    private String tipe;

    public Menu(String nama_menu, int harga_menu, String tipe) {
        this.nama_menu = nama_menu;
        this.harga_menu = harga_menu;
        this.tipe = tipe;
    }

    private static List<Menu> menus() {
        try {
            Menu[] daftarMenus = gson().fromJson(get("/menus").getData(), Menu[].class);
            return FXCollections.observableArrayList(daftarMenus);
        } catch (IOException e) {
            return FXCollections.observableArrayList();
        }
    }

    public static List<Menu> menus(String tipe) {
        return menus()
                .stream()
                .filter(menu -> menu.tipe.equals(tipe))
                .collect(Collectors.toList());
    }

    public static Menu menu(Pesanan pesanan) {
        return menus()
                .stream()
                .filter(menu -> menu.nama_menu.equals(pesanan.getNama_menu()))
                .findFirst()
                .orElse(null);
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public int getHarga_menu() {
        return harga_menu;
    }

    public String getTipe() {
        return tipe;
    }

    public StringProperty nama_menuProperty() {
        return new SimpleStringProperty(nama_menu);
    }

    public StringProperty harga_menuProperty() {
        return new SimpleStringProperty(rupiah(harga_menu));
    }

    @Override
    public String toString() {
        return "Menu{" +
                ", nama_menu='" + nama_menu + '\'' +
                ", harga_menu=" + harga_menu +
                '}';
    }
}
