import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day9 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        System.out.println(part1(arr));

        System.out.println(part2(arr));
    }

    public static int part1(String[] rows) {
        Set<String> tpositions = new HashSet<>();
        int hx = 0, hy = 0, tx = 0, ty = 0;
        tpositions.add(tx + "," + ty);
        for (String s: rows) {
            char dir = s.charAt(0);
            int dx = 0, dy = 0;
            if (dir == 'U') dy = -1;
            else if (dir == 'D') dy = 1;
            else if (dir == 'L') dx = -1;
            else dx = 1;

            int x = Integer.parseInt(s.substring(2));
            for (int i = 0; i < x; i++) {
                hy += dy;
                hx += dx;
                // update tail
                int distX = Math.abs(hx - tx);
                int distY = Math.abs(hy - ty);
                if (distX > 1 && distY >= 1 || distX >= 1 && distY > 1) {
                    // diagonal move
                    // head above the tail, move tail up
                    if (hy < ty) ty--;
                    else ty++;
                    // head left of tail, move tail left
                    if (hx < tx) tx--;
                    else tx++;
                } else if (distX > 1 || distY > 1) {
                    // horizontal or vertical move
                    ty += dy;
                    tx += dx;
                } // else it is touching, do nothing
                tpositions.add(tx + "," + ty);
            }
        }
        // System.out.println(tpositions);
        return tpositions.size();
    }

    public static int part2(String[] rows) {
        Set<String> tpos = new HashSet<String>();
        int[][] pos = new int[10][2];
        tpos.add(pos[9][0] + "," + pos[9][1]);
        for (String s: rows) {
            char dir = s.charAt(0);
            int dx = 0, dy = 0;
            if (dir == 'U') dy = -1;
            else if (dir == 'D') dy = 1;
            else if (dir == 'L') dx = -1;
            else dx = 1;
            int x = Integer.parseInt(s.substring(2));
            for (int i = 0; i < x; i++) {
                pos[0][0] += dx;
                pos[0][1] += dy;
                // update segments
                for (int j = 1; j < 10; j++) {
                    int distX = Math.abs(pos[j-1][0] - pos[j][0]);
                    int distY = Math.abs(pos[j-1][1] - pos[j][1]);
                    if (distX > 1 && distY >= 1 || distX >= 1 && distY > 1) {
                        // diagonal move
                        // head above the tail, move tail up
                        if (pos[j-1][1] < pos[j][1]) pos[j][1]--;
                        else pos[j][1]++;
                        // head left of tail, move tail left
                        if (pos[j-1][0] < pos[j][0]) pos[j][0]--;
                        else pos[j][0]++;
                    } else if (distX > 1) {
                        // horizontal move
                        if (pos[j-1][0] < pos[j][0]) pos[j][0]--;
                        else pos[j][0]++;
                    } else if (distY > 1) {
                        // vertical move
                        if (pos[j-1][1] < pos[j][1]) pos[j][1]--;
                        else pos[j][1]++;
                    } // else it is touching, do nothing
                }
                // System.out.println(tx + "," + ty);
                tpos.add(pos[9][0] + "," + pos[9][1]);
            }
        }
        return tpos.size();
    }
    
    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
    }

}