package com.unindra.restoclient.models;

import com.google.gson.JsonElement;

public class StandardResponse {
    private StatusResponse status;
    private String message;
    private JsonElement data;

    public StandardResponse(StatusResponse status) {
        this(status, "", null);
    }

    private StandardResponse(StatusResponse status, String message, JsonElement data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public StatusResponse getStatus() {
        return status;
    }

    String getMessage() {
        return message;
    }

    JsonElement getData() {
        return data;
    }
}
