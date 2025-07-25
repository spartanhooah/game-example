package net.frey;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import net.frey.entity.Game;
import net.frey.entity.ResponseModel;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/games")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@APIResponse(
        responseCode = "200",
        description = "Return operation data.",
        content = @Content(schema = @Schema(implementation = ResponseModel.class)))
public class GreetingResource {
    private final List<Game> games = new ArrayList<>(List.of(
            new Game(1, "The Elder Scrolls III: Morrowind", "RPG"),
            new Game(2, "Warframe", "FPS"),
            new Game(3, "Mass Effect", "RPG"),
            new Game(4, "Beat Saber", "VR")));

    @GET
    @Operation(
            summary = "Get all games",
            description =
                    "Retrieves a paginated list of games. The results can be filtered by game name and sorted by the game category specified in the cookies.")
    @APIResponse(
            responseCode = "200",
            description = "Return games list.",
            content = @Content(schema = @Schema(implementation = Game.class, type = SchemaType.ARRAY)))
    public Response getGamesList(
            @HeaderParam("page") int page,
            @HeaderParam("size") int size,
            @RestQuery String name,
            @CookieParam("gameCategory") String gameCategory) {
        var pagedGames = games;
        if (isNotEmpty(name)) {
            pagedGames = pagedGames.stream()
                    .filter(game -> game.getName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
        }

        int totalGames = pagedGames.size();
        int start;
        int end;

        if (page > 0 && size > 0) {
            start = (page - 1) * size;
            end = Math.min(start + size, totalGames);
        } else {
            start = 0;
            end = totalGames;
        }

        if (start >= totalGames) {
            return Response.status(NOT_FOUND).build();
        }

        if (isNotEmpty(gameCategory)) {
            pagedGames = pagedGames.stream()
                    .sorted((g1, g2) -> {
                        var isG1InCategory = gameCategory.equals(g1.getCategory());
                        var isG2InCategory = gameCategory.equals(g2.getCategory());

                        if (isG1InCategory && !isG2InCategory) {
                            return -1;
                        }

                        if (!isG1InCategory && isG2InCategory) {
                            return 1;
                        }

                        return 0;
                    })
                    .toList();
        }

        pagedGames = pagedGames.subList(start, end);

        return Response.ok(pagedGames).header("X-Total-Count", totalGames).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get game by id", description = "Retrieves specified game by id.")
    @APIResponse(
            responseCode = "200",
            description = "Return game.",
            content = @Content(schema = @Schema(implementation = Game.class)))
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(schema = @Schema(implementation = ResponseModel.class)))
    public Response getGame(@PathParam("id") int id) {
        return games.stream()
                .filter(game -> game.getId() == id)
                .findFirst()
                .map(game -> Response.ok(game)
                        .cookie(new NewCookie.Builder("gameCategory")
                                .value(game.getCategory())
                                .path("/")
                                .comment("Games Category")
                                .maxAge(3600)
                                .build())
                        .build())
                .orElseGet(() -> Response.status(NOT_FOUND).build());
    }

    @POST
    @Operation(summary = "Create a new game", description = "Create a new game.")
    @APIResponse(responseCode = "204", description = "Game created")
    public Response createGame(Game game) {
        var newId =
                games.stream().max(Comparator.comparingLong(Game::getId)).get().getId() + 1;

        game.setId(newId);
        games.add(game);

        return Response.status(Status.CREATED)
                .entity(new ResponseModel("Game created", 201))
                .build();
    }

    @PATCH
    @Operation(summary = "Update game", description = "Update specified game fields")
    @APIResponse(responseCode = "204", description = "Game successfully updated.")
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(schema = @Schema(implementation = ResponseModel.class)))
    public Response updateGame(Game game) {
        games.stream().filter(g -> g.getId() == game.getId()).findFirst().ifPresent(g -> {
            if (isNotEmpty(game.getName())) {
                g.setName(game.getName());
            }

            if (isNotEmpty(game.getCategory())) {
                g.setCategory(game.getCategory());
            }
        });

        return Response.noContent().build();
    }

    @PUT
    @Operation(summary = "Replace game", description = "Replace game.")
    @APIResponse(responseCode = "204", description = "Game successfully replaced.")
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(schema = @Schema(implementation = ResponseModel.class)))
    public Response replaceGame(Game game) {
        IntStream.range(0, games.size())
                .filter(i -> games.get(i).getId() == game.getId())
                .findFirst()
                .ifPresent(i -> games.set(i, game));

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete game by id", description = "Deletes the game with the specified id")
    @APIResponse(responseCode = "204", description = "Game successfully deleted.")
    @APIResponse(
            responseCode = "400",
            description = "Return operation data.",
            content = @Content(schema = @Schema(implementation = ResponseModel.class)))
    public Response deleteGame(@PathParam("id") int id) {
        games.removeIf(game -> game.getId() == id);

        return Response.noContent().build();
    }
}
