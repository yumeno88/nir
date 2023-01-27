package ru.yumeno.nir.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable int id) {
        return tagService.getTagById(id);
    }

    @PostMapping
    public Tag addTag(@RequestBody Tag tag) {
        return tagService.addTag(tag);
    }

    @PutMapping
    public Tag updateTag(@RequestBody Tag tag) {
        return tagService.updateTag(tag);
    }

    @DeleteMapping("/{id}")
    public void addTag(@PathVariable int id) {
        tagService.deleteTag(id);
    }
}