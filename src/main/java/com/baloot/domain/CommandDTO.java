package com.baloot.domain;

import org.json.JSONObject;

public class CommandDTO {
    private String type;

    private JSONObject data;

    public CommandDTO(String type, String data) {
        this.type = type;
        if (data != null)
            this.data = new JSONObject(data);
        else
            this.data = null;
    }

    public String getType() {
        return this.type;
    }

    public JSONObject getData() {
        return this.data;
    }
}
