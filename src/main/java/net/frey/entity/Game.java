package net.frey.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Game model")
public class Game extends PanacheEntityBase {
    @Id
    @GeneratedValue(generator = "games_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "games_id_seq", sequenceName = "games_id_seq", allocationSize = 1)
    @Schema(description = "Game id", examples = "1")
    private long id;

    @Schema(description = "Name of game", examples = "Frostpunk")
    private String name;

    @Schema(description = "Category of game", examples = "FPS")
    private String category;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id && Objects.equals(name, game.name) && Objects.equals(category, game.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category);
    }
}
