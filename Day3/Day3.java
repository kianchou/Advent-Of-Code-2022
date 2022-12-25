import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day3 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        System.out.println(part1(arr));

        System.out.println(part2(arr));
    }

    public static int part1(String[] arr) {
        int ct = 0;
        for (String s: arr) {
            boolean[] ct1 = new boolean[128];
            int h = s.length() / 2;
            for (int i = 0; i < h; i++) {
                ct1[s.charAt(i)] = true;
            }
            boolean[] ct2 = new boolean[128];
            for (int i = h; i < s.length(); i++) {
                ct2[s.charAt(i)] = true;
            }
            for (int i = 0; i < 128; i++) {
                if (ct1[i] && ct2[i]) {
                    if (Character.isUpperCase((char)i)) {
                        ct += i - 'A' + 27;
                    } else {
                        ct += i - 'a' + 1;
                    }
                    break;
                }
            }
        }
        return ct;
    }

    public static int part2(String[] arr) {
        int ct = 0;
        for (int i = 0; i < arr.length / 3; i++) {
            boolean[][] cts = new boolean[3][128];
            for (char c: arr[3*i].toCharArray()) {
                cts[0][c] = true;
            }
            for (char c: arr[(3*i) + 1].toCharArray()) {
                cts[1][c] = true;
            }
            for (char c: arr[(3*i) + 2].toCharArray()) {
                cts[2][c] = true;
            }
            for (int j = 0; j < 128; j++) {
                if (cts[0][j] && cts[1][j] && cts[2][j]) {
                    if (Character.isUpperCase((char)j)) {
                        ct += j - 'A' + 27;
                    } else {
                        ct += j - 'a' + 1;
                    }
                    break;
                }
            }
        }
        return ct;
    }

    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
    }
}
