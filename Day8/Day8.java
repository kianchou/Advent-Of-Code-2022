import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day8 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("inputs.txt"));
        int[][] grid = gridParseI(input);

        System.out.println(part1(grid));

        System.out.println(part2(grid));
    }

    public static int part1(int[][] grid) {
        HashSet<String> visible = new HashSet<String>();

        int[] row = new int[grid[0].length];
        Arrays.fill(row, -1);
        int i = 0;
        // check down
        while (i < grid.length) {
            for (int j = 0; j < row.length; j++) {
                if (grid[i][j] > row[j]) {
                    visible.add(i + "," + j);
                    row[j] = grid[i][j];
                }
            }
            i++;
        }

        Arrays.fill(row, -1);
        i = grid.length - 1;
        // check up
        while (i >= 0) {
            for (int j = 0; j < row.length; j++) {
                if (grid[i][j] > row[j]) {
                    visible.add(i + "," + j);
                    row[j] = grid[i][j];
                }
            }
            i--;
        }

        int[] col = new int[grid.length];
        Arrays.fill(col, -1);
        i = 0;
        // check right
        while (i < grid[0].length) {
            for (int j = 0; j < col.length; j++) {
                if (grid[j][i] > col[j]) {
                    visible.add(j + "," + i);
                    col[j] = grid[j][i];
                }
            }
            i++;
        }
        Arrays.fill(col, -1);
        i = grid[0].length - 1;
        // check left
        while (i >= 0) {
            for (int j = 0; j < col.length; j++) {
                if (grid[j][i] > col[j]) {
                    visible.add(j + "," + i);
                    col[j] = grid[j][i];
                }
            }
            i--;
        }
        // System.out.println(visible);
        /*
         *  X
         */
        return visible.size();
    }

    public static int part2(int[][] grid) {
        int max = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                int ct = 1;
                ct *= check(grid, j, i, 1, 0);
                ct *= check(grid, j, i, -1, 0);
                ct *= check(grid, j, i, 0, 1);
                ct *= check(grid, j, i, 0, -1);
                if (ct > max) max = ct;
            }
        }
        return max;
    }

    public static int check(int[][] grid, int x, int y, int dx, int dy) {
        if (dx == 0) {
            if (y == 0 && dy == -1) return 0;
            if (y == grid.length && dy == 1) return 0;
            int pt = y + dy;
            while (pt > 0 && pt < grid.length - 1 && grid[pt][x] < grid[y][x]) {
                pt += dy;
            }
            return Math.abs(pt - y);
        } else {
            if (x == 0 && dx == -1) return 0;
            if (x == grid[0].length && dx == 1) return 0;
            int pt = x + dx;
            while (pt > 0 && pt < grid[0].length - 1 && grid[y][pt] < grid[y][x]) {
                pt += dx;
            }
            return Math.abs(pt - x);
        }
    }
    
    // Parse an input string into an int matrix
    public static int[][] gridParseI(String input) {
        String[] rows = input.split("\\r?\\n");
        int[][] result = new int[rows.length][];
        for (int i = 0; i < result.length; i++) {
            String[] r = rows[i].split("");
            result[i] = new int[r.length];
            for (int j = 0; j < r.length; j++) {
                result[i][j] = Integer.parseInt(r[j]);
            }
        }
        return result; 
    }
}