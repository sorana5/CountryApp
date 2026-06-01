package com.sds2.country.controller;

import com.sds2.country.export.CsvCountryExporter;
import com.sds2.country.export.JsonCountryExporter;
import com.sds2.country.export.XmlCountryExporter;
import com.sds2.country.model.Country;
import com.sds2.country.service.CountryQueryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class ExportController {

    private final CountryQueryService queryService;
    private final JsonCountryExporter jsonExporter;
    private final XmlCountryExporter xmlExporter;
    private final CsvCountryExporter csvExporter;

    public ExportController(CountryQueryService queryService,
                            JsonCountryExporter jsonExporter,
                            XmlCountryExporter xmlExporter,
                            CsvCountryExporter csvExporter) {
        this.queryService = queryService;
        this.jsonExporter = jsonExporter;
        this.xmlExporter = xmlExporter;
        this.csvExporter = csvExporter;
    }

    @GetMapping("/countries/export")
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
        List<Country> countries = queryService.findByFilters(name, continent, pageable)
                .getContent();

        var exporter = switch (format) {
            case "xml" -> xmlExporter;
            case "csv" -> csvExporter;
            default ->   jsonExporter;
        };

        byte[] data = exporter.export(countries);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + exporter.getFileName())
                .header(HttpHeaders.CONTENT_TYPE, exporter.getContentType())
                .body(data);
    }
}