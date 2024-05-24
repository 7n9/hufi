
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
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
    private int entCounter = 0;
    private String oldOutput;
    private String outToReplace;

    private final StringTranslate st = StringTranslate.getInstance();

    public ProperDisplay() {

        encodeButton.setText(st.translateString("btn.start.string"));
        jlab1.setText(st.translateString("input.string") + ":");
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent aev) {
                if (encodeButton.getText().equals(st.translateString("btn.start.string"))) {
                    if (inputfield.getText().isEmpty()) {
                        return;
                    }

                    //re set / clear
                    entries = new ArrayList<Entry>();
                    placeholder.removeAll();
                    placeholder.updateUI();

                    contents = inputfield.getText();
                    outToReplace = contents;
                    strArray = contents.toCharArray();
                    boolean isSameLetter = true;
                    char firstchar = strArray[0];
                    for (char c : strArray) {
                        if (c != firstchar) {
                            isSameLetter = false;
                            break;
                        }
                    }

                    if (isSameLetter) {
                        SameLetterDialog dialog = new SameLetterDialog();
                        dialog.pack();
                        dialog.setLocationRelativeTo(null);
                        dialog.setVisible(true);
                        return;
                    }

                    printSet(st.translateString("desc.countingfreqs"));


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
                        printAppend(chars.get(i) + " -> " + freqs.get(i));
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

                    encodeButton.setText(st.translateString("btn.continue.string"));
                    return;
                }
                if (encodeButton.getText().equals(st.translateString("btn.continue.string"))) {


                    //parse q
                    if (queue.size() > 1) {

                        Entry left = queue.poll();
                        Entry right = queue.poll();
                        Entry top = new Entry('\0', left.freq + right.freq);
                        top.left = left;
                        top.right = right;
                        queue.add(top);


                        String rightc = String.valueOf(right.c);
                        String leftc = String.valueOf(left.c);
                        if (left.c == '\0') {
                            leftc = left.combined;
                        }
                        if (right.c == '\0') {
                            rightc = right.combined;
                        }
                        top.combined = leftc + rightc;


                        printAppend(st.translateString("desc.combining") +
                                " " + leftc +
                                " " + st.translateString("desc.and") +
                                " " + rightc
                        );

                        top.left.last = false;
                        top.right.last = false;

                        placeholder.removeAll();
                        placeholder.updateUI();
                        TreePanel treePanel = new TreePanel(top, getQueueStringSpecial());
                        placeholder.setLayout(new BorderLayout());
                        placeholder.add(treePanel, BorderLayout.CENTER);

                        if (queue.size() == 1) {
                            encodeButton.setText(st.translateString("btn.genout.string"));
                        }
                        return;
                    }
                    encodeButton.setText(st.translateString("btn.genout.string"));
                    return;
                }


                if (encodeButton.getText().equals(st.translateString("btn.genout.string"))) {

                    printAppend(st.translateString("desc.generating"));
                    enc_codes = new HashMap<Character, String>();
                    printCode(enc_codes, queue.peek(), "");
                    oldOutput = textArea1.getText();
                    encodeButton.setText(st.translateString("btn.replace.string"));
                }

                if (encodeButton.getText().equals(st.translateString("btn.replace.string"))) {


//                        for (Entry e : entries){
//                            printAppend(e.c + " -> " + enc_codes.get(e.c));;
//                        }
                    if (!entries.isEmpty()) {
                        Entry temp = entries.get(entCounter);
                        entries.remove(temp);
//                            System.out.println(entries.size());
                        textArea1.setText(oldOutput);
                        printAppend(temp.c + " -> " + enc_codes.get(temp.c));
                        oldOutput = textArea1.getText();

                        Entry colornode = queue.peek();
                        colornode.last = true;
                        String code = enc_codes.get(temp.c);
                        char[] codeArray = code.toCharArray();
                        int colorint = 0;

                        clearColors(queue.peek());

                        while (!(colornode.left == null) && !(colornode.right == null)) {
                            if (codeArray[colorint] == '0') {
                                colornode.left.last = true;
                                colornode = colornode.left;
                            }
                            if (codeArray[colorint] == '1') {
                                colornode.right.last = true;
                                colornode = colornode.right;
                            }
                            colorint++;
                        }
                        placeholder.removeAll();
                        placeholder.updateUI();
                        TreePanel treePanel = new TreePanel(queue.peek(), getQueueStringSpecial());
                        placeholder.setLayout(new BorderLayout());
                        placeholder.add(treePanel, BorderLayout.CENTER);
                        placeholder.setVisible(true);

                        String toreplace = String.valueOf(temp.c);


                        //  \.[]{}()<>*+-=!?^$|

                        switch (temp.c) {
                            case '.':
                                toreplace = "\\.";
                                break;
                            case '\\':
                                toreplace = "\\\\";
                                break;
                            case '[':
                                toreplace = "\\[";
                                break;
                            case '{':
                                toreplace = "\\{";
                                break;
                            case '$':
                                toreplace = "\\$";
                                break;
                            case '(':
                                toreplace = "\\(";
                                break;
                            case ')':
                                toreplace = "\\)";
                                break;
                            case '*':
                                toreplace = "\\*";
                                break;
                            case '+':
                                toreplace = "\\+";
                                break;
                            case '?':
                                toreplace = "\\?";
                                break;
                            case '^':
                                toreplace = "\\^";
                                break;
                            case '|':
                                toreplace = "\\|";
                                break;
                        }
                        outToReplace = outToReplace.replaceAll(toreplace, String.valueOf(enc_codes.get(temp.c)));
                        printAppend(outToReplace);

                        if (entries.isEmpty()) {
                            encodeButton.setText(st.translateString("btn.finish.string"));
                        }
                        return;
                    }
                    return;
                }
                if (encodeButton.getText().equals(st.translateString("btn.finish.string"))) {
                    textArea1.setText(oldOutput);

                    printAppend(st.translateString("desc.generated") + "\n");
                    printAppend(outToReplace);
                    printAppend("");

                    float contentsbits = contents.toCharArray().length * 8;
                    float codebits = outToReplace.length() * 2;
                    printAppend(st.translateString("desc.inputbits") + " " + (int) contentsbits);
                    printAppend(st.translateString("desc.encodedbits") + " " + (int) codebits);

                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    String saved = df.format(100 - (codebits / contentsbits) * 100);
                    printAppend(st.translateString("desc.savedspace") + " " + saved);

                    placeholder.removeAll();
                    placeholder.updateUI();
                    clearColors(queue.peek());
                    TreePanel treePanel = new TreePanel(queue.peek(), getQueueStringSpecial());
                    placeholder.setLayout(new BorderLayout());
                    placeholder.add(treePanel, BorderLayout.CENTER);
                    placeholder.setVisible(true);
                    encodeButton.setText(st.translateString("btn.start.string"));
                    if (contents.toLowerCase().contains("somebody once told me") || contents.toLowerCase().contains("world is gonna roll me")) {
                        try {
                            Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=L_jWHffIx5E"));
                        } catch (IOException | URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entries = new ArrayList<Entry>();
                textArea1.setText("");
                inputfield.setText("");
                encodeButton.setText(st.translateString("btn.start.string"));
                placeholder.removeAll();
                placeholder.updateUI();
            }
        });
    }

    private String getQueueStringSpecial() {
        PriorityQueue<Entry> tempQueue = new PriorityQueue<Entry>(queue);
        StringBuilder queueEntries = new StringBuilder("[");
        while (!tempQueue.isEmpty()) {
            if (tempQueue.peek().c == '\0') {
                queueEntries.append(tempQueue.poll().combined);
            } else {
                queueEntries.append(tempQueue.poll().c);
            }
            if (!tempQueue.isEmpty()) {
                queueEntries.append(", ");
            } else {
                queueEntries.append("]");
            }
        }

        return queueEntries.toString();
    }

    private void createUIComponents() {
        placeholder = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.gray);
            }
        };
    }

    private void printAppend(String s) {
        textArea1.append("\n" + s);
        textArea1.setCaretPosition(textArea1.getDocument().getLength());
    }

    private void printSet(String s) {
        textArea1.setText(s);
        textArea1.setCaretPosition(textArea1.getDocument().getLength());
    }

    public static void clearColors(Entry root) {
        if (root.left == null && root.right == null) {
            root.last = false;
            return;
        }
        root.last = false;
        // l = 0, r = 1
        clearColors(root.left);
        clearColors(root.right);
    }

    public static void printCode(HashMap<Character, String> e_c, Entry root, String s) {
        if (root.left == null && root.right == null && root.c != '\0') {
            //System.out.println(root.c + ":" + s);
            if (!e_c.containsKey(root.c)) {
                e_c.put(root.c, s);
            } else {
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
