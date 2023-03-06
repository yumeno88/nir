package ru.yumeno.nir.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.dto.NewsRequestDTO;
import ru.yumeno.nir.dto.NewsRequestForUpdateDTO;
import ru.yumeno.nir.dto.NewsResponseDTO;
import ru.yumeno.nir.entity.News;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.service.NewsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/news")
@Api
@CrossOrigin("*")
public class NewsController {
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    @ApiOperation("Получение всех новостей")
    public List<NewsResponseDTO> getAllNews() {
        return newsService.getAllNews().stream().map(this::toNewsResponseDTO).toList();
    }

    @GetMapping("/sort")
    @ApiOperation("Получение всех новостей по тегам")
    public List<NewsResponseDTO> getAllNewsByTags(@RequestParam(name = "tags") List<String> strTags) {
        List<Tag> tags = strTags.stream().map(this::toTag).toList();
        return newsService.getAllNewsByTags(tags).stream().map(this::toNewsResponseDTO).toList();
    }

    @GetMapping("/{id}")
    @ApiOperation("Получение новости по ее id")
    public NewsResponseDTO getNewsById(@PathVariable int id) {
        return toNewsResponseDTO(newsService.getNewsById(id));
    }

    @PostMapping
    @ApiOperation("Добавлние новости")
    public NewsResponseDTO addNews(@Valid @RequestBody NewsRequestDTO newsRequestDTO) {
        return toNewsResponseDTO(newsService.addNews(toNews(newsRequestDTO)));
    }

    @PutMapping
    @ApiOperation("Обновление новости")
    public NewsResponseDTO updateNews(@RequestBody NewsRequestForUpdateDTO newsRequestForUpdateDTO) {
        return toNewsResponseDTO(newsService.updateNews(toNewsWithId(newsRequestForUpdateDTO)));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаление новости")
    public void addNews(@PathVariable int id) {
        newsService.deleteNews(id);
    }

    private News toNews(NewsRequestDTO newsRequestDTO) {
        return News.builder()
                .header(newsRequestDTO.getHeader())
                .body(newsRequestDTO.getBody())
                .tags(newsRequestDTO.getTags())
                .imageUrl(newsRequestDTO.getImageUrl())
                .build();
    }

    private News toNewsWithId(NewsRequestForUpdateDTO newsRequestForUpdateDTO) {
        return News.builder()
                .id(newsRequestForUpdateDTO.getId())
                .header(newsRequestForUpdateDTO.getHeader())
                .body(newsRequestForUpdateDTO.getBody())
                .tags(newsRequestForUpdateDTO.getTags())
                .createDate(newsRequestForUpdateDTO.getCreateDate())
                .imageUrl(newsRequestForUpdateDTO.getImageUrl())
                .build();
    }

    private NewsResponseDTO toNewsResponseDTO(News news) {
        return NewsResponseDTO.builder()
                .id(news.getId())
                .header(news.getHeader())
                .body(news.getBody())
                .createDate(news.getCreateDate())
                .tags(news.getTags())
                .imageUrl(news.getImageUrl())
                .build();
    }

    private Tag toTag(String strTag) {
        return new Tag(strTag);
    }
}
