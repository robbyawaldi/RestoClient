package com.unindra.restoclient;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unindra.restoclient.models.StandardResponse;
import com.unindra.restoclient.models.StatusResponse;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.unindra.restoclient.models.Setting.setting;

public class Client {

    public static StandardResponse get(String paramUrl) throws IOException {
        URL url = new URL(setting().getBaseUrl() + paramUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            bufferedReader.close();
            connection.disconnect();
            return gson().fromJson(stringBuilder.toString(), StandardResponse.class);
        } else return new StandardResponse(StatusResponse.ERROR);
    }

    public static StandardResponse send(String paramUrl, String requestMethod, String json) {
        try {
            URL url = new URL(setting().getBaseUrl() + paramUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestMethod(requestMethod);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            connection.disconnect();
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
            outputStream.close();
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return gson().fromJson(result, StandardResponse.class);
        } catch (IOException e) {
            return new StandardResponse(StatusResponse.ERROR);
        }
    }

    public static Gson gson() {
        return new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return fieldAttributes.getDeclaringClass().equals(RecursiveTreeObject.class);
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).addDeserializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return fieldAttributes.getDeclaringClass().equals(RecursiveTreeObject.class);
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).create();
    }
}
