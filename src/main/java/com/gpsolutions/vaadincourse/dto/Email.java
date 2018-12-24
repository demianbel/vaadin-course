package com.gpsolutions.vaadincourse.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Email {
    private String name;
    private String message;
    private List<String> recipients;
    private LocalDate date;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public List<String> getRecipients() {
        if (recipients == null) {
            recipients = new ArrayList<>();
        }
        return recipients;
    }

    public void setRecipients(final List<String> recipients) {
        this.recipients = recipients;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }
}
