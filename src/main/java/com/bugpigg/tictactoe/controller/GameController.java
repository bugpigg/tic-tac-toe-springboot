package com.bugpigg.tictactoe.controller;

import com.bugpigg.tictactoe.controller.dto.ConnectRequest;
import com.bugpigg.tictactoe.exception.InvalidGameException;
import com.bugpigg.tictactoe.exception.InvalidParamException;
import com.bugpigg.tictactoe.exception.NotFoundException;
import com.bugpigg.tictactoe.model.Game;
import com.bugpigg.tictactoe.model.GamePlay;
import com.bugpigg.tictactoe.model.Player;
import com.bugpigg.tictactoe.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Player player) {
        log.info("start game request: {}", player);
        return ResponseEntity.ok(gameService.createGame(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectRequest connectRequest)
        throws InvalidParamException, InvalidGameException {
        log.info("connect request: {}", connectRequest);
        return ResponseEntity.ok(gameService.connectToGame(connectRequest.getPlayer(),
            connectRequest.getGameId()));
    }

    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestBody Player player) throws NotFoundException {
        log.info("connect random: {}", player);
        return ResponseEntity.ok(gameService.connectToRandomGame(player));
    }

    @PostMapping("/gameplay")
    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request)
        throws InvalidGameException, NotFoundException {
        log.info("gameplay: {}", request);
        Game game = gameService.gamePlay(request);
        // 여기서 websocket 활용
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
}
