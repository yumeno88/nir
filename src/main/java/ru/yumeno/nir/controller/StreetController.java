package ru.yumeno.nir.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.entity.Street;
import ru.yumeno.nir.service.StreetService;

import java.util.List;

@RestController
@RequestMapping("/streets")
@Api
public class StreetController {
    private final StreetService streetService;

    @Autowired
    public StreetController(StreetService streetService) {
        this.streetService = streetService;
    }

    @GetMapping
    @ApiOperation("Получение всех улиц")
    public List<Street> getAllStreets() {
        return streetService.getAllStreets();
    }

    @GetMapping("/{id}")
    @ApiOperation("Получение улицы по ее id")
    public Street getStreetById(@PathVariable int id) {
        return streetService.getStreetById(id);
    }

    @PostMapping
    @ApiOperation("Добавлние улицы")
    public Street addStreet(@RequestBody Street street) {
        return streetService.addStreet(street);
    }

    @PutMapping
    @ApiOperation("Обновление улицы")
    public Street updateStreet(@RequestBody Street street) {
        return streetService.updateStreet(street);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаление улицы")
    public void addStreet(@PathVariable int id) {
        streetService.deleteStreet(id);
    }
}