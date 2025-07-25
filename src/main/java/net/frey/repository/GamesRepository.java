package net.frey.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import net.frey.entity.Game;

@ApplicationScoped
public class GamesRepository implements PanacheRepositoryBase<Game, Long> {
    public List<Game> findPaginated(int page, int size) {
        return findAll().page(Page.of(page, size)).list();
    }
}
