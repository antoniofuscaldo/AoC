import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class day18_2024 {
    // Define the grid size based on the problem statement
    private static final int GRID_SIZE = 71; // 0 to 70 inclusive
    private static final int MAX_BYTES = 1024;

    // Define movement directions (up, right, down, left)
    private static final int[] DX = { 0, 1, 0, -1 };
    private static final int[] DY = { -1, 0, 1, 0 };

    public static void main(String[] args) {
        try {
            // Read the input file
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            List<Point> bytes = new ArrayList<>();

            // Parse input file to get byte positions
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] coordinates = line.split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                bytes.add(new Point(x, y));
            }
            reader.close();

            // Part 1: Find the shortest path after first 1024 bytes
            solvePartOne(bytes);

            // Part 2: Find the first byte that cuts off the path to exit
            solvePartTwo(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void solvePartOne(List<Point> bytes) {
        // Handle the case where there are fewer than 1024 bytes in the input
        int numBytesToSimulate = Math.min(MAX_BYTES, bytes.size());

        // Create the grid with the first 1024 bytes marked as corrupted
        boolean[][] grid = new boolean[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < numBytesToSimulate; i++) {
            Point p = bytes.get(i);
            grid[p.y][p.x] = true; // Mark as corrupted
        }

        // Find the shortest path
        int shortestPath = findShortestPath(grid);

        System.out.println("Part 1 - Minimum number of steps needed: " + shortestPath);
    }

    private static void solvePartTwo(List<Point> bytes) {
        // Start with an empty grid
        boolean[][] grid = new boolean[GRID_SIZE][GRID_SIZE];

        // Try adding bytes one by one until the path is blocked
        for (int i = 0; i < bytes.size(); i++) {
            Point p = bytes.get(i);

            // First, check if this byte would corrupt the start or end position
            // If so, this is immediately the blocking byte
            if ((p.x == 0 && p.y == 0) || (p.x == GRID_SIZE - 1 && p.y == GRID_SIZE - 1)) {
                System.out.println("Part 2 - First blocking byte: " + p.x + "," + p.y);
                return;
            }

            // Add this byte to the grid
            grid[p.y][p.x] = true;

            // Check if a path still exists
            if (findShortestPath(grid) == -1) {
                // No path exists anymore, this is the blocking byte
                System.out.println("Part 2 - First blocking byte: " + p.x + "," + p.y);
                return;
            }
        }

        // If we get here, no byte blocks the path completely
        System.out.println("Part 2 - No blocking byte found in the input");
    }

    /**
     * Finds the shortest path from (0,0) to (GRID_SIZE-1, GRID_SIZE-1)
     * using Breadth-First Search (BFS) algorithm.
     * 
     * @return The shortest path length or -1 if no path exists
     */
    private static int findShortestPath(boolean[][] grid) {
        // Check if start or end positions are corrupted
        if (grid[0][0] || grid[GRID_SIZE - 1][GRID_SIZE - 1]) {
            return -1; // Impossible to reach end
        }

        // Initialize the queue for BFS
        Queue<Point> queue = new LinkedList<>();
        boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
        int[][] distance = new int[GRID_SIZE][GRID_SIZE];

        // Initialize all distances to -1 (unreachable)
        for (int i = 0; i < GRID_SIZE; i++) {
            Arrays.fill(distance[i], -1);
        }

        // Start BFS from (0,0)
        queue.add(new Point(0, 0));
        visited[0][0] = true;
        distance[0][0] = 0;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            // Try all four directions
            for (int i = 0; i < 4; i++) {
                int newX = current.x + DX[i];
                int newY = current.y + DY[i];

                // Check if the new position is valid
                if (isValid(newX, newY, grid, visited)) {
                    queue.add(new Point(newX, newY));
                    visited[newY][newX] = true;
                    distance[newY][newX] = distance[current.y][current.x] + 1;

                    // If we reached the end, return the distance
                    if (newX == GRID_SIZE - 1 && newY == GRID_SIZE - 1) {
                        return distance[newY][newX];
                    }
                }
            }
        }

        // If we can't reach the end, return -1
        return -1;
    }

    /**
     * Checks if a position is valid for the path:
     * - Within grid boundaries
     * - Not corrupted
     * - Not already visited
     */
    private static boolean isValid(int x, int y, boolean[][] grid, boolean[][] visited) {
        return x >= 0 && x < GRID_SIZE &&
                y >= 0 && y < GRID_SIZE &&
                !grid[y][x] && // Not corrupted
                !visited[y][x]; // Not visited
    }

    /**
     * Simple class to represent a point in the grid
     */
    private static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}