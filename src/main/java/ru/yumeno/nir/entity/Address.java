package ru.yumeno.nir.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @JoinColumn(name = "district_id", referencedColumnName = "id")
    private District district;
    @ManyToOne
    @JoinColumn(name = "street_id", referencedColumnName = "id")
    private Street street;
}
