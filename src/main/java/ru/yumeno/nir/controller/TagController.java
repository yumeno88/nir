package ru.yumeno.nir.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.dto.TagDTO;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.service.TagService;

import java.util.List;

@RestController
@RequestMapping(value = "/tags")
@Api
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "")
    @ApiOperation("Получение всех тегов")
    public List<TagDTO> getAllTags() {
        return tagService.getAllTags().stream().map(this::toTagDTO).toList();
    }

    @GetMapping(value = "/{id}")
    @ApiOperation("Получение тега по его id")
    public TagDTO getTagById(@PathVariable int id) {
        return toTagDTO(tagService.getTagById(id));
    }

    @PostMapping(value = "")
    @ApiOperation("Добавлние тега")
    public TagDTO addTag(@RequestBody TagDTO tagDTO) {
        return toTagDTO(tagService.addTag(toTag(tagDTO)));
    }

    @PutMapping(value = "")
    @ApiOperation("Обновление тега")
    public TagDTO updateTag(@RequestBody TagDTO tagDTO) {
        return toTagDTO(tagService.updateTag(toTag(tagDTO)));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("Удаление тега")
    public void deleteTag(@PathVariable int id) {
        tagService.deleteTag(id);
    }

    private Tag toTag(TagDTO tagDTO) {
        return Tag.builder()
                .name(tagDTO.getName())
                .build();
    }

    private TagDTO toTagDTO(Tag tag) {
        return TagDTO.builder()
                .name(tag.getName())
                .build();
    }
}