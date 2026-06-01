package com.sds2.country.controller;

import com.sds2.country.model.Country;
import com.sds2.country.service.CountryCommandService;
import com.sds2.country.service.CountryQueryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class CountryCommandController {

    private final CountryCommandService commandService;
    private final CountryQueryService queryService;

    public CountryCommandController(CountryCommandService commandService,
                                    CountryQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("countries", queryService.findAll());
        return "admin/dashboard";
    }

    @GetMapping("/countries/new")
    public String newForm(Model model) {
        model.addAttribute("country", new Country());
        return "admin/country-form";
    }

    @PostMapping("/countries/new")
    public String create(@ModelAttribute Country country, Principal principal) {
        commandService.create(country, principal.getName());
        return "redirect:/admin";
    }

    @GetMapping("/countries/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("country", queryService.findById(id));
        return "admin/country-form";
    }

    @PostMapping("/countries/{id}/edit")
    public String update(@PathVariable Long id,
                         @ModelAttribute Country country,
                         Principal principal) {
        commandService.update(id, country, principal.getName());
        return "redirect:/admin";
    }

    @PostMapping("/countries/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal) {
        commandService.delete(id, principal.getName());
        return "redirect:/admin";
    }
}

//package com.sds2.country.controller;
//
//import com.sds2.country.model.Country;
//import com.sds2.country.service.CountryCommandService;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.security.Principal;
//
//@Controller
//@RequestMapping("/admin/countries")
//public class CountryCommandController {
//
//    private final CountryCommandService commandService;
//
//    @PostMapping("/new")
//    public String create(@ModelAttribute Country country, Principal principal) {
//        commandService.create(country, principal.getName());
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/{id}/edit")
//    public String update(@PathVariable Long id, @ModelAttribute Country country, Principal principal) {
//        commandService.update(id, country, principal.getName());
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/{id}/delete")
//    public String delete(@PathVariable Long id, Principal principal) {
//        commandService.delete(id, principal.getName());
//        return "redirect:/admin";
//    }
//}
