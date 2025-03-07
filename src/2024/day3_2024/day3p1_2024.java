import java.io.*;
import java.util.regex.*;

public class day3p1_2024 {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            StringBuilder inputBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                inputBuilder.append(line);
            }
            reader.close();
            String input = inputBuilder.toString();

            Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
            Matcher matcher = pattern.matcher(input);
            int sum = 0;

            while (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                sum += x * y;
            }

            System.out.println(sum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}