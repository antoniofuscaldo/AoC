import java.io.*;
import java.util.regex.*;

public class day13p2_2024 {
    public static void main(String[] args) {
        long totalTokens = 0;
        long shift = 10000000000000L;

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
                int A_x = 0, A_y = 0;
                if (mA.find()) {
                    A_x = Integer.parseInt(mA.group(1));
                    A_y = Integer.parseInt(mA.group(2));
                }

                line = br.readLine();
                Matcher mB = patternB.matcher(line);
                int B_x = 0, B_y = 0;
                if (mB.find()) {
                    B_x = Integer.parseInt(mB.group(1));
                    B_y = Integer.parseInt(mB.group(2));
                }

                line = br.readLine();
                Matcher mPrize = patternPrize.matcher(line);
                long prizeX = 0, prizeY = 0;
                if (mPrize.find()) {
                    prizeX = Long.parseLong(mPrize.group(1));
                    prizeY = Long.parseLong(mPrize.group(2));
                }

                prizeX += shift;
                prizeY += shift;

                long det = (long) A_x * B_y - (long) A_y * B_x;
                if (det == 0) {
                    continue;
                }

                long numA = prizeX * B_y - prizeY * B_x;
                long numB = A_x * prizeY - A_y * prizeX;

                if (numA % det != 0 || numB % det != 0) {
                    continue;
                }

                long a = numA / det;
                long b = numB / det;

                if (a < 0 || b < 0) {
                    continue;
                }

                long cost = 3 * a + b;
                totalTokens += cost;
            }

            System.out.println(totalTokens);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
