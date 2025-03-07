import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class day17_2024 {
    private static int registerA;
    private static int registerB;
    private static int registerC;
    private static List<Integer> outputs = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        // Read input file
        String input = new String(Files.readAllBytes(Paths.get("input.txt")));

        // Parse the input to extract register values and program
        parseInput(input);

        // Run the program
        runProgram();

        // Print the output
        StringJoiner joiner = new StringJoiner(",");
        for (int output : outputs) {
            joiner.add(String.valueOf(output));
        }
        System.out.println(joiner.toString());
    }

    private static void parseInput(String input) {
        // Parse register values
        String[] lines = input.split("\n");

        for (String line : lines) {
            if (line.startsWith("Register A:")) {
                registerA = Integer.parseInt(line.substring("Register A:".length()).trim());
            } else if (line.startsWith("Register B:")) {
                registerB = Integer.parseInt(line.substring("Register B:".length()).trim());
            } else if (line.startsWith("Register C:")) {
                registerC = Integer.parseInt(line.substring("Register C:".length()).trim());
            } else if (line.startsWith("Program:")) {
                // Found the program line
                break;
            }
        }

        // Extract the program part
        int programIndex = input.indexOf("Program:");
        if (programIndex != -1) {
            String programPart = input.substring(programIndex + "Program:".length()).trim();
            programValues = parseProgram(programPart);
        }
    }

    private static int[] programValues;

    private static int[] parseProgram(String programPart) {
        String[] parts = programPart.split(",");
        int[] program = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            program[i] = Integer.parseInt(parts[i].trim());
        }

        return program;
    }

    private static void runProgram() {
        int instructionPointer = 0;

        while (instructionPointer < programValues.length) {
            int opcode = programValues[instructionPointer];

            // Check if we have enough space for operand
            if (instructionPointer + 1 >= programValues.length) {
                break; // End of program
            }

            int operand = programValues[instructionPointer + 1];
            instructionPointer += 2; // Move past opcode and operand

            // Execute instruction
            boolean jumped = executeInstruction(opcode, operand);

            // If jnz jumped, don't increment instruction pointer
            if (jumped) {
                instructionPointer = operand;
            }
        }
    }

    private static boolean executeInstruction(int opcode, int operand) {
        switch (opcode) {
            case 0: // adv - division, result in A
                registerA = registerA / (1 << getComboValue(operand));
                break;

            case 1: // bxl - XOR B with literal, result in B
                registerB = registerB ^ operand;
                break;

            case 2: // bst - set B to combo operand modulo 8
                registerB = getComboValue(operand) % 8;
                break;

            case 3: // jnz - jump if A is not zero
                if (registerA != 0) {
                    return true; // Signal that we jumped
                }
                break;

            case 4: // bxc - XOR B with C, result in B
                registerB = registerB ^ registerC;
                break;

            case 5: // out - output combo operand modulo 8
                outputs.add(getComboValue(operand) % 8);
                break;

            case 6: // bdv - division, result in B
                registerB = registerA / (1 << getComboValue(operand));
                break;

            case 7: // cdv - division, result in C
                registerC = registerA / (1 << getComboValue(operand));
                break;
        }

        return false; // No jump occurred
    }

    private static int getComboValue(int operand) {
        switch (operand) {
            case 0:
            case 1:
            case 2:
            case 3:
                return operand; // Literal values 0-3
            case 4:
                return registerA; // Value in register A
            case 5:
                return registerB; // Value in register B
            case 6:
                return registerC; // Value in register C
            case 7:
                throw new IllegalArgumentException(
                        "Combo operand 7 is reserved and should not appear in valid programs");
            default:
                throw new IllegalArgumentException("Invalid combo operand: " + operand);
        }
    }
}