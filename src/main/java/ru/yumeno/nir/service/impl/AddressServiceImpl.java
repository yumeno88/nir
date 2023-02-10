package ru.yumeno.nir.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yumeno.nir.entity.Address;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;
import ru.yumeno.nir.repository.AddressRepository;
import ru.yumeno.nir.service.AddressService;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address getAddressById(int id) {
        Optional<Address> optional = addressRepository.findById(id);
        return optional.
                orElseThrow(() -> new ResourceNotFoundException("Address not exist with id : " + id));
    }

    @Override
    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(int id) {
        addressRepository.deleteById(id);
    }
}
