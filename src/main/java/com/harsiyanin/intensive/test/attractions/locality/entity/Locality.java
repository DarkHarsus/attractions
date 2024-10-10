package com.harsiyanin.intensive.test.attractions.locality.entities;

import com.harsiyanin.intensive.test.attractions.attraction.entities.Attraction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Locality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String region;

    @OneToMany(mappedBy = "locality", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attraction> attractions;

    private Double latitude;
    private Double longitude;

}

