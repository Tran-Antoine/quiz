package net.starype.quiz.api.game;

import net.starype.quiz.api.game.player.Player;

public class MutableGuessContext implements PlayerGuessContext {

    private Player<?> player;
    private double correctness;
    private boolean eligibility;

    public MutableGuessContext(Player<?> player, Double correctness, boolean eligibility) {
        this.player = player;
        if(correctness != null) {
            this.correctness = correctness;
        }
        this.eligibility = eligibility;
    }

    public double getCorrectness() {
        return correctness;
    }

    public Player<?> getPlayer() {
        return player;
    }

    public boolean isEligible() {
        return eligibility;
    }

    public void setCorrectness(double correctness) {
        this.correctness = correctness;
    }

    public void setEligibility(boolean eligibility) {
        this.eligibility = eligibility;
    }
}
