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

            // File a = new File("ipsum.txt");

            // byte[] bytes = Files.readAllBytes(a.toPath());
            // String contents = new String(bytes);

            Scanner sc = new Scanner(System.in);
            String contents = sc.nextLine();
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

            while (queue.size() > 1) {

                Entry left = queue.poll();
                Entry right = queue.poll();
                Entry top = new Entry('$', left.freq + right.freq);
                top.left = left;
                top.right = right;
                queue.add(top);
            }



            HashMap<Character, String> enc_codes = new HashMap<Character, String>();
            printCode(enc_codes, queue.peek(), "");
            
            for (int i = 0; i < strArray.length; i++) {
                char achar = strArray[i];
                System.out.println(enc_codes.get(achar));
                contents = contents.replaceAll(String.valueOf(achar), String.valueOf(enc_codes.get(achar)));
                System.out.println(contents);
            }
            System.out.println(contents);
            System.out.println(decodeString(queue.peek(), contents));
        } catch (Exception e) {
        }
    }

    public static void printCode(HashMap<Character, String> e_c, Entry root, String s) {
        if (root.left == null && root.right == null && root.c != '$') {
            System.out.println(root.c + ":" + s);
            if(!e_c.containsKey(root.c)){
                e_c.put(root.c, s);
            }else{
                e_c.put(root.c, e_c.get(root.c) + s);
            }
            return;
        }

        // l = 0, r = 1
        printCode(e_c, root.left, s + "0");
        printCode(e_c, root.right, s + "1");
    }

    private static String decodeString(Entry root, String s) {
        String ans = "";
        Entry curr = root;
        int n = s.length();
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '0') {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
            if (curr.left == null && curr.right == null) {
                ans += curr.c;
                curr = root;
            }
        }
        return ans;
    }
}
