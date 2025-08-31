package com.tinyurl.model;

import jakarta.validation.constraints.NotBlank;

public class ShortenRequest {
    @NotBlank
    private String url;
    private String alias;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
