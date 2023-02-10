package ru.yumeno.nir.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yumeno.nir.entity.News;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;
import ru.yumeno.nir.repository.NewsRepository;
import ru.yumeno.nir.service.NewsService;

import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public News getNewsById(int id) {
        Optional<News> optional = newsRepository.findById(id);
        return optional.
                orElseThrow(() -> new ResourceNotFoundException("News not exist with id : " + id));
    }

    @Override
    public News addNews(News news) {
        return newsRepository.save(news);
    }

    @Override
    public News updateNews(News news) {
        return newsRepository.save(news);
    }

    @Override
    public void deleteNews(int id) {
        newsRepository.deleteById(id);
    }

    @Override
    public List<News> getAllNewsByTags(List<Tag> tags) {
        return newsRepository.findByTagsIn(tags);
    }
}
