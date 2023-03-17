package ru.yumeno.nir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yumeno.nir.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
}
