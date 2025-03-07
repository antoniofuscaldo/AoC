import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day3p2_2024 {
    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("input.txt")));

        long sum = 0;
        boolean enabled = true;

        Pattern pattern = Pattern.compile("don't\\(\\)|do\\(\\)|mul\\((\\d+),(\\d+)\\)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String token = matcher.group();
            if (token.equals("do()")) {
                enabled = true;
            } else if (token.equals("don't()")) {
                enabled = false;
            } else {
                if (enabled) {
                    int num1 = Integer.parseInt(matcher.group(1));
                    int num2 = Integer.parseInt(matcher.group(2));
                    sum += num1 * num2;
                }
            }
        }
        System.out.println(sum);
    }
}
