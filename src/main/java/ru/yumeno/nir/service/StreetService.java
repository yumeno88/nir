package ru.yumeno.nir.service;

import ru.yumeno.nir.entity.Street;

import java.util.List;

public interface StreetService {
    List<Street> getAllStreets();

    Street getStreetById(int id);

    Street addStreet(Street street);

    Street updateStreet(Street street);

    void deleteStreet(int id);
}
