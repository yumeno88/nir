package ru.yumeno.nir.dto;

import lombok.Builder;
import lombok.Data;
import ru.yumeno.nir.entity.Address;
import ru.yumeno.nir.entity.Tag;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
public class SubscriptionDTO {
    private int id;
    @Email(message = "uncorrect email")
    private String email;
    private String phoneNumber;
    @NotBlank(message = "Subscription address cannot be null")
    private Address address;
    private List<Tag> tags;
}
