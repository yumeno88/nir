package ru.yumeno.nir.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.dto.SubscriptionDTO;
import ru.yumeno.nir.entity.Subscription;
import ru.yumeno.nir.service.SubscriptionService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/subscriptions")
@Api
@Slf4j
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping(value = "")
    @ApiOperation("Получение всех подписок")
    public List<SubscriptionDTO> getAllSubscriptions() {
        log.info("Try to get all subscriptions");
        return subscriptionService.getAllSubscriptions().stream().map(this::toSubscriptionDTO).toList();
    }

    @GetMapping(value = "/{id}")
    @ApiOperation("Получение подписки по ее id")
    public SubscriptionDTO getSubscriptionById(@PathVariable int id) {
        log.info("Try to get subscription by id = " + id);
        return toSubscriptionDTO(subscriptionService.getSubscriptionById(id));
    }

    @PostMapping(value = "")
    @ApiOperation("Добавлние подписки")
    public SubscriptionDTO addSubscription(@Valid @RequestBody SubscriptionDTO subscriptionDTO) {
        log.info("Try to add subscription: " + subscriptionDTO.toString());
        return toSubscriptionDTO(subscriptionService.addSubscription(toSubscription(subscriptionDTO)));
    }

    @PutMapping(value = "")
    @ApiOperation("Обновление подписки")
    public SubscriptionDTO updateSubscription(@Valid @RequestBody SubscriptionDTO subscriptionDTO) {
        log.info("Try to update subscription: " + subscriptionDTO.toString());
        return toSubscriptionDTO(subscriptionService.updateSubscription(toSubscription(subscriptionDTO)));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("Удаление подписки")
    public void deleteSubscription(@PathVariable int id) {
        log.info("Try to delete subscription by id = " + id);
        subscriptionService.deleteSubscription(id);
    }

    private Subscription toSubscription(SubscriptionDTO subscriptionDTO) {
        return Subscription.builder()
                .id(subscriptionDTO.getId())
                .address(subscriptionDTO.getAddress())
                .tags(subscriptionDTO.getTags())
                .chatId(subscriptionDTO.getChatId())
                .build();
    }

    private SubscriptionDTO toSubscriptionDTO(Subscription subscription) {
        return SubscriptionDTO.builder()
                .id(subscription.getId())
                .address(subscription.getAddress())
                .tags(subscription.getTags())
                .chatId(subscription.getChatId())
                .build();
    }
}
