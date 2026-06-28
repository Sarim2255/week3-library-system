package library;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private static final String BOOKS_FILE   = "data/books.txt";
    private static final String MEMBERS_FILE = "data/members.txt";

    public static void saveBooks(ArrayList<Book> books) {
        ensureDataDirectory();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books) {
                writer.write(book.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("  [Warning] Could not save books: " + e.getMessage());
        }
    }

    /**
     * Reads all books from disk and returns them as an ArrayList.
     * Returns an empty list (not null) if the file does not yet exist.
     */
    public static ArrayList<Book> loadBooks() {
        ArrayList<Book> books = new ArrayList<>();
        File file = new File(BOOKS_FILE);
        if (!file.exists()) return books; // First run — no file yet

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    books.add(Book.fromFileString(line));
                }
            }
        } catch (IOException e) {
            System.out.println("  [Warning] Could not load books: " + e.getMessage());
        }
        return books;
    }

    public static void saveMembers(ArrayList<Member> members) {
        ensureDataDirectory();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEMBERS_FILE))) {
            for (Member member : members) {
                writer.write(member.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("  [Warning] Could not save members: " + e.getMessage());
        }
    }

    public static ArrayList<Member> loadMembers() {
        ArrayList<Member> members = new ArrayList<>();
        File file = new File(MEMBERS_FILE);
        if (!file.exists()) return members;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    members.add(Member.fromFileString(line));
                }
            }
        } catch (IOException e) {
            System.out.println("  [Warning] Could not load members: " + e.getMessage());
        }
        return members;
    }

    private static void ensureDataDirectory() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static String getBooksFilePath()   { return BOOKS_FILE; }
    public static String getMembersFilePath() { return MEMBERS_FILE; }
}
