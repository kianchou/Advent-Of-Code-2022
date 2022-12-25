import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day15 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        System.out.println(part1(arr, 2_000_000));

        // part 2 needs some work currently its just brute force
        System.out.println(part2(arr, 4_000_000));
    }

    public static int part1(String[] arr, int d) {
        HashMap<String, String> map = new HashMap<String, String>();
        for (String s: arr) {
            String x1 = s.substring(12, s.indexOf(','));
            String y1 = s.substring(s.indexOf(',') + 4, s.indexOf(':'));
            String x2 = s.substring(s.indexOf("is") + 8, s.indexOf(',', s.indexOf("is")));
            String y2 = s.substring(s.indexOf("y=", s.indexOf("is")) + 2);
            map.put(x1 + "," + y1, x2 + "," + y2);
        }
        List<int[]> ranges = new ArrayList<int[]>();
        for (String s: map.keySet()) {
            String[] sensor = s.split(",");
            int sx = stoi(sensor[0]), sy = stoi(sensor[1]);
            String[] beacon = map.get(s).split(",");
            int bx = stoi(beacon[0]), by = stoi(beacon[1]);
            // System.out.println(sx + "," + sy + " to " + bx + "," + by);
            int distance = Math.abs(bx - sx) + Math.abs(by - sy);
            // System.out.println(distance);
            // find the spot where it intersects with y = 2,000,000
            int remaining = sy > d ? sy - d : d - sy;
            distance -= remaining;
            if (distance > 0) ranges.add(new int[]{sx - distance, sx + distance});
        }
        ranges.sort((int[] a, int[] b) -> Integer.compare(a[0], b[0]));
        /*
        for (int[] r: ranges) {
            // System.out.println(r[0] + " to " + r[1]);
        }
        */
        // combine the ranges
        int[] curr = ranges.get(0);
        int result = 0;
        for (int i = 1; i < ranges.size(); i++) {
            int[] r = ranges.get(i);
            if (r[0] > curr[1]) {
                System.out.println("added");
                result += curr[1] - curr[0];
                curr = r;
            } else if (r[0] <= curr[1] && r[1] > curr[1]) {
                curr[1] = r[1];
            }
        }
        result += curr[1] - curr[0];
        // System.out.println("added > " + curr[0] + " to " + curr[1]);
        return result;
    }

    public static int part2(String[] arr, int d) {
        HashMap<String, String> map = new HashMap<String, String>();
        for (String s: arr) {
            String x1 = s.substring(12, s.indexOf(','));
            String y1 = s.substring(s.indexOf(',') + 4, s.indexOf(':'));
            String x2 = s.substring(s.indexOf("is") + 8, s.indexOf(',', s.indexOf("is")));
            String y2 = s.substring(s.indexOf("y=", s.indexOf("is")) + 2);
            map.put(x1 + "," + y1, x2 + "," + y2);
        }
        // sweep the y coordinates
        for (int z = d; z >= 0; z--) {
            List<int[]> ranges = new ArrayList<int[]>();
            for (String s: map.keySet()) {
                String[] sensor = s.split(",");
                int sx = stoi(sensor[0]), sy = stoi(sensor[1]);
                String[] beacon = map.get(s).split(",");
                int bx = stoi(beacon[0]), by = stoi(beacon[1]);
                // System.out.println(sx + "," + sy + " to " + bx + "," + by);
                int distance = Math.abs(bx - sx) + Math.abs(by - sy);
                // System.out.println(distance);
                
                int remaining = sx > z ? sx - z : z - sx;
                distance -= remaining;
                if (distance > 0) ranges.add(new int[]{sy - distance, sy + distance});
            }
            ranges.sort((int[] a, int[] b) -> Integer.compare(a[0], b[0]));
            
            // combine the ranges and put into new list
            List<int[]> newranges = new ArrayList<int[]>();
            int[] curr = ranges.get(0);
            for (int i = 1; i < ranges.size(); i++) {
                int[] r = ranges.get(i);
                if (r[0] > curr[1]) {
                    newranges.add(curr);
                    curr = r;
                } else if (r[0] <= curr[1] && r[1] > curr[1]) {
                    curr[1] = r[1];
                }
            }
            newranges.add(curr);
            if (newranges.size() > 1) {
                for (int[] r: newranges) {
                    System.out.println(r[0] + " to " + r[1]);
                }
                return 0;
            }
        }
        // System.out.println("added > " + curr[0] + " to " + curr[1]);
        return 0;
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