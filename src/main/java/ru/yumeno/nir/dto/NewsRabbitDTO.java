package ru.yumeno.nir.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yumeno.nir.entity.Tag;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsRabbitDTO {
    private int id;
    private String header;
    private String body;
    private String createDate;
    private List<Tag> tags;
    private String imageUrl;
}
