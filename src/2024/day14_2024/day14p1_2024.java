import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class day14p1_2024 {
    public static void main(String[] args) {
        // Set our grid dimensions and simulation duration.
        final int WIDTH = 101;
        final int HEIGHT = 103;
        final int SECONDS = 100;

        // Determine the "middle" column and row.
        // On a 0-indexed grid with width 101, the middle column is 50.
        // With height 103, the middle row is 51.
        final int midX = WIDTH / 2; // 101/2 evaluates to 50
        final int midY = HEIGHT / 2; // 103/2 evaluates to 51

        // Counters for each quadrant.
        long ulCount = 0, urCount = 0, llCount = 0, lrCount = 0;

        // Read the input file.
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines, if any.
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Split the line into position and velocity parts.
                // Expected format: "p=X,Y v=VX,VY"
                String[] parts = line.split(" ");

                // Remove the "p=" and "v=" prefixes.
                String posPart = parts[0].substring(2);
                String velPart = parts[1].substring(2);

                // Split "X,Y" and "VX,VY" into their respective numbers.
                String[] pos = posPart.split(",");
                String[] vel = velPart.split(",");

                int x = Integer.parseInt(pos[0]);
                int y = Integer.parseInt(pos[1]);
                int vx = Integer.parseInt(vel[0]);
                int vy = Integer.parseInt(vel[1]);

                // Calculate the new position after SECONDS seconds.
                // Because the robots “wrap,” we use modulo arithmetic.
                int newX = mod(x + vx * SECONDS, WIDTH);
                int newY = mod(y + vy * SECONDS, HEIGHT);

                // Skip robots exactly on the division lines.
                if (newX == midX || newY == midY) {
                    continue;
                }

                // Determine the quadrant and count accordingly.
                // Upper quadrants: y < midY; Lower quadrants: y > midY.
                // Left quadrants: x < midX; Right quadrants: x > midX.
                if (newX < midX && newY < midY) {
                    ulCount++;
                } else if (newX > midX && newY < midY) {
                    urCount++;
                } else if (newX < midX && newY > midY) {
                    llCount++;
                } else if (newX > midX && newY > midY) {
                    lrCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // The safety factor is defined as the product of the counts in the four
        // quadrants.
        long safetyFactor = ulCount * urCount * llCount * lrCount;
        System.out.println("Safety Factor: " + safetyFactor);
    }

    /**
     * Computes a proper modulus that always returns a number in the range 0 to m-1,
     * even if the input a is negative.
     */
    private static int mod(int a, int m) {
        int result = a % m;
        if (result < 0) {
            result += m;
        }
        return result;
    }
}
