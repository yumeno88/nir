package ru.yumeno.nir.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(nullable = false)
    private String header;
    @Column(length = 3000)
    private String body;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;
    @Column(nullable = false)
    private String imageUrl;
    @ManyToMany // (cascade = CascadeType.REMOVE) // TODO
    @JoinTable(
            name = "news_tag",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_name")
    )
    private List<Tag> tags;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "news_address",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private List<Address> addresses;

    @PrePersist
    protected void prePersist() {
        createDate = LocalDateTime.now();
    }
}
