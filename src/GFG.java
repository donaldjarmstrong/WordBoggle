import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by donaldarmstrong on 2/21/17.
 */
public class GFG {
    static boolean DEBUG = false;

    static class BoggleCell {
        String letter;
        List<BoggleCell> related;
        int x;
        int y;

        BoggleCell(String letter, int x, int y) {
            this.letter = letter;
            this.x = x;
            this.y = y;
            this.related = new ArrayList<>();
        }

        @Override
        public String toString() {
            String rel = "relations=";
            for (BoggleCell cell: related) {
                rel = rel.concat(cell.letter + "(" + cell.x + "," + cell.y+ ")");
            }
            return "letter: " + letter + "(" + x + "," + y+ ") " + rel;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader testConfiguration = new BufferedReader(new InputStreamReader(System.in));
        int totalTests = Integer.parseInt(testConfiguration.readLine());
        for (int test = 0; test < totalTests; test++) {
            int numWordsInDict = Integer.parseInt(testConfiguration.readLine());

            String[] wordList = testConfiguration.readLine().split("\\s");
            HashSet<String> dict = new HashSet<>(Arrays.asList(wordList));

            if (DEBUG) {
                // print the dictionary
                for (String word : dict) {
                    System.out.println("word=" + word);
                }
            }

            String[] xy = testConfiguration.readLine().split("\\s");
            int maxX = Integer.parseInt((xy[0]));
            int maxY = Integer.parseInt((xy[1]));

            String[] letters = testConfiguration.readLine().split("\\s");
            List<String> letterList = Arrays.asList(letters);

            Set<String> foundWords = new HashSet<>();

            List<BoggleCell> boggleCells = buildBoggleCube(maxX, maxY, letterList);

            for (BoggleCell cell : boggleCells) {
                findWords(cell, "", new ArrayList<>(), foundWords, dict);
            }

            if (foundWords.isEmpty()) {
                System.out.println("-1");
            } else {
                String collect = foundWords.stream().sorted().collect(Collectors.joining(" "));
                System.out.println(collect);
            }
        }
    }

    private static BoggleCell findBoggleCell(int x, int y, List<BoggleCell> boggleCells) {
        return boggleCells.stream().filter( boggleCell -> boggleCell.x == x && boggleCell.y == y).findFirst().orElse(null);
    }

    private static List<BoggleCell> buildBoggleCube(int maxX, int maxY, List<String> letterList) {
        List<BoggleCell> boggleCells = new ArrayList<>();

        int letterIdx = 0;

        // fill up boggle cells with letters and their X,Y in the rectangle
        for (int curr_x = 0; curr_x < maxX; curr_x++) {
            for (int curr_y = 0; curr_y < maxY; curr_y++) {
                BoggleCell currentCell = new BoggleCell(letterList.get(letterIdx++), curr_x, curr_y);
                boggleCells.add(currentCell);
            }
        }

        // walk the cube
        for (int curr_x = 0; curr_x < maxX; curr_x++) {
            for (int curr_y = 0; curr_y < maxY; curr_y++) {
                BoggleCell currentCell = findBoggleCell(curr_x, curr_y, boggleCells);

                // build the adjacencies
                for (int x = curr_x - 1; x <= (curr_x + 1); x++) {
                    for (int y = curr_y - 1; y <= (curr_y + 1); y++) {
                        if (x >= 0 && y >= 0 && x < maxX && y < maxY) {
                            if (curr_x != x || curr_y != y) {
                                // find the cell and relate it to the current cell
                                currentCell.related.add(findBoggleCell(x, y, boggleCells));
                            }
                        }
                    }
                }
            }
        }

        if (DEBUG) {
            // print the boggle board
            for (BoggleCell cell : boggleCells) {
                System.out.println("board cell=" + cell);
            }
        }

        return boggleCells;
    }

    private static void findWords(BoggleCell currentCell, String wordSoFar, List<BoggleCell> positionsAlreadyVisited, Set<String> foundWords, Set<String> dict) {
        // if already visited, throw it away
        if (positionsAlreadyVisited.contains(currentCell)) {
            if (DEBUG) {
                System.out.println("already visited=" + currentCell);
            }
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
        if (dict.stream().noneMatch((word) -> word.startsWith(possibleWord))) {
            return;
        }

        if (DEBUG) {
            System.out.println("possible word=" + possibleWord + " " + dict.contains(possibleWord));
        }

        // is current word in dictionary
        if (dict.contains(possibleWord)) {
            foundWords.add(possibleWord);
        }

        // for each connected cell
        for (BoggleCell boggleCell : currentCell.related) {
            if (DEBUG) {
                System.out.println("walking from " + currentCell + " to " + boggleCell);
                for (BoggleCell visited: positionsAlreadyVisited) {
                    System.out.println("visited=" + visited);
                }
            }

            // my visited positions
            List<BoggleCell> myVisitedPositions = new ArrayList<>(positionsAlreadyVisited);
            findWords(boggleCell, possibleWord, myVisitedPositions, foundWords, dict);
        }
    }
}
