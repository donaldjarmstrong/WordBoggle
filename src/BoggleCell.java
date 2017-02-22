import java.util.ArrayList;
import java.util.List;

/**
 * Created by donaldarmstrong on 2/21/17.
 */
public class BoggleCell {
    String letter;
    List<BoggleCell> related;
    int x;
    int y;

    BoggleCell(String letter) {
        this.letter = letter;
        this.related = new ArrayList<>();
    }

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
            rel = rel.concat(cell.letter);
        }
        return "letter: " + letter + " x=" + x + " y=" + y + rel;
    }

    //    void addRelated(BoggleCell cell) {
//        this.related.add(cell);
//    }
}
