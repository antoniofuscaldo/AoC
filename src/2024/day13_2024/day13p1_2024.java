import java.io.*;
import java.util.regex.*;

public class day13p1_2024 {
    public static void main(String[] args) {
        int totalTokens = 0;

        Pattern patternA = Pattern.compile("Button A: X([+-]\\d+), Y([+-]\\d+)");
        Pattern patternB = Pattern.compile("Button B: X([+-]\\d+), Y([+-]\\d+)");
        Pattern patternPrize = Pattern.compile("Prize: X=([+-]?\\d+), Y=([+-]?\\d+)");

        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                Matcher mA = patternA.matcher(line);
                int xA = 0, yA = 0;
                if (mA.find()) {
                    xA = Integer.parseInt(mA.group(1));
                    yA = Integer.parseInt(mA.group(2));
                }

                line = br.readLine();
                Matcher mB = patternB.matcher(line);
                int xB = 0, yB = 0;
                if (mB.find()) {
                    xB = Integer.parseInt(mB.group(1));
                    yB = Integer.parseInt(mB.group(2));
                }

                line = br.readLine();
                Matcher mPrize = patternPrize.matcher(line);
                int prizeX = 0, prizeY = 0;
                if (mPrize.find()) {
                    prizeX = Integer.parseInt(mPrize.group(1));
                    prizeY = Integer.parseInt(mPrize.group(2));
                }

                int minCost = Integer.MAX_VALUE;
                for (int a = 0; a <= 100; a++) {
                    for (int b = 0; b <= 100; b++) {
                        if (a * xA + b * xB == prizeX && a * yA + b * yB == prizeY) {
                            int cost = 3 * a + b;
                            if (cost < minCost) {
                                minCost = cost;
                            }
                        }
                    }
                }

                if (minCost != Integer.MAX_VALUE) {
                    totalTokens += minCost;
                }
            }

            System.out.println(totalTokens);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
