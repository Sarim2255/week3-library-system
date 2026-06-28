package library;

import java.util.ArrayList;
public class Member {

    private static final int MAX_BORROW_LIMIT = 3;


    private String            memberId;  
    private String            name;      
    private String            email;     
    private ArrayList<String> borrowedIsbns;


    public Member(String memberId, String name, String email) {
        this.memberId      = memberId;
        this.name          = name;
        this.email         = email;
        this.borrowedIsbns = new ArrayList<>();
    }

    public String            getMemberId()      { return memberId; }
    public String            getName()          { return name; }
    public String            getEmail()         { return email; }
    public ArrayList<String> getBorrowedIsbns() { return borrowedIsbns; }
    public int               getBorrowLimit()   { return MAX_BORROW_LIMIT; }

    public void setName(String name)   { this.name  = name; }
    public void setEmail(String email) { this.email = email; }

    public boolean borrowBook(String isbn) {
        if (borrowedIsbns.size() >= MAX_BORROW_LIMIT) {
            return false; // Limit reached — cannot borrow more
        }
        borrowedIsbns.add(isbn);
        return true;
    }


    public boolean returnBook(String isbn) {
        return borrowedIsbns.remove(isbn);
    }


    public boolean hasBorrowed(String isbn) {
        return borrowedIsbns.contains(isbn);
    }

    public String toFileString() {
        String isbns = String.join(";", borrowedIsbns);
        return memberId + "|" + name + "|" + email + "|" + isbns;
    }

    public static Member fromFileString(String line) {
        String[] parts = line.split("\\|", 4);
        Member member = new Member(parts[0], parts[1], parts[2]);
        // Restore borrowed ISBNs if any exist
        if (parts.length == 4 && !parts[3].isEmpty()) {
            for (String isbn : parts[3].split(";")) {
                member.getBorrowedIsbns().add(isbn);
            }
        }
        return member;
    }


    @Override
    public String toString() {
        return String.format(
            "  Member ID : %s%n  Name      : %s%n  Email     : %s%n  Borrowed  : %d / %d books",
            memberId, name, email,
            borrowedIsbns.size(), MAX_BORROW_LIMIT
        );
    }
}
