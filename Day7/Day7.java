import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day7 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt")).trim();
        String[] arr = arrParseS(input);

        System.out.println(part1(arr));

        System.out.println(part2(arr));
    }

    public static int part1(String[] input) {
        Map<String, Integer> vals = new HashMap<String, Integer>();
        // Map<String, HashSet<String>> dir = new HashMap<String, HashSet<String>>();
        ArrayList<String> curr = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            String[] args = input[i].split(" ");
            if (args[0].equals("$")) {
                if (args[1].equals("cd")) {
                    if (args[2].equals("..")) {
                        curr.remove(curr.size() - 1);
                    } else { // else go in
                        curr.add(args[2]);
                        vals.put(curr.toString(), vals.getOrDefault(curr.toString(), 0));
                    }
                }
            } else {
                if (args[0].equals("dir")) {
                    curr.add(args[1]);
                    vals.put(curr.toString(), vals.getOrDefault(curr.toString(), 0));
                    curr.remove(curr.size() - 1);
                } else {
                    // System.out.println(Arrays.toString(str));
                    int size = Integer.parseInt(args[0]);
                    ArrayList<String> path = new ArrayList<String>();
                    for (String s: curr) {
                        path.add(s);
                        vals.put(path.toString(), vals.get(path.toString()) + size);
                    }
                }
            }
        }

        int ct = 0;
        // int max = 0;
        for (String s: vals.keySet()) {
            int x = vals.get(s);
            // if (x > max) max = x;
            // System.out.println(s + ":" + x);
            if (x <= 100_000) {
                ct += x;
            }// else {
                // System.out.println(s + ":" + x);
            // }
        }
        // System.out.println(max);

        // System.out.println(vals.get("svgbqd"));
        return ct;
    }

    public static int part2(String[] input) {
        Map<String, Integer> vals = new HashMap<String, Integer>();
        ArrayList<String> curr = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            String[] args = input[i].split(" ");
            if (args[0].equals("$")) {
                if (args[1].equals("cd")) {
                    if (args[2].equals("..")) {
                        curr.remove(curr.size() - 1);
                    } else { // else go in
                        curr.add(args[2]);
                        vals.put(curr.toString(), vals.getOrDefault(curr.toString(), 0));
                    }
                }
            } else {
                if (args[0].equals("dir")) {
                    curr.add(args[1]);
                    vals.put(curr.toString(), vals.getOrDefault(curr.toString(), 0));
                    curr.remove(curr.size() - 1);
                } else {
                    int size = Integer.parseInt(args[0]);
                    ArrayList<String> path = new ArrayList<String>();
                    for (String s: curr) {
                        path.add(s);
                        vals.put(path.toString(), vals.get(path.toString()) + size);
                    }
                }
            }
        }

        String home = List.of("/").toString();
        int space = 70000000 - vals.get(home);
        int min = vals.get(home);
        for (String s: vals.keySet()) {
            if (s.equals(home)) continue;
            int x = vals.get(s);
            if (x + space >= 30000000 && x < min) {
                min = x;
            }
        }

        return min;
    }
    
    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
    }
}