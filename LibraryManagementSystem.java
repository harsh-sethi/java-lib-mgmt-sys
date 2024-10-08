import java.util.*;
import java.io.*;

class Book implements Serializable {
    String title;
    String author;
    boolean isBorrowed;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
    }

    public void borrowBook() {
        if (!isBorrowed) {
            isBorrowed = true;
            System.out.println("Book borrowed: " + title);
        } else {
            System.out.println("Book is already borrowed.");
        }
    }

    public void returnBook() {
        if (isBorrowed) {
            isBorrowed = false;
            System.out.println("Book returned: " + title);
        } else {
            System.out.println("This book was not borrowed.");
        }
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", Borrowed: " + (isBorrowed ? "Yes" : "No");
    }
}

class Member implements Serializable {
    String name;
    int borrowedBooks;
    final int MAX_BORROW_LIMIT = 5;

    public Member(String name) {
        this.name = name;
        this.borrowedBooks = 0;
    }

    public void borrowBook(Book book) {
        if (borrowedBooks < MAX_BORROW_LIMIT && !book.isBorrowed) {
            book.borrowBook();
            borrowedBooks++;
        } else if (borrowedBooks >= MAX_BORROW_LIMIT) {
            System.out.println("Borrow limit reached for member: " + name);
        }
    }

    public void returnBook(Book book) {
        if (book.isBorrowed) {
            book.returnBook();
            borrowedBooks--;
        }
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Borrowed Books: " + borrowedBooks;
    }
}

public class LibraryManagementSystem {
    static List<Book> books = new ArrayList<>();
    static List<Member> members = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadData();
        while (true) {
            System.out.println("\n--- Library Management System ---");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Add Member");
            System.out.println("6. View Members");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    viewBooks();
                    break;
                case 3:
                    borrowBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    addMember();
                    break;
                case 6:
                    viewMembers();
                    break;
                case 7:
                    saveData();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        books.add(new Book(title, author));
        System.out.println("Book added successfully.");
    }

    public static void viewBooks() {
        System.out.println("\n--- Book List ---");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public static void borrowBook() {
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        Member member = findMemberByName(name);
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }
        System.out.print("Enter book title to borrow: ");
        String title = scanner.nextLine();
        Book book = findBookByTitle(title);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        member.borrowBook(book);
    }

    public static void returnBook() {
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        Member member = findMemberByName(name);
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }
        System.out.print("Enter book title to return: ");
        String title = scanner.nextLine();
        Book book = findBookByTitle(title);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        member.returnBook(book);
    }

    public static void addMember() {
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        members.add(new Member(name));
        System.out.println("Member added successfully.");
    }

    public static void viewMembers() {
        System.out.println("\n--- Member List ---");
        for (Member member : members) {
            System.out.println(member);
        }
    }

    public static Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.title.equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    public static Member findMemberByName(String name) {
        for (Member member : members) {
            if (member.name.equalsIgnoreCase(name)) {
                return member;
            }
        }
        return null;
    }

    public static void saveData() {
        try {
            ObjectOutputStream bookOut = new ObjectOutputStream(new FileOutputStream("books.dat"));
            bookOut.writeObject(books);
            bookOut.close();

            ObjectOutputStream memberOut = new ObjectOutputStream(new FileOutputStream("members.dat"));
            memberOut.writeObject(members);
            memberOut.close();

            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    public static void loadData() {
        try {
            ObjectInputStream bookIn = new ObjectInputStream(new FileInputStream("books.dat"));
            books = (List<Book>) bookIn.readObject();
            bookIn.close();

            ObjectInputStream memberIn = new ObjectInputStream(new FileInputStream("members.dat"));
            members = (List<Member>) memberIn.readObject();
            memberIn.close();

            System.out.println("Data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous data found.");
        }
    }
}
