package ru.yumeno.nir.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(nullable = false)
    @NotBlank(message = "Address apartment cannot be null")
    private String apartment;
    @Column(nullable = false)
    @NotBlank(message = "Address house cannot be null")
    private String house;
    @Column(nullable = false)
    @NotBlank(message = "Address porch cannot be null")
    private String porch;
    @ManyToOne()
    @JoinColumn(name = "district_id", referencedColumnName = "id", nullable = false)
    @NotBlank(message = "Address district cannot be null")
    private District district;
    @ManyToOne()
    @JoinColumn(name = "street_id", referencedColumnName = "id", nullable = false)
    @NotBlank(message = "Address street cannot be null")
    private Street street;
}
