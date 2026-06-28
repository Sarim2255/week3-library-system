# Console-Based Library Management System
**The Developers Arena — Week 3 Internship Project**

A command-line Java application to manage books, members, and borrowing operations with file-based data persistence.

---

## Features

- Add, remove, and search for books
- Register and manage library members
- Borrow and return books (max 3 books per member)
- Search books by title, author, or genre
- Search members by name or ID
- File-based data persistence — data survives program restarts
- Comprehensive input validation and exception handling
- Library statistics view

---

## Project Structure

```
week3-library-system/
├── src/
│   └── main/
│       ├── java/
│       │   └── library/
│       │       ├── Main.java          ← Entry point & console menu
│       │       ├── Book.java          ← Book data model
│       │       ├── Member.java        ← Member data model
│       │       ├── Library.java       ← All business logic
│       │       └── FileHandler.java   ← File I/O (read/write)
│       └── resources/
├── data/
│   ├── books.txt                      ← Auto-generated on first run
│   └── members.txt                    ← Auto-generated on first run
├── README.md
├── .gitignore
└── pom.xml
```

---

## How to Run

### Compile
```bash
javac -d bin src/main/java/library/*.java
```

### Run
```bash
java -cp bin library.Main
```

> The `data/` folder and data files are created automatically on first run.

### Using Maven
```bash
mvn clean package
java -jar target/LibraryManagementSystem.jar
```

---

## Sample Menu

```
  === LIBRARY MANAGEMENT SYSTEM ===
  1. Add New Book
  2. View All Books
  3. Search Books
  4. Register Member
  5. Borrow Book
  6. Return Book
  7. View Library Statistics
  8. Exit

  Enter your choice:
```

---

## OOP Concepts Used

| Concept | Where Applied |
|---|---|
| Encapsulation | Private fields in Book, Member, Library with public getters/setters |
| Single Responsibility | FileHandler handles only file I/O; Library handles only logic |
| ArrayList | `ArrayList<Book>` and `ArrayList<Member>` for dynamic collections |
| Exception Handling | `NumberFormatException` caught in `getIntInput()`; all file I/O in try-catch |
| Static Factory Methods | `Book.fromFileString()` and `Member.fromFileString()` |
| Java Streams | `searchBooks()` and `searchMembers()` use `.stream().filter()` |

---

## Author
Week 3 Internship Submission — The Developers Arena
