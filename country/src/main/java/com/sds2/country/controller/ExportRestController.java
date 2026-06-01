package com.sds2.country.controller;

import com.sds2.country.export.*;
import com.sds2.country.model.Country;
import com.sds2.country.service.CountryQueryService;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/countries/export")
public class ExportRestController {

    private final CountryQueryService queryService;
    private final JsonCountryExporter jsonExporter;
    private final XmlCountryExporter xmlExporter;
    private final CsvCountryExporter csvExporter;

    public ExportRestController(CountryQueryService queryService,
                                JsonCountryExporter jsonExporter,
                                XmlCountryExporter xmlExporter,
                                CsvCountryExporter csvExporter) {
        this.queryService = queryService;
        this.jsonExporter = jsonExporter;
        this.xmlExporter = xmlExporter;
        this.csvExporter = csvExporter;
    }

    @GetMapping
    public ResponseEntity<byte[]> export(
            @RequestParam(defaultValue = "json") String format,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String continent,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String dir) throws Exception {

        Sort.Direction direction = dir.equals("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE,
                Sort.by(direction, sort));
        List<Country> countries = queryService
                .findByFilters(name, continent, pageable).getContent();

        CountryExporter exporter = switch (format) {
            case "xml" -> xmlExporter;
            case "csv" -> csvExporter;
            default -> jsonExporter;
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + exporter.getFileName())
                .header(HttpHeaders.CONTENT_TYPE, exporter.getContentType())
                .body(exporter.export(countries));
    }
}