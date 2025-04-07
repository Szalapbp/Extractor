import javax.swing.*;
import java.awt.*;

public class ExtractorFrame extends JFrame
{
    JPanel buttonPnl;
    JButton openFileBtn, openStopWordsBtn, extractTagsBtn, saveTagsBtn;
    JTextArea wordsArea;
    JScrollPane scrollPane;
    public ExtractorFrame(){
        setTitle("Tag Extractor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton openFileBtn = new JButton("Open Text File");
        JButton openStopWordsBtn = new JButton("Open Stop Words File");
        JButton extractTagsBtn = new JButton("Extract Tags");
        JButton saveTagsBtn = new JButton("Save Tags");
        JTextArea wordsArea = new JTextArea(20, 40);
        JScrollPane scrollPane = new JScrollPane(wordsArea);

        wordsArea.setEditable(false);

        JPanel btnPanel = new JPanel();
        btnPanel.add(openFileBtn);
        btnPanel.add(openStopWordsBtn);
        btnPanel.add(extractTagsBtn);
        btnPanel.add(saveTagsBtn);
        add(btnPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
