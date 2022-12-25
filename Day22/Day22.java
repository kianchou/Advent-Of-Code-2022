import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day22 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));

        // System.out.println(Arrays.toString(input.split("\\r?\\n\\r?\\n")));

        System.out.println(part1(input.split("\\r?\\n\\r?\\n")));

        System.out.println(part2(input.split("\\r?\\n\\r?\\n")));
    }

    public static int part1(String[] input) {
        int len = 0;
        String[] inputMap = input[0].split("\\r?\\n");
        for (String s: inputMap) 
            if (s.length() > len) 
                len = s.length();

        char[][] map = new char[inputMap.length][len];

        for (int i = 0; i < inputMap.length; i++) {
            for (int j = 0; j < inputMap[i].length(); j++) {
                if (inputMap[i].charAt(j) == '.' || inputMap[i].charAt(j) == '#') 
                    map[i][j] = inputMap[i].charAt(j);
            }
        }
        
        int[][] rowBound = new int[map.length][2];
        int[][] colBound = new int[map[0].length][2];

        // find the bounds for the rows and cols
        for (int row = 0; row < rowBound.length; row++) {
            // find the first non-space
            int start = 0;
            while (map[row][start] == '\0') start++;
            
            // finds 1 after last nonspace
            int end = start;
            while (end + 1 < map[row].length && map[row][end + 1] != '\0') end++;

            rowBound[row][0] = start;
            rowBound[row][1] = end;
        }
        for (int col = 0; col < colBound.length; col++) {
            // find the first non-space
            int start = 0;
            while (map[start][col] == '\0') start++;
            
            // finds 1 after last nonspace
            int end = start;
            while (end + 1 < map.length && map[end + 1][col] != '\0') end++;

            colBound[col][0] = start;
            colBound[col][1] = end;
        }
        
        // facing: 0 - >, 1 - v, 2 - <, 3 - ^
        int[] dx = new int[]{1, 0, -1, 0};
        int[] dy = new int[]{0, 1, 0, -1};
        int facing = 0, y = 0, x = rowBound[0][0];

        String[] directions = seperate(input[1]);

        for (String s: directions) {
            // check if its a direction
            if (s.equals("R")) {
                facing = (facing + 1) % 4;
                continue;
            }
            if (s.equals("L")) {
                facing = (facing + 4 - 1) % 4;
                continue;
            }

            // System.out.printf("from %d,%d", x, y);

            // move the number of steps
            int n = stoi(s);
            for (int i = 0; i < n; i++) {
                // find next position
                int nx = x + dx[facing];
                if (dx[facing] < 0 && nx < rowBound[y][0]) nx = rowBound[y][1];
                else if (dx[facing] > 0 && nx > rowBound[y][1]) nx = rowBound[y][0];
                    
                int ny = y + dy[facing];
                if (dy[facing] < 0 && ny < colBound[x][0]) ny = colBound[x][1];
                else if (dy[facing] > 0 && ny > colBound[x][1]) ny = colBound[x][0];

                // stop if wall is hit
                if (map[ny][nx] == '#') break;

                // update x and y otherwise
                x = nx;
                y = ny;
            }

            // System.out.printf(" to %d,%d\n", x, y);

        }

        // System.out.println(y - colBound[x][0] + 1);

        return (1000 * (y + 1)) + (4 * (x + 1)) + facing;
    }

    public static int part2(String[] input) {
        int len = 0;
        String[] inputMap = input[0].split("\\r?\\n");
        for (String s: inputMap) 
            if (s.length() > len) 
                len = s.length();

        char[][] map = new char[inputMap.length][len];

        for (int i = 0; i < inputMap.length; i++) {
            for (int j = 0; j < inputMap[i].length(); j++) {
                if (inputMap[i].charAt(j) == '.' || inputMap[i].charAt(j) == '#') 
                    map[i][j] = inputMap[i].charAt(j);
            }
        }
        
        int[][] rowBound = new int[map.length][2];
        int[][] colBound = new int[map[0].length][2];

        // find the bounds for the rows and cols
        for (int row = 0; row < rowBound.length; row++) {
            // find the first non-space
            int start = 0;
            while (map[row][start] == '\0') start++;
            
            // finds 1 after last nonspace
            int end = start;
            while (end + 1 < map[row].length && map[row][end + 1] != '\0') end++;

            rowBound[row][0] = start;
            rowBound[row][1] = end;
        }
        for (int col = 0; col < colBound.length; col++) {
            // find the first non-space
            int start = 0;
            while (map[start][col] == '\0') start++;
            
            // finds 1 after last nonspace
            int end = start;
            while (end + 1 < map.length && map[end + 1][col] != '\0') end++;

            colBound[col][0] = start;
            colBound[col][1] = end;
        }

        /*
        for (int i = 0; i < map.length; i++) {
            // System.out.printf("From %3d to %3d: ", rowBound[i][0], rowBound[i][1]);
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 0) System.out.print(" ");
                else System.out.print(map[i][j]);
            }
            System.out.print("\n");
        }
        */
        
        // facing: 0 - >, 1 - v, 2 - <, 3 - ^
        int[] dx = new int[]{1, 0, -1, 0};
        int[] dy = new int[]{0, 1, 0, -1};
        int facing = 0, y = 0, x = rowBound[0][0];

        String[] directions = seperate(input[1]);

        for (String s: directions) {
            // check if its a direction
            if (s.equals("R")) {
                facing = (facing + 1) % 4;
                continue;
            }
            if (s.equals("L")) {
                facing = (facing + 4 - 1) % 4;
                continue;
            }

            // System.out.printf("from %d,%d", x, y);

            // move the number of steps
            int n = stoi(s);
            for (int i = 0; i < n; i++) {
                // find next position
                int newfacing = facing;
                int nx = x + dx[facing];
                int ny = y + dy[facing];

                // hard coded for my map shape
                /*  Map     Area/Direction > new Area/Direction
                 *   1 2  | 1< - 4> , 1^ - 6> , 2V - 3<, 2> - 5< , 2^ - 6^
                 *   3    | 3> - 2^ , 3< - 4V
                 * 4 5    | 4^ - 3> , 4< - 1> , 5> - 2< , 5V - 6< 
                 * 6      | 6> - 5^ , 6< - 1V, 6V - 2V
                 */ 

                // moved off left
                if (dx[facing] < 0 && nx < rowBound[y][0]) {
                    // 4 positions: 1 to 4, 3 to 4, 4 to 1, 6 to 1
                    if (colBound[x][0] == 0) { // 1 or 3
                        if (y < 50) { // 1< to 4>
                            newfacing = 0;
                            nx = 0;
                            ny = (49 - y) + 100;
                        } else { // 3< to 4V
                            newfacing = 1;
                            nx = y - 50;
                            ny = 100;
                        }
                    } else { // 4 or 6
                        if (y < 150) { // 4< to 1>
                            newfacing = 0;
                            ny = 149 - y;
                            nx = 50;
                        } else { // 6< to 1V
                            newfacing = 1;
                            ny = 0;
                            nx = (y - 150) + 50;
                        }
                    }
                } // moved off right
                else if (dx[facing] > 0 && nx > rowBound[y][1]) {
                    // 2 to 5, 3 to 2, 5 to 2, 6 to 5
                    if (rowBound[y][1] == len - 1) { // 2> - 5<
                        newfacing = 2;
                        nx = 99;
                        ny = (49 - y) + 100;
                    } else if (rowBound[y][1] == (len / 3) - 1) { // 6> - 5^
                        newfacing = 3;
                        nx = 50 + (y - 150);
                        ny = 149;
                    } else { // 3 or 5
                        if (y < 100) { // 3> - 2^
                            newfacing = 3;
                            ny = 49;
                            nx = (y - 50) + 100;
                        } else { // 5> - 2<
                            newfacing = 2;
                            nx = 149;
                            ny = (149 - y);
                        }
                    }
                    
                } // moved off up
                else if (dy[facing] < 0 && ny < colBound[x][0]) {
                    // 1 to 6, 2 to 6, 4 to 3
                    if (x < 50) { // 4^ - 3>
                        newfacing = 0;
                        nx = 50;
                        ny = x + 50;
                    } else if (x < 100) { // 1^ - 6>
                        newfacing = 0;
                        nx = 0;
                        ny = (x - 50) + 150;
                    } else { //  2^ - 6^
                        newfacing = 3;
                        nx = x - 100;
                        ny = 199;
                    }

                } // moved off down
                else if (dy[facing] > 0 && ny > colBound[x][1]) {
                    // 2 to 3, 5 to 6, 6 to 2
                    if (x < 50) { // 6V - 2V
                        newfacing = 1;
                        ny = 0;
                        nx = x + 100;
                    } else if (x < 100) { // 5V - 6< 
                        newfacing = 2;
                        nx = 49;
                        ny = (x - 50) + 150;
                    } else { // 2V - 3<
                        newfacing = 2;
                        nx = 99;
                        ny = (x - 100) + 50;
                    }

                }

                // stop if wall is hit
                if (map[ny][nx] == '#') break;

                // update x and y otherwise
                facing = newfacing;
                x = nx;
                y = ny;
            }

            // System.out.printf(" to %d,%d\n", x, y);

        }

        // System.out.println(y - colBound[x][0] + 1);

        return (1000 * (y + 1)) + (4 * (x + 1)) + facing;
    }

    public static String[] seperate(String str) {
        List<String> list = new ArrayList<>();

        StringBuilder curr = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (Character.isDigit(c))
                curr.append(c);
            else {
                list.add(curr.toString());
                list.add(String.valueOf(c));
                curr.setLength(0);
            }
        }

        if (!curr.toString().isBlank()) list.add(curr.toString());
        
        return list.toArray(new String[0]);
    }

    // returns the integer value of a string, or 0 if its bad
    public static int stoi(String s) {
        try {
            int x = Integer.parseInt(s);
            return x;
        } catch (Exception e) {
            return 0;
        }
    }
    
    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
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
    
    // Parse an input string into an int matrix
    public static int[][] gridParseI(String input) {
        String[] rows = input.split("\\r?\\n");
        int[][] result = new int[rows.length][];
        for (int i = 0; i < result.length; i++) {
            String[] r = rows[i].split("");
            for (int j = 0; j < r.length; j++) {
                result[i][j] = Integer.parseInt(r[j]);
            }
        }
        return result; 
    }
}