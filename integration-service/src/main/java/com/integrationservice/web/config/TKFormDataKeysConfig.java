package com.integrationservice.web.config;

/**
 * This enum includes the keys that require for the data-form when calling TK extract service
 */
public enum TKFormDataKeysConfig {
    ACCOUNT("account"), USERNAME("username"), PASSWORD("password"), UPLOADED_FILE("uploaded_file");
    private final String key;

    TKFormDataKeysConfig(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
