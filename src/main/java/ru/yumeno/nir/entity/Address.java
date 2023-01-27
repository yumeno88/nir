package ru.yumeno.nir.entity;

import jakarta.persistence.*;
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
    private String apartment;
    @Column(nullable = false)
    private String house;
    @Column(nullable = false)
    private String porch;
    @ManyToOne()
    @JoinColumn(name = "district_id", referencedColumnName = "id", nullable = false)
    private District district;
    @ManyToOne()
    @JoinColumn(name = "street_id", referencedColumnName = "id", nullable = false)
    private Street street;
}
