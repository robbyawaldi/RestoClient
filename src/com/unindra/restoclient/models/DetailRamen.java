package com.unindra.restoclient.models;

import java.io.IOException;
import java.util.Arrays;

import static com.unindra.restoclient.Client.get;
import static com.unindra.restoclient.Client.gson;

public class DetailRamen {
    private String nama_menu;
    private byte[] foto;
    private String deskripsi;

    public DetailRamen(String nama_menu, byte[] foto, String deskripsi) {
        this.nama_menu = nama_menu;
        this.foto = foto;
        this.deskripsi = deskripsi;
    }

    public static DetailRamen detailRamen(String nama_menu) throws IOException {
        StandardResponse standardResponse = get("/detail_ramen/"+nama_menu);
        if (standardResponse.getStatus() == StatusResponse.SUCCESS) {
            return gson().fromJson(standardResponse.getData(), DetailRamen.class);
        }
        return null;
    }

    public byte[] getFoto() {
        return foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    @Override
    public String toString() {
        return "DetailRamen{" +
                "nama_menu='" + nama_menu + '\'' +
                ", foto=" + Arrays.toString(foto) +
                ", deskripsi='" + deskripsi + '\'' +
                '}';
    }
}
