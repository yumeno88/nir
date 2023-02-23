package ru.yumeno.nir.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/tags")
@Api
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @ApiOperation("Получение всех тегов")
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    @ApiOperation("Получение тега по его id")
    public Tag getTagById(@PathVariable int id) {
        return tagService.getTagById(id);
    }

    @PostMapping
    @ApiOperation("Добавлние тега")
    public Tag addTag(@RequestBody Tag tag) {
        return tagService.addTag(tag);
    }

    @PutMapping
    @ApiOperation("Обновление тега")
    public Tag updateTag(@RequestBody Tag tag) {
        return tagService.updateTag(tag);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаление тега")
    public void addTag(@PathVariable int id) {
        tagService.deleteTag(id);
    }
}