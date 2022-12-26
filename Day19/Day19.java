import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class Day19 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        System.out.println(part1(arr));

        // System.out.println(part2(arr));
    }

    record Pos(int ore, int clay, int obs, int geo,
               int oreR, int clayR, int obsR, int geoR,
               boolean prevOre, boolean prevClay, boolean prevObs,
               int min) {};

    // thanks ChatGPT
    public static int[] extractInts(String input) {
        List<Integer> intList = new ArrayList<>();
    
        // Use a regular expression to extract all integers from the string
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(input);
        while (m.find()) {
          intList.add(Integer.parseInt(m.group()));
        }
    
        // Convert the list of integers to an array
        int[] ints = new int[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
          ints[i] = intList.get(i);
        }
        return ints;
    }

    public static int dfs(int rounds, int[] blueprint) {
        // blueprint: [index, ore, ore, ore, clay, ore, obsidian]
        // storing as array of robots, and array of materials
        // int[0] = # robots of {ore, clay, obsidian, geode}
        // int[1] = # materials of {ore, clay, obsidian, geode}
        // int[2] = minute
        Deque<Pos> stack = new ArrayDeque<Pos>();
        stack.push(new Pos(0,0,0,0,1,0,0,0,false,false,false,0));
        // at minute (i - 1), record the max amount of geodes made
        int[] minMax = new int[rounds];
        int max = 0;
        // calculate the max number of ore robots we should ever need at once
        int maxOre = max4(blueprint[1], blueprint[2], blueprint[3], blueprint[5]);
        while (!stack.isEmpty()) {
            Pos curr = stack.pop();
            int minute = curr.min + 1;
            // System.out.printf("minute: %d, size: %d\n", minute, reps);
            // find the new values for materials
            int newOre = curr.ore + curr.oreR;
            int newClay = curr.clay + curr.clayR;
            int newObs = curr.obs + curr.obsR;
            int newGeo = curr.geo + curr.geoR;

            // at final minute, we dont keep making more robots so we finish
            if (minute == rounds) {
                if (newGeo > max)
                    max = newGeo;
                continue;
            }

            if (newGeo < minMax[curr.min]) continue;
            if (newGeo > minMax[curr.min]) minMax[curr.min] = newGeo;

            // technically "start of minute", see which robots you can build from old materials
            boolean makeOre = false, makeClay = false, makeObs = false;
            // check if geode robot can be made
            if (curr.obs >= blueprint[6] && curr.ore >= blueprint[5]) {
                stack.push(new Pos(newOre - blueprint[5], newClay, newObs - blueprint[6], newGeo,
                    curr.oreR, curr.clayR, curr.obsR, curr.geoR + 1, false, false, false, minute));
                // continue;
            }
            // check if obsidian robot can be made
            if (curr.obsR < blueprint[6] && curr.clay >= blueprint[4] && curr.ore >= blueprint[3]) {
                if (!curr.prevObs) {
                stack.push(new Pos(newOre - blueprint[3], newClay - blueprint[4], newObs, newGeo,
                    curr.oreR, curr.clayR, curr.obsR + 1, curr.geoR, false, false, false, minute));
                makeObs = true;
                }
            }
            // check if clay robot can be made (and if it even should be made)
            if (curr.clayR < blueprint[4] && curr.ore >= blueprint[2]) {
                // if (!curr.prevClay) {
                stack.push(new Pos(newOre - blueprint[2], newClay, newObs, newGeo,
                    curr.oreR, curr.clayR + 1, curr.obsR, curr.geoR, false, false, makeObs, minute));
                makeClay = true;
                // }
            }
            // check if ore robot can be made (and if it even should be made)
            if (curr.oreR < maxOre && curr.ore >= blueprint[1]) {
                if (!curr.prevOre) { 
                stack.push(new Pos(newOre - blueprint[1], newClay, newObs, newGeo,
                    curr.oreR + 1, curr.clayR, curr.obsR, curr.geoR, false, makeClay, makeObs, minute));
                makeOre = true;
                }
            }
            stack.push(new Pos(newOre, newClay, newObs, newGeo,
                curr.oreR, curr.clayR, curr.obsR, curr.geoR, makeOre, makeClay, makeObs, minute));
        }
        // System.out.println(" " + max);
        // System.out.println(best);
        return max;
    }

    public static int part1(String[] arr) {
        ArrayList<int[]> list = new ArrayList<>();
        for (String str: arr) list.add(extractInts(str));
        int res = 0;
        
        for (int[] blueprint: list) {
            int max = dfs(24, blueprint);
            System.out.print(max + " ");
            res += (blueprint[0] * max);
        }

        return res;
    }

    public static int part2(String[] arr) {
        ArrayList<int[]> list = new ArrayList<>();
        for (String str: arr) list.add(extractInts(str));
        int res = 1;
        for (int b = 0; b < 3; b++) {
            int[] blue = list.get(b);
            int max = 0;
            for (int r = 0; r < 5_000_000; r++) {
                int[] mats = new int[]{0,0,0,0};
                int[] robs = new int[]{1,0,0,0};
                boolean[] build = new boolean[4];
                for (int i = 0; i < 32; i++) {
                    double rnd = Math.random();
                    if (mats[0] >= blue[5] && mats[2] >= blue[6]) {
                        mats[0] -= blue[5];
                        mats[2] -= blue[6];
                        build[3] = true;
                    } else if (rnd <= 0.3 && mats[0] >= blue[1]) {
                        mats[0] -= blue[1];
                        build[0] = true;
                    } else if (rnd <= 0.7 && mats[0] >= blue[3] && mats[1] >= blue[4]) {
                        mats[0] -= blue[3];
                        mats[1] -= blue[4];
                        build[2] = true;
                    } else if (rnd <= 0.9 && mats[0] >= blue[2]) {
                        mats[0] -= blue[2];
                        build[1] = true;
                    }
                    // update mats
                    for (int j = 0; j < 4; j++) {
                        mats[j] += robs[j];
                    }
                    // update robos
                    for (int j = 0; j < 4; j++) {
                        if (build[j]) {
                            build[j] = false;
                            robs[j]++;
                            break;
                        }
                    }
                }
                if (mats[3] > max) {
                    // System.out.println(max);
                    // System.out.println(Arrays.toString(robs));
                    // System.out.println(Arrays.toString(mats));
                    max = mats[3];
                }
            }
            System.out.println(max);
            res *= max;
        }
        
        return res;
    }

    public static int max4(int a, int b, int c, int d) {
        int a1 = Math.max(a,b);
        int b1 = Math.max(c,d);
        return Math.max(a1,b1);
    }
    
    // Parse input to String array
    public static String[] arrParseS(String input) {
        return input.split("\\r?\\n");
    }
}