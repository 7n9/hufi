
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class ProperDisplay extends JFrame {
    private JButton resetButton;
    private JButton encodeButton;
    private JTextField inputfield;
    JPanel mainpanel;
    private JPanel placeholder;
    private JLabel jlab1;
    private JTextArea textArea1;
    private JScrollPane scrollpane;
    private ArrayList<Entry> entries = new ArrayList<Entry>();
    private PriorityQueue<Entry> queue;
    private String contents;
    private char[] strArray;
    private HashMap<Character, String> enc_codes;

    public ProperDisplay(){
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent aev) {
                switch (encodeButton.getText()){
                    case "Start":
                        if(inputfield.getText().isEmpty()){
                            break;
                        }
                        entries = new ArrayList<Entry>();
                        textArea1.setText("");
                        textArea1.setCaretPosition(textArea1.getDocument().getLength());
                        placeholder.removeAll();
                        placeholder.updateUI();

                        encodeButton.setText("Generate Codes");
                        textArea1.append("\nCounting frequencies...");
                        textArea1.setCaretPosition(textArea1.getDocument().getLength());
                        contents = inputfield.getText();
                        strArray = contents.toCharArray();

                        // Make a frequency map
                        HashMap<Character, Integer> charMap = new HashMap<Character, Integer>();
                        for (Character c : strArray) {
                            if (!charMap.containsKey(c)) {
                                charMap.put(c, 1);
                            } else {
                                charMap.put(c, charMap.get(c) + 1);
                            }
                        }

                        java.util.List<Character> chars = new ArrayList<>(charMap.keySet());
                        List<Integer> freqs = new ArrayList<>(charMap.values());

                        for (int i = 0; i < chars.size(); i++) {
                            entries.add(new Entry(chars.get(i), freqs.get(i)));
                            textArea1.append("\n" + chars.get(i) + " -> " + freqs.get(i));
                            textArea1.setCaretPosition(textArea1.getDocument().getLength());
                        }

                        Collections.sort(entries);
                        Collections.reverse(entries);

                        //make q
                        queue = new PriorityQueue<Entry>(entries.size(), new EntryComp());
                        for (Entry entry : entries) {
                            Entry temp_entry_node = new Entry();
                            temp_entry_node.c = entry.c;
                            temp_entry_node.freq = entry.freq;
                            temp_entry_node.left = null;
                            temp_entry_node.right = null;
                            queue.add(temp_entry_node);
                        }

                        textArea1.append( "\nMaking queue...");
                        textArea1.setCaretPosition(textArea1.getDocument().getLength());

                        //parse q
                        while (queue.size() > 1) {

                            Entry left = queue.poll();
                            Entry right = queue.poll();
                            Entry top = new Entry('\0', left.freq + right.freq);
                            top.left = left;
                            top.right = right;
                            queue.add(top);
                        }

                        textArea1.append("\nFinished making queue.");
                        textArea1.setCaretPosition(textArea1.getDocument().getLength());
                        break;
                    case "Generate Codes":
                        encodeButton.setText("Generate Tree");
                        textArea1.append("\nGenerating Codes...");
                        textArea1.setCaretPosition(textArea1.getDocument().getLength());

                        enc_codes = new HashMap<Character, String>();
                        printCode(enc_codes, queue.peek(), "");

                        for (Entry e : entries){
                            textArea1.append("\n" + (e.c + " -> " + enc_codes.get(e.c)));
                            textArea1.setCaretPosition(textArea1.getDocument().getLength());
                        }
                        break;
                    case "Generate Tree":
                        encodeButton.setText("Start");
                        TreePanel treePanel = new TreePanel(queue.peek());
                        placeholder.setLayout(new BorderLayout());
                        placeholder.add(treePanel, BorderLayout.CENTER);

                        textArea1.append("\nTree Generated.");
                        textArea1.append("\nOutput:");
                        textArea1.setCaretPosition(textArea1.getDocument().getLength());

                        for (int i = 0; i < strArray.length; i++) {
                            char achar = strArray[i];
                            contents = contents.replaceAll(String.valueOf(achar), String.valueOf(enc_codes.get(achar)));
                        }

                        textArea1.append("\n" + contents);
                        textArea1.setCaretPosition(textArea1.getDocument().getLength());
                        placeholder.setVisible(true);
                        break;
                }

            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entries = new ArrayList<Entry>();
                textArea1.setText("");
                inputfield.setText("");
                encodeButton.setText("Start");
                placeholder.removeAll();
                placeholder.updateUI();
            }
        });
    }

    private void createUIComponents(){
        placeholder = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.gray);
            }
        };
    }

    public static void printCode(HashMap<Character, String> e_c, Entry root, String s) {
        if (root.left == null && root.right == null && root.c != '$') {
            //System.out.println(root.c + ":" + s);
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
            }else{
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
