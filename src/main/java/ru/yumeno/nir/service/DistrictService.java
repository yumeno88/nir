package ru.yumeno.nir.service;

import ru.yumeno.nir.entity.District;

import java.util.List;

public interface DistrictService {
    List<District> getAllDistricts();

    District getDistrictById(int id);

    District addDistrict(District district);

    District updateDistrict(District district);

    void deleteDistrict(int id);
}
