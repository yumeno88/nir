package ru.yumeno.nir.service;

import ru.yumeno.nir.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();

    Tag getTagById(String id);

    Tag addTag(Tag tag);

    void deleteTag(String id);
}
