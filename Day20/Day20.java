import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day20 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        int[] arr = arrParseI(input);

        // System.out.println(Arrays.toString(arr));

        // System.out.println((-1 % 6 + 6) % 6);

        System.out.println(part1(arr));

        System.out.println(part2(arr));
    }

    record Integer2(int a, boolean moved) {};
    // .equals moment
    record Long2(long a, double rand) {};

    public static int part1(int[] arr) {
        int len = arr.length;
        ArrayList<Integer2> list = new ArrayList<>(len);
        for (int i: arr) list.add(new Integer2(i, false));
        int idx = 0;
        while (idx < len) {
            if (list.get(idx).moved) {
                idx++;
                continue;
            }
            int curr = list.remove(idx).a;
            list.add(((idx + curr) % (len - 1) + (len - 1)) % (len - 1), new Integer2(curr, true));
        }

        int zeroIdx = 0;
        for (int i = 0; i < len; i++) {
            if (list.get(i).a == 0) {
                zeroIdx = i;
                break;
            }
        }

        /*
        System.out.println("num1 idx: " + ((zeroIdx + 1000) % len));
        System.out.println("num2 idx: " + ((zeroIdx + 2000) % len));
        System.out.println("num3 idx: " + ((zeroIdx + 3000) % len));
        */

        return list.get((zeroIdx + 1000) % len).a + list.get((zeroIdx + 2000) % len).a + list.get((zeroIdx + 3000) % len).a;
        
    }
    
    public static long part2(int[] arr) {
        int len = arr.length;
        // store original ordering
        Map<Integer, Long2> original = new HashMap<Integer, Long2>();
        // check if it has been moved that turn
        Map<Long2, Boolean> moved  = new HashMap<Long2, Boolean>();
        // store new positions
        ArrayList<Long2> list = new ArrayList<>(len);

        for (int i = 0; i < len; i++) {
            Long2 p = new Long2(arr[i] * 811589153L, Math.random());
            original.put(i, p);
            moved.put(p, false);
            list.add(p);
        }

        for (int i = 0; i < 10; i++) {
            int idx = 0;
            boolean sign = moved.get(original.get(0));
            while (idx < len) {
                if (moved.get(original.get(idx)) != sign) {
                    idx++;
                    // System.out.println(idx);
                    continue;
                }
                // get one to be moved
                Long2 curr = original.get(idx);
                // find it's index to use
                // bro why does .indexOf use .equals vs ==, random moment
                int oldIdx = list.indexOf(curr);
                // calculate the new index using the old index
                int newIdx = (int) (((oldIdx + curr.a) % (len - 1) + (len - 1)) % (len - 1));

                /*
                if (newIdx == 0 && curr.a < 0)
                    newIdx = len - 1;
                */
                
                if (newIdx != oldIdx) {
            
                    // remove the old element
                    list.remove(oldIdx);
                    
                    // for looping backwards (i actually dont fully know)
                    if (newIdx == 0 && curr.a < 0)
                        newIdx = len - 1;

                    list.add(newIdx, curr);
                }
                moved.put(curr, !sign);
            }
        }

        int zeroIdx = 0;
        for (int i = 0; i < len; i++) {
            if (list.get(i).a == 0) {
                zeroIdx = i;
                break;
            }
        }

        /*
        System.out.println("num1: " + list.get((zeroIdx + 1000) % len).a);
        System.out.println("num2: " + list.get((zeroIdx + 2000) % len).a);
        System.out.println("num3: " + list.get((zeroIdx + 3000) % len).a);

        System.out.println(((zeroIdx + 1000) % len) + ", " + ((zeroIdx + 2000) % len) + ", " + ((zeroIdx + 3000) % len));
        */

        return list.get((zeroIdx + 1000) % len).a + list.get((zeroIdx + 2000) % len).a + list.get((zeroIdx + 3000) % len).a;
    }
    
    // man, why are there duplicates
    public static boolean isUnique(int[] array) {
        Set<Integer> set = new HashSet<Integer>();
        for (int i: array) {
            if (!set.add(i)) return false;
        }
        return true;
    }

    // returns the integer value of a string, or 0 if its bad
    public static int stoi(String s) {
        try {
            int x = Integer.parseInt(s);
            return x;
        } catch (Exception e) {
            return 0;
        }
    }
    
    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
    }

    // Parse input to Integer array
    public static int[] arrParseI(String input) {
        String[] arr = arrParseS(input);
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = stoi(arr[i]);
        }
        return res;
    }

}