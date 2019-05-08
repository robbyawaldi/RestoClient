package com.unindra.restoclient.models;

import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.List;

import static com.unindra.restoclient.Client.get;
import static com.unindra.restoclient.Client.gson;

public class Level {
    private int level;
    private int harga;
    private static final String paramUrl = "/levels";

    private Level(int level, int harga) {
        this.level = level;
        this.harga = harga;
    }

    private static List<Level> levelList() throws IOException {
        StandardResponse standardResponse = get(paramUrl);
        if (standardResponse.getStatus() == StatusResponse.SUCCESS) {
            Level[] levels = gson().fromJson(standardResponse.getData(), Level[].class);
            return FXCollections.observableArrayList(levels);
        }
        return FXCollections.observableArrayList();
    }

    public static Level level(int level) throws IOException {
        return levelList().stream().filter(l -> l.level == level).findFirst().orElse(null);
    }

    public int getHarga() {
        return harga;
    }

    @Override
    public String toString() {
        return "Level{" +
                "level=" + level +
                ", harga=" + harga +
                '}';
    }
}
