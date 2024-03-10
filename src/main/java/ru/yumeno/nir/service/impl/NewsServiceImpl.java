package ru.yumeno.nir.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yumeno.nir.entity.News;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.exception_handler.exceptions.AdditionFailedException;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;
import ru.yumeno.nir.repository.NewsRepository;
import ru.yumeno.nir.service.NewsService;

import java.time.LocalDateTime;
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
        List<News> newsList = newsRepository.findAll();
        newsList.sort((n1, n2) -> n2.getCreateDate().compareTo(n1.getCreateDate()));
        return newsList;
    }

    @Override
    public News getNewsById(int id) {
        Optional<News> optional = newsRepository.findById(id);
        return optional.
                orElseThrow(() -> new ResourceNotFoundException("News not exist with id : " + id));
    }

    @Override
    public News addNews(News news) {
        if (news.getId() != 0) {
            throw new AdditionFailedException("News with id cannot be added");
        }
        return newsRepository.save(news);
    }

    @Override
    public News updateNews(News news) {
        return newsRepository.save(news);
    }

    @Override
    public void deleteNews(int id) {
        Optional<News> optional = newsRepository.findById(id);
        if (optional.isPresent()) {
            newsRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("News not exist with id : " + id);
        }
    }

    @Override
    public List<News> getAllNewsByTags(List<Tag> tags) {
        return newsRepository.findByTagsIn(tags);
    }

    @Override
    public List<News> getAllNewsByDateBetween(LocalDateTime start, LocalDateTime end) {
        return newsRepository.findAllByCreateDateBetween(start, end);
    }

    @Override
    public List<News> getAllByTagsDateLimit(List<Tag> tags, int limit) {
        return newsRepository.findAllByTagsIn(tags, limit);
    }
}
