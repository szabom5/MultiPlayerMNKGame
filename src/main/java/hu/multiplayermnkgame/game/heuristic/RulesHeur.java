package hu.multiplayermnkgame.game.heuristic;

import hu.multiplayermnkgame.game.gamerepresentation.GameState;
import hu.multiplayermnkgame.game.statespacerepresentation.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hu.multiplayermnkgame.game.gameplay.GameLoop.*;

/**
 * This implementation of a heuristic function is based on a so-called rule-based
 * evaluation.
 * Rules are in decreasing order of their precedence.
 * 1. If the supported player has a winning move, take it.
 * 2. If the opponent may have a winning move (K-1 long), block it on one side.
 * 3. Place in the position such as the supported player may win in the most number of possible ways.
 *
 * the points for achieving the rule are: 500 , 100 , 2^i
 */
public class RulesHeur {

    private State state;

    // for all players, each index represents the index of the player
    private final List<Pattern> patternFirstRule;

    private final List<List<Pattern>> patternSecondRule;

    private final List<Map<Pattern, Double>> patternThirdRule;

    private int[] result;

    public RulesHeur() {
        patternFirstRule = new ArrayList<>();
        patternSecondRule = new ArrayList<>();
        patternThirdRule = new ArrayList<>();

        initFirstRulePatterns();

        initSecondRulePatterns();

        initThirdRulePatterns();
    }

    private void initThirdRulePatterns() {
        for(int p = 1; p <= numberOfPlayers; ++p){

            Map<Pattern, Double>  patterns = new HashMap<>();
            for(int i = 1; i <= K; ++i){

                //Same as in PatternHeur
                Pattern patterns1side1 = Pattern.compile(".*?0{"+(K-i)+"}"+p+"{"+i+"}0{0,"+Math.max((K-i-1),0)+"}[^0"+p+"]*?");
                patterns.put(patterns1side1, Math.pow(2,i));

                Pattern patterns1side2 = Pattern.compile("[^0"+p+"]*?0{0,"+Math.max((K-i-1),0)+"}"+p+"{"+i+"}0{"+(K-i)+"}.*?");
                patterns.put(patterns1side2, Math.pow(2,i));

                Pattern pattern2 = Pattern.compile(".*?0{"+(K-i)+"}"+p+"{"+i+"}0{"+(K-i)+"}.*?");
                patterns.put(pattern2, 2*Math.pow(2,i));
            }
            patternThirdRule.add(patterns);
        }
    }

    private void initSecondRulePatterns() {
        for(int i = 1; i <= numberOfPlayers; ++i){
            List<Pattern> blockOtherPlayerPatterns = new ArrayList<>();
            for(int j = 1; j <= numberOfPlayers; ++j){
                if(j == i){
                    continue;
                }

                //Example: 0022100 or 00122000 blocking the second player from one side, player 1
                Pattern pattern = Pattern.compile(".*?[^"+j+"]?"+j+"{"+(K-1)+"}"+i+".*?|.*?"+i+j+"{"+(K-1)+"}[^"+j+"]?.*?");
                blockOtherPlayerPatterns.add(pattern);
            }
            patternSecondRule.add(blockOtherPlayerPatterns);
        }
    }

    private void initFirstRulePatterns() {
        for(int i = 1; i <= numberOfPlayers; ++i){
            // Example: 0011120 winning line for player 1
            Pattern pattern = Pattern.compile(".*?"+i+"{"+K+"}.*?");
            patternFirstRule.add(pattern);
        }
    }

    public int[] heuristic(GameState gameState) {

        state = gameState.state;

        result = new int[numberOfPlayers +1];

        checkTable();

        return result;
    }

    private void checkTable(){
        checkRows();
        checkColumns();
        checkDiagonals();
    }

    private void checkRows() {
        for(int i = 1; i <= N;i++){
            StringBuilder sb = new StringBuilder();
            for(int j = 1; j<= M;j++){
                sb.append(state.a[i][j]);
            }
            matchPatternToString(sb.toString());
        }
    }

