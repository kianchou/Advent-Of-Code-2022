import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day6 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt")).trim();

        System.out.println(part1(input));

        System.out.println(part2(input));
    }

    public static int part1(String input) {
        int[] cts = new int[26];
        for (int i = 0; i < 3; i++) {
            cts[input.charAt(i) - 'a']++;
        }
        for (int i = 3; i < input.length(); i++) {
            cts[input.charAt(i) - 'a']++;
            boolean dup = false;
            for (int j = 0; j < 26; j++) {
                if (cts[j] >= 2) dup = true;
            }
            if (!dup) return i + 1;
            cts[input.charAt(i - 3) - 'a']--;
        }
        return 0;
    }

    public static int part2(String input) {
        int[] cts = new int[26];
        for (int i = 0; i < 13; i++) {
            cts[input.charAt(i) - 'a']++;
        }
        for (int i = 13; i < input.length(); i++) {
            cts[input.charAt(i) - 'a']++;
            boolean dup = false;
            for (int j = 0; j < 26; j++) {
                if (cts[j] >= 2) dup = true;
            }
            if (!dup) return i + 1;
            cts[input.charAt(i - 13) - 'a']--;
        }
        return 0;
    }
}