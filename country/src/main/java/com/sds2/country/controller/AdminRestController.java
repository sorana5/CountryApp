package com.sds2.country.controller;

import com.sds2.country.model.Country;
import com.sds2.country.service.CountryCommandService;
import com.sds2.country.service.CountryQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/countries")
public class AdminRestController {

    private final CountryCommandService commandService;
    private final CountryQueryService queryService;

    public AdminRestController(CountryCommandService commandService,
                               CountryQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Country country,
                                    Principal principal) {
        commandService.create(country, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Country created"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Country country,
                                    Principal principal) {
        commandService.update(id, country, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Country updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    Principal principal) {
        commandService.delete(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Country deleted"));
    }
}