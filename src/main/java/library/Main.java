package library;

import java.util.List;
import java.util.Scanner;
public class Main {

    private static final Library library = new Library();
    private static final Scanner scanner  = new Scanner(System.in);

    // ── Entry Point ──────────────────────────────────────────────────────────
    public static void main(String[] args) {
        printWelcomeBanner();
        loadDemoDataIfEmpty();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> bookMenu();
                case 2 -> memberMenu();
                case 3 -> borrowingMenu();
                case 4 -> searchMenu();
                case 5 -> viewStatistics();
                case 0 -> {
                    System.out.println("\n  Goodbye! Thank you for using the Library Management System.");
                    running = false;
                }
                default -> System.out.println("\n  Invalid choice. Please enter a number between 0 and 5.");
            }
        }
        scanner.close();
    }


    static void printMainMenu() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║       LIBRARY MANAGEMENT SYSTEM      ║");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.println("  ║   1.  Book Management                ║");
        System.out.println("  ║   2.  Member Management              ║");
        System.out.println("  ║   3.  Borrow / Return                ║");
        System.out.println("  ║   4.  Search & Filter                ║");
        System.out.println("  ║   5.  Library Statistics             ║");
        System.out.println("  ║   0.  Exit                           ║");
        System.out.println("  ╚══════════════════════════════════════╝");
    }


    static void bookMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  --- Book Management ---");
            System.out.println("  1. Add a New Book");
            System.out.println("  2. Remove a Book");
            System.out.println("  3. View All Books");
            System.out.println("  4. View Available Books");
            System.out.println("  5. View Borrowed Books");
            System.out.println("  0. Back to Main Menu");
            switch (getIntInput("  Choice: ")) {
                case 1 -> addBook();
                case 2 -> removeBook();
                case 3 -> viewAllBooks();
                case 4 -> viewAvailableBooks();
                case 5 -> viewBorrowedBooks();
                case 0 -> back = true;
                default -> System.out.println("  Invalid option.");
            }
        }
    }

    static void addBook() {
        System.out.println("\n  -- Add New Book --");
        System.out.print("  ISBN   : ");
        String isbn = scanner.nextLine().trim();

        System.out.print("  Title  : ");
        String title = scanner.nextLine().trim();

        System.out.print("  Author : ");
        String author = scanner.nextLine().trim();

        System.out.print("  Genre  : ");
        String genre = scanner.nextLine().trim();

        // Input validation — ISBN, Title, and Author are required
        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty()) {
            System.out.println("  ERROR: ISBN, Title, and Author are all required.");
            return;
        }
        if (genre.isEmpty()) genre = "General";

        Book book = new Book(isbn, title, author, genre);
        if (library.addBook(book)) {
            System.out.println("  Book added successfully and saved to file.");
        } else {
            System.out.println("  ERROR: A book with ISBN \"" + isbn + "\" already exists.");
        }
    }

    static void removeBook() {
        System.out.print("\n  Enter the ISBN of the book to remove: ");
        String isbn = scanner.nextLine().trim();

        Book book = library.findBookByIsbn(isbn);
        if (book == null) {
            System.out.println("  ERROR: No book found with that ISBN.");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("  ERROR: Cannot remove \"" + book.getTitle() + "\" — it is currently borrowed.");
            System.out.println("         The book must be returned before it can be removed.");
            return;
        }

        System.out.println("  Book found: " + book.getTitle() + " by " + book.getAuthor());
        System.out.print("  Are you sure you want to remove it? (yes / no): ");
        if ("yes".equalsIgnoreCase(scanner.nextLine().trim())) {
            library.removeBook(isbn);
            System.out.println("  Book removed successfully.");
        } else {
            System.out.println("  Cancelled.");
        }
    }

    static void viewAllBooks() {
        List<Book> books = library.getAllBooks();
        System.out.println("\n  === All Books (" + books.size() + " total) ===");
        if (books.isEmpty()) {
            System.out.println("  No books in the library yet.");
        } else {
            for (int i = 0; i < books.size(); i++) {
                System.out.println("\n  [" + (i + 1) + "]");
                System.out.println(books.get(i));
                System.out.println("  " + "-".repeat(40));
            }
        }
    }

    static void viewAvailableBooks() {
        List<Book> books = library.getAvailableBooks();
        System.out.println("\n  === Available Books (" + books.size() + ") ===");
        if (books.isEmpty()) {
            System.out.println("  No books are currently available.");
        } else {
            for (int i = 0; i < books.size(); i++) {
                System.out.println("\n  [" + (i + 1) + "]");
                System.out.println(books.get(i));
                System.out.println("  " + "-".repeat(40));
            }
        }
    }

    static void viewBorrowedBooks() {
        List<Book> books = library.getBorrowedBooks();
        System.out.println("\n  === Currently Borrowed Books (" + books.size() + ") ===");
        if (books.isEmpty()) {
            System.out.println("  No books are currently checked out.");
        } else {
            for (int i = 0; i < books.size(); i++) {
                System.out.println("\n  [" + (i + 1) + "]");
                System.out.println(books.get(i));
                System.out.println("  " + "-".repeat(40));
            }
        }
    }


    static void memberMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  --- Member Management ---");
            System.out.println("  1. Register New Member");
            System.out.println("  2. Remove a Member");
            System.out.println("  3. View All Members");
            System.out.println("  4. View Member Details");
            System.out.println("  0. Back to Main Menu");
            switch (getIntInput("  Choice: ")) {
                case 1 -> addMember();
                case 2 -> removeMember();
                case 3 -> viewAllMembers();
                case 4 -> viewMemberDetails();
                case 0 -> back = true;
                default -> System.out.println("  Invalid option.");
            }
        }
    }

    static void addMember() {
        System.out.println("\n  -- Register New Member --");
        System.out.print("  Member ID : ");
        String id = scanner.nextLine().trim();

        System.out.print("  Name      : ");
        String name = scanner.nextLine().trim();

        System.out.print("  Email     : ");
        String email = scanner.nextLine().trim();

        if (id.isEmpty() || name.isEmpty()) {
            System.out.println("  ERROR: Member ID and Name are required.");
            return;
        }

        Member member = new Member(id, name, email);
        if (library.addMember(member)) {
            System.out.println("  Member registered successfully and saved to file.");
        } else {
            System.out.println("  ERROR: A member with ID \"" + id + "\" already exists.");
        }
    }

    static void removeMember() {
        System.out.print("\n  Enter Member ID to remove: ");
        String id = scanner.nextLine().trim();

        Member member = library.findMemberById(id);
        if (member == null) {
            System.out.println("  ERROR: No member found with that ID.");
            return;
        }
        if (!member.getBorrowedIsbns().isEmpty()) {
            System.out.println("  ERROR: Cannot remove \"" + member.getName() + "\" — they still have books checked out:");
            for (String isbn : member.getBorrowedIsbns()) {
                Book b = library.findBookByIsbn(isbn);
                System.out.println("         - " + (b != null ? b.getTitle() : isbn));
            }
            System.out.println("         All books must be returned before the member can be removed.");
            return;
        }

        library.removeMember(id);
        System.out.println("  Member removed successfully.");
    }

    static void viewAllMembers() {
        List<Member> members = library.getAllMembers();
        System.out.println("\n  === All Members (" + members.size() + " registered) ===");
        if (members.isEmpty()) {
            System.out.println("  No members registered yet.");
        } else {
            for (int i = 0; i < members.size(); i++) {
                System.out.println("\n  [" + (i + 1) + "]");
                System.out.println(members.get(i));
                System.out.println("  " + "-".repeat(40));
            }
        }
    }

    static void viewMemberDetails() {
        System.out.print("\n  Enter Member ID: ");
        String id = scanner.nextLine().trim();

        Member member = library.findMemberById(id);
        if (member == null) {
            System.out.println("  ERROR: No member found with that ID.");
            return;
        }

        System.out.println("\n  === Member Details ===");
        System.out.println(member);

        List<String> isbns = member.getBorrowedIsbns();
        if (isbns.isEmpty()) {
            System.out.println("  Books Out : None");
        } else {
            System.out.println("  Books Out :");
            for (String isbn : isbns) {
                Book book = library.findBookByIsbn(isbn);
                System.out.println("    - " + (book != null ? book.getTitle() + " (ISBN: " + isbn + ")" : isbn));
            }
        }
    }


    static void borrowingMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  --- Borrow / Return ---");
            System.out.println("  1. Borrow a Book");
            System.out.println("  2. Return a Book");
            System.out.println("  0. Back to Main Menu");
            switch (getIntInput("  Choice: ")) {
                case 1 -> doBorrow();
                case 2 -> doReturn();
                case 0 -> back = true;
                default -> System.out.println("  Invalid option.");
            }
        }
    }

    static void doBorrow() {
        System.out.println("\n  -- Borrow Book --");
        System.out.print("  Member ID  : ");
        String memberId = scanner.nextLine().trim();
        System.out.print("  Book ISBN  : ");
        String isbn = scanner.nextLine().trim();
        System.out.println("  " + library.borrowBook(memberId, isbn));
    }

    static void doReturn() {
        System.out.println("\n  -- Return Book --");
        System.out.print("  Member ID  : ");
        String memberId = scanner.nextLine().trim();
        System.out.print("  Book ISBN  : ");
        String isbn = scanner.nextLine().trim();
        System.out.println("  " + library.returnBook(memberId, isbn));
    }


    static void searchMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  --- Search & Filter ---");
            System.out.println("  1. Search Books  (by title, author, or genre)");
            System.out.println("  2. Search Members  (by name or ID)");
            System.out.println("  3. Find Book by exact ISBN");
            System.out.println("  0. Back to Main Menu");
            switch (getIntInput("  Choice: ")) {
                case 1 -> searchBooks();
                case 2 -> searchMembers();
                case 3 -> findByIsbn();
                case 0 -> back = true;
                default -> System.out.println("  Invalid option.");
            }
        }
    }

    static void searchBooks() {
        System.out.print("\n  Enter keyword to search: ");
        String keyword = scanner.nextLine().trim();
        List<Book> results = library.searchBooks(keyword);
        System.out.println("\n  Found " + results.size() + " result(s) for \"" + keyword + "\":");
        if (results.isEmpty()) {
            System.out.println("  No books matched. Try a different keyword.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                System.out.println("\n  [" + (i + 1) + "]");
                System.out.println(results.get(i));
                System.out.println("  " + "-".repeat(40));
            }
        }
    }

    static void searchMembers() {
        System.out.print("\n  Enter name or ID to search: ");
        String keyword = scanner.nextLine().trim();
        List<Member> results = library.searchMembers(keyword);
        System.out.println("\n  Found " + results.size() + " result(s):");
        if (results.isEmpty()) {
            System.out.println("  No members matched.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                System.out.println("\n  [" + (i + 1) + "]");
                System.out.println(results.get(i));
                System.out.println("  " + "-".repeat(40));
            }
        }
    }

    static void findByIsbn() {
        System.out.print("\n  Enter ISBN: ");
        String isbn = scanner.nextLine().trim();
        Book book = library.findBookByIsbn(isbn);
        if (book == null) {
            System.out.println("  No book found with ISBN: " + isbn);
        } else {
            System.out.println("\n  Book found:");
            System.out.println(book);
        }
    }


    static void viewStatistics() {
        System.out.println("\n  === Library Statistics ===");
        System.out.println("  Total Books       : " + library.getTotalBooks());
        System.out.println("  Available Books   : " + library.getAvailableCount());
        System.out.println("  Borrowed Books    : " + library.getBorrowedCount());
        System.out.println("  Registered Members: " + library.getTotalMembers());
        System.out.println("  Data saved to     : " + FileHandler.getBooksFilePath()
                                               + " / " + FileHandler.getMembersFilePath());
    }

    static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input — please enter a number.");
            }
        }
    }

    static void printWelcomeBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════╗");
        System.out.println("  ║      CONSOLE-BASED LIBRARY MANAGEMENT SYSTEM     ║");
        System.out.println("  ║                                                  ║");
        System.out.println("  ╠══════════════════════════════════════════════════╣");
        System.out.println("  ║  Data is automatically saved to the data/ folder ║");
        System.out.println("  ╚══════════════════════════════════════════════════╝");
    }

    static void loadDemoDataIfEmpty() {
        if (library.getTotalBooks() == 0) {
            library.addBook(new Book("ISBN-001", "Clean Code",               "Robert C. Martin", "Technology"));
            library.addBook(new Book("ISBN-002", "The Pragmatic Programmer", "David Thomas",     "Technology"));
            library.addBook(new Book("ISBN-003", "1984",                    "George Orwell",    "Dystopian"));
            library.addBook(new Book("ISBN-004", "To Kill a Mockingbird",   "Harper Lee",       "Fiction"));
            library.addBook(new Book("ISBN-005", "Think and Grow Rich",     "Napoleon Hill",    "Self-Help"));
            System.out.println("  [Demo] 5 sample books loaded.");
        }
        if (library.getTotalMembers() == 0) {
            library.addMember(new Member("M001", "Alice Johnson", "alice@email.com"));
            library.addMember(new Member("M002", "Bob Smith",     "bob@email.com"));
            System.out.println("  [Demo] 2 sample members loaded.");
        }
    }
}
