import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day25 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        System.out.println(part1(arr));
    }

    public static String part1(String[] arr) {
        long m = 0;
        for (String str: arr)
            m += convert(str);
        return toSNAFU(m);
    }

    public static String toSNAFU(long n) {
        char[] map = new char[]{'0', '1', '2', '=', '-'};
        StringBuilder sb = new StringBuilder();
        int carry = 0;
        while (n > 0) {
            long temp = n % 5 + carry;
            carry = 0;

            if (temp > 2) carry++;

            sb.append(map[(int) (temp % 5)]);
            
            n /= 5;
        }
        return sb.reverse().toString();
    }

    public static long convert(String str) {
        char[] arr = str.toCharArray();
        long v = 0;
        long m = 1;
        for (int i = arr.length - 1; i >= 0; i--) {
            switch (arr[i]) {
                case '0':
                    v += (m * 0);
                    break;
                case '1':
                    v += (m * 1);
                    break;
                case '2':
                    v += (m * 2);
                    break;
                case '-':
                    v -= (m * 1);
                    break;
                case '=':
                    v -= (m * 2);
                    break;
            }
            m *= 5;
        }
        return v;
    }
    
    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
    }
}