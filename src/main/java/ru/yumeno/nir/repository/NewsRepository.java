package ru.yumeno.nir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yumeno.nir.entity.Address;
import ru.yumeno.nir.entity.News;
import ru.yumeno.nir.entity.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    List<News> findByTagsIn(List<Tag> tags);

    List<News> findAllByTags(Tag tag);

    List<News> findAllByAddresses(Address address);

    List<News> findAllByCreateDateBetween(LocalDateTime start, LocalDateTime end);

    @Query(value="select * from news n inner join news_tag nt on n.id = nt.news_id \n" +
            "where tag_name in (:tags) order by create_date DESC LIMIT :limit", nativeQuery = true)
    List<News> findAllByTagsIn(@Param("tags") List<Tag> tags, @Param("limit") int limit);
}
