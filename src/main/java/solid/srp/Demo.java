package solid.srp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * The idea behind SRP is to force you to put a single
 * responsibility or 'concern' into a single class, avoiding
 * god objects, which are hard to refactor, maintain
 */

// Concern is storing and manipulation of journal entries
class Journal {
    private final List<String> entries = new ArrayList<>();
    private static int count = 0;

    // Directly linked to Journal
    public void addEntry(String text) {
        entries.add("" + (++count) + ": " + text);
    }

    // Directly linked to Journal
    public void removeEntry(int index) {
        entries.remove(index);
    }

    @Override
    public String toString() {
        return String.join(System.lineSeparator(), entries);
    }

    // Violation of SRP
    // Journal is taking care of 'Persistence concern'
    // Separation of concerns is not applied
    public void save(String filename) throws FileNotFoundException {
        try (PrintStream out = new PrintStream(filename)) {
            out.println(toString());
        }
    }
    public void load(String filename) {}
    public void load(URL url) {}
}

// Persistence concern is extracted into
// a different class
// SRP is not violated
class Persistence {
    public void saveToFile(Journal j, String filename, boolean overwrite) {
        if (overwrite || !new File(filename).exists()) {
            try (PrintStream o = new PrintStream(filename)) {
                o.println(j.toString());
            } catch (FileNotFoundException e) {
                System.out.println("File not found. " + e.getCause().getMessage());
            }
        }
    }
    public Journal loadFromFile(String filename) { return new Journal(); }
    public Journal loadFromUrl(URL url) {return new Journal();}
}

public class Demo {
    public static void main(String[] args) throws FileNotFoundException {
        Journal j = new Journal();
        j.addEntry("Hello ");
        j.addEntry("There ");

        // SRP violation
        j.save("new_file");
        System.out.println(j);

        // SRP applied
        Persistence p = new Persistence();
        p.saveToFile(j, "new_filename", true);
    }
}
