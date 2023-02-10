package ru.yumeno.nir.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "street")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Street {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(nullable = false)
    @NotBlank(message = "Street name cannot be null")
    private String name;
}