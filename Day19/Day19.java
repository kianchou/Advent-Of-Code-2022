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

        List<int[]> blueprints = getBlueprints(arr);

        // part 1 works good now (kinda)
        // originally used iterative dfs
        System.out.println(part1(blueprints, 24));

        // for some reason blueprint 1 keeps giving 37, while the random algo gives 38 (real)
        // rewrote the dfs recursively and now it works correctly in ~5 seconds???
        // found out the optimal solution is not greedy, for blueprint 1 the optimal path is to make the 
        // first geode robot during minute 22 rather than during minute 21. optimized down to ~2 seconds
        System.out.println(part2(blueprints, 32));
    }

    public static int[] maxVals;

    record Pos(int ore, int clay, int obs, int geo,
               int oreR, int clayR, int obsR, int geoR,
               boolean prevOre, boolean prevClay, boolean prevObs) {};

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

    public static List<int[]> getBlueprints(String[] arr) {
        List<int[]> list = new ArrayList<>();
        for (String str: arr) 
            list.add(extractInts(str));
        return list;
    }

    // types: 0: ore, 1: clay, 2: obsidian, 3: geode
    public static boolean canMake(int robotType, Pos curr, int[] blueprint) {
        switch (robotType) {
            case 0: 
                return (curr.ore >= blueprint[1]);
            case 1: 
                return (curr.ore >= blueprint[2]);
            case 2: 
                return (curr.ore >= blueprint[3] && curr.clay >= blueprint[4]);
            case 3: 
                return (curr.ore >= blueprint[5] && curr.obs >= blueprint[6]);
        }
        return false;
    }

    public static int dfs(int min, int limit, Pos curr, int[] blueprint) {
        min++;
        // first update material values
        int newOre = curr.ore + curr.oreR;
        int newClay = curr.clay + curr.clayR;
        int newObs = curr.obs + curr.obsR;
        int newGeo = curr.geo + curr.geoR;

        int max = newGeo;

        if (min == limit) 
            return max;

        // + 1 since it is possible to be better off making the geode robot next step
        if (curr.geoR + 1 < maxVals[min - 1]) return max;
        if (curr.geoR > maxVals[min - 1]) maxVals[min - 1] = curr.geoR;

        // now update number of robots
        Pos make_geo = null, make_obs = null, make_clay = null, make_ore = null;

        // geode robot
        if (canMake(3, curr, blueprint)) {
            make_geo = new Pos(newOre - blueprint[5], newClay, newObs - blueprint[6], newGeo, curr.oreR, curr.clayR, 
                curr.obsR, curr.geoR + 1, false, false, false);
            max = Math.max(max, dfs(min, limit, make_geo, blueprint));
            
            Pos make_none = new Pos(newOre, newClay, newObs, newGeo, curr.oreR, curr.clayR, 
                curr.obsR, curr.geoR, false, false, false);
            return Math.max(max, dfs(min, limit, make_none, blueprint));
        }

        // obsidian robot
        if (curr.obsR < blueprint[6] && canMake(2, curr, blueprint)) {
            if (curr.prevObs) return max;
            make_obs = new Pos(newOre - blueprint[3], newClay - blueprint[4], newObs, newGeo, curr.oreR, curr.clayR, 
                curr.obsR + 1, curr.geoR, false, false, false);
            max = Math.max(max, dfs(min, limit, make_obs, blueprint));
        }

        // clay robot
        if (curr.clayR < blueprint[4] && canMake(1, curr, blueprint)) {
            // if (curr.prevClay) return max;
            make_clay = new Pos(newOre - blueprint[2], newClay, newObs, newGeo, curr.oreR, curr.clayR + 1, 
                curr.obsR, curr.geoR, false, false, false);
            max = Math.max(max, dfs(min, limit, make_clay, blueprint));
        }

        if (curr.oreR < 4 && canMake(0, curr, blueprint)) {
            if (curr.prevOre) return max;
            make_ore = new Pos(newOre - blueprint[1], newClay, newObs, newGeo, curr.oreR + 1, curr.clayR,
                curr.obsR, curr.geoR, false, false, false);
            max = Math.max(max, dfs(min, limit, make_ore, blueprint));
        }

        Pos make_none = new Pos(newOre, newClay, newObs, newGeo, curr.oreR, curr.clayR, 
            curr.obsR, curr.geoR, make_ore != null, make_clay != null, make_obs != null);
        max = Math.max(max, dfs(min, limit, make_none, blueprint));

        // if (max > part1Max) part1Max = max;
        return max;
    }

    public static int part1(List<int[]> blueprints, int time) {
        int sum = 0;
        for (int[] blueprint: blueprints) {
            maxVals = new int[time];
            Pos p = new Pos(0, 0, 0, 0, 1, 0, 0, 0, false, false, false);
            int max = dfs(0, time, p, blueprint);
            // int max = dfsP(24, blueprint);
            // System.out.print(max + " ");
            sum += (blueprint[0] * max);
        }
        return sum;
    }

    public static int part2(List<int[]> blueprints, int time) {
        int res = 1;
        for (int i = 0; i < 3; i++) {
            int[] blueprint = blueprints.get(i);
            maxVals = new int[time];
            Pos p = new Pos(0, 0, 0, 0, 1, 0, 0, 0, false, false, false);
            // int max = dfsP(time, blueprint);
            int max = dfs(0, time, p, blueprint);
            // System.out.println(max);
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