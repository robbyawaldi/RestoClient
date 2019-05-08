package com.unindra.restoclient.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.unindra.restoclient.Client.get;
import static com.unindra.restoclient.Client.gson;
import static com.unindra.restoclient.Rupiah.rupiah;

public class Menu {
    private int id_menu;
    private String nama_menu;
    private int harga_menu;
    private String tipe_menu;
    private String deskripsi;

    public Menu(int id_menu, String nama_menu, int harga_menu, String type, String deskripsi) {
        this.id_menu = id_menu;
        this.nama_menu = nama_menu;
        this.harga_menu = harga_menu;
        this.tipe_menu = type;
        this.deskripsi = deskripsi;
    }

    private static List<Menu> menus() {
        try {
            Menu[] daftarMenus = gson().fromJson(get("/menus").getData(), Menu[].class);
            return FXCollections.observableArrayList(daftarMenus);
        } catch (IOException e) {
            return FXCollections.observableArrayList();
        }
    }

    public static List<Menu> menus(String type) {
        return menus()
                .stream()
                .filter(menu -> menu.tipe_menu.equals(type))
                .collect(Collectors.toList());
    }

    public static Menu menu(Pesanan pesanan) {
        return menus()
                .stream()
                .filter(menu -> menu.id_menu == pesanan.getId_menu())
                .findFirst()
                .orElse(null);
    }

    int getId_menu() {
        return id_menu;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public int getHarga_menu() {
        return harga_menu;
    }

    public String getTipe_menu() {
        return tipe_menu;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public Image getImage() {
        return new Image(String.format("/img/%s.jpg", nama_menu));
    }

    public StringProperty namaProperty() {
        return new SimpleStringProperty(nama_menu);
    }

    public StringProperty hargaProperty() {
        return new SimpleStringProperty(rupiah(harga_menu));
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id_menu=" + id_menu +
                ", nama_menu='" + nama_menu + '\'' +
                ", harga_menu=" + harga_menu +
                ", deskripsi='" + deskripsi + '\'' +
                '}';
    }
}
