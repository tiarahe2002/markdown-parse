
// File reading code from https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Stack;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;

//imports for CommonMark
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;



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
        Parser parser = Parser.builder().build();
        Node node = parser.parse(markdown);
        LinkCountVisitor visitor = new LinkCountVisitor();
        node.accept(visitor);
        return visitor.links;
    }

    public static void main(String[] args) throws IOException {
        Map<String, List<String>> links = getLinks(new File(args[0]));
        System.out.println(links);
        
    }
}

//this class can be defined anywhere in the file
class LinkCountVisitor extends AbstractVisitor {
    int linkCount = 0;
    ArrayList<String> links = new ArrayList<>();
 
    @Override
    public void visit(Link link) {
        // This is called for all Link nodes.

        // Count links
        linkCount += 1;

        //add link to list
        links.add(link.getDestination());
 
        // Descend into children
        visitChildren(link);
    }
}