import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day2 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        System.out.println(part1(arr));

        System.out.println(part2(arr));
    }

    public static int part1(String[] arr) {
        int score = 0;
        for (String s: arr) {
            String[] moves = s.split(" ");
            if (moves[0].equals("A")) {
                if (moves[1].equals("X")) {
                    score += 1 + 3;
                } else if (moves[1].equals("Y")) {
                    score += 2 + 6;
                } else {
                    score += 3 + 0;
                }
            } else if (moves[0].equals("B")) {
                if (moves[1].equals("X")) {
                    score += 1 + 0;
                } else if (moves[1].equals("Y")) {
                    score += 2 + 3;
                } else {
                    score += 3 + 6;
                }
            } else {
                if (moves[1].equals("X")) {
                    score += 1 + 6;
                } else if (moves[1].equals("Y")) {
                    score += 2 + 0;
                } else {
                    score += 3 + 3;
                }
            }
        }
        return score;
    }

    public static int part2(String[] arr) {
        int score = 0;
        for (String s: arr) {
            String[] moves = s.split(" ");
            if (moves[0].equals("A")) {
                if (moves[1].equals("X")) {
                    score += 3 + 0;
                } else if (moves[1].equals("Y")) {
                    score += 1 + 3;
                } else {
                    score += 2 + 6;
                }
            } else if (moves[0].equals("B")) {
                if (moves[1].equals("X")) {
                    score += 1 + 0;
                } else if (moves[1].equals("Y")) {
                    score += 2 + 3;
                } else {
                    score += 3 + 6;
                }
            } else {
                if (moves[1].equals("X")) {
                    score += 2 + 0;
                } else if (moves[1].equals("Y")) {
                    score += 3 + 3;
                } else {
                    score += 1 + 6;
                }
            }
        }
        return score;
    }

    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
    }
}
