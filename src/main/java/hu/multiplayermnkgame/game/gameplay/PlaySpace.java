package hu.multiplayermnkgame.game.gameplay;

import hu.multiplayermnkgame.game.gamerepresentation.*;
import hu.multiplayermnkgame.game.statespacerepresentation.Operator;

import java.util.ArrayList;
import java.util.List;

import static hu.multiplayermnkgame.game.gameplay.GameLoop.*;

public class PlaySpace {
    public List<Step> steps;

    PlaySpace(){
        steps = new ArrayList<>();
        for(int i=1; i<=N;i++){
            for(int j=1; j<=M;j++){
                for(int p = 1; p<=numOfPlayers;p++)
                steps.add(new Step(new Operator(i,j,p),p));
            }
        }
    }

    GameState start() {
        return new GameState();
    }
}
