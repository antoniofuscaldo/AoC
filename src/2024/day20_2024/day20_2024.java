import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day20_2024 {
    private static class Point {
        int row, col;

        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Point point = (Point) o;
            return row == point.row && col == point.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    private static class CheatKey {
        Point start, end;

        CheatKey(Point start, Point end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            CheatKey cheatKey = (CheatKey) o;
            return start.equals(cheatKey.start) && end.equals(cheatKey.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
    }

    private static final int[][] DIRECTIONS = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }; // Up, Down, Left, Right

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));

        char[][] grid = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        Point start = findPoint(grid, 'S');
        Point end = findPoint(grid, 'E');

        // Replace S and E with . for easier path finding
        grid[start.row][start.col] = '.';
        grid[end.row][end.col] = '.';

        // Find the normal shortest path length
        int normalPathLength = findShortestPath(grid, start, end);

        // Part 1: Find all possible 2-step cheats
        int part1 = countCheats(grid, start, end, normalPathLength, 2, 100);
        System.out.println("Part 1: " + part1);

        // Part 2: Find all possible 20-step cheats
        int part2 = countCheats(grid, start, end, normalPathLength, 20, 100);
        System.out.println("Part 2: " + part2);
    }

    private static Point findPoint(char[][] grid, char target) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == target) {
                    return new Point(i, j);
                }
            }
        }
        throw new IllegalArgumentException("Point " + target + " not found in grid");
    }

    private static int findShortestPath(char[][] grid, Point start, Point end) {
        int rows = grid.length;
        int cols = grid[0].length;

        Queue<Point> queue = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];
        int[][] distance = new int[rows][cols];

        queue.add(start);
        visited[start.row][start.col] = true;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.equals(end)) {
                return distance[current.row][current.col];
            }

            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                if (isInBounds(grid, newRow, newCol) && !visited[newRow][newCol] && grid[newRow][newCol] == '.') {
                    queue.add(new Point(newRow, newCol));
                    visited[newRow][newCol] = true;
                    distance[newRow][newCol] = distance[current.row][current.col] + 1;
                }
            }
        }

        return -1; // No path found
    }

    private static int countCheats(char[][] grid, Point start, Point end, int normalPathLength, int maxCheatSteps,
            int minSavedTime) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Calculate distances from start to all points
        int[][] distFromStart = calculateDistances(grid, start);

        // Calculate distances from end to all points (reverse BFS)
        int[][] distFromEnd = calculateDistances(grid, end);

        // Maps to track unique cheats (based on start and end points)
        Set<CheatKey> uniqueCheats = new HashSet<>();

        // For each valid position, perform a BFS allowing up to maxCheatSteps through
        // walls
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] != '.' || distFromStart[r][c] == -1) {
                    continue; // Skip walls and unreachable positions
                }

                // This is a valid cheat start position
                Point cheatStart = new Point(r, c);

                // BFS from this start position, allowing wall passages
                Queue<Point> queue = new LinkedList<>();
                Map<Point, Integer> stepsInCheat = new HashMap<>();

                queue.add(cheatStart);
                stepsInCheat.put(cheatStart, 0);

                while (!queue.isEmpty()) {
                    Point current = queue.poll();
                    int currentSteps = stepsInCheat.get(current);

                    // If we've used up all our cheating steps, stop exploring from this point
                    if (currentSteps >= maxCheatSteps) {
                        continue;
                    }

                    // Try all four directions
                    for (int[] dir : DIRECTIONS) {
                        int newRow = current.row + dir[0];
                        int newCol = current.col + dir[1];

                        // Check if the new position is within bounds
                        if (!isInBounds(grid, newRow, newCol)) {
                            continue;
                        }

                        Point next = new Point(newRow, newCol);

                        // Skip if we've already visited this point with fewer or equal steps
                        if (stepsInCheat.containsKey(next) && stepsInCheat.get(next) <= currentSteps + 1) {
                            continue;
                        }

                        // Add the next position to our queue
                        queue.add(next);
                        stepsInCheat.put(next, currentSteps + 1);

                        // If this is a valid track position and can reach the end
                        if (grid[newRow][newCol] == '.' && distFromEnd[newRow][newCol] != -1) {
                            // Calculate time saved
                            int cheatedPathLength = distFromStart[r][c] + (currentSteps + 1)
                                    + distFromEnd[newRow][newCol];
                            int timeSaved = normalPathLength - cheatedPathLength;

                            if (timeSaved > 0) {
                                // Check if this is a valid cheat end position
                                Point cheatEnd = new Point(newRow, newCol);
                                CheatKey cheatKey = new CheatKey(cheatStart, cheatEnd);

                                // Only add if it's a unique cheat (based on start and end)
                                // and if it saves enough time
                                if (timeSaved >= minSavedTime) {
                                    uniqueCheats.add(cheatKey);
                                }
                            }
                        }
                    }
                }
            }
        }

        return uniqueCheats.size();
    }

    private static int[][] calculateDistances(char[][] grid, Point source) {
        int rows = grid.length;
        int cols = grid[0].length;

        int[][] distances = new int[rows][cols];
        for (int[] row : distances) {
            Arrays.fill(row, -1); // -1 means unreachable
        }

        Queue<Point> queue = new LinkedList<>();
        queue.add(source);
        distances[source.row][source.col] = 0;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                if (isInBounds(grid, newRow, newCol) && grid[newRow][newCol] == '.'
                        && distances[newRow][newCol] == -1) {
                    distances[newRow][newCol] = distances[current.row][current.col] + 1;
                    queue.add(new Point(newRow, newCol));
                }
            }
        }

        return distances;
    }

    private static boolean isInBounds(char[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }
}