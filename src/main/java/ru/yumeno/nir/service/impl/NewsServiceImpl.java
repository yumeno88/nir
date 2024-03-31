package ru.yumeno.nir.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.yumeno.nir.entity.News;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.exception_handler.exceptions.AdditionFailedException;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;
import ru.yumeno.nir.repository.NewsRepository;
import ru.yumeno.nir.security.entity.User;
import ru.yumeno.nir.security.service.UserService;
import ru.yumeno.nir.service.NewsService;
import ru.yumeno.nir.utils.ExcelWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final ExcelWriter excelWriter;
    private final UserService userService;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, ExcelWriter excelWriter, UserService userService) {
        this.newsRepository = newsRepository;
        this.excelWriter = excelWriter;
        this.userService = userService;
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

    @Override
    public File writeExcel(List<Tag> tags, LocalDateTime startDate, LocalDateTime endDate, int limit) {
        List<News> allNews = getAllNews();
        excelWriter.writeNews(allNews, "allNews");

        List<News> tagsNews = getAllNewsByTags(tags);
        excelWriter.writeNews(tagsNews, "tagsNews");

        List<News> dateNews = getAllNewsByDateBetween(startDate, endDate);
        excelWriter.writeNews(dateNews, "dateNews");

        List<News> sortNews = getAllByTagsDateLimit(tags, limit);
        excelWriter.writeNews(sortNews, "sortNews");

        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);
        excelWriter.writeUser(user);

        return excelWriter.writeFile();
    }

    @Override
    public String getMimeType(File file) throws IOException {
        Path path = file.toPath();
        String mimeType = Files.probeContentType(path);
        if (mimeType == null) {
            return "application/octet-stream";
        }
        return mimeType;
    }
}
