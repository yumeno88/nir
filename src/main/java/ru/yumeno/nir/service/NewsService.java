package ru.yumeno.nir.service;

import ru.yumeno.nir.entity.News;
import ru.yumeno.nir.entity.Tag;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface NewsService {
    List<News> getAllNews();

    News getNewsById(int id);

    News addNews(News news);

    News updateNews(News news);

    void deleteNews(int id);

    List<News> getAllNewsByTags(List<Tag> tags);

    List<News> getAllNewsByDateBetween(LocalDateTime start, LocalDateTime end);

    List<News> getAllByTagsDateLimit(List<Tag> tags, int limit);

    File writeExcel(List<Tag> tags, LocalDateTime startDate, LocalDateTime endDate, int limit);

    String getMimeType(File file) throws IOException;
}
