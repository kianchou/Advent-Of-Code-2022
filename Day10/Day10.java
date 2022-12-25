import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day10 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        System.out.println(part1(arr));

        part2(arr);
    }

    public static int part1(String[] arr) {
        int x = 1;
        int cycle = 0;
        int sum = 0;
        boolean repeat = false;
        for (int i = 0; i < arr.length; i++) {
            String[] line = arr[i].split(" ");
            cycle++;
            if ((cycle + 20) % 40 == 0) {
                // System.out.println("Cycle " + cycle + ", val = " + x + ", add = " + (cycle * x));
                sum += (cycle * x);
            }
            if (line[0].equals("noop")) {
                continue;
            } else if (repeat == true) {
                repeat = false;
                x += Integer.parseInt(line[1]);
            } else {
                i--;
                repeat = true;
            }
        }
        return sum;
    }

    public static void part2(String[] arr) {
        int x = 1;
        int cycle = 0;
        boolean repeat = false;
        for (int i = 0; i < arr.length; i++) {
            if (cycle % 40 == 0) {
                System.out.print("\n");
            }
            String[] line = arr[i].split(" ");
            if ((cycle % 40)  == x - 1 || (cycle % 40)  == x || (cycle % 40) == x + 1) {
                System.out.print("#");
            } else {
                System.out.print(".");
            }
            cycle++;
            if (line[0].equals("noop")) {
                continue;
            } else if (repeat == true) {
                repeat = false;
                x += Integer.parseInt(line[1]);
            } else {
                i--;
                repeat = true;
            }
        }
    }
    
    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
    }
}
