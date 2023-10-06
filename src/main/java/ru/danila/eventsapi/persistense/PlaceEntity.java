package ru.danila.eventsapi.persistense;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "place_entity", schema = "public")
public class PlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    Long id;

    @Column(name = "address", nullable = false)
    String address;

    @Column(name = "capacity", nullable = false)
    Long capacity;

    @OneToMany(mappedBy = "place",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    List<EventEntity> events;
}
