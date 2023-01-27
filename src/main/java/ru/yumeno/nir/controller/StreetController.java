package ru.yumeno.nir.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.entity.Street;
import ru.yumeno.nir.service.StreetService;

import java.util.List;

@RestController
@RequestMapping("/streets")
public class StreetController {
    private final StreetService streetService;

    @Autowired
    public StreetController(StreetService streetService) {
        this.streetService = streetService;
    }

    @GetMapping
    public List<Street> getAllStreets() {
        return streetService.getAllStreets();
    }

    @GetMapping("/{id}")
    public Street getStreetById(@PathVariable int id) {
        return streetService.getStreetById(id);
    }

    @PostMapping
    public Street addStreet(@RequestBody Street street) {
        return streetService.addStreet(street);
    }

    @PutMapping
    public Street updateStreet(@RequestBody Street street) {
        return streetService.updateStreet(street);
    }

    @DeleteMapping("/{id}")
    public void addStreet(@PathVariable int id) {
        streetService.deleteStreet(id);
    }
}