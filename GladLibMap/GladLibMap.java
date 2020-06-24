
/**
 * Write a description of GladLibMap here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import java.util.*;

public class GladLibMap {
    private HashMap<String, ArrayList<String>> myMap;
    
    private ArrayList<String> s;
    
    private Random myRandom;
    
    private static String dataSourceURL = "http://dukelearntoprogram.com/course3/data";
    private static String dataSourceDirectory = "data";
    
    public GladLibMap(){
        myMap = new HashMap<String, ArrayList<String>>(); 
        initializeFromSource(dataSourceDirectory);
        myRandom = new Random();
        s = new ArrayList<String>(); 
    }
    
    public GladLibMap(String source){
        initializeFromSource(source);
        myRandom = new Random();
        s = new ArrayList<String>(); 
    }
    
    private void initializeFromSource(String source) {
        String[] labels = {"country", "noun", "animal", "adjevtive", "name", "color", "timeframe", "verb", "fruit"};
        for(String s : labels)
        {
            ArrayList<String> list = readIt(source + "/" + s + ".txt");
            myMap.put(s, list);
        }
    }
    
    private String randomFrom(ArrayList<String> source){
        int index = myRandom.nextInt(source.size());
        return source.get(index);
    }
    
    private String getSubstitute(String label) {
        if(myMap.keySet().contains(label))
            return randomFrom(myMap.get(label));
        return "**UNKNOWN**";
    }
    
    private String processWord(String w){
        String sub = null;        
        int first = w.indexOf("<");
        int last = w.indexOf(">",first);
        if (first == -1 || last == -1){
            return w;
        }
        String prefix = w.substring(0,first);
        String suffix = w.substring(last+1);
        while(s.contains(sub) || sub == null){    
            sub = getSubstitute(w.substring(first+1,last));
        }
        s.add(sub);
        return prefix + sub + suffix;
    }
    
    private void printOut(String s, int lineWidth){
        int charsWritten = 0;
        for(String w : s.split("\\s+")){
            if (charsWritten + w.length() > lineWidth){
                System.out.println();
                charsWritten = 0;
            }
            System.out.print(w+" ");
            charsWritten += w.length() + 1;
        }
    }
    
    private String fromTemplate(String source){
        String story = "";
        if (source.startsWith("http")) {
            URLResource resource = new URLResource(source);
            for(String word : resource.words()){
                story = story + processWord(word) + " ";
            }
        }
        else {
            FileResource resource = new FileResource(source);
            for(String word : resource.words()){
                story = story + processWord(word) + " ";
            }
        }
        return story;
    }
    
    private ArrayList<String> readIt(String source){
        ArrayList<String> list = new ArrayList<String>();
        if (source.startsWith("http")) {
            URLResource resource = new URLResource(source);
            for(String line : resource.lines()){
                list.add(line);
            }
        }
        else {
            FileResource resource = new FileResource(source);
            for(String line : resource.lines()){
                list.add(line);
            }
        }
        return list;
    }
    
    public void makeStory(){
        System.out.println("\n");
        String story = fromTemplate("data/madtemplate2.txt");
        printOut(story, 60);
        int i = printTotalReplaced();
        System.out.println("\n\nTotal words replaced = " + i);
    }
    
    public int printTotalReplaced(){
        int i = 0;
        FileResource fr = new FileResource("data/madtemplate2.txt"); 
        for(String word : fr.words()){
            int first = word.indexOf("<");
            int last = word.indexOf(">");
            if(first > -1 && last > 0)
                i++;
        }
        return i;
    }
    
    public void totalWordsInMap(){
        int total = 0;
        for(String s : myMap.keySet())
        {
            for(String f : myMap.get(s))
            {
                total++;
            }
        }
        System.out.println("Total words in map = " + total);
    }
    
    public int totalWordsConsidered(){
        int total = 0; 
        for(String s : myMap.keySet())
        {
            if(s == "noun" || s == "color" || s == "adjective")
            {
                for(String f : myMap.get(s))
                {
                    total++;
                }
            }
        }
        return total;
    }
}

