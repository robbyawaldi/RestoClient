package com.unindra.restoclient.models;

import com.google.gson.Gson;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;

import java.util.List;
import java.util.stream.Collectors;

import static com.unindra.restoclient.Client.get;
import static com.unindra.restoclient.Rupiah.rupiah;

public class DaftarMenu {
    private int id_menu;
    private String nama_menu;
    private int harga_menu;
    private String type;
    private String deskripsi;

    public DaftarMenu(int id_menu, String nama_menu, int harga_menu, String type, String deskripsi) {
        this.id_menu = id_menu;
        this.nama_menu = nama_menu;
        this.harga_menu = harga_menu;
        this.type = type;
        this.deskripsi = deskripsi;
    }

    public static List<DaftarMenu> menus() {
        DaftarMenu[] daftarMenus = new Gson().fromJson(get("/menus").getData(), DaftarMenu[].class);
        return FXCollections.observableArrayList(daftarMenus);
    }

    public static List<DaftarMenu> menus(String type) {
        return menus()
                .stream()
                .filter(menu -> menu.type.equals(type))
                .collect(Collectors.toList());
    }

    public static DaftarMenu menu(Item item) {
        return menus()
                .stream()
                .filter(menu -> menu.id_menu == item.getId_menu())
                .findFirst()
                .orElse(null);
    }

    public int getId_menu() {
        return id_menu;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public int getHarga_menu() {
        return harga_menu;
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
        return "DaftarMenu{" +
                "id_menu=" + id_menu +
                ", nama_menu='" + nama_menu + '\'' +
                ", harga_menu=" + harga_menu +
                ", deskripsi='" + deskripsi + '\'' +
                '}';
    }
}
