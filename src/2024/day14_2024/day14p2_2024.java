import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class day14p2_2024 {

    // Inner class to hold each robot's data.
    private static class Robot {
        int x0, y0, vx, vy;

        public Robot(int x0, int y0, int vx, int vy) {
            this.x0 = x0;
            this.y0 = y0;
            this.vx = vx;
            this.vy = vy;
        }
    }

    public static void main(String[] args) {
        final int WIDTH = 101;
        final int HEIGHT = 103;

        // Read the input file into a list of Robot objects.
        ArrayList<Robot> robots = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;
                // Each line is expected to be in the form: "p=X,Y v=VX,VY"
                String[] parts = line.split(" ");
                // Remove the "p=" and "v=" prefixes.
                String posPart = parts[0].substring(2); // after "p="
                String velPart = parts[1].substring(2); // after "v="

                // Split the coordinates (they are separated by commas).
                String[] posCoords = posPart.split(",");
                String[] velCoords = velPart.split(",");

                int x0 = Integer.parseInt(posCoords[0]);
                int y0 = Integer.parseInt(posCoords[1]);
                int vx = Integer.parseInt(velCoords[0]);
                int vy = Integer.parseInt(velCoords[1]);

                robots.add(new Robot(x0, y0, vx, vy));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Because positions wrap modulo WIDTH and HEIGHT and both dimensions are prime,
        // each robot’s x coordinate is periodic with period 101 (if vx isn’t 0) and
        // each y coordinate with period 103. In the worst case the overall period is
        // 101*103 = 10403.
        // We will simulate t from 0 up to 10403 seconds and choose the time when the
        // robots are most clustered.
        int bestTime = 0;
        int bestArea = Integer.MAX_VALUE;

        // We use the toroidal “bounding box” measure.
        // (At time t, for each robot we compute:
        // newX = (x0 + vx * t) mod WIDTH, newY = (y0 + vy * t) mod HEIGHT.
        // Then we compute the minimal arc length containing all newX values on a circle
        // of circumference WIDTH.
        // Similarly for newY (with circumference HEIGHT). Their product is our “area”.)
        int period = 10403; // maximum period for both directions.
        for (int t = 0; t <= period; t++) {
            int[] xs = new int[robots.size()];
            int[] ys = new int[robots.size()];
            for (int i = 0; i < robots.size(); i++) {
                Robot r = robots.get(i);
                xs[i] = mod(r.x0 + r.vx * t, WIDTH);
                ys[i] = mod(r.y0 + r.vy * t, HEIGHT);
            }

            int spanX = circularSpan(xs, WIDTH);
            int spanY = circularSpan(ys, HEIGHT);
            int area = spanX * spanY;

            // We choose the first time we see the smallest area.
            // (Under the assumption the robots display the Easter egg when tightly
            // grouped.)
            if (area < bestArea) {
                bestArea = area;
                bestTime = t;
                // If you wish to track progress, you could print out t and area here.
                // System.out.println("t=" + t + " area=" + area);
            }
        }

        System.out.println("Fewest seconds required for the Easter egg: " + bestTime);

        // Optionally, show the arrangement at bestTime.
        // We will build the grid (of size HEIGHT rows by WIDTH columns) and mark any
        // tile
        // containing one or more robots with a '#' character.
        char[][] grid = new char[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            Arrays.fill(grid[y], '.');
        }

        // Now compute the positions at bestTime.
        for (Robot r : robots) {
            int x = mod(r.x0 + r.vx * bestTime, WIDTH);
            int y = mod(r.y0 + r.vy * bestTime, HEIGHT);
            grid[y][x] = '#';
        }

        System.out.println("\nArrangement at time = " + bestTime + " seconds:");
        for (int y = 0; y < HEIGHT; y++) {
            System.out.println(new String(grid[y]));
        }
    }

    /**
     * Returns a nonnegative modulus result.
     */
    private static int mod(int a, int m) {
        int result = a % m;
        if (result < 0) {
            result += m;
        }
        return result;
    }

    /**
     * Given an array of positions (already in the range 0 .. mod-1),
     * compute the minimal span on a circle of circumference mod that covers all
     * positions.
     * For example, if mod = 101 and the positions (sorted) are [99, 0, 1],
     * the gap from 1 to 99 is 98 but wrapping shows that they actually lie in a
     * span of 3.
     */
    private static int circularSpan(int[] arr, int mod) {
        if (arr.length == 0)
            return 0;
        int[] positions = arr.clone();
        Arrays.sort(positions);

        int largestGap = 0;
        for (int i = 0; i < positions.length - 1; i++) {
            int gap = positions[i + 1] - positions[i];
            if (gap > largestGap) {
                largestGap = gap;
            }
        }
        // Also check the gap wrapping around the end to the beginning.
        int wrapGap = positions[0] + mod - positions[positions.length - 1];
        if (wrapGap > largestGap) {
            largestGap = wrapGap;
        }
        return mod - largestGap;
    }
}
