package ru.yumeno.nir.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DistrictDTO {
    private int id;
    private String name;
}
