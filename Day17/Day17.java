import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day17 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));

        // this actually took me way too long
        System.out.println(part1(input));

        // especially figuring out the math behind skipping in this part
        System.out.println(part2(input));
    }

    public static int part1(String str) {
        ArrayList<char[][]> shapes = new ArrayList<char[][]>();

        shapes.add(new char[][]{{'#','#','#','#'}});

        shapes.add(new char[][]{{'.','#','.'},
                                {'#','#','#'},
                                {'.','#','.'}});

        shapes.add(new char[][]{{'.','.','#'},
                                {'.','.','#'},
                                {'#','#','#'}});

        shapes.add(new char[][]{{'#'},
                                {'#'},
                                {'#'},
                                {'#'}});

        shapes.add(new char[][]{{'#','#'},
                                {'#','#'}});

        char[] wind = str.toCharArray();
        int currWind = 0;
        int currShape = 0;
        // we need a 2d arraylist for the chamber
        ArrayList<char[]> chamber = new ArrayList<char[]>();
        int lowest = -1;
        for (int i = 0; i < 2022; i++) {
            char[][] shape = shapes.get(currShape);
            currShape = (currShape + 1) % 5;
            int y = lowest + 3 + shape.length;
            // add empty rows as needed
            for (int z = chamber.size(); z <= y; z++) chamber.add(new char[]{'.','.','.','.','.','.','.'});
            // starts with topleft-most point at size-1,2
            int x = 2;
            boolean falling = true;
            while (falling) {
                // check wind
                boolean canmove = true;
                if (wind[currWind] == '<') {
                    for (int a = 0; a < shape.length; a++) {
                        for (int b = 0; b < shape[a].length; b++) {
                            if (shape[a][b] == '.') continue;
                            if (x + b - 1 < 0 || chamber.get(y - a)[x + b - 1] == '#') canmove = false; 
                        }
                    }
                    if (canmove) x--;
                } else {
                    for (int a = 0; a < shape.length; a++) {
                        for (int b = 0; b < shape[a].length; b++) {
                            if (shape[a][b] == '.') continue;
                            if (x + b + 1 >= chamber.get(y - a).length || chamber.get(y - a)[x + b + 1] == '#') canmove = false; 
                        }
                    }
                    if (canmove) x++;
                }
                // System.out.println(x);
                currWind = (currWind + 1) % wind.length;
                // System.out.println(currWind);
                // now move down
                for (int a = 0; a < shape.length; a++) {
                    for (int b = 0; b < shape[a].length; b++) {
                        if (y - a <= 0 || (chamber.get(y - a - 1)[x + b] == '#' && shape[a][b] == '#')) falling = false;
                    }
                }
                if (y - (shape.length - 1) <= 0) {
                    falling = false;
                    break;
                }
                else if (falling) y--;
            }

            // put the shape in
            for (int a = 0; a < shape.length; a++) {
                for (int b = 0; b < shape[a].length; b++) {
                    if (shape[a][b] == '#') {
                        chamber.get(y - a)[x + b] = shape[a][b];
                        if (y - a > lowest) lowest = y - a;
                    }
                }
            }
        }

            // System.out.println("------------");
            // for (int i = chamber.size() - 1; i >= 0; i--) {
            // System.out.print("|");
            // for (char c: chamber.get(i)) {
            //     System.out.print(c);
            // }
            // System.out.print("|\n");
            // }
            // System.out.println("-----------------");

        return lowest + 1;
    }

    public static long part2(String str) {
        // c0,c1,c2,c3,c4,c5,c6,currWind,currShape -> counts;
        HashMap<String, String> skip = new HashMap<String, String>(); 
        ArrayList<char[][]> shapes = new ArrayList<char[][]>();

        shapes.add(new char[][]{{'#','#','#','#'}});

        shapes.add(new char[][]{{'.','#','.'},
                                {'#','#','#'},
                                {'.','#','.'}});

        shapes.add(new char[][]{{'.','.','#'},
                                {'.','.','#'},
                                {'#','#','#'}});

        shapes.add(new char[][]{{'#'},
                                {'#'},
                                {'#'},
                                {'#'}});

        shapes.add(new char[][]{{'#','#'},
                                {'#','#'}});

        char[] wind = str.toCharArray();
        int currWind = 0;
        int currShape = 0;
        // we need a 2d arraylist for the chamber
        ArrayList<char[]> chamber = new ArrayList<char[]>();
        int lowest = -1;
        long rocks = 0;
        long skipheight = 0;
        String dupe = ""; // <== we looking for this
        while (rocks < 1000000000000L) {
            char[][] shape = shapes.get(currShape);
            currShape = (currShape + 1) % 5;
            int y = lowest + 3 + shape.length;
            // add empty rows as needed
            for (int z = chamber.size(); z <= y; z++) chamber.add(new char[]{'.','.','.','.','.','.','.'});
            // starts with topleft-most point at size-1,2
            int x = 2;
            boolean falling = true;
            while (falling) {
                // check wind
                boolean canmove = true;
                if (wind[currWind] == '<') {
                    for (int a = 0; a < shape.length; a++) {
                        for (int b = 0; b < shape[a].length; b++) {
                            if (shape[a][b] == '.') continue;
                            if (x + b - 1 < 0 || chamber.get(y - a)[x + b - 1] == '#') canmove = false; 
                        }
                    }
                    if (canmove) x--;
                } else {
                    for (int a = 0; a < shape.length; a++) {
                        for (int b = 0; b < shape[a].length; b++) {
                            if (shape[a][b] == '.') continue;
                            if (x + b + 1 >= chamber.get(y - a).length || chamber.get(y - a)[x + b + 1] == '#') canmove = false; 
                        }
                    }
                    if (canmove) x++;
                }
                // System.out.println(x);
                currWind = (currWind + 1) % wind.length;
                // System.out.println(currWind);
                // now move down
                for (int a = 0; a < shape.length; a++) {
                    for (int b = 0; b < shape[a].length; b++) {
                        if (y - a <= 0 || (chamber.get(y - a - 1)[x + b] == '#' && shape[a][b] == '#')) falling = false;
                    }
                }
                if (y - (shape.length - 1) <= 0) {
                    falling = false;
                    break;
                }
                else if (falling) y--;
            }

            // put the shape in
            for (int a = 0; a < shape.length; a++) {
                for (int b = 0; b < shape[a].length; b++) {
                    if (shape[a][b] == '#') {
                        chamber.get(y - a)[x + b] = shape[a][b];
                        if (y - a > lowest) lowest = y - a;
                    }
                }
            }
            rocks++;
            // add a skip
            int min = chamber.size();
            int[] pos = new int[7];
            for (int col = 0; col < 7; col++) {
                int ypos = chamber.size() - 1;
                while (ypos > 0 && chamber.get(ypos)[col] != '#') ypos--;
                if (ypos < min) min = ypos;
                pos[col] = ypos;
            }
            String keystr = (pos[0] - min) + "," + (pos[1] - min) + "," + (pos[2] - min) + "," + (pos[3] - min) + "," + (pos[4] - min) + "," + (pos[5] - min) + "," + (pos[6] - min) + "," + currWind + "," + currShape;
            /*
            if (keystr.equals("0,0,2,9,9,9,9,1796,1")) {
                System.out.println(keystr + "\n" + lowest);
            }
            */
            // found the first dupe - can perform skip
            if (dupe.isEmpty()) {
                if (skip.containsKey(keystr)) {
                    dupe = keystr;
                    String[] data = skip.get(dupe).split(",");
                    long temp = (1000000000000L - stoi(data[1]))/(rocks - stoi(data[1]));
                    // minus 1 to not recount the first part already done
                    skipheight = (temp - 1) * (lowest - stoi(data[0]));
                    // should be temp-1 * rockskip VV (if i did math correct)
                    rocks += (temp - 1) * (rocks - stoi(data[1]));
                } else {
                    skip.put(keystr, lowest + "," + rocks);
                }
            }
        }
        /*
        // dupe = first duplicate key
        String[] info = dupe.split(",");
        String[] data = skip.get(dupe).split(",");
        // number of rocks we can skip at a time
        long rockskip = rocks - stoi(data[1]);
        // the height we can skip
        int heightskip = lowest - stoi(data[0]);
        currWind = stoi(info[7]);
        currShape = stoi(info[8]);
        System.out.println("rockskip: " + rockskip + ", heightskip: " + heightskip);
        System.out.println(Arrays.toString(info));
        System.out.println(Arrays.toString(data));
        long newPos = 0;
        while (newPos + rockskip < 1000000000000L) newPos += rockskip;
        System.out.println(newPos);
        long currPos = ((((1000000000000L - stoi(data[1]))/ rockskip) - 1) * heightskip) + stoi(data[0]);
        System.out.println("current height (after skip) = " + currPos);
        // long currPos = stoi(data[0]) + (1000000000000L - stoi)
        // now setup the battelfield
        // convert the stored positions to ints
        int[] field = new int[7];
        lowest = 0;
        for (int i = 0; i < 7; i++) {
            field[i] = stoi(info[i]);
            if (field[i] > lowest) lowest = field[i];
        }
        ArrayList<char[]> chamber2 = new ArrayList<char[]>();
        for (int i = 0; i <= lowest; i++) chamber2.add(new char[]{'.','.','.','.','.','.','.'});
        for (int i = 0; i < field.length; i++) {
            chamber2.get(field[i])[i] = '#';
        }
        long newRocks = 1000000000000L - rockskip + stoi(data[1]);
        System.out.println(newRocks);
        /* bruh this may or may not work */

        return lowest + skipheight + 1;
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