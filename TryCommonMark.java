import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import java.util.ArrayList;

class TryCommonMark {
    public static void main(String[] args) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse("This is *Sparta*");
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        //System.out.print(renderer.render(document));
        // "<p>This is <em>Sparta</em></p>\n"
        
        //this part actually does the computation
        Node node = parser.parse("[link](abc.com)[link](abc.com)");
        LinkCountVisitor visitor = new LinkCountVisitor();
        node.accept(visitor);
        System.out.println(visitor.links);  // 4
    }
}


 
//this class can be defined anywhere in the file
class WordCountVisitor extends AbstractVisitor {
    int wordCount = 0;
 
    @Override
    public void visit(Text text) {
        // This is called for all Text nodes. Override other visit methods for other node types.
 
        // Count words (this is just an example, don't actually do it this way for various reasons).
        wordCount += text.getLiteral().split("\\W+").length;
 
        // Descend into children (could be omitted in this case because Text nodes don't have children).
        visitChildren(text);
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