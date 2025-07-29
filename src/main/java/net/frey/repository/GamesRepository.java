package net.frey.repository;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import net.frey.entity.Game;

@ApplicationScoped
public class GamesRepository implements PanacheRepositoryBase<Game, Long> {
    public List<Game> findPaginated(int page, int size, String sortBy) {
        if (isNotEmpty(sortBy)) {
            findAll(Sort.ascending("category")).page(Page.of(page, size)).list();
        }

        return findAll().page(Page.of(page, size)).list();
    }

    public List<Game> findPaginatedByName(int page, int size, String sortBy, String name) {
        if (isNotEmpty(sortBy)) {
            return find("name like ?1", Sort.ascending("category"), "%" + name + "%")
                    .page(Page.of(page, size))
                    .list();
        }

        return find("name like ?1", "%" + name + "%").page(Page.of(page, size)).list();
    }

    public long countByName(String name) {
        return count("name like ?1", "%" + name + "%");
    }
}
