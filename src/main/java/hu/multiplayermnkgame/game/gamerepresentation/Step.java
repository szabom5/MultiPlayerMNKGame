package hu.multiplayermnkgame.game.gamerepresentation;

import hu.multiplayermnkgame.game.statespacerepresentation.Operator;

import static hu.multiplayermnkgame.game.gameplay.GameLoop.numberOfPlayers;

public class Step {
    // the operator to use, which contains the numberOfPlayers already
    public Operator operator;
    // the players who is placing a mark on the table
    public int player;

    public Step(){
        operator = new Operator(-1,-1,0);
        player = 0;
    }

    public Step(Operator operator, int player) {
        this.operator = operator;
        this.player = player;
    }

    public boolean applicable(GameState gameState){
        return operator.applicable(gameState.state);
    }

    public boolean nextToMark(GameState gameState) {
        return operator.isNextToMark(gameState.state);
    }

    public GameState apply(GameState oldState){
        return new GameState(operator.apply(oldState.state), (player== numberOfPlayers)?1:(player+1), this);
    }

    public int getX() {
        return operator.x;
    }

    public int getY() {
        return operator.y;
    }

    public String toString() {
        return "step= "+operator.toString();
    }
}
