import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day1 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = input.split("\n");

        System.out.println(part1(arr));

        System.out.println(part2(arr));
    }

    public static int part1(String[] arr) {
        int most = 0;
        int curr = 0;
        for (String s: arr) {
            if (s.isBlank()) {
                if (curr > most) most = curr;
                curr = 0;
            } else {
                curr += Integer.parseInt(s);
            }
        }
        if (curr > most) most = curr;
        return most;
    }
    
    public static int part2(String[] arr) {
        PriorityQueue<Integer> tops = new PriorityQueue<>();
        int curr = 0;
        for (String s: arr) {
            if (s.isBlank()) {
                tops.offer(curr);
                // keep only the best 3
                if (tops.size() > 3) tops.poll();
                curr = 0;
            } else {
                curr += Integer.parseInt(s);
            }
        }
        tops.offer(curr);
        if (tops.size() > 3) tops.poll();
        return tops.poll() + tops.poll() + tops.poll();
    }
}
