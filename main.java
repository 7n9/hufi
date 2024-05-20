import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

class Huffman {
    public static void main(String[] args) {
        try {

            File a = new File("ipsum.txt");

            byte[] bytes = Files.readAllBytes(a.toPath());
            String contents = new String(bytes);
            char[] strArray = contents.toCharArray();

            // Make a frequency map
            HashMap<Character, Integer> charMap = new HashMap<Character, Integer>();
            for (Character c : strArray) {
                if (!charMap.containsKey(c)) {
                    charMap.put(c, 1);
                } else {
                    charMap.put(c, charMap.get(c) + 1);
                }
            }
            // System.out.println(wordMap.toString());
            System.out.println("Unique Entries: " + charMap.size());
            for (Map.Entry entry : charMap.entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
            System.out.println("\n\n");


            List<Character> chars = new ArrayList<>(charMap.keySet());
            List<Integer> freqs = new ArrayList<>(charMap.values());

            ArrayList<Entry> entries = new ArrayList<Entry>();
            for (int i = 0; i < chars.size(); i++) {
                entries.add(new Entry(chars.get(i), freqs.get(i)));
            }

            Collections.sort(entries);
            Collections.reverse(entries);

            int n = entries.size();
            char[] charArray = new char[n];
            int[] charfreq = new int[n];

            for (int i = 0; i < entries.size(); i++) {
                Entry t = entries.get(i);
                charArray[i] = t.c;
                charfreq[i] = t.freq;
            }

            for (Entry entry : entries) {
                System.out.println(entry.print());
            }
            System.out.println("\n\n");

            PriorityQueue<Entry> queue = new PriorityQueue<Entry>(n, new EntryComp());

            for (int i = 0; i < n; i++) {
                Entry temp_entry_node = new Entry();
                temp_entry_node.c = charArray[i];
                temp_entry_node.freq = charfreq[i];
                temp_entry_node.left = null;
                temp_entry_node.right = null;
                queue.add(temp_entry_node);
            }

            Entry root = null;
            while (queue.size() > 1) {

                Entry x = queue.peek();
                queue.poll();
                Entry y = queue.peek();
                queue.poll();

                Entry f = new Entry();

                f.freq = x.freq + y.freq;
                f.c = '-';
                f.left = x;
                f.right = y;
                root = f;

                queue.add(f);
            }



            ArrayList<Entry> enc_codes = new ArrayList<Entry>();
            printCode(enc_codes, root, "");

            System.out.println();
            int siz = enc_codes.size();
            char[] fin_chars = new char[siz];
            int[] codes = new int[siz];
            for (int i = 0; i < enc_codes.size(); i++) {
                Entry t = enc_codes.get(i);
                fin_chars[i] = t.c;
                codes[i] = t.freq;
            }
            
            for (int i = 0; i < fin_chars.length; i++) {
                contents = contents.replaceAll(String.valueOf(fin_chars[i]), String.valueOf(codes[i]));
            }
            System.out.println(contents);
        } catch (Exception e) {
        }
    }

    public static void printCode(ArrayList<Entry> e_c, Entry root, String s) {
        if (root.left == null && root.right == null) {
            System.out.println(root.c + ":" + s);
            e_c.add(new Entry(root.c, Integer.parseInt(s)));
            return;
        }

        // l = 0, r = 1
        printCode(e_c, root.left, s + "0");
        printCode(e_c, root.right, s + "1");
    }
}
