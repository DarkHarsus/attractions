package com.harsiyanin.intensive.test.attractions.assistance.entities;

import com.harsiyanin.intensive.test.attractions.attraction.entities.Attraction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Assistance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AssistanceType type;

    private String description;
    private String provider;

    @ManyToOne
    @JoinColumn(name = "attraction_id")
    private Attraction attraction;
}

