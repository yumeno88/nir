package ru.yumeno.nir.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.dto.AddressDTO;
import ru.yumeno.nir.entity.Address;
import ru.yumeno.nir.service.AddressService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/addresses")
@Api
@Slf4j
public class AddressController {
    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(value = "")
    @ApiOperation("Получение всех адресов")
    public List<AddressDTO> getAllAddresses() {
        log.info("Try to get all addresses");
        return addressService.getAllAddresses().stream().map(this::toAddressDTO).toList();
    }

    @GetMapping(value = "/{id}")
    @ApiOperation("Получение адреса по его id")
    public AddressDTO getAddressById(@PathVariable int id) {
        log.info("Try to get address by id = " + id);
        return toAddressDTO(addressService.getAddressById(id));
    }

    @PostMapping(value = "")
    @ApiOperation("Добавлние адреса")
    public AddressDTO addAddress(@Valid @RequestBody AddressDTO addressDTO) {
        log.info("Try to add address: " + addressDTO.toString());
        return toAddressDTO(addressService.addAddress(toAddress(addressDTO)));
    }

    @PutMapping(value = "")
    @ApiOperation("Обновление адреса")
    public AddressDTO updateAddress(@Valid @RequestBody AddressDTO addressDTO) {
        log.info("Try to update address: " + addressDTO.toString());
        return toAddressDTO(addressService.updateAddress(toAddress(addressDTO)));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("Удаление адреса")
    public void deleteAddress(@PathVariable int id) {
        log.info("Try to delete address by id = " + id);
        addressService.deleteAddress(id);
    }

    private Address toAddress(AddressDTO addressDTO) {
        return Address.builder()
                .id(addressDTO.getId())
                .street(addressDTO.getStreet())
                .porch(addressDTO.getPorch())
                .house(addressDTO.getHouse())
                .district(addressDTO.getDistrict())
                .apartment(addressDTO.getApartment())
                .build();
    }

    private AddressDTO toAddressDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .porch(address.getPorch())
                .house(address.getHouse())
                .district(address.getDistrict())
                .apartment(address.getApartment())
                .build();
    }
}
