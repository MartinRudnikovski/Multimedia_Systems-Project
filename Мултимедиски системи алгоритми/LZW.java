import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LZW {

    public static void main(String[] args) throws IOException {
        System.out.print("Input string: ");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input = bufferedReader.readLine();
        Hashtable<String, Integer> table = insertTable(bufferedReader);
        //setupTable(table, input);
        int code, j;
        StringBuilder sb = new StringBuilder();
        List<Integer> codedList = new LinkedList<>();
        for (int i = 0; i < input.length(); i++) {
            sb.append(input.charAt(i));
            code = table.get(sb.toString());
            j = i;
            while (++j < input.length()) {
                sb.append(input.charAt(j));
                if (table.containsKey(sb.toString())) {
                    code = table.get(sb.toString());
                } else {
                    table.put(sb.toString(), table.size() + 1);
                    break;
                }
            }
            sb.delete(0, sb.length());
            codedList.add(code);
            i += j - i - 1;
        }
        System.out.println(codedList);
        System.out.println(table);
    }

    private static void setupTable(Hashtable<String, Integer> table, String input) {
        List<String> singletonList = Arrays.stream(input.split("").clone()).distinct().collect(Collectors.toList());
        AtomicInteger t = new AtomicInteger(0);
        singletonList.forEach(i -> table.put(i, t.incrementAndGet()));
    }

    private static Hashtable<String, Integer> insertTable(BufferedReader br) throws IOException {
        System.out.format("Insert number of entries: ");
        int n = Integer.parseInt(br.readLine().trim());
        System.out.print("Insert table entries:\n");
        Hashtable<String, Integer> table = new Hashtable<>();
        String[] temp = null;
        for (int i =0; i<n; i++){
            try {
                temp = br.readLine().split(" ");
                if (temp.length != 2 || !temp[0].chars().allMatch(Character::isDigit)) {
                    throw new InvalidInputException();
                }
            }
            catch (InvalidInputException e){
                System.out.println("Invalid input, try again.  --- Format: string - index.");
                i--;
                continue;
            }

            table.put(temp[1], Integer.parseInt(temp[0]));
        }

        return table;
    }

    private static class InvalidInputException extends Throwable {

    }
}
