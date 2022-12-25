import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day16 {
    // hehe i love records, records so cool
    record Valve(String name, int rate, List<String> tunnels) {};
    
    record Pos(String pos, int minute, int pressure, int total, String opened) {};

    record Pos2(String pos1, String pos2, int minute, int pressure, int total, String opened) {};
    
    record Pos2m(String pos1, String pos2, int minute, int pressure, int total, String opened, int numOpen) {};

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        String[] arr = arrParseS(input);

        // System.out.println(part1(arr));
        
        // part2 takes pretty long to run, ~25 sec. needs improving
        // suspects > cloning the sets, repeated loops
        // got it down to 13s
        // System.out.println(part2(arr));

        // using strings instead of sets reduces to 7-8s
        // ignore 0 rates, 6s
        // tracking number opened, ~5-5.8s
        System.out.println(part2m(arr));
    }
    
    public static int part1(String[] arr) {
        HashMap<String, Valve> valves = new HashMap<String, Valve>();
        for (String s: arr) {
            String name = s.substring(6, 8);
            int rate = stoi(s.substring(s.indexOf("=") + 1, s.indexOf(";")));
            String end = s.substring(s.indexOf("to") + 9);
            String[] ends = end.trim().split(", ");
            List<String> connections = Arrays.asList(ends);
            valves.put(name, new Valve(name, rate, connections));
        }
        // record "position:time" -> best
        int max = 0;
        HashMap<String, Integer> memo = new HashMap<String, Integer>();
        Deque<Pos> queue = new ArrayDeque<Pos>();
        queue.offer(new Pos("AA", 0, 0, 0, ""));
        while (!queue.isEmpty()) {
            Pos curr = queue.poll();
            // update pressure released values and minnute change
            int minute = curr.minute + 1;
            int total = curr.total + curr.pressure;
            int pressure = curr.pressure;
            String opened = curr.opened;
            if (total > max) max = total;
            // not the most optimal so we keep going
            if (memo.getOrDefault(curr.pos + ":" + minute, -1) >= total) continue;
            memo.put(curr.pos + ":" + minute, total);

            // cant go past min 30
            if (minute == 30) continue;
            Valve cv = valves.get(curr.pos);
            // can either stay/open, or move
            // move
            for (String s: cv.tunnels) {
                queue.offer(new Pos(s, minute, curr.pressure, total, opened));
            }
            // stay
            if (!opened.contains(curr.pos)) {
                queue.offer(new Pos(curr.pos, minute, pressure + cv.rate, total, opened + ":" + curr.pos));
            } else {
                queue.offer(new Pos(curr.pos, minute, pressure, total, opened));
            }
        }
        // find max after
        return max;
    }
    
    public static int part2(String[] arr) {
        HashMap<String, Valve> valves = new HashMap<String, Valve>();
        for (String s: arr) {
            String name = s.substring(6, 8);
            int rate = stoi(s.substring(s.indexOf("=") + 1, s.indexOf(";")));
            String end = s.substring(s.indexOf("to") + 9);
            String[] ends = end.trim().split(", ");
            List<String> connections = Arrays.asList(ends);
            valves.put(name, new Valve(name, rate, connections));
        }
        // record "posYou:posElephant:time" -> best
        int max = 0;
        HashMap<String, Integer> memo = new HashMap<String, Integer>();
        Deque<Pos2> queue = new ArrayDeque<Pos2>();
        queue.offer(new Pos2("AA", "AA", 0, 0, 0, ""));
        while (!queue.isEmpty()) {
            Pos2 curr = queue.poll();
            // update pressure released values and minnute change
            int minute = curr.minute + 1;
            int total = curr.total + curr.pressure;
            int pressure = curr.pressure;
            String opened = curr.opened;
            if (total > max) max = total;
            // not the most optimal so we keep going
            if (memo.getOrDefault(curr.pos1 + ":" + curr.pos2 + ":" + minute, -1) >= total) continue;
            memo.put(curr.pos1 + ":" + curr.pos2 + ":" + minute, total);

            // cant go past min 26 now
            if (minute == 26) continue;

            Valve cv1 = valves.get(curr.pos1);
            Valve cv2 = valves.get(curr.pos2);
            // can either stay/open, or move, for both you and elephant
            // both move
            for (String s1: cv1.tunnels) {
                for (String s2: cv2.tunnels) {
                    queue.offer(new Pos2(s1, s2, minute, pressure, total, opened));
                }
            }

            String youmove = opened;
            int pressure1 = pressure;
            if (!youmove.contains(curr.pos2)) {
                youmove += ":" + curr.pos2;
                pressure1 += cv2.rate;
            }
            // you move
            for (String s1: cv1.tunnels) {
                queue.offer(new Pos2(s1, curr.pos2, minute, pressure1, total, youmove));
            }

            String theymove = opened;
            int pressure2 = pressure;
            if (!theymove.contains(curr.pos1)) {
                theymove += ":" + curr.pos1;
                pressure2 += cv1.rate;
            }
            // they move
            for (String s2: cv2.tunnels) {
                queue.offer(new Pos2(curr.pos1, s2, minute, pressure2, total, theymove));
            }

            // both stay
            if (!theymove.contains(curr.pos2)) {
                theymove += ":" + curr.pos2;
                pressure2 += cv2.rate;
            }
            queue.offer(new Pos2(curr.pos1, curr.pos2, minute, pressure2, total, theymove));
        }
        // find max after
        return max;
    }
    
    public static int part2m(String[] arr) {
        HashMap<String, Valve> valves = new HashMap<String, Valve>();
        int nonzeros = 0;
        for (String s: arr) {
            String name = s.substring(6, 8);
            int rate = stoi(s.substring(s.indexOf("=") + 1, s.indexOf(";")));
            if (rate > 0) nonzeros++;
            String end = s.substring(s.indexOf("to") + 9);
            String[] ends = end.trim().split(", ");
            List<String> connections = Arrays.asList(ends);
            valves.put(name, new Valve(name, rate, connections));
        }
        // record "posYou:posElephant:time" -> best
        int max = 0;
        HashMap<String, Integer> memo = new HashMap<String, Integer>();
        Deque<Pos2m> queue = new ArrayDeque<Pos2m>();
        queue.offer(new Pos2m("AA", "AA", 0, 0, 0, "", 0));
        int nump1 = 0;
        while (!queue.isEmpty()) {
            Pos2m curr = queue.poll();
            // update pressure released values and minnute change
            int minute = curr.minute + 1;
            int pressure = curr.pressure;
            int total = curr.total + pressure;
            String opened = curr.opened;
            int nopen = curr.numOpen;
            if (nopen > nump1) nump1 = nopen;
            if (total > max) max = total;
            // not the most optimal so we keep going
            if (memo.getOrDefault(curr.pos1 + ":" + curr.pos2 + ":" + minute, -1) >= total) continue;
            memo.put(curr.pos1 + ":" + curr.pos2 + ":" + minute, total);

            // cant go past min 26 now
            if (minute == 26) continue;

            // all valves are opened, can just both stay and gg go next
            if (nopen >= nonzeros) {
                queue.offer(new Pos2m(curr.pos1, curr.pos2, minute, pressure, total, opened, nopen));
                continue;
            }

            Valve cv1 = valves.get(curr.pos1);
            Valve cv2 = valves.get(curr.pos2);
            // can either stay/open, or move, for both you and elephant
            // both move
            for (String s1: cv1.tunnels) {
                for (String s2: cv2.tunnels) {
                    queue.offer(new Pos2m(s1, s2, minute, pressure, total, opened, nopen));
                }
            }

            String bothstay = opened;
            int pressureBoth = pressure;
            int bothopen = nopen;

            // you move, they stay - ignore flow rate 0s
            if (cv2.rate != 0) {
                if (!opened.contains(curr.pos2)) {
                    bothstay += ':' + curr.pos2;
                    pressureBoth += cv2.rate;
                    bothopen++;
                    for (String s1: cv1.tunnels) {
                        queue.offer(new Pos2m(s1, curr.pos2, minute, pressureBoth, total, bothstay, bothopen));
                    }
                } else {
                    for (String s1: cv1.tunnels) {
                        queue.offer(new Pos2m(s1, curr.pos2, minute, pressure, total, opened, nopen));
                    }
                }
                /*
                for (String s1: cv1.tunnels) {
                    queue.offer(new Pos2m(s1, curr.pos2, minute, pressureThey, total, theystay, theyopen));
                }*/
            }
            /*
            if (cv2.rate != 0) {
                String youmove = opened;
                int pressure1 = pressure;
                int open = 0;
                if (youmove.indexOf(curr.pos2) == -1) {
                    youmove += ":" + curr.pos2;
                    pressure1 += cv2.rate;
                    open++;
                }
                int n = nopen + open;
                for (String s1: cv1.tunnels) {
                    queue.offer(new Pos2m(s1, curr.pos2, minute, pressure1, total, youmove, n));
                }
            }
            */

            // they move, you stay - ignore flow rate 0s
            if (cv1.rate != 0) {
                if (!opened.contains(curr.pos1)) {
                    String youstay = opened + ":" + curr.pos1;
                    int pressureYou = pressure + cv1.rate;
                    int youopen = nopen + 1;
                    bothstay += ':' + curr.pos1;
                    pressureBoth += cv1.rate;
                    bothopen++;
                    for (String s2: cv2.tunnels) {
                        queue.offer(new Pos2m(curr.pos1, s2, minute, pressureYou, total, youstay, youopen));
                    }
                } else {
                    for (String s2: cv2.tunnels) {
                        queue.offer(new Pos2m(curr.pos1, s2, minute, pressure, total, opened, nopen));
                    }
                }
                /*
                for (String s2: cv2.tunnels) {
                    queue.offer(new Pos2m(curr.pos1, s2, minute, pressureYou, total, youstay, youopen));
                }
                */
            }
            /*
            String theymove = opened;
            int pressure2 = pressure;
            int open = nopen;
            if (cv1.rate != 0) {
                if (theymove.indexOf(curr.pos1) == -1) {
                    theymove += ":" + curr.pos1;
                    pressure2 += cv1.rate;
                    open++;
                }
                for (String s2: cv2.tunnels) {
                    queue.offer(new Pos2m(curr.pos1, s2, minute, pressure2, total, theymove, open));
                }
            }
            */

            // both stay
            if (cv1.rate != 0 || cv2.rate != 0) {
                queue.offer(new Pos2m(curr.pos1, curr.pos2, minute, pressureBoth, total, bothstay, bothopen));
            }
            /*
            if (cv1.rate != 0 && cv2.rate != 0) {
                if (theymove.indexOf(curr.pos2) == -1) {
                    theymove += ":" + curr.pos2;
                    pressure2 += cv2.rate;
                    open++;
                }
                queue.offer(new Pos2m(curr.pos1, curr.pos2, minute, pressure2, total, theymove, open));
            }
            */
        }
        // find max after
        return max;
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
}