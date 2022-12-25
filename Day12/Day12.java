import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day12 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[][] grid = gridParseS(input);

        int sX = 0, sY = 0;
        for (int i = 0; i < grid.length; i++) {
            if (grid[i][0].equals("S")) {
                sY = i;
                break;
            }
        }

        int pt1 = part1(grid, sY, sX);

        System.out.println(pt1);

        grid = gridParseS(input);

        int min = pt1;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j].equals("a")) {
                    // try
                    int x = part1(grid, i, j);
                    if (x < min && x != 0) min = x;
                    grid = gridParseS(input);
                }
            }
        }

        System.out.println(min);
        // System.out.println(part2(arr));
    }

    public static int part1(String[][] grid, int sY, int sX) {
        int[] dY = {-1, 1, 0, 0};
        int[] dX = {0, 0, -1, 1};

        // System.out.println("Start at " + sY + "," + sX);
        Deque<String> queue = new ArrayDeque<String>();
        // FORMAT: y x distance
        queue.offer("` " + sY + " " + sX + " 0");
        grid[sY][sX] = "~";

        while (!queue.isEmpty()) {
            String[] curr = queue.poll().split(" ");
            // try 4 sides
            char c = curr[0].charAt(0);
            int y = Integer.parseInt(curr[1]);
            int x = Integer.parseInt(curr[2]);
            int d = Integer.parseInt(curr[3]);
            for (int i = 0; i < 4; i++) {
                int cY = y + dY[i];
                int cX = x + dX[i];
                if (cY >= 0 && cY < grid.length && cX >= 0 && cX < grid[y].length) {
                    char getChar = grid[cY][cX].charAt(0);
                    if (getChar == '~') continue;
                    if (getChar == 'E') {
                        if (c == 'y' || c == 'z') return d + 1;
                        continue;
                    }
                    if (getChar - c <= 1) {
                        // System.out.println(c + " > " + grid[cY][cX]);
                        queue.offer(grid[cY][cX] + " " + cY + " " + cX + " " + (d + 1));
                        grid[cY][cX] = "~";
                    }
                }
            }
        }
        
        return 0;
    }
    
    // Parse a String input into a string matrix
    public static String[][] gridParseS(String input) {
        String[] rows = input.split("\\r?\\n");
        String[][] result = new String[rows.length][];
        for (int i = 0; i < result.length; i++) {
            result[i] = rows[i].split("");
        }
        return result; 
    }
}
