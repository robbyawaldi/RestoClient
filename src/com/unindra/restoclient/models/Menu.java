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
    private String nama;
    private int harga;
    private String tipe;
    private String deskripsi;

    public Menu(int id_menu, String nama, int harga, String tipe, String deskripsi) {
        this.id_menu = id_menu;
        this.nama = nama;
        this.harga = harga;
        this.tipe = tipe;
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

    public static List<Menu> menus(String tipe) {
        return menus()
                .stream()
                .filter(menu -> menu.tipe.equals(tipe))
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

    public String getNama() {
        return nama;
    }

    public int getHarga() {
        return harga;
    }

    public String getTipe() {
        return tipe;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public Image getImage() {
        return new Image(String.format("/img/%s.jpg", nama));
    }

    public StringProperty namaProperty() {
        return new SimpleStringProperty(nama);
    }

    public StringProperty hargaProperty() {
        return new SimpleStringProperty(rupiah(harga));
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id_menu=" + id_menu +
                ", nama='" + nama + '\'' +
                ", harga=" + harga +
                ", deskripsi='" + deskripsi + '\'' +
                '}';
    }
}
