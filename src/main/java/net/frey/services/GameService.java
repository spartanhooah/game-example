package net.frey.services;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.frey.entity.Game;
import net.frey.repository.GamesRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@RequiredArgsConstructor
public class GameService {
    @ConfigProperty(name = "test")
    String test;

    private final GamesRepository gamesRepository;

    public void test() {
        Log.info(test);
    }

    public List<Game> getAllGames() {
        return gamesRepository.listAll();
    }

    public List<Game> findPaginated(int page, int pageSize) {
        return gamesRepository.findPaginated(page, pageSize);
    }

    public long count() {
        return gamesRepository.count();
    }
}
