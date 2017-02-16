package hu.multiplayermnkgame.game.gamerepresentation;

import hu.multiplayermnkgame.game.statespacerepresentation.State;

import static hu.multiplayermnkgame.game.gameplay.GameLoop.numOfPlayers;

public class GameState {
    public State state;
    public int player;
    //extra information
    public Step lastStep;

    //the start of the game: empty table, the first player, -(1;-1) step
    public GameState() {
        state = new State();
        player = 1;
        lastStep = new Step();
    }

    public GameState(State state, int player, Step lastStep) {
        this.state = state;
        this.player = player;
        this.lastStep = lastStep;
    }

    public boolean isEnd(){
        return state.isEnd(lastStep.getX(), lastStep.getY())==0?false:true;
    }

    @Override
    public String toString() {
        if ( lastStep != null && isEnd() ) {
            String result = state.toString()+"\n End ";
            if ( state.isNotDeal(lastStep.getX(),lastStep.getY()) )
                result += player==1?numOfPlayers:(player-1) +" win.";
            else
                result +="DEAL";
            return result;
        } else {
            return state.toString();
        }
    }
}
