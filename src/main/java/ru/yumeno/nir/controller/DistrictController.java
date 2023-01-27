package ru.yumeno.nir.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.entity.District;
import ru.yumeno.nir.service.DistrictService;

import java.util.List;

@RestController
@RequestMapping("/districts")
public class DistrictController {
    private final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping
    public List<District> getAllDistricts() {
        return districtService.getAllDistricts();
    }

    @GetMapping("/{id}")
    public District getDistrictById(@PathVariable int id) {
        return districtService.getDistrictById(id);
    }

    @PostMapping
    public District addDistrict(@RequestBody District district) {
        return districtService.addDistrict(district);
    }

    @PutMapping
    public District updateDistrict(@RequestBody District district) {
        return districtService.updateDistrict(district);
    }

    @DeleteMapping("/{id}")
    public void addDistrict(@PathVariable int id) {
        districtService.deleteDistrict(id);
    }
}
