import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day14 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        System.out.println(part1(arr));

        System.out.println(part2(arr));
    }

    public static int part1(String[] arr) {
        // made board smaller for this one
        char[][] grid = new char[200][200];
        for (char[] row: grid) {
            Arrays.fill(row, '.');
        }
        grid[0][100] = 'X';
        // put input into grid
        for (String s: arr) {
            addline(grid, s, -400);
        }
        // now simulate the sand
        boolean sim = true;
        int ct = 0;
        while (sim) {
            ct++;
            // System.out.print(ct + " ");
            int x = 100, y = 0; // (500,0) -> (500-400,0) -> (100,0)
            boolean falling = true;
            while (y < grid.length - 1 && falling) {
                if (grid[y + 1][x] == '.') {
                    y++;
                } else if (grid[y + 1][x - 1] == '.') {
                    y++;
                    x--;
                } else if (grid[y + 1][x + 1] == '.') {
                    y++;
                    x++;
                } else {
                    // System.out.println("placed #" + ct);
                    grid[y][x] = 'O';
                    falling = false;
                }
            }
            if (y >= grid.length - 1) sim = false;
        }
        /*
        for (char[] row: grid) {
            for (char c: row)
                System.out.print(c);
            System.out.print('\n');
        }
        */
        return ct - 1; // delete the last dropped sand (into void)
    }

    public static int part2(String[] arr) {
        // now we need the enitre board
        char[][] grid = new char[200][1000];
        for (char[] row: grid) {
            Arrays.fill(row, '.');
        }
        grid[0][500] = 'X';
        int height = 0;
        // put input into grid
        for (String s: arr) {
            int h = addline(grid, s, 0);
            if (h > height) height = h;
        }
        height += 2;
        for (int i = 0; i < grid[0].length; i++) {
            grid[height][i] = '#';
        }
        // now simulate the sand
        boolean sim = true;
        int ct = 0;
        while (sim) {
            int x = 500, y = 0; // (500,0) -> (500-400,0) -> (100,0)
            if (grid[y][x] != 'X') break;
            ct++;
            // System.out.print(ct + " ");
            boolean falling = true;
            while (y < grid.length - 1 && falling) {
                if (grid[y + 1][x] == '.') {
                    y++;
                } else if (grid[y + 1][x - 1] == '.') {
                    y++;
                    x--;
                } else if (grid[y + 1][x + 1] == '.') {
                    y++;
                    x++;
                } else {
                    // System.out.println("placed #" + ct);
                    grid[y][x] = 'O';
                    falling = false;
                }
            }
            if (y >= grid.length - 1) sim = false;
        }
        /*
        for (char[] row: grid) {
            for (char c: row)
                System.out.print(c);
            System.out.print('\n');
        }
        */
        return ct; // now we include the sand covering the 'X'
    }

    public static int addline(char[][] grid, String path, int dx) {
        int maxY = 0;
        String[] arr = path.split(" -> ");
        for (int i = 0; i < arr.length - 1; i++) {
            String[] pt1 = arr[i].split(",");
            String[] pt2 = arr[i + 1].split(",");
            int tempY = Math.max(Integer.parseInt(pt1[1]), Integer.parseInt(pt2[1]));
            if (tempY > maxY) maxY = tempY;
            if (pt1[0].equals(pt2[0]) && pt1[1].equals(pt2[1])) {
                grid[Integer.parseInt(pt1[1])][Integer.parseInt(pt1[0])] = '#';
            } else if (pt1[0].equals(pt2[0])) { // x is same
                int xVal = Integer.parseInt(pt1[0]) + dx;
                int a = Integer.parseInt(pt1[1]);
                int b = Integer.parseInt(pt2[1]);
                int x = a > b ? -1 : 1;
                for (int j = a; j != b + x; j += x) {
                    grid[j][xVal] = '#';
                }
            } else { // y is same
                int yVal = Integer.parseInt(pt1[1]);
                int a = Integer.parseInt(pt1[0]) + dx;
                int b = Integer.parseInt(pt2[0]) + dx;
                int x = a > b ? -1 : 1;
                // System.out.println("a:" + a + ", b:" + b + ", x:" + x);
                for (int j = a; j != b + x; j += x) {
                    grid[yVal][j] = '#';
                }
            }
        }
        return maxY;
    }
    
    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
    }
}
