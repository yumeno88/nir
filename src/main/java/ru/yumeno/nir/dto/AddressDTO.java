package ru.yumeno.nir.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yumeno.nir.entity.District;
import ru.yumeno.nir.entity.Street;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    private int id;
    @NotBlank(message = "Address apartment cannot be null")
    private String apartment;
    @NotBlank(message = "Address house cannot be null")
    private String house;
    @NotBlank(message = "Address porch cannot be null")
    private String porch;
    @NotNull(message = "Address district cannot be null")
    private District district;
    @NotNull(message = "Address street cannot be null")
    private Street street;
}