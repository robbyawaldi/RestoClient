package com.unindra.restoclient.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class Setting {
    private String no_meja;
    private String host;
    private String port;
    @Expose
    private static final String fileName = "resources/json/setting.json";

    private Setting(String no_meja, String host, String port) {
        this.no_meja = no_meja;
        this.host = host;
        this.port = port;
    }

    public static Setting setting() {
        try {
            return new Gson().fromJson(new JsonReader(new FileReader(fileName)), Setting.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Setting("0", "localhost", "4567");
        }
    }

    public void simpan() {
        String setting = new Gson().toJson(this);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
            bufferedWriter.write(setting);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNo_meja() {
        return no_meja;
    }

    public void setNo_meja(String no_meja) {
        this.no_meja = no_meja;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getBaseUrl() {
        return String.format("http://%s:%s", host, port);
    }

    @Override
    public String toString() {
        return "Setting{" +
                "no_meja='" + no_meja + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
