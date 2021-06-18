import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Aritmetichko {

    static class Interval implements Comparable<Interval>{
        public Float a, b;

        public Interval(Float lower, Float higher) {
            this.a = lower;
            this.b = higher;
        }


        @Override
        public String toString() {
            return String.format("[%f, %f]", this.a, this.b);
        }

        @Override
        public int compareTo(Interval o) {
            return 0;
        }
    }

    static class Entry<K, V extends Comparable<V>> implements Comparable<Entry<K,V>> {
        public K key;
        public V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%s -> %s",key.toString(), value.toString() );
        }

        @Override
        public int compareTo(Entry<K, V> o) {
            return this.value.compareTo(o.value);
        }

    }

    static class Table{
        LinkedList<Entry<String, Interval>> table;
        LinkedList<Entry<String, Float>> originalTable;

        public Table(BufferedReader bufferedReader) throws IOException {
            this.table = new LinkedList<>();
            System.out.print("Enter number of entries: ");
            int n = Integer.parseInt(bufferedReader.readLine().trim());
            System.out.println(n);
            String[] buffer;
            List<Entry<String, Float>> entries = new LinkedList<>();
            System.out.println("Enter the entries in format [character probability]");
            for (int i=0; i<n; i++){
                buffer = bufferedReader.readLine().split(" ");
                try {
                    if (buffer.length != 2)
                        throw new InvalidInputException();
                    buffer[1] = buffer[1].replaceFirst(",", ".");
                    //System.out.println(buffer[1]);
                    if (buffer[1].contains("/"))
                        buffer[1] = String.valueOf(Float.parseFloat(buffer[1].split("/")[0]) / Float.parseFloat(buffer[1].split("/")[1]));
                    entries.add(new Entry<>(buffer[0], Float.parseFloat(buffer[1])));
                }
                catch (NumberFormatException e){
                    System.out.println("Invalid number format exception.");
                    i--;
                }
                catch (InvalidInputException e){

                    //Try again.
                    i--;
                }
            }

            entries = entries.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            originalTable = new LinkedList<>(entries);


            float sum = 0;
            float previousProbability = 0;

            for (Entry<String, Float> entry : entries){
                sum += entry.value;
                table.add(new Entry<>(entry.key, new Interval(previousProbability, sum)));
                previousProbability = sum;
            }

        }

        public Interval getInterval(String[] text){
            Interval rez = null;
            LinkedList<Entry<String, Interval>> tempTable= new LinkedList<>(table);
            for (String s : text) {
                rez = tempTable.stream().filter(x -> x.key.equals(s)).collect(Collectors.toList()).get(0).value;
                tempTable = new LinkedList<>();

                float l;
                float r;
                for (Entry<String, Interval> stringIntervalEntry : table) {

                    l = rez.a + (stringIntervalEntry.value.a * (rez.b - rez.a));
                    r = rez.a + (stringIntervalEntry.value.b * (rez.b - rez.a));

                    tempTable.add(new Entry<>(stringIntervalEntry.key, new Interval(l, r)));
                }
            }
            return rez;
        }

        private static class InvalidInputException extends Exception {
            public InvalidInputException() {
                System.out.println("Invalud input. Try again. Input parametars should be: [character probability]");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // write your code here
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Table newTable = new Table(br);
        System.out.println("Enter text to encrypt.");
        System.out.println(newTable.getInterval(br.readLine().split("")));
    }
}
