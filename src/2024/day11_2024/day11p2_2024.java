import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.math.BigInteger;

public class day11p2_2024 {

    static Map<String, BigInteger> memo = new HashMap<>();

    public static BigInteger countStones(String s, int blinks) {
        if (blinks == 0) {
            return BigInteger.ONE;
        }

        String key = blinks + "_" + s;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        BigInteger result;
        if (s.equals("0")) {
            result = countStones("1", blinks - 1);
        } else {
            if (s.length() % 2 == 0) {
                String left = s.substring(0, s.length() / 2);
                String right = canonical(s.substring(s.length() / 2));
                result = countStones(left, blinks - 1).add(countStones(right, blinks - 1));
            } else {
                BigInteger num = new BigInteger(s);
                BigInteger prod = num.multiply(BigInteger.valueOf(2024));
                String next = prod.toString();
                result = countStones(next, blinks - 1);
            }
        }

        memo.put(key, result);
        return result;
    }

    public static String canonical(String s) {
        return new BigInteger(s).toString();
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        List<String> tokens = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.trim().split("\\s+");
            tokens.addAll(Arrays.asList(parts));
        }

        int blinks = 75;
        BigInteger totalStones = BigInteger.ZERO;
        for (String token : tokens) {
            totalStones = totalStones.add(countStones(token, blinks));
        }

        System.out.println(totalStones);
    }
}
