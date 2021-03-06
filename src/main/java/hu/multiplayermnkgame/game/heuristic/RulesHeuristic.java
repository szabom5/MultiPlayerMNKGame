package hu.multiplayermnkgame.game.heuristic;

import hu.multiplayermnkgame.game.gamerepresentation.GameAttributes;
import hu.multiplayermnkgame.game.gamerepresentation.GameState;
import hu.multiplayermnkgame.game.statespacerepresentation.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This implementation of a heuristic function is based on a set of rules.
 * <p>
 * Rules are in decreasing order of their precedence.
 * 1. If the supported player has a winning move, take it.
 * 2. If the opponent may have a winning move (K-1 long), block it on one side.
 * 3. Place in the position such as the supported player may have more marks next to each other in a line.
 * <p>
 * The points for achieving these states are: 500, 100, and
 * 2^i, where i is the number of marks already placed in a line.
 * <p>
 * The rules are represented by matching patterns to the lines of the board.
 * The list of Patterns must contain regular expressions for all players,
 * because these Patterns are constructed only once, and not every time
 * the heuristic function is called to evaluate a game state.
 * Of course when calculating a value only a subset ot these Patterns will be used,
 * because the function takes only one player into consideration.
 */
public class RulesHeuristic implements Heuristic {
    private GameAttributes attributes;

    private State state;

    private int supportedPlayer;

    private List<Pattern> patternFirstRule;

    private List<List<Pattern>> patternSecondRule;

    private List<Map<Pattern, Double>> patternThirdRule;

    private double result;

    public void initialize(GameAttributes attributes) {
        this.attributes = attributes;

        patternFirstRule = new ArrayList<>();
        patternSecondRule = new ArrayList<>();
        patternThirdRule = new ArrayList<>();

        initFirstRulePatterns();

        initSecondRulePatterns();

        initThirdRulePatterns();
    }

    /**
     * Generates Patterns for each player, creating a regular expression for a winning line.
     * Example regex: 1{5}
     * The "001111120" is a winning line for player 1
     */
    private void initFirstRulePatterns() {
        for (int i = 1; i <= attributes.getNumberOfPlayers(); ++i) {
            Pattern pattern = Pattern.compile( i + "{" + attributes.getK() + "}");
            patternFirstRule.add(pattern);
        }
    }

    /**
     * Generates Patterns for each player, creating a regular expression for a line of blocking the opponent.
     * For each player there is a list of Patterns containing all the possible opponents that the player could block.
     * Regex: [^E]E{k-1}J|^E{k-1}J|JE{k-1}[^E]
     * Example regex: [^2]2{4}1|^2{4}1|12{4}[^2]
     * The "002222100" or "0012222000" is a line where player 1 is blocking the second player from one side.
     */
    private void initSecondRulePatterns() {
        for (int i = 1; i <= attributes.getNumberOfPlayers(); ++i) {

            List<Pattern> blockOtherPlayerPatterns = new ArrayList<>();

            for (int j = 1; j <= attributes.getNumberOfPlayers(); ++j) {
                if (j == i) {
                    continue;
                }

                Pattern pattern =
                        Pattern.compile("[^" + j + "]" + j + "{" + (attributes.getK() - 1) + "}" + i +
                                "|" + "^" + j + "{" + (attributes.getK() - 1) + "}" + i +
                                "|" + i + j + "{" + (attributes.getK() - 1) + "}[^" + j + "]");

                blockOtherPlayerPatterns.add(pattern);
            }

            patternSecondRule.add(blockOtherPlayerPatterns);
        }
    }

