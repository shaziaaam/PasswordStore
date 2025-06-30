//Representasi data tambahan (passwordId, key, value)
package com.passwordmanager.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdditionalInfo {
    private int id;
    private final StringProperty entryKey;
    private final StringProperty entryValue;
    private boolean isPassword;
    private int passwordId;

    public AdditionalInfo(int id, String entryKey, String entryValue, boolean isPassword, int passwordId) {
        this.id = id;
        this.entryKey = new SimpleStringProperty(entryKey);
        this.entryValue = new SimpleStringProperty(entryValue);
        this.isPassword = isPassword;
        this.passwordId = passwordId;
    }

    public int getId() {
        return id;
    }

    public String getEntryKey() {
        return entryKey.get();
    }

    public void setEntryKey(String entryKey) {
        this.entryKey.set(entryKey);
    }

    public StringProperty entryKeyProperty() {
        return entryKey;
    }

    public String getEntryValue() {
        return entryValue.get();
    }

    public void setEntryValue(String entryValue) {
        this.entryValue.set(entryValue);
    }

    public StringProperty entryValueProperty() {
        return entryValue;
    }

    public boolean isPassword() {
        return isPassword;
    }

    public void setPassword(boolean password) {
        isPassword = password;
    }

    public int getPasswordId() {
        return passwordId;
    }

    public void setPasswordId(int passwordId) {
        this.passwordId = passwordId;
    }
}
