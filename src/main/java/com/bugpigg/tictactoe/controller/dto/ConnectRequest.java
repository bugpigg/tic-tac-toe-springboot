package com.bugpigg.tictactoe.controller.dto;

import com.bugpigg.tictactoe.model.Player;
import lombok.Data;

@Data
public class ConnectRequest {

    private Player player;
    private String gameId;

}