    /**
     * Generates Patterns for each player, creating a regular expression for every possible number of marks that can be
     * placed next to each other, and assigning a value to it. This value is calculated with the exact number of
     * marks counted: 2^i, where i is this number.
     * Regex 1: 0{k-i}J{i}(0{1,max(1,k-i-1)}[^0]|[^0J])
     * Regex 2: ([^0]0{1,max(1,k-i-1)}|[^0J])J{i}0{k-i}
     * Regex 3: 0{k-i}J{i}0{k-i}
     * Example regex 1 : 0{2}1{3}(0{1,3}[^0]|[^01])
     * Example regex 2 : ([^0]0{1,3}|[^01])1{3}0{2}
     * Example regex 3 : 0{2}1{3}0{2}
     * The "00001112" or "221110000" is a line where player 1 has placed 3 marks already,
     * but can continue the line in only one direction. Value: 2^3 = 8
     * However the "0011100" or "00111000" is a line where player 1 has placed 3 marks already,
     * and can continue trying to create a winning line in both ways. Value: 2^3 *2 = 16
     */
    private void initThirdRulePatterns() {
        int K = attributes.getK();

        for (int p = 1; p <= attributes.getNumberOfPlayers(); ++p) {

            Map<Pattern, Double> patterns = new HashMap<>();
            for (int i = 1; i < K; ++i) {

                Pattern patterns1side1 =
                        Pattern.compile("0{" + (K - i) + "}" + p + "{" + i + "}(0{1," + Math.max((K - i - 1), 1) + "}[^0]|[^0" + p + "])");
                patterns.put(patterns1side1, Math.pow(2, i));

                Pattern patterns1side2 =
                        Pattern.compile("([^0]0{1," + Math.max((K - i - 1), 1) + "}|[^0" + p + "])" + p + "{" + i + "}0{" + (K - i) + "}");
                patterns.put(patterns1side2, Math.pow(2, i));

                Pattern pattern2 =
                        Pattern.compile("0{" + (K - i) + "}" + p + "{" + i + "}0{" + (K - i) + "}");
                patterns.put(pattern2, 2 * Math.pow(2, i));
            }
            patternThirdRule.add(patterns);
        }
    }

    @Override
    public double heuristic(GameState gameState, int player) {
        state = gameState.state;

        supportedPlayer = player;

        result = 0.0;

        checkTable();

        return result;
    }

    private void checkTable() {
        checkRows();
        checkColumns();
        checkDiagonals();
    }

    private void checkRows() {
        for (int i = 1; i <= attributes.getM(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 1; j <= attributes.getN(); j++) {
                sb.append(state.a[i][j]);
            }
            matchPatternToString(sb.toString());
        }
    }

    private void checkColumns() {
        for (int j = 1; j <= attributes.getN(); j++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= attributes.getM(); i++) {
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
        checkAboveRightMainDiagonal();
    }

    private void checkLeftMainDiagonal() {
        // Left main diagonal line
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= Math.min(attributes.getM(), attributes.getN()); i++) {
            sb.append(state.a[i][i]);
        }
        matchPatternToString(sb.toString());
    }

    private void checkUnderLeftMainDiagonal() {
        //Under the left main diagonal line
        for (int i = 2; i <= attributes.getM() - attributes.getK() + 1; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 1, k = i; j <= Math.min(attributes.getN(), attributes.getM() - i + 1); j++, k++) {
                sb.append(state.a[k][j]);
            }
            matchPatternToString(sb.toString());
        }
    }

    private void checkAboveLeftMainDiagonal() {
        //Above the left main diagonal line
        for (int j = 2; j <= attributes.getN() - attributes.getK() + 1; j++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1, k = j; i <= Math.min(attributes.getM(), attributes.getN() - j + 1); i++, k++) {
                sb.append(state.a[i][k]);
            }
            matchPatternToString(sb.toString());
        }
    }

    private void checkRightMainDiagonal() {
        StringBuilder sb = new StringBuilder();

        //Right main diagonal line
        for (int i = 1; i <= Math.min(attributes.getM(), attributes.getN()); i++) {
            //j = n - i +1;
            sb.append(state.a[i][attributes.getN() - i + 1]);
        }
        matchPatternToString(sb.toString());
    }

    private void checkUnderRightMainDiagonal() {
        //Under the right main diagonal line
        for (int i = 2; i <= attributes.getM() - attributes.getK() + 1; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = attributes.getN(), k = i; j >= Math.max(1, attributes.getN() - attributes.getM() + i); j--, k++) {
                sb.append(state.a[k][j]);
            }
            matchPatternToString(sb.toString());
        }
    }

    private void checkAboveRightMainDiagonal() {
        //Above the right main diagonal line
        for (int j = attributes.getN() - 1; j >= attributes.getK(); j--) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1, k = j; k >= 1 && i <= attributes.getM(); i++, k--) {
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
        Matcher m = patternFirstRule.get(supportedPlayer - 1).matcher(s);
        if (m.find()) {
            result += 10000;
        }
    }

    private void matchSecondRuleToString(String s) {
        for (int i = 0; i < patternSecondRule.get(supportedPlayer - 1).size(); ++i) {
            Matcher m = patternSecondRule.get(supportedPlayer - 1).get(i).matcher(s);
            if (m.find()) {
                result += 1000;
            }
        }
    }

    private void matchThirdRuleToString(String s) {
        for (Map.Entry<Pattern, Double> entry : patternThirdRule.get(supportedPlayer - 1).entrySet()) {
            Matcher m = entry.getKey().matcher(s);
            if (m.find()) {
                result += entry.getValue();
            }
        }
    }

    @Override
    public String name() {
        return "Rules Heuristic";
    }
}
