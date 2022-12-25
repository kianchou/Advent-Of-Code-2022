import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day24 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        char[][] grid = gridParseC(input);

        System.out.println(part1(grid));

        // silly elf and his snacks
        System.out.println(part2(grid));
    }

    // directions: 0: Right, 1: Down, 2: Left, 3: Up
    record Blizzard(int y, int x, int dir) {};

    public static int part1(char[][] grid) {
        // store boards at specific positions
        Set<Blizzard> set = new HashSet<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                switch (grid[i][j]) {
                    case '>':
                        set.add(new Blizzard(i, j, 0));
                        break;
                    case 'v':
                        set.add(new Blizzard(i, j, 1));
                        break;
                    case '<':
                        set.add(new Blizzard(i, j, 2));
                        break;
                    case '^':
                        set.add(new Blizzard(i, j, 3));
                        break;
                    default:
                        break;
                }
            }
        }

        int maxRow = grid.length - 1;
        int maxCol = grid[0].length - 1;
        
        return bfs(0, 1, maxRow, maxCol - 1, maxRow, maxCol, set);
    }

    public static int part2(char[][] grid) {
        // store boards at specific positions
        Set<Blizzard> set = new HashSet<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                switch (grid[i][j]) {
                    case '>':
                        set.add(new Blizzard(i, j, 0));
                        break;
                    case 'v':
                        set.add(new Blizzard(i, j, 1));
                        break;
                    case '<':
                        set.add(new Blizzard(i, j, 2));
                        break;
                    case '^':
                        set.add(new Blizzard(i, j, 3));
                        break;
                    default:
                        break;
                }
            }
        }
        // System.out.println(set.size());
        int maxRow = grid.length - 1;
        int maxCol = grid[0].length - 1;
        
        int total = 0;

        total += bfs(0, 1, maxRow, maxCol - 1, maxRow, maxCol, set);

        total += bfs(maxRow, maxCol - 1, 0, 1, maxRow, maxCol, set);

        total += bfs(0, 1, maxRow, maxCol - 1, maxRow, maxCol, set);

        // if it reaches here this means its impossible (shouldnt ever happen since we can just stay at position 0)
        return total;
    }

    public static int bfs(int x, int y, int goalX, int goalY, int maxRow, int maxCol, Set<Blizzard> set) {
        int[] dx = {0, 1, 0, -1, 0};
        int[] dy = {0, 0, 1, 0, -1};
        // store positions as y,x (row, col)
        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{0,1});
        int min = 0;
        while (!queue.isEmpty()) {
            // update map
            min++;
            set = getNext(set, maxRow, maxCol);
            // System.out.println(set.size());
            int reps = queue.size();
            // use empty set to avoid duplicates
            Set<String> seen = new HashSet<>();
            for (int i = 0; i < reps; i++) {
                int[] curr = queue.poll();
                
                // try to see where current position can move, delete duplicates
                for (int z = 0; z < 5; z++) {
                    int nY = curr[0] + dy[z];
                    int nX = curr[1] + dx[z];

                    if (nY == maxRow && nX == maxCol - 1) {
                        return min;
                    }

                    if (nY == 0 && nX == 1 && seen.add(nY + "," + nX)) {
                        queue.offer(new int[]{nY, nX});
                        continue;
                    }

                    // continue if in wall
                    if (nY <= 0 || nX <= 0 || nY >= maxRow || nX >= maxCol) continue;
                    // continue if this position already exists
                    if (!seen.add(nY + "," + nX)) continue;

                    if (set.contains(new Blizzard(nY, nX, 0)) || 
                        set.contains(new Blizzard(nY, nX, 1)) || 
                        set.contains(new Blizzard(nY, nX, 2)) || 
                        set.contains(new Blizzard(nY, nX, 3))) continue;

                    queue.offer(new int[]{nY, nX});
                }

            }
        }

        // if it reaches here this means its impossible (shouldnt ever happen since we can just stay at position 0)
        return 0;
    }

    public static Set<Blizzard> getNext(Set<Blizzard> set, int maxRow, int maxCol) {
        Set<Blizzard> result = new HashSet<Blizzard>();
        for (Blizzard b: set) {
            if (b.dir == 0) {
                if (b.x == maxCol - 1) result.add(new Blizzard(b.y, 1, b.dir));
                else result.add(new Blizzard(b.y, b.x + 1, b.dir));
            } else if (b.dir == 1) {
                if (b.y == maxRow - 1) result.add(new Blizzard(1, b.x, b.dir));
                else result.add(new Blizzard(b.y + 1, b.x, b.dir));
            } else if (b.dir == 2) {
                if (b.x == 1) result.add(new Blizzard(b.y, maxCol - 1, b.dir));
                else result.add(new Blizzard(b.y, b.x - 1, b.dir));
            } else if (b.dir == 3) {
                if (b.y == 1) result.add(new Blizzard(maxRow - 1, b.x, b.dir));
                else result.add(new Blizzard(b.y - 1, b.x, b.dir));
            }
        }
        return result;
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

    public static char[][] gridParseC(String input) {
        String[] rows = input.split("\\r?\\n");
        char[][] result = new char[rows.length][rows[0].length()];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = rows[i].charAt(j);
            }
        }
        return result; 
    }
}