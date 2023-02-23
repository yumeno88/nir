package ru.yumeno.nir.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "subscription")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription { // TODO change phone and email to telegram account id
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Email(message = "uncorrect email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber; // TODO regex
    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
    @NotBlank(message = "Subscription address cannot be null")
    private Address address;
    @ManyToMany
    @JoinTable(
            name = "subscription_tag",
            joinColumns = @JoinColumn(name = "subscription_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_name")
    )
    private List<Tag> tags;
}
