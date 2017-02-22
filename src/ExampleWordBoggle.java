import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by donaldarmstrong on 2/21/17.
 */
public class ExampleWordBoggle {
    static public List<String> dictionary = Arrays.asList("GEEKS", "FOR", "QUIZ", "GO");
    static String[] letterList = new String[]{"G", "I", "Z", "U", "E", "K", "Q", "S", "E"};
    static BoggleCell[] boggleCells;

    public static void main(String[] args) {
        List<String> foundWords = new ArrayList<>();

        // create the cells
        BoggleCell _0 = new BoggleCell("G");
        BoggleCell _1 = new BoggleCell("I");
        BoggleCell _2 = new BoggleCell("Z");
        BoggleCell _3 = new BoggleCell("U");
        BoggleCell _4 = new BoggleCell("E");
        BoggleCell _5 = new BoggleCell("K");
        BoggleCell _6 = new BoggleCell("Q");
        BoggleCell _7 = new BoggleCell("S");
        BoggleCell _8 = new BoggleCell("E");

        // relate the cells
        _0.related = Arrays.asList(_1, _3, _4);
        _1.related = Arrays.asList(_0, _3, _4, _5, _2);
        _2.related = Arrays.asList(_1, _4, _5);
        _3.related = Arrays.asList(_0, _1, _4, _7, _6);
        _4.related = Arrays.asList(_0, _1, _2, _3, _5, _6, _7, _8);
        _5.related = Arrays.asList(_2, _1, _4, _7, _8);
        _6.related = Arrays.asList(_3, _4, _7);
        _7.related = Arrays.asList(_6, _3, _4, _5, _8);
        _8.related = Arrays.asList(_5, _4, _7);

        // create the boggle board
        boggleCells = new BoggleCell[]{_0, _1, _2, _3, _4, _5, _6, _7, _8};

        for (BoggleCell cell : boggleCells) {
            findWords(cell, "", new ArrayList<>(), foundWords);
        }

        foundWords.stream().sorted().collect(Collectors.toList()).stream().forEach((word) -> System.out.println(word));
    }

    static void findWords(BoggleCell currentCell, String wordSoFar, List<BoggleCell> positionsAlreadyVisited, List<String> foundWords) {
        // if already visited, throw it away
        if (positionsAlreadyVisited.contains(currentCell)) {
            return;
        }

        // every iteration, I mark myself as visited
        positionsAlreadyVisited.add(currentCell);

        // lookup letter in index
        String letterLookup = currentCell.letter;

        // add current letter to current word
        final String possibleWord = wordSoFar.concat(letterLookup);

        /*
        OPTIMIZATION
            is there a dictionary word that starts with our possible word? if so, we can keep going on this path
         */
        if (!dictionary.stream().anyMatch((word) -> word.startsWith(possibleWord))) {
            return;
        }

        // is current word in dictionary
        if (dictionary.contains(possibleWord)) {
            foundWords.add(possibleWord);
            return;
        }

        // for each connected cell
        for (BoggleCell boggleCell : currentCell.related) {
            // my visited positions
            List myVisitedPositions = new ArrayList(positionsAlreadyVisited);
            findWords(boggleCell, possibleWord, myVisitedPositions, foundWords);
        }
    }
}
