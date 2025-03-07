import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day16_2024 {
    // Direction enumeration
    enum Direction {
        NORTH(0, -1),
        EAST(1, 0),
        SOUTH(0, 1),
        WEST(-1, 0);

        final int dx, dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        // Get the direction after turning left (counter-clockwise)
        Direction turnLeft() {
            return switch (this) {
                case NORTH -> WEST;
                case EAST -> NORTH;
                case SOUTH -> EAST;
                case WEST -> SOUTH;
            };
        }

        // Get the direction after turning right (clockwise)
        Direction turnRight() {
            return switch (this) {
                case NORTH -> EAST;
                case EAST -> SOUTH;
                case SOUTH -> WEST;
                case WEST -> NORTH;
            };
        }
    }

    // State class to represent position and direction
    static class State {
        int x, y;
        Direction dir;

        State(int x, int y, Direction dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            State state = (State) o;
            return x == state.x && y == state.y && dir == state.dir;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, dir);
        }
    }

    public static void main(String[] args) throws IOException {
        // Read input file
        String[] maze = Files.readString(Paths.get("input.txt")).split("\n");

        // Find start and end positions
        int startX = -1, startY = -1;
        int endX = -1, endY = -1;

        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length(); x++) {
                if (maze[y].charAt(x) == 'S') {
                    startX = x;
                    startY = y;
                } else if (maze[y].charAt(x) == 'E') {
                    endX = x;
                    endY = y;
                }
            }
        }

        if (startX == -1 || endX == -1) {
            System.out.println("Start or end position not found!");
            return;
        }

        // Dijkstra's algorithm to find minimum cost path
        int minScore = findMinScore(maze, startX, startY, endX, endY);
        System.out.println("The lowest score a Reindeer could possibly get: " + minScore);
    }

    private static int findMinScore(String[] maze, int startX, int startY, int endX, int endY) {
        // Initial state: start position facing EAST
        State initialState = new State(startX, startY, Direction.EAST);

        // Priority queue for Dijkstra's algorithm
        PriorityQueue<Map.Entry<State, Integer>> queue = new PriorityQueue<>(
                Comparator.comparingInt(Map.Entry::getValue));

        // Add initial state with score 0
        queue.add(new AbstractMap.SimpleEntry<>(initialState, 0));

        // Keep track of visited states and their minimum scores
        Map<State, Integer> bestScores = new HashMap<>();
        bestScores.put(initialState, 0);

        while (!queue.isEmpty()) {
            // Get the state with the lowest score so far
            Map.Entry<State, Integer> entry = queue.poll();
            State currentState = entry.getKey();
            int currentScore = entry.getValue();

            // Skip if we've found a better path to this state already
            if (bestScores.get(currentState) < currentScore) {
                continue;
            }

            // If we reached the end position, return the score
            if (currentState.x == endX && currentState.y == endY) {
                return currentScore;
            }

            // Try all possible moves: forward, turn left, turn right

            // Move forward
            int newX = currentState.x + currentState.dir.dx;
            int newY = currentState.y + currentState.dir.dy;

            if (isValidPosition(maze, newX, newY)) {
                State newState = new State(newX, newY, currentState.dir);
                int newScore = currentScore + 1; // Forward costs 1 point

                if (!bestScores.containsKey(newState) || newScore < bestScores.get(newState)) {
                    bestScores.put(newState, newScore);
                    queue.add(new AbstractMap.SimpleEntry<>(newState, newScore));
                }
            }

            // Turn left
            Direction leftDir = currentState.dir.turnLeft();
            State leftState = new State(currentState.x, currentState.y, leftDir);
            int leftScore = currentScore + 1000; // Turning costs 1000 points

            if (!bestScores.containsKey(leftState) || leftScore < bestScores.get(leftState)) {
                bestScores.put(leftState, leftScore);
                queue.add(new AbstractMap.SimpleEntry<>(leftState, leftScore));
            }

            // Turn right
            Direction rightDir = currentState.dir.turnRight();
            State rightState = new State(currentState.x, currentState.y, rightDir);
            int rightScore = currentScore + 1000; // Turning costs 1000 points

            if (!bestScores.containsKey(rightState) || rightScore < bestScores.get(rightState)) {
                bestScores.put(rightState, rightScore);
                queue.add(new AbstractMap.SimpleEntry<>(rightState, rightScore));
            }
        }

        return -1; // No path found
    }

    private static boolean isValidPosition(String[] maze, int x, int y) {
        // Check if position is within bounds
        if (y < 0 || y >= maze.length || x < 0 || x >= maze[y].length()) {
            return false;
        }

        // Check if position is not a wall
        return maze[y].charAt(x) != '#';
    }
}