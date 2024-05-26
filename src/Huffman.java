package src;

import javax.swing.*;
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

    static String languageChoosen = "NULL";
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            ChooseLanguageDialog dialog = new ChooseLanguageDialog();
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

            if(languageChoosen.equals("NULL")){
                System.exit(1);
            }
            SwingUtilities.invokeLater(() -> {
                ProperDisplay pd = new ProperDisplay();
                pd.setContentPane(pd.mainpanel);
                pd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                pd.pack();
                pd.setVisible(true);
                pd.setTitle(StringTranslate.getInstance().translateString("title.string"));
                pd.setSize(1500, 1000);
                pd.setLocationRelativeTo(null);
                pd.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
