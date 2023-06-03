package ru.yumeno.nir.service;

import ru.yumeno.nir.dto.NewsRabbitDTO;

public interface NewsProducer {
    void produce(String rabbitQueue, NewsRabbitDTO news);
}
