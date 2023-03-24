package ru.yumeno.nir.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yumeno.nir.entity.Address;
import ru.yumeno.nir.entity.News;
import ru.yumeno.nir.entity.Subscription;
import ru.yumeno.nir.exception_handler.exceptions.AdditionFailedException;
import ru.yumeno.nir.exception_handler.exceptions.DeletionFailedException;
import ru.yumeno.nir.exception_handler.exceptions.ResourceAlreadyExistException;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;
import ru.yumeno.nir.repository.AddressRepository;
import ru.yumeno.nir.repository.NewsRepository;
import ru.yumeno.nir.repository.SubscriptionRepository;
import ru.yumeno.nir.service.AddressService;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final NewsRepository newsRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, NewsRepository newsRepository, SubscriptionRepository subscriptionRepository) {
        this.addressRepository = addressRepository;
        this.newsRepository = newsRepository;
        this.subscriptionRepository = subscriptionRepository;
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
        if (address.getId() != 0) {
            throw new AdditionFailedException("Address with id cannot be added");
        }
        Optional<Address> optional = addressRepository.findByApartmentAndHouseAndPorchAndDistrictAndStreet(
                address.getApartment(),
                address.getHouse(),
                address.getPorch(),
                address.getDistrict(),
                address.getStreet()
        );
        if (optional.isEmpty()) {
            return addressRepository.save(address);
        } else {
            throw new ResourceAlreadyExistException("Address: " + address + " already exist");
        }
    }

    @Override
    public Address updateAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(int id) {
        Optional<Address> optional = addressRepository.findById(id);
        List<News> news = newsRepository.findAllByAddresses(optional.
                orElseThrow(() -> new ResourceNotFoundException("Address not exist with id : " + id)));
        List<Subscription> subscriptions = subscriptionRepository.findAllByAddress(optional.
                orElseThrow(() -> new ResourceNotFoundException("Address not exist with id : " + id)));
        if (news.isEmpty() && subscriptions.isEmpty()) {
            addressRepository.deleteById(id);
        } else {
            throw new DeletionFailedException("Address cannot be deleted with id : " + id);
        }
    }
}
