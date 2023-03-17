package ru.yumeno.nir.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yumeno.nir.entity.News;
import ru.yumeno.nir.entity.Subscription;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.exception_handler.exceptions.DeletionFailedException;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;
import ru.yumeno.nir.repository.NewsRepository;
import ru.yumeno.nir.repository.SubscriptionRepository;
import ru.yumeno.nir.repository.TagRepository;
import ru.yumeno.nir.service.TagService;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final NewsRepository newsRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, NewsRepository newsRepository, SubscriptionRepository subscriptionRepository) {
        this.tagRepository = tagRepository;
        this.newsRepository = newsRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getTagById(String id) {
        Optional<Tag> optional = tagRepository.findById(id);
        return optional.
                orElseThrow(() -> new ResourceNotFoundException("Tag not exist with id : " + id));
    }

    @Override
    public Tag addTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public void deleteTag(String id) {
        Optional<Tag> optional = tagRepository.findById(id);
        List<News> news = newsRepository.findAllByTags(optional.
                orElseThrow(() -> new ResourceNotFoundException("Tag not exist with id : " + id)));
        List<Subscription> subscriptions = subscriptionRepository.findAllByTags(optional.
                orElseThrow(() -> new ResourceNotFoundException("Tag not exist with id : " + id)));
        if (news.isEmpty() && subscriptions.isEmpty()) {
            tagRepository.deleteById(id);
        } else {
            throw new DeletionFailedException("Tag cannot be deleted with id : " + id);
        }
    }
}
