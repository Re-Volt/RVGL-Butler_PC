package main.io.github.tavisco.rvglbutler.model.enums;

public enum UpdateStatus {
    UPDATE_AVAIABLE("Update avaible!"),
    UPDATED("Up to date"),
    NOT_INSTALLED("Not installed"),
    ERROR("Error"),
    UNKNOWN("Unknown");

    private final String message;


    UpdateStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}