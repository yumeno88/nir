package ru.yumeno.nir.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yumeno.nir.entity.Tag;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsRequestDTO {
    @NotBlank(message = "News header cannot be null")
    @Size(min = 5, message = "News header length cannot be shorter than 5 characters")
    private String header;
    private String body;
    private List<Tag> tags;
    //    private List<Address> addresses; //TODO mb remove addresses
    private String imageUrl;
}
