package com.sakila.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Representa una película en el sistema Sakila.
 * Este es un POJO (Plain Old Java Object) que mapea a la tabla 'film'.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class Film {
    private int filmId;
    private String title;
    private String description;
    private Integer releaseYear; //
    private Integer languageId;  //
    private Integer originalLanguageId; //
    private Integer rentalDuration; //
    private BigDecimal rentalRate;
    private Integer length;      //
    private BigDecimal replacementCost;
    private String rating;       //
    private String specialFeatures; //
    private Timestamp lastUpdate;

    // Constructor vacío
    public Film() {
    }

    // Constructor completo
    public Film(int filmId, String title, String description, Integer releaseYear, Integer languageId, Integer originalLanguageId, Integer rentalDuration, BigDecimal rentalRate, Integer length, BigDecimal replacementCost, String rating, String specialFeatures, Timestamp lastUpdate) {
        this.filmId = filmId;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.languageId = languageId;
        this.originalLanguageId = originalLanguageId;
        this.rentalDuration = rentalDuration;
        this.rentalRate = rentalRate;
        this.length = length;
        this.replacementCost = replacementCost;
        this.rating = rating;
        this.specialFeatures = specialFeatures;
        this.lastUpdate = lastUpdate;
    }

    // --- Getters y Setters ---

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReleaseYear() { // Devuelve Integer
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) { // Recibe Integer
        this.releaseYear = releaseYear;
    }

    public Integer getLanguageId() { // Devuelve Integer
        return languageId;
    }

    public void setLanguageId(Integer languageId) { // Recibe Integer
        this.languageId = languageId;
    }

    public Integer getOriginalLanguageId() { // Devuelve Integer
        return originalLanguageId;
    }

    public void setOriginalLanguageId(Integer originalLanguageId) { // Recibe Integer
        this.originalLanguageId = originalLanguageId;
    }

    public Integer getRentalDuration() { // Devuelve Integer
        return rentalDuration;
    }

    public void setRentalDuration(Integer rentalDuration) { // Recibe Integer
        this.rentalDuration = rentalDuration;
    }

    public BigDecimal getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    public Integer getLength() { // Devuelve Integer
        return length;
    }

    public void setLength(Integer length) { // Recibe Integer
        this.length = length;
    }

    public BigDecimal getReplacementCost() {
        return replacementCost;
    }

    public void setReplacementCost(BigDecimal replacementCost) {
        this.replacementCost = replacementCost;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Film{" +
                "filmId=" + filmId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", releaseYear=" + releaseYear +
                ", languageId=" + languageId +
                ", originalLanguageId=" + originalLanguageId +
                ", rentalDuration=" + rentalDuration +
                ", rentalRate=" + rentalRate +
                ", length=" + length +
                ", replacementCost=" + replacementCost +
                ", rating='" + rating + '\'' +
                ", specialFeatures='" + specialFeatures + '\'' +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}