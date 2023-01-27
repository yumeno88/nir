package ru.yumeno.nir.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yumeno.nir.entity.Address;
import ru.yumeno.nir.entity.Tag;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsRequestDTO {
    private String header;
    private String body;
    private List<Tag> tags;
    private List<Address> addresses;
}
