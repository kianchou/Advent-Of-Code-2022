import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day11 {
    public static class Monkey {
        public Deque<Long> items;
        public String op;
        public int test;
        public int ifTrue;
        public int ifFalse;
        Monkey(Deque<Integer> i, String o, int t, int ifT, int ifF) {
            items = new ArrayDeque<Long>();
            while (!i.isEmpty()) {
                items.push(Integer.toUnsignedLong(i.pop()));
            }
            // items = i;
            op = o;
            test = t;
            ifTrue = ifT;
            ifFalse = ifF;
        }
        public String toString() {
            return "Items: " + items.toString() + 
            "\n Operation: " + op + 
            "\n If divisible by " + test + 
            "\n TRUE: pass to " + ifTrue +
            "\n FALSE: pass to " + ifFalse;
        }
    }
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        // System.out.println(Arrays.toString(arr));

        System.out.println(part1(arr, 8, 20));

        System.out.println(part2(arr, 8, 10000));
    }

    public static long part1(String[] arr, int number, int runs) {
        Monkey[] monkeys = new Monkey[number];
        long[] times = new long[number];
        for (int i = 0; i < number; i++) {
            String[] items = arr[(i * 7) + 1].substring(arr[(i * 7) + 1].indexOf(":") + 2).split(", ");
            Deque<Integer> itemList = new ArrayDeque<Integer>();
            for (String s: items) {
                itemList.push(Integer.parseInt(s));
            }
            String op = arr[(i * 7) + 2].substring(arr[(i * 7) + 2].indexOf("=") + 2);
            int test = Integer.parseInt(arr[(i * 7) + 3].substring(arr[(i * 7) + 3].indexOf("by") + 3));
            int ifTrue = Integer.parseInt(arr[(i * 7) + 4].substring(arr[(i * 7) + 4].indexOf("monkey") + 7));
            int ifFalse = Integer.parseInt(arr[(i * 7) + 5].substring(arr[(i * 7) + 5].indexOf("monkey") + 7));
            monkeys[i] = new Monkey(itemList, op, test, ifTrue, ifFalse);
        }
        for (int i = 0; i < runs; i++) {
            for (int j = 0; j < number; j++) {
                Monkey m = monkeys[j];
                // check every item in from that monkey
                while (!m.items.isEmpty()) {
                    times[j]++;
                    long item = m.items.pop();
                    String[] op = m.op.split(" ");
                    long n1 = op[0].equals("old") ? item : Integer.parseInt(op[0]);
                    long n2 = op[2].equals("old") ? item : Integer.parseInt(op[2]);
                    long worry = 0;
                    if (op[1].equals("+")) {
                        worry = n1 + n2;
                    } else if (op[1].equals("-")) {
                        worry = n1 - n2;
                    } else if (op[1].equals("*")) {
                        worry = n1 * n2;
                    }
                    worry /= 3;
                    if (worry % m.test == 0) {
                        monkeys[m.ifTrue].items.push(worry);
                    } else {
                        monkeys[m.ifFalse].items.push(worry);
                    }
                }
            }
        }
        // System.out.println(Arrays.toString(times));
        Arrays.sort(times);
        return times[times.length - 1] * times[times.length - 2];
    }

    public static long part2(String[] arr, int number, int runs) {
        Monkey[] monkeys = new Monkey[number];
        long[] times = new long[number];
        for (int i = 0; i < number; i++) {
            String[] items = arr[(i * 7) + 1].substring(arr[(i * 7) + 1].indexOf(":") + 2).split(", ");
            Deque<Integer> itemList = new ArrayDeque<Integer>();
            for (String s: items) {
                itemList.push(Integer.parseInt(s));
            }

            String op = arr[(i * 7) + 2].substring(arr[(i * 7) + 2].indexOf("=") + 2);

            int test = Integer.parseInt(arr[(i * 7) + 3].substring(arr[(i * 7) + 3].indexOf("by") + 3));

            int ifTrue = Integer.parseInt(arr[(i * 7) + 4].substring(arr[(i * 7) + 4].indexOf("monkey") + 7));
            int ifFalse = Integer.parseInt(arr[(i * 7) + 5].substring(arr[(i * 7) + 5].indexOf("monkey") + 7));
            monkeys[i] = new Monkey(itemList, op, test, ifTrue, ifFalse);
        }
        int mod = 1;
        for (Monkey m: monkeys) {
            mod *= m.test;
        }
        // System.out.println(mod);
        for (int i = 0; i < runs; i++) {
            for (int j = 0; j < number; j++) {
                Monkey m = monkeys[j];
                // check every item in from that monkey
                while (!m.items.isEmpty()) {
                    times[j]++;
                    long item = m.items.pop();
                    String[] op = m.op.split(" ");
                    long n1 = op[0].equals("old") ? item : Integer.parseInt(op[0]);
                    long n2 = op[2].equals("old") ? item : Integer.parseInt(op[2]);
                    long worry = 0;
                    if (op[1].equals("+")) {
                        worry = n1 + n2;
                    } else if (op[1].equals("-")) {
                        worry = n1 - n2;
                    } else if (op[1].equals("*")) {
                        worry = n1 * n2;
                    }

                    if (worry % m.test == 0) {
                        monkeys[m.ifTrue].items.push(worry % mod);
                    } else {
                        monkeys[m.ifFalse].items.push(worry % mod);
                    }
                }
            }
        }
        /*
        int x = 0;
        for (Monkey m: monkeys) {
            System.out.println("Monkey " + x++);
            System.out.println(m);
        } 
        */
        // System.out.println(Arrays.toString(times));
        Arrays.sort(times);
        return times[times.length - 1] * times[times.length - 2];
    }
    
    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
    }
}
