package ru.yumeno.nir.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.entity.District;
import ru.yumeno.nir.service.DistrictService;

import java.util.List;

@RestController
@RequestMapping("/districts")
@Api
public class DistrictController {
    private final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping
    @ApiOperation("Получение всех районов")
    public List<District> getAllDistricts() {
        return districtService.getAllDistricts();
    }

    @GetMapping("/{id}")
    @ApiOperation("Получение района по его id")
    public District getDistrictById(@PathVariable int id) {
        return districtService.getDistrictById(id);
    }

    @PostMapping
    @ApiOperation("Добавлние района")
    public District addDistrict(@RequestBody District district) {
        return districtService.addDistrict(district);
    }

    @PutMapping
    @ApiOperation("Обновление района")
    public District updateDistrict(@RequestBody District district) {
        return districtService.updateDistrict(district);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаление района")
    public void addDistrict(@PathVariable int id) {
        districtService.deleteDistrict(id);
    }
}
