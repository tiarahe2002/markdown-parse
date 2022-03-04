import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Stack;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;



public class MarkdownParse {
    public static boolean containsBrackets(String link) {
        if(!link.contains("[")){return false;}
        if(!link.contains("]")){return false;}
        if(!link.contains("(")){return false;}
        if(!link.contains(")")){return false;}
        return true;
    }
    public static boolean containsSpace(String link) {
        return link.contains(" ");
    }

    //Copied from https://github.com/ucsd-cse15l-w22/markdown-parse/blob/1f7485854d92deb69c1bf8290c267fee27371d17/MarkdownParse.java#L32-L50
    //For Lab 9
    public static Map<String, List<String>> getLinks(File dirOrFile) throws IOException {
        Map<String, List<String>> result = new HashMap<>();
        if(dirOrFile.isDirectory()) {
            for(File f: dirOrFile.listFiles()) {
                result.putAll(getLinks(f));
            }
            return result;
        }
        else {
            Path p = dirOrFile.toPath();
            int lastDot = p.toString().lastIndexOf(".");
            if(lastDot == -1 || !p.toString().substring(lastDot).equals(".md")) {
                return result;
            }
            ArrayList<String> links = getLinks(Files.readString(p));
            String filename = p.toString();
            if(containsBrackets(Files.readString(p))){
                System.out.println(filename + " does contain the needed brackets.");
            }else{
                System.out.println(filename + " does not contain the needed brackets.");
            }
            result.put(dirOrFile.getPath(), links);
            return result;
        }
    }
    public static ArrayList<String> getLinks(String markdown) {
        ArrayList<String> toReturn = new ArrayList<>();

        int currentIndex = 0;
        Stack<Character> bracketTracker = new Stack<>();
        boolean findLink = false;
        int start = 0;
        int end = 0;
        while (currentIndex < markdown.length()) {
            char curr = markdown.charAt(currentIndex);
            // if an escape char is found, skip it and the
            // character it is escaping
            if (curr == '\\') {
                currentIndex += 2;
                continue;
            }
            // if we are potentially looking at a link after finding []
            if (findLink) {
                // if there arent any other brackets on the bracket tracker we 
                // should check what we are looking at and if it is an open 
                // braket and mark the current index as the start of a link
                if (bracketTracker.isEmpty()) {
                    if (curr == '(') {
                        bracketTracker.push(curr);
                        start = currentIndex;
                    } 
                    // something else came after the ] that wasn't (
                    else { 
                        findLink = false;
                    }
                } else {
                    // if ) then we should note the end of the link down
                    if (curr == ')') {
                        end = currentIndex;
                        //create substring with only the text of the link
                        String link = markdown.substring(start + 1, end).trim();
                        //check the link for any spaces
                        if (!containsSpace(link)) {
                            toReturn.add(link);
                        }
                        // take the ( off the bracket tracker
                        bracketTracker.pop();
                        findLink = false;
                    }
                }
            } 
            else { 
                // looking for [ in file and adding it to the tracker if so
                if (curr == '[') {
                    bracketTracker.push(curr);
                } else if (curr == ']') { 
                    // if found a close bracket and there is a [ on the tracker
                    // we potentially found a link and should move onto that
                    if (!bracketTracker.isEmpty()) {
                        bracketTracker.clear();
                        findLink = true;
                    }
                } 
                else {
                    // if we find an potential image tag
                    if (curr == '!') {
                        //and it is actually before a '[' and not at the end
                        if (currentIndex < markdown.length() - 1 && 
                            markdown.charAt(currentIndex + 1) == '[') {
                            
                            currentIndex += 2;
                            }
                    }
                }
            }
            // move to next char
            currentIndex++;
        }

        return toReturn;

    }

    public static void main(String[] args) throws IOException {
        Map<String, List<String>> links = getLinks(new File(args[0]));
        System.out.println(links);
        
    }
}
