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

        // for (String str: arr) System.out.println(Arrays.toString(extractInts(str)));

        // System.out.println(part1(arr));

        System.out.println(part2(arr));
    }

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

    public static int part1(String[] arr) {
        ArrayList<int[]> list = new ArrayList<>();
        for (String str: arr) list.add(extractInts(str));
        int res = 0;
        /*
        for (int[] blueprint: list) {
            // 0: index
            // 1: ore robot cost (ore)
            // 2: clay robot cost (ore)
            // 3,4: obsidian robot cost (ore, clay)
            // 5,6: geode robot cost (ore, obsidian)
            // ore, clay, obsidian, geode
            Deque<Pos> queue = new ArrayDeque<Pos>();
            //       0    1     2         3
            // mats: ore, clay, obsidian, geode
            // robs: ^ same ordering
            queue.offer(new Pos(1, new int[]{0,0,0,0}, new int[]{1,0,0,0}));
            // Map<Integer, int[][]> bests = new HashMap<>();
            int maxOre = max4(blueprint[1], blueprint[2], blueprint[3], blueprint[5]);
            int max = 0;
            while (!queue.isEmpty()) {
                // add to the counts
                Pos curr = queue.poll();
                int mins = curr.minutes + 1;
                // if (bests.containsKey(bests) && isBetter(bests.get(curr.minutes)[0], curr.mats) && isBetter(bests.get(curr.minutes)[1], curr.robs)) continue;
                // bests.put(curr.minutes, new int[][]{curr.mats, curr.robs});
                int[] newMats = new int[4];
                // update mats
                for (int r = 0; r < 4; r++) {
                    newMats[r] = curr.robs[r] + curr.mats[r];
                }
                if (curr.minutes == 23) {
                    if (newMats[3] > max) {
                        max = newMats[3];
                        System.out.println("max:" + max + ", maxOre:" + maxOre + ", maxClay:" + blueprint[4]);
                        System.out.println(Arrays.toString(newMats));
                        System.out.println(Arrays.toString(curr.robs));
                    }
                    continue;
                }
                // try building robots (try building from best to worst)
                // make geode robo
                if (curr.mats[0] >= blueprint[5] && curr.mats[2] >= blueprint[6]) {
                    int[] newRobs = curr.robs.clone();
                    newMats[0] -= blueprint[5];
                    newMats[2] -= blueprint[6];
                    newRobs[3]++;
                    queue.offer(new Pos(mins, newMats, newRobs));
                    continue;
                }
                // try obby robo
                if (curr.mats[0] >= blueprint[3] && curr.mats[1] >= blueprint[4]) {
                    int[] newRobs = curr.robs.clone();
                    newMats[0] -= blueprint[3];
                    newMats[1] -= blueprint[4];
                    newRobs[2]++;
                    queue.offer(new Pos(mins, newMats, newRobs));
                    continue;
                }
                // boolean madeClay = false;
                // try clay robo
                if (curr.robs[1] < blueprint[4] && curr.mats[0] >= blueprint[2]) {
                    int[] newRobs = curr.robs.clone();
                    int[] newMatsC = newMats.clone();
                    newMatsC[0] -= blueprint[2];
                    newRobs[1]++;
                    queue.offer(new Pos(mins, newMatsC, newRobs));
                }
                // try ore robo
                if (curr.robs[0] < maxOre && curr.mats[0] >= blueprint[1]) {
                    int[] newRobs = curr.robs.clone();
                    int[] newMatsD = newMats.clone();
                    newMatsD[0] -= blueprint[1];
                    newRobs[0]++;
                    queue.offer(new Pos(mins, newMats, newRobs));
                }
                // if (newMats[0] < 4) 
                queue.offer(new Pos(mins, newMats, curr.robs));
            }
            System.out.println("max " + blueprint[0] + ": " + max);
            res += (blueprint[0] * max);
        }
        */
        /*
        for (int[] blue: list) {
            int max = 0;
            int maxOre = max4(blue[1], blue[2], blue[3], blue[5]);
            Deque<Pos> queue = new ArrayDeque<Pos>();
            queue.offer(new Pos(0, new int[]{0,0,0,0}, new int[]{1,0,0,0}, new boolean[]{false, false, false, false}));
            while (!queue.isEmpty()) {
                Pos curr = queue.poll();
                // modified material amounts (for end of minute)
                int[] newMaterials = new int[4];
                int[] newRobs = curr.robs.clone();
                for (int i = 0; i < 4; i++) {
                    // current amounts + number of robots
                    newMaterials[i] = curr.mats[i] + curr.robs[i];
                }
                // mins++ here since we done updating materials
                // if at 24 mins, we can stop
                if (curr.minutes + 1 == 24) {
                    if (max < newMaterials[3]) {
                        max = newMaterials[3];
                        System.out.println(max);
                        System.out.println(Arrays.toString(newMaterials));
                        System.out.println(Arrays.toString(curr.robs));
                    }
                    continue;
                }

                if (newMaterials[3] + (25 - curr.minutes) < max) continue;

                boolean[] skip = new boolean[4];
                // try to build a robot (use old materials before gathering, since we build at start of day)
                if (curr.mats[0] >= blue[5] && curr.mats[2] >= blue[6]) {
                    // build geode robot since possible
                    newRobs[3]++;
                    newMaterials[0] -= blue[5];
                    newMaterials[2] -= blue[6];
                    queue.offer(new Pos(curr.minutes + 1, newMaterials.clone(), newRobs.clone(), skip));
                    continue;
                }
                if (!curr.skip[2] && curr.robs[2] < blue[5] && curr.mats[0] >= blue[3] && curr.mats[1] >= blue[4]) {
                    // build obsidian robot since possible?
                    newRobs[2]++;
                    newMaterials[0] -= blue[3];
                    newMaterials[1] -= blue[4];
                    queue.offer(new Pos(curr.minutes + 1, newMaterials.clone(), newRobs.clone(), skip));
                    newMaterials[0] += blue[3];
                    newMaterials[1] += blue[4];
                    newRobs[2]--;
                    skip[2] = true;
                    // continue;
                }
                // this part is stupid, idk try all 3?
                if (!curr.skip[1] && curr.robs[1] < blue[4] && curr.mats[0] >= blue[2]) {
                    newRobs[1]++;
                    newMaterials[0] -= blue[2];
                    queue.offer(new Pos(curr.minutes + 1, newMaterials.clone(), newRobs.clone(), skip));
                    newRobs[1]--;
                    newMaterials[0] += blue[2];
                    skip[1] = true;
                }
                if (!curr.skip[0] && curr.robs[0] < maxOre && curr.mats[0] >= blue[1]) {
                    newRobs[0]++;
                    newMaterials[0] -= blue[1];
                    queue.offer(new Pos(curr.minutes + 1, newMaterials.clone(), newRobs.clone(), skip));
                    newRobs[0]--;
                    newMaterials[0] += blue[1];
                    skip[0] = true;
                }
                queue.offer(new Pos(curr.minutes + 1, newMaterials.clone(), newRobs.clone(), skip));
            }
            System.out.println(blue[0] + " : " + max);
            res += (blue[0] * max);
        }*/

        // boi aint no way boi
        for (int[] blue: list) {
            int max = 0;
            for (int r = 0; r < 500_000; r++) {
                int[] mats = new int[]{0,0,0,0};
                int[] robs = new int[]{1,0,0,0};
                boolean[] build = new boolean[4];
                for (int i = 0; i < 24; i++) {
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
            res += (max * blue[0]);
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

    public static boolean isBetter(int[] a1, int[] a2) {
        for (int i = 0; i < a1.length; i++) if (a1[i] < a2[i]) return false;
        return true;
    }

    public static int max4(int a, int b, int c, int d) {
        int a1 = Math.max(a,b);
        int b1 = Math.max(c,d);
        return Math.max(a1,b1);
    }

    record Pos(int minutes, int[] mats, int[] robs, boolean[] skip) {};

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
    
    // Parse a String input into a string matrix
    public static String[][] gridParseS(String input) {
        String[] rows = input.split("\\r?\\n");
        String[][] result = new String[rows.length][];
        for (int i = 0; i < result.length; i++) {
            result[i] = rows[i].split("");
        }
        return result; 
    }
    
    // Parse an input string into an int matrix
    public static int[][] gridParseI(String input) {
        String[] rows = input.split("\\r?\\n");
        int[][] result = new int[rows.length][];
        for (int i = 0; i < result.length; i++) {
            String[] r = rows[i].split("");
            for (int j = 0; j < r.length; j++) {
                result[i][j] = Integer.parseInt(r[j]);
            }
        }
        return result; 
    }
}