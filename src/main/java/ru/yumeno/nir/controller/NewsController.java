package ru.yumeno.nir.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.dto.NewsRequestDTO;
import ru.yumeno.nir.dto.NewsResponseDTO;
import ru.yumeno.nir.entity.News;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.service.NewsService;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<NewsResponseDTO> getAllNews() {
        return newsService.getAllNews().stream().map(this::toNewsResponseDTO).toList();
    }

    @GetMapping("/sort")
    public List<NewsResponseDTO> getAllNewsByTags(@RequestParam(name = "tags") List<String> strTags) {
        List<Tag> tags = strTags.stream().map(this::toTag).toList();
        return newsService.getAllNewsByTags(tags).stream().map(this::toNewsResponseDTO).toList();
    }

    @GetMapping("/{id}")
    public NewsResponseDTO getNewsById(@PathVariable int id) {
        return toNewsResponseDTO(newsService.getNewsById(id));
    }

    @PostMapping
    public NewsResponseDTO addNews(@Valid @RequestBody NewsRequestDTO newsRequestDTO) {
        return toNewsResponseDTO(newsService.addNews(toNews(newsRequestDTO)));
    }

    @PutMapping
    public NewsResponseDTO updateNews(@RequestBody NewsRequestDTO newsRequestDTO) {
        return toNewsResponseDTO(newsService.updateNews(toNews(newsRequestDTO)));
    }

    @DeleteMapping("/{id}")
    public void addNews(@PathVariable int id) {
        newsService.deleteNews(id);
    }

    private News toNews(NewsRequestDTO newsRequestDTO) {
        return News.builder()
                .header(newsRequestDTO.getHeader())
                .body(newsRequestDTO.getBody())
                .tags(newsRequestDTO.getTags())
                .addresses(newsRequestDTO.getAddresses())
                .build();
    }

    private NewsResponseDTO toNewsResponseDTO(News news) {
        return NewsResponseDTO.builder()
                .header(news.getHeader())
                .body(news.getBody())
                .tags(news.getTags())
                .build();
    }

    private Tag toTag(String strTag) {
        return new Tag(strTag);
    }
}
