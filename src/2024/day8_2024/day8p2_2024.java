import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day8p2_2024 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        if (lines.isEmpty()) {
            System.out.println(0);
            return;
        }

        int rows = lines.size();
        int cols = lines.get(0).length();

        Map<Character, List<int[]>> antennaGroups = new HashMap<>();
        for (int r = 0; r < rows; r++) {
            String line = lines.get(r);
            for (int c = 0; c < cols; c++) {
                char ch = line.charAt(c);
                if (ch != '.') {
                    antennaGroups.computeIfAbsent(ch, k -> new ArrayList<>()).add(new int[] { r, c });
                }
            }
        }

        Set<String> antinodePositions = new HashSet<>();

        for (Map.Entry<Character, List<int[]>> entry : antennaGroups.entrySet()) {
            List<int[]> positions = entry.getValue();
            if (positions.size() < 2) {
                continue;
            }

            Set<String> lineSet = new HashSet<>();
            int n = positions.size();
            for (int i = 0; i < n; i++) {
                int[] posA = positions.get(i);
                for (int j = i + 1; j < n; j++) {
                    int[] posB = positions.get(j);

                    int dR = posB[0] - posA[0];
                    int dC = posB[1] - posA[1];

                    int g = gcd(Math.abs(dR), Math.abs(dC));
                    if (g != 0) {
                        dR /= g;
                        dC /= g;
                    }

                    if (dR < 0 || (dR == 0 && dC < 0)) {
                        dR = -dR;
                        dC = -dC;
                    }

                    int kVal = dC * posA[0] - dR * posA[1];

                    String lineKey = dR + "," + dC + "," + kVal;
                    lineSet.add(lineKey);
                }
            }

            for (String lineKey : lineSet) {
                String[] tokens = lineKey.split(",");
                int dR = Integer.parseInt(tokens[0]);
                int dC = Integer.parseInt(tokens[1]);
                int kVal = Integer.parseInt(tokens[2]);

                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        if (dC * r - dR * c == kVal) {
                            antinodePositions.add(r + "," + c);
                        }
                    }
                }
            }
        }

        System.out.println(antinodePositions.size());
    }

    private static int gcd(int a, int b) {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }
}
