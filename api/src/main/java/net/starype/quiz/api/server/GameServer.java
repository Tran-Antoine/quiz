package net.starype.quiz.api.server;

import net.starype.quiz.api.game.player.Player;

public interface GameServer {

    void onRoundEnded();
    void onGameOver();
    void onPlayerVoted(); // parameters (not defined yet) required
    void onPlayerScoreUpdated(Player player);
}
