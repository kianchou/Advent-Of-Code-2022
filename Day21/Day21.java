import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day21 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);
        
        // this actually reminds me of context free grammars LOL

        System.out.println(part1(arr));

        // i wolfram alpha-ed this part (copy paste the equation)
        System.out.println(part2(arr));
    }

    public static long part1(String[] arr) {
        Map<String, String> map = new HashMap<String, String>();

        for (String s: arr)
            map.put(s.substring(0, s.indexOf(':')), s.substring(s.indexOf(' ') + 1));

        return solve("root", map);
    }

    public static String part2(String[] arr) {
        Map<String, String> map = new HashMap<String, String>();

        for (String s: arr)
            map.put(s.substring(0, s.indexOf(':')), s.substring(s.indexOf(' ') + 1));
        
        String[] parts = map.get("root").split(" *[*-/] *");
        return solve2(parts[0], map) + " = " + solve2(parts[1], map);
    }

    public static long solve(String curr, Map<String, String> map) {
        String eq = map.get(curr);
        String[] parts = eq.split(" *[*-/] *");
        if (parts.length == 1) return stoi(parts[0]);
        long a = solve(parts[0], map);
        long b = solve(parts[1], map);
        // System.out.println(map.get(curr) + " >> " + curr + ":" + Arrays.toString(parts) + ":" + a + ", " + b);
        if (eq.indexOf('+') != -1)
            return a + b;
        if (eq.indexOf('-') != -1)
            return a - b;
        if (eq.indexOf('/') != -1)
            return a / b;
        if (eq.indexOf('*') != -1)
            return a * b;

        // shouldnt ever happen
        return 0;
    }

    // now we just fill what we can, leave in terms of x
    public static String solve2(String curr, Map<String, String> map) {
        if (curr.equals("humn")) return "x";
        String eq = map.get(curr);
        String[] parts = eq.split(" *[*-/] *");
        if (parts.length == 1) return parts[0];
        String a = solve2(parts[0], map);
        String b = solve2(parts[1], map);
        // System.out.println(parts[0] + "=" + a + ", " + parts[1] + "=" + b);
        if (a.matches("^[0-9]+$") && b.matches("^[0-9]+$")) {
            long n1 = Long.parseLong(a);
            long n2 = Long.parseLong(b);
            if (eq.indexOf('+') != -1)
                return Long.toString(n1 + n2);
            if (eq.indexOf('-') != -1)
                return Long.toString(n1 - n2);
            if (eq.indexOf('/') != -1)
                return Long.toString(n1 / n2);
            if (eq.indexOf('*') != -1)
                return Long.toString(n1 * n2);

        } else {
            eq = eq.replace(parts[0] + " ", a);
            eq = eq.replace(" " + parts[1], b);
            // System.out.println(eq);
            return "(" + eq + ")";
        }

        // shouldnt ever happen
        return "it broke";
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