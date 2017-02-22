import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by donaldarmstrong on 2/21/17.
 */
public class SubmittedWordBoggle {
//    static public List<String> dictionary = Arrays.asList("GEEKS", "FOR", "QUIZ", "GO");
//    static String[] letterList = new String[]{"G", "I", "Z", "U", "E", "K", "Q", "S", "E"};
//    static int MAX_X = 3;
//    static int MAX_Y = 3;
    static List<String> dictionary = Arrays.asList("db", "bcd");
    static String[] letterList = new String[] { "d", "d", "b", "f", "e", "c", "b", "c", "d", "c"};
    static int MAX_X = 5;
    static int MAX_Y = 2;

    public static void main(String[] args) {
        Set<String> foundWords = new HashSet<>();

        List<BoggleCell> boggleCells = buildBoggleCube();

        for (BoggleCell cell : boggleCells) {
            findWords(cell, "", new ArrayList<>(), foundWords);
        }

        foundWords.stream().sorted().collect(Collectors.toList()).stream().forEach((word) -> System.out.println(word));
    }

    static BoggleCell findBoggleCell(int x, int y, List<BoggleCell> boggleCells) {
        return boggleCells.stream().filter( boggleCell -> boggleCell.x == x && boggleCell.y == y).findFirst().get();
    }

    static List<BoggleCell> buildBoggleCube() {
        List<BoggleCell> boggleCells = new ArrayList<>();

        int letterIdx = 0;

        // fill up boggle cells with letters and their X,Y in the rectangle
        for (int curr_x = 0; curr_x < MAX_X; curr_x++) {
            for (int curr_y = 0; curr_y < MAX_Y; curr_y++) {
                BoggleCell currentCell = new BoggleCell(letterList[letterIdx++], curr_x, curr_y);
                boggleCells.add(currentCell);
            }
        }

        // walk the cube
        for (int curr_x = 0; curr_x < MAX_X; curr_x++) {
            for (int curr_y = 0; curr_y < MAX_Y; curr_y++) {
                BoggleCell currentCell = findBoggleCell(curr_x, curr_y, boggleCells);

                // build the adjacencies
                for (int x = curr_x - 1; x <= (curr_x + 1); x++) {
                    for (int y = curr_y - 1; y <= (curr_y + 1); y++) {
                        if (x >= 0 && y >= 0 && x < MAX_X && y < MAX_Y) {
                            if (curr_x == x && curr_y == y) {
                                // do not relate the current cell with itself
                            } else {
                                // find the cell and relate it to the current cell
                                currentCell.related.add(findBoggleCell(x, y, boggleCells));
                            }
                        }
                    }
                }
            }
        }

        return boggleCells;
    }

    static void findWords(BoggleCell currentCell, String wordSoFar, List<BoggleCell> positionsAlreadyVisited, Set<String> foundWords) {
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
