package ru.yumeno.nir.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yumeno.nir.dto.NewsRabbitDTO;
import ru.yumeno.nir.service.NewsProducer;

@Service
@Slf4j
public class NewsProducerImpl implements NewsProducer {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public NewsProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String rabbitQueue, NewsRabbitDTO news) {
        log.debug(news.toString());
        rabbitTemplate.convertAndSend(rabbitQueue, news);
    }
}
