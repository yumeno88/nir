package ru.yumeno.nir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yumeno.nir.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
