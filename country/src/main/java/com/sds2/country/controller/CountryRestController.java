package com.sds2.country.controller;

import com.sds2.country.model.Country;
import com.sds2.country.service.CountryCommandService;
import com.sds2.country.service.CountryQueryService;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/countries")
public class CountryRestController {

    private final CountryQueryService queryService;
    private final CountryCommandService commandService;

    public CountryRestController(CountryQueryService queryService,
                                 CountryCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String continent,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String dir) {

        Sort.Direction direction = dir.equals("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, 10, Sort.by(direction, sort));
        Page<Country> result = queryService.findByFilters(name, continent, pageable);
        return ResponseEntity.ok(Map.of(
                "content", result.getContent(),
                "totalPages", result.getTotalPages(),
                "currentPage", page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(queryService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponse("NOT_FOUND", "Country not found"));
        }
    }

    @PostMapping("/{id}/fun-facts")
    public ResponseEntity<?> addFunFact(@PathVariable Long id,
                                        @RequestBody Map<String, String> body,
                                        Principal principal) {
        commandService.addFunFact(id, body.get("content"), principal.getName());
        return ResponseEntity.ok(Map.of("message", "Fun fact added"));
    }

    @DeleteMapping("/{id}/fun-facts/{factId}")
    public ResponseEntity<?> deleteFunFact(@PathVariable Long id,
                                           @PathVariable Long factId,
                                           Principal principal) {
        commandService.deleteFunFact(id, factId, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Fun fact deleted"));
    }
}