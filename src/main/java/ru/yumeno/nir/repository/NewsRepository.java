package ru.yumeno.nir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yumeno.nir.entity.News;
import ru.yumeno.nir.entity.Tag;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    List<News> findByTagsIn(List<Tag> tags);
}
