package com.tui.gitmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("quotes")
public class Quote {
    @Id @JsonIgnore
    private String id;
    @JsonProperty("_id") @Indexed(unique = true)
    private String externalId;
    private String text;
    private String author;
    private String genre;
    @JsonProperty("__v")
    private int version;

    public Quote() {
        super();
    }

    public Quote(String id, String externalId, String text, String author, String genre, int version) {
        this.id = id;
        this.externalId = externalId;
        this.text = text;
        this.author = author;
        this.genre = genre;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
