package ru.yumeno.nir.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yumeno.nir.entity.Address;
import ru.yumeno.nir.entity.District;
import ru.yumeno.nir.exception_handler.exceptions.DeletionFailedException;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;
import ru.yumeno.nir.repository.AddressRepository;
import ru.yumeno.nir.repository.DistrictRepository;
import ru.yumeno.nir.service.DistrictService;

import java.util.List;
import java.util.Optional;

@Service
public class DistrictServiceImpl implements DistrictService {
    private final DistrictRepository districtRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository, AddressRepository addressRepository) {
        this.districtRepository = districtRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    @Override
    public District getDistrictById(int id) {
        Optional<District> optional = districtRepository.findById(id);
        return optional.
                orElseThrow(() -> new ResourceNotFoundException("District not exist with id : " + id));
    }

    @Override
    public District addDistrict(District district) {
        return districtRepository.save(district);
    }

    @Override
    public District updateDistrict(District district) {
        return districtRepository.save(district);
    }

    @Override
    public void deleteDistrict(int id) {
        Optional<District> optional = districtRepository.findById(id);
        List<Address> addresses = addressRepository.findAllByDistrict(optional.
                orElseThrow(() -> new ResourceNotFoundException("District not exist with id : " + id)));
        if (addresses.isEmpty()) {
            districtRepository.deleteById(id);
        } else {
            throw new DeletionFailedException("District cannot be deleted with id : " + id);
        }
    }
}
