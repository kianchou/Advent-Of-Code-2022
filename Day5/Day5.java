import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day5 {

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);
        int size = 9;

        // part 1
        ArrayList<Deque<Character>> stacks1 = new ArrayList<Deque<Character>>();
        // part 2
        ArrayList<Deque<Character>> stacks2 = new ArrayList<Deque<Character>>();

        for (int i = 0; i < size; i++) {
            stacks1.add(new ArrayDeque<Character>());
            stacks2.add(new ArrayDeque<Character>());
        }

        // parsing

        int x = 0;
        while (arr[x].indexOf("1") == -1) {
            for (int i = 0; i < arr[x].length(); i += 4) {
                String str = arr[x].substring(i, i+2);
                if (!str.isBlank()) {
                    stacks1.get(i/4).offerFirst(str.charAt(1));
                    stacks2.get(i/4).offerFirst(str.charAt(1));
                }
            }
            x++;
        }

        System.out.println(part1(arr, stacks1));

        System.out.println(part2(arr, stacks2));
    }

    public static String part1(String[] arr, ArrayList<Deque<Character>> stacks) {
        int p = 0;
        while (arr[p].indexOf("move") == -1) p++;

        for (int i = p; i < arr.length; i++) {
            String s = arr[i];
            // index of spaces
            int sp1 = s.indexOf(" ");
            int sp2 = s.indexOf(" ", sp1 + 1);
            int sp3 = s.indexOf(" ", sp2 + 1);
            int sp4 = s.indexOf(" ", sp3 + 1);
            int sp5 = s.indexOf(" ", sp4 + 1);

            // get the 3 numbers
            int ct = Integer.parseInt(s.substring(sp1 + 1, sp2));
            int from = Integer.parseInt(s.substring(sp3 + 1, sp4)) - 1;
            int to = Integer.parseInt(s.substring(sp5 + 1)) - 1;

            for (int j = 0; j < ct; j++) {
                stacks.get(to).offerLast(stacks.get(from).pollLast());
            }
        }

        String ret = "";
        for (int i = 0; i < stacks.size(); i++) {
            ret += stacks.get(i).peekLast();
        }

        return ret;
    }

    public static String part2(String[] arr, ArrayList<Deque<Character>> stacks) {
        int p = 0;
        while (arr[p].indexOf("move") == -1) p++;

        for (int i = p; i < arr.length; i++) {
            String s = arr[i];
            int sp1 = s.indexOf(" ");
            int sp2 = s.indexOf(" ", sp1 + 1);
            int sp3 = s.indexOf(" ", sp2 + 1);
            int sp4 = s.indexOf(" ", sp3 + 1);
            int sp5 = s.indexOf(" ", sp4 + 1);
            int ct = Integer.parseInt(s.substring(sp1 + 1, sp2));
            int from = Integer.parseInt(s.substring(sp3 + 1, sp4)) - 1;
            int to = Integer.parseInt(s.substring(sp5 + 1)) - 1;
            ArrayDeque<Character> temp = new ArrayDeque<Character>();
            for (int j = 0; j < ct; j++) {
                temp.push(stacks.get(from).pollLast());
            }
            for (int j = 0; j < ct; j++) {
                stacks.get(to).offerLast(temp.pop());
            }
        }

        String ret = "";
        for (int i = 0; i < stacks.size(); i++) {
            ret += stacks.get(i).peekLast();
        }

        return ret;
    }

    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
    }
}