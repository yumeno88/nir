package ru.yumeno.nir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yumeno.nir.entity.Street;

import java.util.Optional;

@Repository
public interface StreetRepository extends JpaRepository<Street, Integer> {
    Optional<Street> findByName(String name);
}
