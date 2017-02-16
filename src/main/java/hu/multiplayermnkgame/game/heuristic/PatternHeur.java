package hu.multiplayermnkgame.game.heuristic;

import hu.multiplayermnkgame.game.gamerepresentation.GameState;
import hu.multiplayermnkgame.game.gamerepresentation.Step;
import hu.multiplayermnkgame.game.statespacerepresentation.State;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hu.multiplayermnkgame.game.gameplay.GameLoop.*;

/**
 * It is optimized in two ways:
 * 1. it uses the last step of the GameState, thus it does not have to check all cells of the table.
 * 2. it only needs to calculate a positive value for the player who made the last step,
 *    because the algorithm which uses this heuristic only uses that value.
 */
public class PatternHeur{

    private State state;

    private Step lastStep;

    private int supportedPlayer;

    private final Map<Pattern, Double> patterns;

    public PatternHeur(){
        patterns = new HashMap<>();

        for(int i = 1; i <= K; ++i){

            Pattern patterns1side1 = Pattern.compile("(.)*?0{"+(K-i)+"}x{"+i+"}0{0,"+Math.max((K-i-1),0)+"}[^0x]*?");
            patterns.put(patterns1side1, Math.pow(2,i));

            Pattern patterns1side2 = Pattern.compile("[^0x]*?0{0,"+Math.max((K-i-1),0)+"}x{"+i+"}0{"+(K-i)+"}(.)*?");
            patterns.put(patterns1side2, Math.pow(2,i));

            Pattern pattern2 = Pattern.compile("(.)*?0{"+(K-i)+"}x{"+i+"}0{"+(K-i)+"}(.)*?");
            patterns.put(pattern2, 2*Math.pow(2,i));
        }
    }

    public int[] heuristic(GameState gameState) {
        state = gameState.state;
        lastStep = gameState.lastStep;
        supportedPlayer = lastStep.player;


        return countPatterns(state);
    }

    private int[] countPatterns(State state) {
        int[] res = new int[numOfPlayers+1];

        //count Right Diagonal
        String rightDiag = readRightDiagString();

        //count Left Diagonal
        String leftDiag = readLeftDiagString();

        //count Row
        String row = readRowString();

        //count Column
        String col = readColString();

        for(Map.Entry<Pattern, Double> entry : patterns.entrySet()){
            Matcher mRow = entry.getKey().matcher(row);
            if(mRow.matches()){
                res[supportedPlayer] += entry.getValue();
            }
            Matcher mCol = entry.getKey().matcher(col);
            if(mCol.matches()){
                res[supportedPlayer] += entry.getValue();
            }
            Matcher mLeftDiag = entry.getKey().matcher(leftDiag);
            if(mLeftDiag.matches()){
                res[supportedPlayer] += entry.getValue();
            }
            Matcher mRightDiag = entry.getKey().matcher(rightDiag);
            if(mRightDiag.matches()){
                res[supportedPlayer] += entry.getValue();
            }
        }

        if(logging){
            System.out.println("heuristic:");
            System.out.println(state.toString());
            System.out.print("res[");
            for(int i = 1; i <= numOfPlayers; ++i){
                if(i != supportedPlayer){
                    res[i] = -1 * res[supportedPlayer];
                }
                System.out.print(res[i]+" ");
            }
            System.out.println("]");
        }

        return res;
    }

    private String readRightDiagString() {
        StringBuilder sb = new StringBuilder();

        int x = lastStep.getX(), y = lastStep.getY();
        while(x <= N && y >= 1){
            int cell = state.a[x][y];

            if(cell == supportedPlayer){
                sb.append("x");
            }else {
                sb.append(cell);
            }
            x++;
            y--;
        }
        sb.reverse();

        x = lastStep.getX()-1;
        y = lastStep.getY()+1;
        while(x >= 1 && y <= M){
            int cell = state.a[x][y];

            if(cell == supportedPlayer){
                sb.append("x");
            }else {
                sb.append(cell);
            }
            x--;
            y++;
        }

        return sb.toString();

    }

    private String readLeftDiagString() {
        StringBuilder sb = new StringBuilder();

        int x = lastStep.getX(), y = lastStep.getY();
        while(x >= 1 && y >= 1){
            int cell = state.a[x][y];

            if(cell == supportedPlayer){
                sb.append("x");
            }else {
                sb.append(cell);
            }
            x--;
            y--;
        }
        sb.reverse();

        x = lastStep.getX()+1;
        y = lastStep.getY()+1;
        while(x <= N && y <= M){
            int cell = state.a[x][y];

            if(cell == supportedPlayer){
                sb.append("x");
            }else {
                sb.append(cell);
            }
            x++;
            y++;
        }

        return sb.toString();
    }

    private String readColString() {
        StringBuilder sb = new StringBuilder();

        for(int i = 1; i <= N; ++i){
            int cell = state.a[i][lastStep.getY()];
            if(cell == supportedPlayer){
                sb.append("x");
            }else {
                sb.append(cell);
            }
        }
        return sb.toString();
    }

    private String readRowString() {
        StringBuilder sb = new StringBuilder();

        for(int i = 1; i <= M; ++i){
            int cell = state.a[lastStep.getX()][i];
            if(cell == supportedPlayer){
                sb.append("x");
            }else {
                sb.append(cell);
            }

        }
        return sb.toString();
    }

    public String name() {
        return "PatternHeur";
    }
}
