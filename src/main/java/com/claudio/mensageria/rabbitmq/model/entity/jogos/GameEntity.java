package com.claudio.mensageria.rabbitmq.model.entity.jogos;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "game")
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_entity_seq")
    @SequenceGenerator(name = "game_entity_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    private Long distribuitor;

    private Long categoria;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GameEntity that = (GameEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}