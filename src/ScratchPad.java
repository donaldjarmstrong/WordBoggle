import java.util.ArrayList;
import java.util.List;

/**
 * Created by donaldarmstrong on 2/21/17.
 */
public class ScratchPad {
    static int MAX_X = 3;
    static int MAX_Y = 3;

    static List<BoggleCell> boggleCells = new ArrayList<>();
    static String[] letterList = new String[]{"G", "I", "Z", "U", "E", "K", "Q", "S", "E"};

    static BoggleCell findBoggleCell(int x, int y) {
        return boggleCells.stream().filter( boggleCell -> boggleCell.x == x && boggleCell.y == y).findFirst().get();
    }

    public static void main(String[] args) {
        buildBoggleCube();

        for (BoggleCell cell: boggleCells) {
            System.out.println(cell);
        }
    }

    private static void buildBoggleCube() {
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
                System.out.println("current cell x=" + curr_x + " y=" + curr_y);
                BoggleCell currentCell = findBoggleCell(curr_x, curr_y);

                // build the adjacencies
                for (int x = curr_x - 1; x <= (curr_x + 1); x++) {
                    for (int y = curr_y - 1; y <= (curr_y + 1); y++) {
                        if (x >= 0 && y >= 0 && x < MAX_X && y < MAX_Y) {
                            if (curr_x == x && curr_y == y) {
                                // do not relate the current cell with itself
                            } else {
                                // find the cell and relate it to the current cell
                                currentCell.related.add(findBoggleCell(x, y));
                            }
                        }
                    }
                }
            }
        }
    }
}
