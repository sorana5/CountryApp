package com.sds2.user.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "visited_countries")
public class VisitedCountry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long countryId;
    private String countryName;
    private LocalDate visitedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Long getCountryId() { return countryId; }
    public void setCountryId(Long countryId) { this.countryId = countryId; }
    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
    public LocalDate getVisitedAt() { return visitedAt; }
    public void setVisitedAt(LocalDate visitedAt) { this.visitedAt = visitedAt; }
}