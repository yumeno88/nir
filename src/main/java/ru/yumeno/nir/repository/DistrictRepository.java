package ru.yumeno.nir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yumeno.nir.entity.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
}
