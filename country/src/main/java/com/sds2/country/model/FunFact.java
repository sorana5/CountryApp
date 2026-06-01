package com.sds2.country.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "fun_facts")
public class FunFact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @JsonIgnore
    private Country country;

    private String authorUsername; // just a string now, no User entity

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Country getCountry() { return country; }
    public void setCountry(Country country) { this.country = country; }
    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
}

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User author;

//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getContent() { return content; }
//    public void setContent(String content) { this.content = content; }
//
//    public Country getCountry() { return country; }
//    public void setCountry(Country country) { this.country = country; }
//
//    public User getAuthor() { return author; }
//    public void setAuthor(User author) { this.author = author; }
//}