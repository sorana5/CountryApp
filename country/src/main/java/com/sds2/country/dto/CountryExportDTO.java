package com.sds2.country.dto;

public class CountryExportDTO {

    private String name;
    private String capital;
    private Long population;
    private String continent;

    public CountryExportDTO() {}

    public CountryExportDTO(String name, String capital, Long population, String continent) {
        this.name = name;
        this.capital = capital;
        this.population = population;
        this.continent = continent;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCapital() { return capital; }
    public void setCapital(String capital) { this.capital = capital; }

    public Long getPopulation() { return population; }
    public void setPopulation(Long population) { this.population = population; }

    public String getContinent() { return continent; }
    public void setContinent(String continent) { this.continent = continent; }
}