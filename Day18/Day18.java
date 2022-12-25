import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day18 {

    record Cube(int x, int y, int z) {};
    public static int[] dx = {1, -1, 0, 0, 0, 0};
    public static int[] dy = {0, 0, 1, -1, 0, 0};
    public static int[] dz = {0, 0, 0, 0, 1, -1};

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        // originaly used union find, other solutions are prob better lol
        // System.out.println(part1(arr));
        
        // yeah this is so much better (and shorter)
        System.out.println(part1N(arr));

        System.out.println(part2(arr));
    }

    public static int part1N(String[] arr) {
        Set<String> set = new HashSet<String>();
        int edges = arr.length * 6;
        for (String s: arr) set.add(s);
        // now subtract overlaps
        for (String s: set) {
            String[] cube = s.split(",");
            int[] coords = new int[]{stoi(cube[0]), stoi(cube[1]), stoi(cube[2])};
            // check 6 sides
            for (int i = 0; i < 6; i++) 
                if (set.contains((coords[0] + dx[i]) + "," + (coords[1] + dy[i]) + "," + (coords[2] + dz[i]))) 
                    edges--;
        }
        return edges;
    }

    public static int part1(String[] arr) {
        int sides = 0;
        HashMap<String, String> union = new HashMap<String, String>();
        Deque<String> compress = new ArrayDeque<String>();
        for (String s: arr) {
            String[] cube = s.split(",");
            int[] coords = new int[3];
            coords[0] = stoi(cube[0]);
            coords[1] = stoi(cube[1]);
            coords[2] = stoi(cube[2]);
            // Set for elements (dont want duplicates)?
            TreeSet<String> around = new TreeSet<String>();
            int nAround = 0;
            for (int i = 0; i < 6; i++) {
                // find parents around all 6 sides
                String str = (coords[0] + dx[i]) + "," + (coords[1] + dy[i]) + "," + (coords[2] + dz[i]);
                if (union.containsKey(str)) {
                    while (!union.get(str).equals(str)) {
                        compress.offer(str);
                        str = union.get(str);
                    }
                    // path compression
                    while (!compress.isEmpty())
                        union.put(compress.poll(), str);
                    around.add(str);
                    nAround++;
                }
            }

            // unions the ones found around
            if (around.isEmpty()) {
                sides += 6;
                union.put(s, s);
            } else {
                sides += (6 - (2*nAround));
                for (String element: around)
                    union.put(element, around.first());
                union.put(s, around.first());
            }
        }
        return sides;
    }

    public static int part2(String[] arr) {
        int[] maxes = new int[]{0, 0, 0};
        Set<String> points = new HashSet<String>();
        for (String s: arr) {
            points.add(s);
            // find maxes
            String[] cube = s.split(",");
            int[] coords = new int[3];
            coords[0] = stoi(cube[0]);
            if (coords[0] > maxes[0]) maxes[0] = coords[0];
            coords[1] = stoi(cube[1]);
            if (coords[1] > maxes[1]) maxes[1] = coords[1];
            coords[2] = stoi(cube[2]);
            if (coords[2] > maxes[2]) maxes[2] = coords[2];
        }

        // bfs from 0,0,0 for every edge reached ++
        ArrayDeque<Cube> queue = new ArrayDeque<Cube>();
        Set<String> visited = new HashSet<String>();
        queue.offer(new Cube(0, 0, 0));
        visited.add("0,0,0");
        int outer = 0;
        while (!queue.isEmpty()) {
            Cube curr = queue.poll();
            // check all 6 sides
            for (int i = 0; i < 6; i++) {
                // continue of ob
                if (curr.x + dx[i] < -1 || curr.x + dx[i] > maxes[0] + 1 || curr.y + dy[i] < -1 || curr.y + dy[i] > maxes[1] + 1 || curr.z + dz[i] < -1 || curr.z + dz[i] > maxes[2] + 1) continue;
                String keyString = (curr.x + dx[i]) + "," + (curr.y + dy[i]) + "," + (curr.z + dz[i]);
                // found an edge
                if (points.contains(keyString)) {
                    outer++;
                    continue;
                }
                if (visited.contains(keyString)) 
                    continue;
                visited.add(keyString);
                queue.push(new Cube(curr.x + dx[i], curr.y + dy[i], curr.z + dz[i]));
            }
        }
        return outer;
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

}