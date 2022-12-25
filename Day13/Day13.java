import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day13 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        // part 1 find right order
        System.out.println(part1(arr));

        // part 2
        List<String> list = new ArrayList<String>();
        for (String s: arr) if (!s.isBlank()) list.add(s);
        // add dividers
        list.add("[[2]]");
        list.add("[[6]]");
        // sort based on compare function made
        Collections.sort(list, (s1, s2) -> compare(s1, s2));
        // find dividers
        int p1 = list.indexOf("[[2]]") + 1;
        int p2 = list.indexOf("[[6]]") + 1;
        // part 2 answer
        System.out.println(p1 * p2);

    }

    public static int part1(String[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i+=3) {
            String s1 = arr[i];
            String s2 = arr[i + 1];
            if (compare(s1, s2) < 0) sum += (i / 3) + 1;
        }
        return sum;
    }

    // return < 0 - s1 less than s2 (correct order) negative = right order
    // return > 0 - s1 greater than s2 (wrong order) positive = wrong order
    // return = 0 if they are same (idk shouldnt ever happen)
    public static int compare(String a, String b) {
        String[] s1 = makelist(a), s2 = makelist(b);
        if (s1.length == 0 && s2.length == 0) return 0;
        if (s1.length == 0) return -1;
        if (s2.length == 0) return 1;
        // check if its a list
        for (int i = 0; i < s1.length; i++) {
            if (i >= s2.length) return 1;
            boolean l1 = isList(s1[i]), l2 = isList(s2[i]);
            if (l1 || l2) {
                int x = compare(s1[i], s2[i]);
                if (x != 0) return x;
            } else {
                int n1 = Integer.parseInt(s1[i]);
                int n2 = Integer.parseInt(s2[i]);
                if (n1 < n2) return -1;
                else if (n1 > n2) return 1;
                // if equal, continue;
            }
        }
        return -1;
    }

    // check if a string is a list (contains a leading '[')
    public static boolean isList(String s) {
        return s.charAt(0) == '[';
    }

    public static String[] makelist(String s) {
        // just an integer
        if (s.indexOf('[') == -1) return new String[]{s};
        // else it is a list
        // cut off the surrounding brackets []
        // [2,[3,[4,[5,6,7]]]] = list of -> 2, [3,[4,[5,6,7]]]
        List<String> list = new ArrayList<String>();
        int brackets = 0;
        int prev = 1;
        for (int i = 1; i < s.length() - 1; i++) {
            if (s.charAt(i) == ',' && brackets == 0) {
                list.add(s.substring(prev, i));
                prev = i + 1;
            } else if (s.charAt(i) == '[') {
                brackets++;
            } else if (s.charAt(i) == ']') {
                brackets--;
            }
        }
        if (prev != s.length() - 1) list.add(s.substring(prev, s.length() - 1));
        String[] arr = new String[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
    
    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
    }
}
