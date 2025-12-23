package com.arena.ticketing.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "stadiums")
@Getter
@Setter
@NoArgsConstructor
public class Stadium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Numele stadionului este obligatoriu")
    private String name;

    private String location;

    // Relația cu Sectoarele
    @OneToMany(mappedBy = "stadium", cascade = CascadeType.ALL)
    private List<Sector> sectors;
}
