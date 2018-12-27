package com.gpsolutions.vaadincourse.dbo;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Version
    @Column
    private long optlock;

    @Column
    @Size(min = 1, max = 10)
    @NotNull
    private String name;

    @Column
    @Size(min = 1, max = 30)
    @NotNull
    private String message;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Recipients", joinColumns = @JoinColumn(name = "email_id"))
    @Column
    private List<String> recipients;

    @Column
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public long getOptlock() {
        return optlock;
    }

    public void setOptlock(final long optlock) {
        this.optlock = optlock;
    }

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
