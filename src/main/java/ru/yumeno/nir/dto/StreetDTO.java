package ru.yumeno.nir.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StreetDTO {
    private int id;
    @NotBlank(message = "Street name cannot be null")
    private String name;
}
