package com.unindra.restoclient.models;

import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.List;

import static com.unindra.restoclient.Client.get;
import static com.unindra.restoclient.Client.gson;

public class Level {
    private int level_item;
    private int harga_level;
    private static final String paramUrl = "/levels";

    private Level(int level, int harga_level) {
        this.level_item = level;
        this.harga_level = harga_level;
    }

    private static List<Level> levelList() throws IOException {
        StandardResponse standardResponse = get(paramUrl);
        if (standardResponse.getStatus() == StatusResponse.SUCCESS) {
            Level[] levels = gson().fromJson(standardResponse.getData(), Level[].class);
            return FXCollections.observableArrayList(levels);
        }
        return FXCollections.observableArrayList();
    }

    static Level level(int level) throws IOException {
        return levelList().stream().filter(l -> l.level_item == level).findFirst().orElse(null);
    }

    int getHarga_level() {
        return harga_level;
    }

    @Override
    public String toString() {
        return "Level{" +
                "level_item=" + level_item +
                ", harga_level=" + harga_level +
                '}';
    }
}
