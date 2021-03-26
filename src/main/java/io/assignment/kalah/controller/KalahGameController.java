package io.assignment.kalah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.assignment.kalah.dto.KalahGameDTO;
import io.assignment.kalah.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/games")
@Api(value = "Kalah game API. Set of endpoints for Creating and Playing the Kalah game")
public class KalahGameController {

    private final GameService service;

    @Autowired
    public KalahGameController(final GameService service) {
        this.service = service;
    }

    @PostMapping
    @ApiOperation(value = "Endpoint for creating new Kalah game instance. It returns a KalahaGame object with unique GameId used for playing the game",
            produces = "Application/JSON", response = KalahGameDTO.class, httpMethod = "POST")
    public ResponseEntity<KalahGameDTO> createGame() {

        log.debug("Invoking create() endpoint... ");
        final KalahGameDTO kalahGameDTO = this.service.createGame();
        log.debug("Game instance created. Id=" + kalahGameDTO.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(kalahGameDTO);
    }

    @PutMapping(path = "{gameId}/pits/{pitId}")
    @ApiOperation(value = "Endpoint for playing the game. ",
            produces = "Application/JSON", response = KalahGameDTO.class, httpMethod = "PUT")
    public ResponseEntity<KalahGameDTO> playGame(@ApiParam(value = "The id of game created by calling `/games` API. It can't be empty or null", required = true) @PathVariable(value = "gameId") String gameId, @PathVariable(value = "pitId") Integer pitId) {
        final KalahGameDTO kalahGameDTO = this.service.play(gameId, pitId);

        return ResponseEntity.status(HttpStatus.OK).body(kalahGameDTO);
    }

}
