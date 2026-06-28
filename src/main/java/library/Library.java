package library;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Library.java
 * The core of the system — contains all business logic.
 *
 * Responsibilities:
 *   - Manage the in-memory collections of books and members
 *   - Validate all operations before executing them
 *   - Delegate all file reading/writing to FileHandler
 *   - Return clear result messages to the UI (Main.java)
 *
 * OOP Concepts:
 *   - Encapsulation: private ArrayLists, public methods only
 *   - Separation of Concerns: file I/O is entirely in FileHandler
 */
public class Library {

    // ── In-Memory Collections ────────────────────────────────────────────────
    private ArrayList<Book>   books;
    private ArrayList<Member> members;

    // ── Constructor ──────────────────────────────────────────────────────────
    /**
     * On startup, load all previously saved data from disk into memory.
     * If the data files don't exist yet, empty lists are returned by FileHandler.
     */
    public Library() {
        this.books   = FileHandler.loadBooks();
        this.members = FileHandler.loadMembers();
    }

    // ════════════════════════════════════════════════════════════════════════
    // BOOK OPERATIONS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Adds a new book to the library.
     * Rejects duplicates — each ISBN must be unique.
     */
    public boolean addBook(Book book) {
        if (findBookByIsbn(book.getIsbn()) != null) {
            return false; // Duplicate ISBN
        }
        books.add(book);
        FileHandler.saveBooks(books); // Persist immediately
        return true;
    }

    /**
     * Removes a book from the library.
     * A book cannot be removed while it is borrowed — it must be returned first.
     */
    public boolean removeBook(String isbn) {
        Book book = findBookByIsbn(isbn);
        if (book == null || !book.isAvailable()) return false;
        books.remove(book);
        FileHandler.saveBooks(books);
        return true;
    }

    /**
     * Finds a book by its exact ISBN (case-insensitive).
     * Returns null if no match is found.
     */
    public Book findBookByIsbn(String isbn) {
        for (Book b : books) {
            if (b.getIsbn().equalsIgnoreCase(isbn.trim())) return b;
        }
        return null;
    }

    /**
     * Searches books by a keyword across title, author, and genre fields.
     * Case-insensitive. Uses Java Streams for clean filtering.
     */
    public List<Book> searchBooks(String keyword) {
        String kw = keyword.toLowerCase().trim();
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(kw)
                          || b.getAuthor().toLowerCase().contains(kw)
                          || b.getGenre().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public ArrayList<Book> getAllBooks()     { return books; }

    public List<Book> getAvailableBooks() {
        return books.stream()
                    .filter(Book::isAvailable)
                    .collect(Collectors.toList());
    }

    public List<Book> getBorrowedBooks() {
        return books.stream()
                    .filter(b -> !b.isAvailable())
                    .collect(Collectors.toList());
    }

    // ════════════════════════════════════════════════════════════════════════
    // MEMBER OPERATIONS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Registers a new library member.
     * Rejects duplicates — each member ID must be unique.
     */
    public boolean addMember(Member member) {
        if (findMemberById(member.getMemberId()) != null) {
            return false; // Duplicate ID
        }
        members.add(member);
        FileHandler.saveMembers(members);
        return true;
    }

    /**
     * Removes a member from the library.
     * A member cannot be removed while they still have books checked out.
     */
    public boolean removeMember(String memberId) {
        Member member = findMemberById(memberId);
        if (member == null || !member.getBorrowedIsbns().isEmpty()) return false;
        members.remove(member);
        FileHandler.saveMembers(members);
        return true;
    }

    /**
     * Finds a member by their exact ID (case-insensitive).
     * Returns null if no match is found.
     */
    public Member findMemberById(String memberId) {
        for (Member m : members) {
            if (m.getMemberId().equalsIgnoreCase(memberId.trim())) return m;
        }
        return null;
    }

    /**
     * Searches members by name or ID keyword (case-insensitive).
     */
    public List<Member> searchMembers(String keyword) {
        String kw = keyword.toLowerCase().trim();
        return members.stream()
                .filter(m -> m.getName().toLowerCase().contains(kw)
                          || m.getMemberId().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public ArrayList<Member> getAllMembers() { return members; }

    // ════════════════════════════════════════════════════════════════════════
    // BORROWING OPERATIONS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Issues a book to a member.
     *
     * Validation steps (in order):
     *   1. Member must exist
     *   2. Book must exist
     *   3. Book must currently be available
     *   4. Member must not have reached the borrow limit (max 3)
     *
     * On success: marks book as unavailable, adds ISBN to member's list,
     *             saves both files to disk.
     */
    public String borrowBook(String memberId, String isbn) {
        Member member = findMemberById(memberId);
        if (member == null)
            return "ERROR: Member not found. ID: " + memberId;

        Book book = findBookByIsbn(isbn);
        if (book == null)
            return "ERROR: Book not found. ISBN: " + isbn;

        if (!book.isAvailable())
            return "ERROR: \"" + book.getTitle() + "\" is already borrowed by someone else.";

        if (!member.borrowBook(isbn))
            return "ERROR: " + member.getName() + " has reached the borrow limit (max 3 books).";

        // All checks passed — update both objects and persist
        book.setAvailable(false);
        FileHandler.saveBooks(books);
        FileHandler.saveMembers(members);

        return "SUCCESS: \"" + book.getTitle() + "\" has been issued to " + member.getName() + ".";
    }

    /**
     * Returns a borrowed book back to the library.
     *
     * Validation steps:
     *   1. Member must exist
     *   2. Book must exist
     *   3. Confirm this member actually borrowed this book
     *
     * On success: marks book as available, removes ISBN from member's list,
     *             saves both files to disk.
     */
    public String returnBook(String memberId, String isbn) {
        Member member = findMemberById(memberId);
        if (member == null)
            return "ERROR: Member not found. ID: " + memberId;

        Book book = findBookByIsbn(isbn);
        if (book == null)
            return "ERROR: Book not found. ISBN: " + isbn;

        if (!member.hasBorrowed(isbn))
            return "ERROR: " + member.getName() + " does not have this book checked out.";

        // All checks passed — restore book and update member
        member.returnBook(isbn);
        book.setAvailable(true);
        FileHandler.saveBooks(books);
        FileHandler.saveMembers(members);

        return "SUCCESS: \"" + book.getTitle() + "\" has been returned. Thank you, " + member.getName() + ".";
    }

    // ════════════════════════════════════════════════════════════════════════
    // STATISTICS
    // ════════════════════════════════════════════════════════════════════════

    public int  getTotalBooks()     { return books.size(); }
    public int  getTotalMembers()   { return members.size(); }
    public long getAvailableCount() { return books.stream().filter(Book::isAvailable).count(); }
    public long getBorrowedCount()  { return books.stream().filter(b -> !b.isAvailable()).count(); }
}
