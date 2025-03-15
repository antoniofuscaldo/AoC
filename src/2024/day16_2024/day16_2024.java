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

        // Part 1: Find minimum score
        int minScore = findMinScore(maze, startX, startY, endX, endY);
        System.out.println("Part 1: The lowest score a Reindeer could possibly get: " + minScore);

        // Part 2: Find all tiles that are part of a best path
        Set<String> optimalPathTiles = findOptimalPathTiles(maze, startX, startY, endX, endY, minScore);
        System.out.println("Part 2: Number of tiles that are part of optimal paths: " + optimalPathTiles.size());

        // Print maze with optimal path tiles marked
        printOptimalPaths(maze, optimalPathTiles);
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

    private static Set<String> findOptimalPathTiles(String[] maze, int startX, int startY, int endX, int endY,
            int targetScore) {
        // Initial state: start position facing EAST
        State initialState = new State(startX, startY, Direction.EAST);

        // Priority queue for Dijkstra's algorithm
        PriorityQueue<Map.Entry<State, Integer>> queue = new PriorityQueue<>(
                Comparator.comparingInt(Map.Entry::getValue));

        // Add initial state with score 0
        queue.add(new AbstractMap.SimpleEntry<>(initialState, 0));

        // Maps to track scores
        Map<State, Integer> bestScores = new HashMap<>();
        bestScores.put(initialState, 0);

        // Maps to track predecessors for reconstructing paths
        Map<State, List<State>> predecessors = new HashMap<>();
        predecessors.put(initialState, new ArrayList<>());

        while (!queue.isEmpty()) {
            // Get the state with the lowest score so far
            Map.Entry<State, Integer> entry = queue.poll();
            State currentState = entry.getKey();
            int currentScore = entry.getValue();

            // Skip if we've found a better path to this state already
            if (bestScores.get(currentState) < currentScore) {
                continue;
            }

            // Try all possible moves
            // Move forward
            int newX = currentState.x + currentState.dir.dx;
            int newY = currentState.y + currentState.dir.dy;

            if (isValidPosition(maze, newX, newY)) {
                State newState = new State(newX, newY, currentState.dir);
                int newScore = currentScore + 1; // Forward costs 1 point

                if (!bestScores.containsKey(newState) || newScore < bestScores.get(newState)) {
                    bestScores.put(newState, newScore);
                    queue.add(new AbstractMap.SimpleEntry<>(newState, newScore));

                    List<State> newPredecessors = new ArrayList<>();
                    newPredecessors.add(currentState);
                    predecessors.put(newState, newPredecessors);
                } else if (newScore == bestScores.get(newState)) {
                    // Add additional predecessor for equal score paths
                    predecessors.get(newState).add(currentState);
                }
            }

            // Turn left
            Direction leftDir = currentState.dir.turnLeft();
            State leftState = new State(currentState.x, currentState.y, leftDir);
            int leftScore = currentScore + 1000; // Turning costs 1000 points

            if (!bestScores.containsKey(leftState) || leftScore < bestScores.get(leftState)) {
                bestScores.put(leftState, leftScore);
                queue.add(new AbstractMap.SimpleEntry<>(leftState, leftScore));

                List<State> newPredecessors = new ArrayList<>();
                newPredecessors.add(currentState);
                predecessors.put(leftState, newPredecessors);
            } else if (leftScore == bestScores.get(leftState)) {
                // Add additional predecessor for equal score paths
                predecessors.get(leftState).add(currentState);
            }

            // Turn right
            Direction rightDir = currentState.dir.turnRight();
            State rightState = new State(currentState.x, currentState.y, rightDir);
            int rightScore = currentScore + 1000; // Turning costs 1000 points

            if (!bestScores.containsKey(rightState) || rightScore < bestScores.get(rightState)) {
                bestScores.put(rightState, rightScore);
                queue.add(new AbstractMap.SimpleEntry<>(rightState, rightScore));

                List<State> newPredecessors = new ArrayList<>();
                newPredecessors.add(currentState);
                predecessors.put(rightState, newPredecessors);
            } else if (rightScore == bestScores.get(rightState)) {
                // Add additional predecessor for equal score paths
                predecessors.get(rightState).add(currentState);
            }
        }

        // Find all states at the end position with the target score
        List<State> optimalEndStates = new ArrayList<>();
        for (Map.Entry<State, Integer> entry : bestScores.entrySet()) {
            State state = entry.getKey();
            int score = entry.getValue();
            if (state.x == endX && state.y == endY && score == targetScore) {
                optimalEndStates.add(state);
            }
        }

        // Collect all tiles on optimal paths
        Set<String> optimalPathTiles = new HashSet<>();
        for (State endState : optimalEndStates) {
            collectOptimalPathTiles(endState, predecessors, optimalPathTiles, bestScores, targetScore);
        }

        return optimalPathTiles;
    }

    private static void collectOptimalPathTiles(State state, Map<State, List<State>> predecessors,
            Set<String> optimalPathTiles, Map<State, Integer> bestScores,
            int targetScore) {
        if (state == null)
            return;

        // Add current tile to the set of optimal tiles
        optimalPathTiles.add(state.y + "," + state.x);

        // Base case: no predecessors (start state)
        if (predecessors.get(state).isEmpty()) {
            return;
        }

        // Recursive case: process all predecessors that are part of an optimal path
        for (State pred : predecessors.get(state)) {
            // Check if this predecessor is part of an optimal path
            int currentScore = bestScores.get(state);
            int predScore = bestScores.get(pred);

            // Calculate the cost of the move from pred to state
            int moveCost;
            if (pred.x == state.x && pred.y == state.y) {
                // This was a turn
                moveCost = 1000;
            } else {
                // This was a forward move
                moveCost = 1;
            }

            // If the scores match (pred score + move cost = current score), this is an
            // optimal step
            if (predScore + moveCost == currentScore) {
                collectOptimalPathTiles(pred, predecessors, optimalPathTiles, bestScores, targetScore);
            }
        }
    }

    private static boolean isValidPosition(String[] maze, int x, int y) {
        // Check if position is within bounds
        if (y < 0 || y >= maze.length || x < 0 || x >= maze[y].length()) {
            return false;
        }

        // Check if position is not a wall
        return maze[y].charAt(x) != '#';
    }

    private static void printOptimalPaths(String[] maze, Set<String> optimalPathTiles) {
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length(); x++) {
                char c = maze[y].charAt(x);
                if (c == '#') {
                    System.out.print('#');
                } else if (c == 'S' || c == 'E') {
                    System.out.print(c);
                } else if (optimalPathTiles.contains(y + "," + x)) {
                    System.out.print('O');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
    }
}