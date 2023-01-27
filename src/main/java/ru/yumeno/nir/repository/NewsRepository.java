package ru.yumeno.nir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yumeno.nir.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
}
