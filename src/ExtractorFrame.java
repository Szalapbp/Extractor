import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ExtractorFrame extends JFrame
{
    JPanel buttonPnl;
    JButton openFileBtn, openStopWordsBtn, extractTagsBtn, saveTagsBtn;
    JTextArea wordsArea;
    JScrollPane scrollPane;
    File textFile;
    File stopWordsFile;
    TreeSet<String> stopWords;
    Map<String, Integer> wordFrequencies;


    public ExtractorFrame(){
        setTitle("Tag Extractor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        openFileBtn = new JButton("Open Text File");
        openStopWordsBtn = new JButton("Open Stop Words File");
        extractTagsBtn = new JButton("Extract Tags");
        saveTagsBtn = new JButton("Save Tags");
        wordsArea = new JTextArea(20, 40);
        scrollPane = new JScrollPane(wordsArea);


        wordsArea.setEditable(false);

        JPanel btnPanel = new JPanel();
        btnPanel.add(openFileBtn);
        btnPanel.add(openStopWordsBtn);
        btnPanel.add(extractTagsBtn);
        btnPanel.add(saveTagsBtn);
        add(btnPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        openFileBtn.addActionListener(e -> selectTextFile());
        openStopWordsBtn.addActionListener(e -> selectStopWordsFile());
        extractTagsBtn.addActionListener(e -> extractTags());
        saveTagsBtn.addActionListener(e -> saveResults());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void selectTextFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            textFile = fileChooser.getSelectedFile();
            wordsArea.setText("Selected Text File: " + textFile.getName());
        } else {
            wordsArea.setText("Text File selection canceled.");
        }
    }

    private void selectStopWordsFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            stopWordsFile = fileChooser.getSelectedFile();
            wordsArea.setText("Selected Stop Words File: " + stopWordsFile.getName());
            loadStopWords();
        } else {
            wordsArea.setText("Stop Words File selection canceled.");
        }
    }


    private void loadStopWords() {
        if (stopWordsFile == null) { // Check if stopWordsFile is null
            wordsArea.setText("Stop Words File not selected or invalid.");
            return;
        }

        stopWords = new TreeSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(stopWordsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stopWords.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            wordsArea.setText("Error loading stop words: " + e.getMessage());
        }
    }




    private void extractTags(){
        if(textFile == null || stopWords == null){
            wordsArea.setText("Please select both a text file and a stop words file");
            return;
        }

        wordFrequencies = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(textFile))){
            String line;
            while((line = reader.readLine()) != null){

                String[] words = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
                for(String word : words)
                {
                    if(!stopWords.contains(word) && !word.isEmpty())
                    {
                        wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
                    }
                }
            }
            displayResults();
        } catch(IOException e)
        {
            wordsArea.setText("Error processing text file: " + e.getMessage());
        }
    }

    private void displayResults()
    {
        StringBuilder result = new StringBuilder("Extracted Tags:\n");
        for(Map.Entry<String, Integer> entry : wordFrequencies.entrySet())
        {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        wordsArea.setText(result.toString());
    }

    private void saveResults(){
        if(wordFrequencies == null || wordFrequencies.isEmpty()){
            wordsArea.setText("No results to save. Extract tags to save results.");
            return;
        }
        try(PrintWriter writer = new PrintWriter(new File("frequencies.txt")))
        {
            for(Map.Entry<String, Integer> entry : wordFrequencies.entrySet())
            {
                writer.println(entry.getKey() + ": " + entry.getValue());
            }
            wordsArea.setText("Tags saved to frequencies.txt");

        }catch (IOException e){
            wordsArea.setText("Error saving results to frequencies.txt: " + e.getMessage());
        }
    }
}
