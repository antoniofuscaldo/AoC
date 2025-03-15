import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class day8_2023 {
    public static void main(String[] args) {
        try {
            // Read the input file
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

            // Read the instructions (first line)
            String instructions = reader.readLine();

            // Skip the empty line
            reader.readLine();

            // Read the network map
            Map<String, String[]> network = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse each line to extract node and its connections
                String node = line.substring(0, 3);
                String left = line.substring(7, 10);
                String right = line.substring(12, 15);

                network.put(node, new String[] { left, right });
            }

            reader.close();

            // Part 1: Navigate from AAA to ZZZ
            solvePart1(network, instructions);

            // Part 2: Navigate from all nodes ending with A to all nodes ending with Z
            solvePart2(network, instructions);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void solvePart1(Map<String, String[]> network, String instructions) {
        String currentNode = "AAA";
        String destination = "ZZZ";
        int steps = 0;
        int instructionIndex = 0;

        // Skip if the network doesn't have AAA (for some test cases)
        if (!network.containsKey(currentNode)) {
            System.out.println("Part 1: Network doesn't contain AAA node, skipping Part 1");
            return;
        }

        while (!currentNode.equals(destination)) {
            // Get the current instruction (L or R)
            char instruction = instructions.charAt(instructionIndex);

            // Get the connections for the current node
            String[] connections = network.get(currentNode);

            // Move to the next node based on the instruction
            if (instruction == 'L') {
                currentNode = connections[0];
            } else { // instruction == 'R'
                currentNode = connections[1];
            }

            // Increment steps
            steps++;

            // Move to the next instruction, wrapping around if necessary
            instructionIndex = (instructionIndex + 1) % instructions.length();
        }

        System.out.println("Part 1: Steps required to reach ZZZ: " + steps);
    }

    private static void solvePart2(Map<String, String[]> network, String instructions) {
        // Find all nodes ending with 'A'
        List<String> currentNodes = new ArrayList<>();
        for (String node : network.keySet()) {
            if (node.endsWith("A")) {
                currentNodes.add(node);
            }
        }

        // Find cycle length for each starting node
        long[] cycleLengths = new long[currentNodes.size()];

        for (int i = 0; i < currentNodes.size(); i++) {
            String startNode = currentNodes.get(i);
            String currentNode = startNode;
            int instructionIndex = 0;
            int steps = 0;

            // Keep track of visited states to detect cycles
            Map<String, Integer> visited = new HashMap<>();
            String state = currentNode + "_" + instructionIndex;

            while (!visited.containsKey(state) && !currentNode.endsWith("Z")) {
                visited.put(state, steps);

                // Get the current instruction (L or R)
                char instruction = instructions.charAt(instructionIndex);

                // Get the connections for the current node
                String[] connections = network.get(currentNode);

                // Move to the next node based on the instruction
                if (instruction == 'L') {
                    currentNode = connections[0];
                } else { // instruction == 'R'
                    currentNode = connections[1];
                }

                // Increment steps
                steps++;

                // Move to the next instruction, wrapping around if necessary
                instructionIndex = (instructionIndex + 1) % instructions.length();

                // Update state
                state = currentNode + "_" + instructionIndex;
            }

            if (currentNode.endsWith("Z")) {
                cycleLengths[i] = steps;
                // Found Z-node directly
            } else {
                // We hit a cycle before reaching a Z node
                int cycleStart = visited.get(state);
                int cycleLength = steps - cycleStart;

                // Now we need to find when we'll reach a Z node within the cycle
                boolean foundZ = false;
                currentNode = startNode;
                instructionIndex = 0;
                steps = 0;

                while (steps < cycleStart + cycleLength) {
                    // Get the current instruction (L or R)
                    char instruction = instructions.charAt(instructionIndex);

                    // Get the connections for the current node
                    String[] connections = network.get(currentNode);

                    // Move to the next node based on the instruction
                    if (instruction == 'L') {
                        currentNode = connections[0];
                    } else { // instruction == 'R'
                        currentNode = connections[1];
                    }

                    // Increment steps
                    steps++;

                    // Move to the next instruction, wrapping around if necessary
                    instructionIndex = (instructionIndex + 1) % instructions.length();

                    if (currentNode.endsWith("Z") && steps >= cycleStart) {
                        foundZ = true;
                        break;
                    }
                }

                if (foundZ) {
                    cycleLengths[i] = steps;
                    // Found Z-node in cycle
                } else {
                    throw new RuntimeException("Could not find a Z-node in the cycle for " + startNode);
                }
            }
        }

        // Calculate least common multiple of all cycle lengths
        long lcm = cycleLengths[0];
        for (int i = 1; i < cycleLengths.length; i++) {
            lcm = lcm(lcm, cycleLengths[i]);
        }

        System.out.println("Part 2: Steps required until all paths end at Z nodes: " + lcm);
    }

    // Calculate Greatest Common Divisor (GCD) using Euclidean algorithm
    private static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Calculate Least Common Multiple (LCM)
    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }
}