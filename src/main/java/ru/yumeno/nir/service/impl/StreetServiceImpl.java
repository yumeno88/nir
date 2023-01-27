package ru.yumeno.nir.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yumeno.nir.entity.Street;
import ru.yumeno.nir.repository.StreetRepository;
import ru.yumeno.nir.service.StreetService;

import java.util.List;
import java.util.Optional;

@Service
public class StreetServiceImpl implements StreetService {
    private final StreetRepository streetRepository;

    @Autowired
    public StreetServiceImpl(StreetRepository streetRepository) {
        this.streetRepository = streetRepository;
    }

    @Override
    public List<Street> getAllStreets() {
        return streetRepository.findAll();
    }

    @Override
    public Street getStreetById(int id) {
        Optional<Street> optionalAddress = streetRepository.findById(id);
        return optionalAddress.orElse(null); // TODO change to exception
    }

    @Override
    public Street addStreet(Street street) {
        return streetRepository.save(street);
    }

    @Override
    public Street updateStreet(Street street) {
        return streetRepository.save(street);
    }

    @Override
    public void deleteStreet(int id) {
        streetRepository.deleteById(id);
    }
}
