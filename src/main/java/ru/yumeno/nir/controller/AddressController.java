package ru.yumeno.nir.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.entity.Address;
import ru.yumeno.nir.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@Api
public class AddressController {
    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    @ApiOperation("Получение всех адресов")
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/{id}")
    @ApiOperation("Получение адреса по его id")
    public Address getAddressById(@PathVariable int id) {
        return addressService.getAddressById(id);
    }

    @PostMapping
    @ApiOperation("Добавлние адреса")
    public Address addAddress(@RequestBody Address address) {
        return addressService.addAddress(address);
    }

    @PutMapping
    @ApiOperation("Обновление адреса")
    public Address updateAddress(@RequestBody Address address) {
        return addressService.updateAddress(address);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаление адреса")
    public void deleteAddress(@PathVariable int id) {
        addressService.deleteAddress(id);
    }
}
