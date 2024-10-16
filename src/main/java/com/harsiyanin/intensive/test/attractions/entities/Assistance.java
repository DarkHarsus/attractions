package com.harsiyanin.intensive.test.attractions.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assistance")
public class Assistance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssistanceType type;

    @Column
    private String description;

    @Column(nullable = false)
    private String provider;

    @ManyToMany(mappedBy = "assistanceList")
    private List<Attraction> attractions = new ArrayList<>();
}

