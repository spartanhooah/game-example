package net.frey.services;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.frey.entity.Game;
import net.frey.exception.MissingGameException;
import net.frey.repository.GamesRepository;

@ApplicationScoped
@RequiredArgsConstructor
public class GameService {
    private final GamesRepository gamesRepository;

    public List<Game> findPaginated(int page, int pageSize, String sortBy, String name) {
        if (isNotEmpty(name)) {
            return gamesRepository.findPaginatedByName(page, pageSize, sortBy, name);
        }

        return gamesRepository.findPaginated(page, pageSize, sortBy);
    }

    public long count(String name) {
        if (isNotEmpty(name)) {
            return gamesRepository.countByName(name);
        }

        return gamesRepository.count();
    }

    public Optional<Game> findById(long id) {
        return gamesRepository.findByIdOptional(id);
    }

    @Transactional
    public void createGame(Game game) {
        // ensure we use the generated ID
        game.setId(0);
        gamesRepository.persist(game);
    }

    @Transactional
    public void replaceGame(Game game) {
        gamesRepository.persist(gamesRepository
                .findByIdOptional(game.getId())
                .orElseThrow(() -> new MissingGameException("No game found with id " + game.getId())));
    }

    @Transactional
    public void updateGame(long id, String name, String category) {
        var foundGame = gamesRepository
                .findByIdOptional(id)
                .orElseThrow(() -> new MissingGameException("No game found with id " + id));
        if (isNotEmpty(name)) {
            foundGame.setName(name);
        }

        if (isNotEmpty(category)) {
            foundGame.setCategory(category);
        }

        gamesRepository.persist(foundGame);
    }

    @Transactional
    public void deleteGame(long id) {
        gamesRepository.deleteById(id);
    }
}
