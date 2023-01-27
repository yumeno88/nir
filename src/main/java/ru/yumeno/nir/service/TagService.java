package ru.yumeno.nir.service;

import ru.yumeno.nir.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();

    Tag getTagById(int id);

    Tag addTag(Tag tag);

    Tag updateTag(Tag tag);

    void deleteTag(int id);
}
