package ru.yumeno.nir.service;

import ru.yumeno.nir.entity.News;

import java.util.List;

public interface NewsService {
    List<News> getAllNews();

    News getNewsById(int id);

    News addNews(News news);

    News updateNews(News news);

    void deleteNews(int id);
}
