package com.unindra.restoclient.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DaftarMenu {
    private int id_menu;
    private String nama_menu;
    private int harga_menu;
    private String type;
    private String deskripsi;

    private DaftarMenu(int id_menu, String nama_menu, int harga_menu, String type, String deskripsi) {
        this.id_menu = id_menu;
        this.nama_menu = nama_menu;
        this.harga_menu = harga_menu;
        this.type = type;
        this.deskripsi = deskripsi;
    }

    public static List<DaftarMenu> menuList() {
        DaftarMenu[] menus = new DaftarMenu[]{
                new DaftarMenu(1, "miso", 14000, "ramen","Enak dan khas jepang deh"),
                new DaftarMenu(2, "nemo", 15000, "ramen", "Enak dan khas jepang deh"),
                new DaftarMenu(3, "shoyu", 12000,"ramen","Enak dan khas jepang deh")
        };
        return Arrays.asList(menus);
    }

    public static List<DaftarMenu> menuList(String type) {
        return menuList().stream()
                .filter(menu -> menu.type.equals(type))
                .collect(Collectors.toList());
    }

    public static DaftarMenu menu(Item item) {
        return menuList()
                .stream()
                .filter(menu -> menu.id_menu == item.getId_menu())
                .findFirst()
                .orElse(null);
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

    public ObjectProperty<Integer> hargaProperty() {
        return new SimpleObjectProperty<>(harga_menu);
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
