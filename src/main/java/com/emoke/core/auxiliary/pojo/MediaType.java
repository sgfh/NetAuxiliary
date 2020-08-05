package com.emoke.core.auxiliary.pojo;

public enum MediaType {
    Json("application/json"), FormUrlencoded("application/x-www-form-urlencoded"), FormData("multipart/form-data");
    private String label;

    MediaType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
