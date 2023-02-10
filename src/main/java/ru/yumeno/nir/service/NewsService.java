package ru.yumeno.nir.service;

import ru.yumeno.nir.entity.News;
import ru.yumeno.nir.entity.Tag;

import java.util.List;

public interface NewsService {
    List<News> getAllNews();

    News getNewsById(int id);

    News addNews(News news);

    News updateNews(News news);

    void deleteNews(int id);

    List<News> getAllNewsByTags(List<Tag> tags);
}
