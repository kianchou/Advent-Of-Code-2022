import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day4 {

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        System.out.println(part1(arr));

        System.out.println(part2(arr));
    }

    public static int part1(String[] arr) {
        int ct = 0;
        for (String s: arr) {
            String[] str = s.split(",");
            int a1 = Integer.parseInt(str[0].substring(0,str[0].indexOf("-")));
            int a2 = Integer.parseInt(str[0].substring(str[0].indexOf("-") + 1));
            int b1 = Integer.parseInt(str[1].substring(0,str[1].indexOf("-")));
            int b2 = Integer.parseInt(str[1].substring(str[1].indexOf("-") + 1));
            if ((a1 <= b1 && b2 <= a2) || (b1 <= a1 && a2 <= b2)) ct++;
        }
        return ct;
    }

    public static int part2(String[] arr) {
        int ct = 0;
        for (String s: arr) {
            String[] str = s.split(",");
            int a1 = Integer.parseInt(str[0].substring(0,str[0].indexOf("-")));
            int a2 = Integer.parseInt(str[0].substring(str[0].indexOf("-") + 1));
            int b1 = Integer.parseInt(str[1].substring(0,str[1].indexOf("-")));
            int b2 = Integer.parseInt(str[1].substring(str[1].indexOf("-") + 1));
            if (a1 <= b2 && b1 <= a2) ct++;
        }
        return ct;
    }

    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
    }
}
