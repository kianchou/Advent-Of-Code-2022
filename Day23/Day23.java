import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day23 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        System.out.println(part1(arr));

        System.out.println(part2(arr));
    }

    public static int part1(String[] arr) {
        // write down each elf position
        Set<String> set = new HashSet<>();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length(); j++) {
                if (arr[i].charAt(j) == '#')
                    set.add(j + "," + i);
            }
        }

        Deque<int[]> directions = new ArrayDeque<int[]>();
        directions.offer(new int[]{0, -1}); // North
        directions.offer(new int[]{0, 1}); // South
        directions.offer(new int[]{-1, 0}); // West
        directions.offer(new int[]{1, 0}); // East

        // int miny = 0, maxy = 0, minx = 0, maxx = 0;
        for (int rounds = 0; rounds < 10; rounds++) {
            // System.out.println(set.size());
            List<Deque<String>> moves = new ArrayList<Deque<String>>();
            // 0: N, 1: S, 2: W, 3: E
            for (int i = 0; i < 4; i++)
                moves.add(new ArrayDeque<String>());

            Map<String, Integer> map = new HashMap<>();
            
            for (String s: set) {
                String[] sArr = s.split(",");
                int x = stoi(sArr[0]);
                int y = stoi(sArr[1]);

                boolean NW = set.contains((x - 1) + "," + (y - 1));
                boolean N = set.contains((x) + "," + (y - 1));
                boolean NE = set.contains((x + 1) + "," + (y - 1));
                boolean W = set.contains((x - 1) + "," + (y));
                boolean E = set.contains((x + 1) + "," + (y));
                boolean SW = set.contains((x - 1) + "," + (y + 1));
                boolean S = set.contains((x) + "," + (y + 1));
                boolean SE = set.contains((x + 1) + "," + (y + 1));
                // skip if all 8 posiitons empty
                if (!NW && !N && !NE && !W && !E && !SW && !S && !SE) continue;

                for (int[] d: directions) {
                    // north or south
                    if (d[0] == 0) {
                        if (!set.contains((x - 1) + "," + (y + d[1])) &&
                            !set.contains((x) + "," + (y + d[1])) &&
                            !set.contains((x + 1) + "," + (y + d[1]))) {
                            String mv = (x) + "," + (y + d[1]);
                            map.put(mv, map.getOrDefault(mv, 0) + 1);
                            if (d[1] < 0) moves.get(0).offer(s);
                            else moves.get(1).offer(s);
                            break;
                        }
                    } else { // east or west
                        if (!set.contains((x + d[0]) + "," + (y - 1)) &&
                            !set.contains((x + d[0]) + "," + (y)) &&
                            !set.contains((x + d[0]) + "," + (y + 1))) {
                            String mv = (x + d[0]) + "," + (y);
                            map.put(mv, map.getOrDefault(mv, 0) + 1);
                            if (d[0] < 0) moves.get(2).offer(s);
                            else moves.get(3).offer(s);
                            break;
                        }
                    }
                }
            }

            // move each
            Deque<String> m = moves.get(0);
            // adding order doesnt matter
            // north
            while (!m.isEmpty()) {
                String s = m.poll();
                String[] sArr =  s.split(",");
                int x = stoi(sArr[0]), y = stoi(sArr[1]) - 1;
                if (map.get(x + "," + y) > 1) continue;
                set.remove(s);
                set.add(x + "," + y);
                // System.out.println(set.add(x + "," + y) + " : " + s + " : " + (x + "," + y));
            }
            // south
            m = moves.get(1);
            while (!m.isEmpty()) {
                String s = m.poll();
                String[] sArr =  s.split(",");
                int x = stoi(sArr[0]), y = stoi(sArr[1]) + 1;
                if (map.get(x + "," + y) > 1) continue;
                set.remove(s);
                set.add(x + "," + y);
                // System.out.println(set.add(x + "," + y) + " : " + s + " : " + (x + "," + y));
            }
            // west
            m = moves.get(2);
            while (!m.isEmpty()) {
                String s = m.poll();
                String[] sArr =  s.split(",");
                int x = stoi(sArr[0]) - 1, y = stoi(sArr[1]);
                if (map.get(x + "," + y) > 1) continue;
                set.remove(s);
                set.add(x + "," + y);
                // System.out.println(set.add(x + "," + y) + " : " + s + " : " + (x + "," + y));
            }
            // east
            m = moves.get(3);
            while (!m.isEmpty()) {
                String s = m.poll();
                String[] sArr =  s.split(",");
                int x = stoi(sArr[0]) + 1, y = stoi(sArr[1]);
                if (map.get(x + "," + y) > 1) continue;
                set.remove(s);
                set.add(x + "," + y);
                // System.out.println(set.add(x + "," + y) + " : " + s + " : " + (x + "," + y));
            }

            int[] dir = directions.poll();
            directions.offer(dir);
            /*
            System.out.printf("After move %d: \n", rounds + 1);
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    if (set.contains(j + "," + i)) System.out.print("#");
                    else System.out.print(".");
                }
                System.out.print("\n");
            }
            */
        }

        // System.out.println(set.size());
        // System.out.printf("x: %d - %d\ny: %d - %d\n", minx, maxx, miny, maxy);

        // 
        int maxx = 0, minx = Integer.MAX_VALUE, maxy = 0, miny = Integer.MAX_VALUE;
        for (String s: set) {
            // System.out.println(s);
            String[] str = s.split(",");
            int x = stoi(str[0]), y = stoi(str[1]);
            if (x < minx) minx = x;
            if (x > maxx) maxx = x;
            if (y < miny) miny = y;
            if (y > maxy) maxy = y;
        }
        // System.out.printf("x: %d - %d\ny: %d - %d\n", minx, maxx, miny, maxy);
        // return 0;
        return ((maxx - minx + 1) * (maxy - miny + 1)) - set.size();
    }

    public static int part2(String[] arr) {
        // write down each elf position
        Set<String> set = new HashSet<>();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length(); j++) {
                if (arr[i].charAt(j) == '#')
                    set.add(j + "," + i);
            }
        }

        Deque<int[]> directions = new ArrayDeque<int[]>();
        directions.offer(new int[]{0, -1}); // North
        directions.offer(new int[]{0, 1}); // South
        directions.offer(new int[]{-1, 0}); // West
        directions.offer(new int[]{1, 0}); // East

        // int miny = 0, maxy = 0, minx = 0, maxx = 0;
        int rounds = 0;
        while (true) {
            // System.out.println(set.size());
            List<Deque<String>> moves = new ArrayList<Deque<String>>();
            // 0: N, 1: S, 2: W, 3: E
            for (int i = 0; i < 4; i++)
                moves.add(new ArrayDeque<String>());

            Map<String, Integer> map = new HashMap<>();
            
            for (String s: set) {
                String[] sArr = s.split(",");
                int x = stoi(sArr[0]);
                int y = stoi(sArr[1]);

                boolean NW = set.contains((x - 1) + "," + (y - 1));
                boolean N = set.contains((x) + "," + (y - 1));
                boolean NE = set.contains((x + 1) + "," + (y - 1));
                boolean W = set.contains((x - 1) + "," + (y));
                boolean E = set.contains((x + 1) + "," + (y));
                boolean SW = set.contains((x - 1) + "," + (y + 1));
                boolean S = set.contains((x) + "," + (y + 1));
                boolean SE = set.contains((x + 1) + "," + (y + 1));
                // skip if all 8 posiitons empty
                if (!NW && !N && !NE && !W && !E && !SW && !S && !SE) continue;

                for (int[] d: directions) {
                    // north or south
                    if (d[0] == 0) {
                        if (!set.contains((x - 1) + "," + (y + d[1])) &&
                            !set.contains((x) + "," + (y + d[1])) &&
                            !set.contains((x + 1) + "," + (y + d[1]))) {
                            String mv = (x) + "," + (y + d[1]);
                            map.put(mv, map.getOrDefault(mv, 0) + 1);
                            if (d[1] < 0) moves.get(0).offer(s);
                            else moves.get(1).offer(s);
                            break;
                        }
                    } else { // east or west
                        if (!set.contains((x + d[0]) + "," + (y - 1)) &&
                            !set.contains((x + d[0]) + "," + (y)) &&
                            !set.contains((x + d[0]) + "," + (y + 1))) {
                            String mv = (x + d[0]) + "," + (y);
                            map.put(mv, map.getOrDefault(mv, 0) + 1);
                            if (d[0] < 0) moves.get(2).offer(s);
                            else moves.get(3).offer(s);
                            break;
                        }
                    }
                }
            }

            // move each
            Deque<String> m = moves.get(0);
            int nMoved = 0;
            // adding order doesnt matter
            // north
            while (!m.isEmpty()) {
                nMoved++;
                String s = m.poll();
                String[] sArr =  s.split(",");
                int x = stoi(sArr[0]), y = stoi(sArr[1]) - 1;
                if (map.get(x + "," + y) > 1) continue;
                set.remove(s);
                set.add(x + "," + y);
                // System.out.println(set.add(x + "," + y) + " : " + s + " : " + (x + "," + y));
            }
            // south
            m = moves.get(1);
            while (!m.isEmpty()) {
                nMoved++;
                String s = m.poll();
                String[] sArr =  s.split(",");
                int x = stoi(sArr[0]), y = stoi(sArr[1]) + 1;
                if (map.get(x + "," + y) > 1) continue;
                set.remove(s);
                set.add(x + "," + y);
                // System.out.println(set.add(x + "," + y) + " : " + s + " : " + (x + "," + y));
            }
            // west
            m = moves.get(2);
            while (!m.isEmpty()) {
                nMoved++;
                String s = m.poll();
                String[] sArr =  s.split(",");
                int x = stoi(sArr[0]) - 1, y = stoi(sArr[1]);
                if (map.get(x + "," + y) > 1) continue;
                set.remove(s);
                set.add(x + "," + y);
                // System.out.println(set.add(x + "," + y) + " : " + s + " : " + (x + "," + y));
            }
            // east
            m = moves.get(3);
            while (!m.isEmpty()) {
                nMoved++;
                String s = m.poll();
                String[] sArr =  s.split(",");
                int x = stoi(sArr[0]) + 1, y = stoi(sArr[1]);
                if (map.get(x + "," + y) > 1) continue;
                set.remove(s);
                set.add(x + "," + y);
                // System.out.println(set.add(x + "," + y) + " : " + s + " : " + (x + "," + y));
            }

            int[] dir = directions.poll();
            directions.offer(dir);
            rounds++;
            if (nMoved == 0) break;
        }

        // System.out.println(set.size());
        // System.out.printf("x: %d - %d\ny: %d - %d\n", minx, maxx, miny, maxy);

        /*
        int maxx = 0, minx = Integer.MAX_VALUE, maxy = 0, miny = Integer.MAX_VALUE;
        for (String s: set) {
            // System.out.println(s);
            String[] str = s.split(",");
            int x = stoi(str[0]), y = stoi(str[1]);
            if (x < minx) minx = x;
            if (x > maxx) maxx = x;
            if (y < miny) miny = y;
            if (y > maxy) maxy = y;
        }
        System.out.printf("x: %d - %d\ny: %d - %d\n", minx, maxx, miny, maxy);
        // return 0;
        */
        return rounds;
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