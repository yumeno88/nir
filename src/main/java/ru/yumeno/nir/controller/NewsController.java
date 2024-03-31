package ru.yumeno.nir.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.dto.NewsRabbitDTO;
import ru.yumeno.nir.dto.NewsRequestDTO;
import ru.yumeno.nir.dto.NewsResponseDTO;
import ru.yumeno.nir.entity.News;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.service.FileService;
import ru.yumeno.nir.service.NewsProducer;
import ru.yumeno.nir.service.NewsService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/news")
@Api
@CrossOrigin("*")
@Slf4j
public class NewsController {
    private final NewsService newsService;
    private final NewsProducer newsProducer;
    private final FileService fileService;

    @Autowired
    public NewsController(NewsService newsService, NewsProducer newsProducer, FileService fileService) {
        this.newsService = newsService;
        this.newsProducer = newsProducer;
        this.fileService = fileService;
    }

    @GetMapping(value = "")
    @ApiOperation("Получение всех новостей")
    public List<NewsResponseDTO> getAllNews() {
        log.info("Try to get all news");
        return newsService.getAllNews().stream().map(this::toNewsResponseDTO).toList();
    }

    @GetMapping(value = "/sort")
    @ApiOperation("Получение всех новостей по тегам")
    public List<NewsResponseDTO> getAllNewsByTags(@RequestParam(name = "tags") List<String> strTags) {
        List<Tag> tags = strTags.stream().map(this::toTag).toList();
        log.info("Try to get all streets by tags: " + strTags);
        return newsService.getAllNewsByTags(tags).stream().map(this::toNewsResponseDTO).toList();
    }

    @GetMapping(value = "/{id}")
    @ApiOperation("Получение новости по ее id")
    public NewsResponseDTO getNewsById(@PathVariable int id) {
        log.info("Try to get news by id = " + id);
        return toNewsResponseDTO(newsService.getNewsById(id));
    }

    @PostMapping(value = "")
    @ApiOperation("Добавлние новости")
    public NewsResponseDTO addNews(@Valid @RequestBody NewsRequestDTO newsRequestDTO) {
        log.info("Try to add news: " + newsRequestDTO.toString());
        News news = newsService.addNews(toNews(newsRequestDTO));
//        NewsRabbitDTO newsRabbitDTO = toNewsRabbitDTO(news);
//        newsProducer.produce("news", newsRabbitDTO);
        return toNewsResponseDTO(news);
    }

    @PutMapping(value = "")
    @ApiOperation("Обновление новости")
    public NewsResponseDTO updateNews(@Valid @RequestBody NewsRequestDTO newsRequestDTO) {
        log.info("Try to update news: " + newsRequestDTO.toString());
        return toNewsResponseDTO(newsService.updateNews(toNews(newsRequestDTO)));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("Удаление новости")
    public void deleteNews(@PathVariable int id) {
        log.info("Try to delete news by id = " + id);
        newsService.deleteNews(id);
    }

    @GetMapping(value = "/sort_date")
    @ApiOperation("Получение всех новостей по дате")
    public List<NewsResponseDTO> getAllNewsByDateBetween(@RequestParam(name = "start") String start,
                                                         @RequestParam(name = "end") String end) {
        log.info("Try to get all news by date between: " + start + ", " + end);
        LocalDateTime startDate = LocalDateTime.parse(start + "T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse(end + "T00:00:00");
        return newsService.getAllNewsByDateBetween(startDate, endDate).stream().map(this::toNewsResponseDTO).toList();
    }

    @GetMapping(value = "/sort_all")
    @ApiOperation("Получение всех новостей по тегам, дате с лимитом")
    public List<NewsResponseDTO> getAllNewsByTagsDateLimit(@RequestParam(name = "tags") List<String> strTags,
                                                           @RequestParam(name = "limit") String limit) {
        List<Tag> tags = strTags.stream().map(this::toTag).toList();
        int limitInt = 100;
        if (!Objects.equals(limit, "null")) {
            limitInt = Integer.parseInt(limit);
        }
        return newsService.getAllByTagsDateLimit(tags, limitInt).stream().map(this::toNewsResponseDTO).toList();
    }

    @GetMapping(value = "/report")
    @ApiOperation("Запись отчетов в excel-файл")
    public String writeReports(@RequestParam(name = "tags") List<String> strTags,
                             @RequestParam(name = "start") String start,
                             @RequestParam(name = "end") String end,
                             @RequestParam(name = "limit") String limit) throws IOException {
        log.info("Try to write reports with start: " + start);
        int limitInt = 100;
        if (!Objects.equals(limit, "null")) {
            limitInt = Integer.parseInt(limit);
        }
        if (Objects.equals(start.trim(), "")) {
            start = "2020-01-01";
        }
        if (Objects.equals(end.trim(), "")) {
            end = "2050-01-01";
        }
        List<Tag> tags = strTags.stream().map(this::toTag).toList();
        LocalDateTime startDate = LocalDateTime.parse(start + "T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse(end + "T00:00:00");
        File file = newsService.writeExcel(tags, startDate, endDate, limitInt);

        return fileService.generateFileToken(file.getAbsolutePath());
    }

    private News toNews(NewsRequestDTO newsRequestDTO) {
        return News.builder()
                .id(newsRequestDTO.getId())
                .header(newsRequestDTO.getHeader())
                .body(newsRequestDTO.getBody())
                .tags(newsRequestDTO.getTags())
                .createDate(newsRequestDTO.getCreateDate())
                .imageUrl(newsRequestDTO.getImageUrl())
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

    private NewsRabbitDTO toNewsRabbitDTO(News news) {
        return NewsRabbitDTO.builder()
                .id(news.getId())
                .header(news.getHeader())
                .body(news.getBody())
                .createDate(news.getCreateDate().toString())
                .tags(news.getTags())
                .imageUrl(news.getImageUrl())
                .build();
    }

    private Tag toTag(String strTag) {
        return new Tag(strTag);
    }
}
