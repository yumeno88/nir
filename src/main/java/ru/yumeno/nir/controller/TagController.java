package ru.yumeno.nir.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.dto.TagDTO;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.service.TagService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/tags")
@Api
@Slf4j
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "")
    @ApiOperation("Получение всех тегов")
    public List<TagDTO> getAllTags() {
        log.info("Try to get all tags");
        return tagService.getAllTags().stream().map(this::toTagDTO).toList();
    }

    @GetMapping(value = "/{id}")
    @ApiOperation("Получение тега по его id")
    public TagDTO getTagById(@PathVariable String id) {
        log.info("Try to get tag by id = " + id);
        return toTagDTO(tagService.getTagById(id));
    }

    @PostMapping(value = "")
    @ApiOperation("Добавлние тега")
    public TagDTO addTag(@Valid @RequestBody TagDTO tagDTO) {
        log.info("Try to add tag: " + tagDTO.toString());
        return toTagDTO(tagService.addTag(toTag(tagDTO)));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("Удаление тега")
    public void deleteTag(@PathVariable String id) {
        log.info("Try to delete tag by id = " + id);
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