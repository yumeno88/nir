package ru.yumeno.nir.service;

import ru.yumeno.nir.entity.Address;

import java.util.List;

public interface AddressService {
    List<Address> getAllAddresses();

    Address getAddressById(int id);

    Address addAddress(Address address);

    Address updateAddress(Address address);

    void deleteAddress(int id);
}
