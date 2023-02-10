package ru.yumeno.nir.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;
import ru.yumeno.nir.repository.TagRepository;
import ru.yumeno.nir.service.TagService;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getTagById(int id) {
        Optional<Tag> optional = tagRepository.findById(id);
        return optional.
                orElseThrow(() -> new ResourceNotFoundException("Tag not exist with id : " + id));
    }

    @Override
    public Tag addTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public void deleteTag(int id) {
        tagRepository.deleteById(id);
    }
}
