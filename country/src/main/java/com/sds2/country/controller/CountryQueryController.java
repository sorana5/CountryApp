package com.sds2.country.controller;

import com.sds2.country.model.Country;
import com.sds2.country.service.CountryCommandService;
import com.sds2.country.service.CountryQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@Controller
public class CountryQueryController {

    private final CountryQueryService queryService;
    private final CountryCommandService commandService;
    private final RestTemplate restTemplate;

    public CountryQueryController(CountryQueryService queryService,
                                  CountryCommandService commandService, RestTemplate restTemplate) {
        this.queryService = queryService;
        this.commandService = commandService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/countries")
    public String listCountries(@RequestParam(defaultValue = "") String name,
                                @RequestParam(defaultValue = "") String continent,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "name") String sort,
                                @RequestParam(defaultValue = "asc") String dir,
                                Model model) {

        Sort.Direction direction = dir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, 10, Sort.by(direction, sort));

        Page<Country> countryPage = queryService.findByFilters(name, continent, pageable);

        model.addAttribute("countries", countryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", countryPage.getTotalPages());
        model.addAttribute("name", name);
        model.addAttribute("continent", continent);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("reverseDir", dir.equals("asc") ? "desc" : "asc");
        return "countries";
    }

//    @GetMapping("/countries/{id}")
//    public String countryDetail(@PathVariable Long id, Model model, Principal principal) {
//        model.addAttribute("country", queryService.findById(id));
//        // visited status — call user-service (see note below)
//        return "country-detail";
//    }
    @GetMapping("/countries/{id}")
    public String countryDetail(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("country", queryService.findById(id));

        if (principal != null) {
            try {
                Boolean visited = restTemplate.getForObject(
                        "http://localhost:8081/internal/users/{username}/visited/{countryId}",
                        Boolean.class, principal.getName(), id);
                model.addAttribute("visited", Boolean.TRUE.equals(visited));
            } catch (Exception e) {
                model.addAttribute("visited", false);
            }
        } else {
            model.addAttribute("visited", false);
        }
        return "country-detail";
    }

    @PostMapping("/countries/{id}/fun-facts/add")
    public String addFunFact(@PathVariable Long id,
                             @RequestParam String content,
                             Principal principal) {
        commandService.addFunFact(id, content, principal.getName());
        return "redirect:/countries/" + id;
    }

    @PostMapping("/countries/{id}/fun-facts/delete/{factId}")
    public String deleteFunFact(@PathVariable Long id,
                                @PathVariable Long factId,
                                Principal principal) {
        commandService.deleteFunFact(id, factId, principal.getName());
        return "redirect:/countries/" + id;
    }
}

//@Controller
//@RequestMapping("/countries")
//public class CountryQueryController {
//
//    private final CountryQueryService queryService;
//
//    public CountryQueryController(CountryQueryService queryService) {
//        this.queryService = queryService;
//    }
//
//    @GetMapping
//    public String listCountries(@RequestParam(defaultValue = "") String name,
//                                @RequestParam(defaultValue = "") String continent,
//                                @RequestParam(defaultValue = "0") int page,
//                                @RequestParam(defaultValue = "name") String sort,
//                                @RequestParam(defaultValue = "asc") String dir,
//                                Model model) {
//        // same as before
//    }
//
//    @GetMapping("/{id}")
//    public String countryDetail(@PathVariable Long id, Model model, Principal principal) {
//        // same as before
//    }
//}
