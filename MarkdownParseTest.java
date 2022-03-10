import static org.junit.Assert.*;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;

public class MarkdownParseTest {
    @Test
    public void addition() {
        assertEquals(2, 1 + 1);
    }

    @Test
    public void getLinksTest() throws IOException{
        Path fileName = Path.of("test-file.md");
        String content = Files.readString(fileName);
        assertEquals(List.of("https://something.com", "some-page.html"), MarkdownParse.getLinks(content));
    }

    @Test
    public void getLinksTest2() throws IOException{
        Path fileName = Path.of("test-file2.md");
        String content = Files.readString(fileName);
        assertEquals(List.of("https://something.com", "some-page.html"), MarkdownParse.getLinks(content));
    }

    @Test
    public void getLinksTest3() throws IOException{
        Path fileName = Path.of("test-file3.md");
        String content = Files.readString(fileName);
        assertEquals(List.of("https://alink1.com"), MarkdownParse.getLinks(content));
    }

    @Test
    public void testSnip1() throws IOException, NoSuchFileException {

        ArrayList<String> output = new ArrayList<>();
        output.addAll(Arrays.asList("`google.com","google.com","ucsd.edu"));
        Path fileName= Path.of("snip1.md");
        String contents = Files.readString(fileName);
        ArrayList<String> links = MarkdownParse.getLinks(contents);
        assertEquals(output,links);
    }

    @Test
    public void testSnip2() throws IOException, NoSuchFileException {

        ArrayList<String> output = new ArrayList<>();
        output.addAll(Arrays.asList("a.com","a.com(())","example.com"));
        Path fileName= Path.of("snip2.md");
        String contents = Files.readString(fileName);
        ArrayList<String> links = MarkdownParse.getLinks(contents);
        assertEquals(output,links);
    }

    @Test
    public void testSnip3() throws IOException, NoSuchFileException {

        ArrayList<String> output = new ArrayList<>();
        output.addAll(Arrays.asList("https://ucsd-cse15l-w22.github.io/"));
        Path fileName= Path.of("snip3.md");
        String contents = Files.readString(fileName);
        ArrayList<String> links = MarkdownParse.getLinks(contents);
        assertEquals(output,links);
    }

}