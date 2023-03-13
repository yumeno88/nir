package ru.yumeno.nir.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class StreetDTO {
    private int id;
    @NotBlank(message = "Street name cannot be null")
    private String name;
}
