package com.harsiyanin.intensive.test.attractions.attraction.entities;


import com.harsiyanin.intensive.test.attractions.assistance.entities.Assistance;
import com.harsiyanin.intensive.test.attractions.locality.entities.Locality;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Date creationDate;
    private String description;

    @Enumerated(EnumType.STRING)
    private AttractionType type;

    @ManyToOne
    @JoinColumn(name = "locality_id")
    private Locality locality;

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assistance> assistanceList;
}