    private void checkColumns() {
        for(int j = 1; j<= M;j++){
            StringBuilder sb = new StringBuilder();
            for(int i = 1; i <= N;i++){
                sb.append(state.a[i][j]);
            }
            matchPatternToString(sb.toString());
        }
    }

    private void checkDiagonals() {
        checkLeftMainDiagonal();
        checkUnderLeftMainDiagonal();
        checkAboveLeftMainDiagonal();
        checkRightMainDiagonal();
        checkUnderRightMainDiagonal();
        checkAboveRigthMainDiagonal();
    }

    private void checkLeftMainDiagonal() {
        // Left main diagonal line
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= Math.min(N, M); i++ ){
            sb.append(state.a[i][i]);
        }
        matchPatternToString(sb.toString());
    }

    private void checkUnderLeftMainDiagonal() {
        //Under the left main diagonal line
        for(int i = 2; i <= N - K + 1; i++ ){
            StringBuilder sb = new StringBuilder();
            for(int j = 1, k = i; j <= Math.min(M,N - i + 1); j++, k++){
                sb.append(state.a[k][j]);
            }
            matchPatternToString(sb.toString());
        }
    }

    private void checkAboveLeftMainDiagonal() {
        //Above the left main diagonal line
        for(int j = 2; j <= M - K + 1; j++ ){
            StringBuilder sb = new StringBuilder();
            for(int i = 1, k = j; i <= Math.min(N, M - j + 1); i++, k++){
                sb.append(state.a[i][k]);
            }
            matchPatternToString(sb.toString());
        }
    }

    private void checkRightMainDiagonal() {
        StringBuilder sb = new StringBuilder();

        //Right main diagonal line
        for(int i = 1; i <= Math.min(N,M); i++ ){
            //j = m - i +1;
            sb.append(state.a[i][M - i +1]);
        }
        matchPatternToString(sb.toString());
    }

    private void checkUnderRightMainDiagonal() {
        //Under the right main diagonal line
        for(int i = 2; i <= N - K + 1; i++ ){
            StringBuilder sb = new StringBuilder();
            for(int j = M, k = i; j <= Math.max(1,M - N + i); j--, k++){
                sb.append(state.a[k][j]);
            }
            matchPatternToString(sb.toString());
        }
    }

    private void checkAboveRigthMainDiagonal() {
        //Above the right main diagonal line
        for(int j = M - 1; j <= K; j-- ){
            StringBuilder sb = new StringBuilder();
            for(int i = 1, k = j; i <= j; i++, k--){
                sb.append(state.a[i][k]);
            }
            matchPatternToString(sb.toString());
        }
    }

    private void matchPatternToString(String s) {
        matchFirstRuleToString(s);

        matchSecondRuleToString(s);

        matchThirdRuleToString(s);
    }

    private void matchFirstRuleToString(String s) {
        for(int i = 0; i < patternFirstRule.size(); ++i){
            Matcher m = patternFirstRule.get(i).matcher(s);
            if(m.matches()){
                result[i+1] += 500;
            }
        }
    }

    private void matchSecondRuleToString(String s) {
        for(int i = 0; i < patternSecondRule.size(); ++i){
            for(int j = 0; j < patternSecondRule.get(i).size(); ++j){
                Matcher m = patternSecondRule.get(i).get(j).matcher(s);
                if(m.matches()){
                    result[i+1] += 100;
                }
            }
        }
    }

    private void matchThirdRuleToString(String s) {
        for(int i = 0; i < patternThirdRule.size(); ++i){
            for(Map.Entry<Pattern, Double> entry : patternThirdRule.get(i).entrySet()) {
                Matcher m = entry.getKey().matcher(s);
                if (m.matches()) {
                    result[i+1] += entry.getValue();
                }
            }
        }
    }

    public String name() {
        return "RulesHeur";
    }
}
