package com.sakila.models;

import java.math.BigDecimal;

/**
 * Modelo que representa la tabla film de la base de datos sakila.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 */
public class Film {

    private int filmId;
    private String title;
    private String description;
    private int releaseYear;
    private int languageId;
    private int rentalDuration;
    private BigDecimal rentalRate;
    private int length;
    private BigDecimal replacementCost;
    private String rating;

    /** Constructor vacío */
    public Film() {}

    /** Constructor completo */
    public Film(int filmId, String title, String description, int releaseYear,
                int languageId, int rentalDuration, BigDecimal rentalRate,
                int length, BigDecimal replacementCost, String rating) {
        this.filmId          = filmId;
        this.title           = title;
        this.description     = description;
        this.releaseYear     = releaseYear;
        this.languageId      = languageId;
        this.rentalDuration  = rentalDuration;
        this.rentalRate      = rentalRate;
        this.length          = length;
        this.replacementCost = replacementCost;
        this.rating          = rating;
    }

    // ── Getters & Setters ─────────────────────────────────────────────

    public int getFilmId()                        { return filmId; }
    public void setFilmId(int filmId)             { this.filmId = filmId; }

    public String getTitle()                      { return title; }
    public void setTitle(String title)            { this.title = title; }

    public String getDescription()                { return description; }
    public void setDescription(String desc)       { this.description = desc; }

    public int getReleaseYear()                   { return releaseYear; }
    public void setReleaseYear(int y)             { this.releaseYear = y; }

    public int getLanguageId()                    { return languageId; }
    public void setLanguageId(int id)             { this.languageId = id; }

    public int getRentalDuration()                { return rentalDuration; }
    public void setRentalDuration(int d)          { this.rentalDuration = d; }

    public BigDecimal getRentalRate()             { return rentalRate; }
    public void setRentalRate(BigDecimal r)       { this.rentalRate = r; }

    public int getLength()                        { return length; }
    public void setLength(int l)                  { this.length = l; }

    public BigDecimal getReplacementCost()        { return replacementCost; }
    public void setReplacementCost(BigDecimal c)  { this.replacementCost = c; }

    public String getRating()                     { return rating; }
    public void setRating(String rating)          { this.rating = rating; }

    @Override
    public String toString() {
        return String.format("[%d] %-40s | %d | %s | $%.2f",
                filmId, title, releaseYear, rating, rentalRate);
    }
}
