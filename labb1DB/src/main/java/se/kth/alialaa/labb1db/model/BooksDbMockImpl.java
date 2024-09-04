/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.alialaa.labb1db.model;

import java.io.CharArrayReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A mock implementation of the BooksDBInterface interface to demonstrate how to
 * use it together with the user interface.
 * <p>
 * Your implementation must access a real database.
 *
 * @author anderslm@kth.se
 */
public class BooksDbMockImpl implements BooksDbInterface {

    private final List<Book> books;
    private Connection connection;
    public BooksDbMockImpl() {
        books = Arrays.asList(DATA);
    }

    @Override
    public boolean connect(String databaseUrl) throws BooksDbException {
        try {
            this.connection = DriverManager.getConnection(databaseUrl, "root", "1234567890");
            System.out.println("Database connection established successfully.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // Log the full stack trace
            throw new BooksDbException("Failed to connect to the database: " + e.getMessage(), e);
        }
    }

    @Override
    public void disconnect() throws BooksDbException {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            throw new BooksDbException("Failed to disconnect from the database", e);
        }
    }
    @Override
    public List<Book> getAllBooks(String searchtitle) throws BooksDbException {
        List<Book> allBooks = new ArrayList<>();
        String sql = "SELECT * FROM Books";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Assuming you have a constructor in Book class that takes ResultSet
                // Alternatively, extract data from rs and pass it to the Book constructor
                int bookId = rs.getInt("bookId"); // Adjust the column name as per your database
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                Date published = rs.getDate("published");
                ArrayList<String> genres = new ArrayList<>(); // Populate this as needed
                ArrayList<Author> authors = new ArrayList<>(); // Populate this as needed

                Book book = new Book(bookId, isbn, title, published, genres, authors);
                allBooks.add(book);
            }
        } catch (SQLException e) {
            throw new BooksDbException("Failed to retrieve all books", e);
        }

        return allBooks;
    }

    @Override
    public List<Book> searchBooksByTitle(String searchTitle) throws BooksDbException {
        List<Book> result = new ArrayList<>();
        String sql = "SELECT * FROM Books WHERE LOWER(Title) LIKE ?";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTitle.toLowerCase() + "%");
            System.out.println("Executing query: " + stmt); // Log the query

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("No books found with the title: " + searchTitle);
                    return result; // Return empty list if no books found
                }

                while (rs.next()) {
                    // Create and add book objects to the result list
                    //Book newBook = new Book(rs.getInt(""))
                    //result.add(book)
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the full stack trace
            throw new BooksDbException("Failed to search books by title", e);
        }

        return result;
    }

    @Override
    public List<Book> searchBooksByISBN(String ISBN) throws BooksDbException {
        List<Book> result = new ArrayList<>();
        ISBN = ISBN.toLowerCase();
        for (Book book : books) {
            if(book.getIsbn().toLowerCase().contains(ISBN)) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) throws BooksDbException {
        List<Book> result = new ArrayList<>();
        author = author.toLowerCase();
        for (Book book: books) {
            for(Author bookauthor: book.getAuthors()) {
                if(bookauthor.getName().toLowerCase().contains(author) || bookauthor.getLastName().toLowerCase().contains(author)) {
                    result.add(book);
                }
            }
        }
        return result;
    }

    @Override
    public void insert(Book book) throws BooksDbException {
        // Insert book details
        String bookSql = "INSERT INTO Books (ISBN, Title, PublishedDate) VALUES (?, ?, ?)";
        executeInsertBook(bookSql, book);

        // Insert authors for the book
        for (Author author : book.getAuthors()) {
            String authorSql = "INSERT INTO BookAuthors (ISBN, AuthorID) VALUES (?, ?)";
            executeInsertBookAuthor(authorSql, book.getIsbn(), author.getAuthorId());
        }

        // Insert genres for the book
        for (String genre : book.getGenres()) {
            String genreSql = "INSERT INTO BookGenres (ISBN, GenreID) VALUES (?, ?)";
            executeInsertBookGenre(genreSql, book.getIsbn(), genre);
        }
    }

    private void executeInsertBook(String sql, Book book) throws BooksDbException {
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setDate(3, new java.sql.Date(book.getPublished().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BooksDbException("Failed to insert book: " + book.getTitle(), e);
        }
    }

    private void executeInsertBookAuthor(String sql, String isbn, int authorId) throws BooksDbException {
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.setInt(2, authorId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BooksDbException("Failed to insert book-author relation", e);
        }
    }

    private void executeInsertBookGenre(String sql, String isbn, String genre) throws BooksDbException {
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.setString(2, genre); // Assuming genre is a String. Adjust if it's an integer ID.
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BooksDbException("Failed to insert book-genre relation", e);
        }
    }

    @Override
    public void delete(Book book) {

    }

    @Override
    public void queries() {

    }

    private static final Book[] DATA = {
            new Book(1, "123456789", "Databases Illuminated", new Date(2018, 1, 1), new ArrayList<>(Arrays.asList("Horror")), new ArrayList<>(Arrays.asList(new Author(1,"Alaa", "Abdulrazzaq"),
                    new Author(2,"Ali", "Nabizada")))),
            new Book(2, "234567891", "Dark Databases", new Date(1990, 1, 1), new ArrayList<>(Arrays.asList("Horror")), new ArrayList<>(Arrays.asList(new Author(3,"Messi", "Lionel")))),
            new Book(3, "456789012", "The buried giant", new Date(2000, 1, 1), new ArrayList<>(Arrays.asList("Horror")), new ArrayList<>(Arrays.asList(new Author(4,"Christiano", "Ronaldo")))),
            new Book(4, "567890123", "Never let me go", new Date(2000, 1, 1), new ArrayList<>(Arrays.asList("Horror")), new ArrayList<>(Arrays.asList(new Author(5,"Nicklas", "Stenkrona")))),
            new Book(5, "678901234", "The remains of the day", new Date(2000, 1, 1), new ArrayList<>(Arrays.asList("Horror")), new ArrayList<>(Arrays.asList(new Author(6,"Dag", "Hammarskjöld")))),
            new Book(6, "234567890", "Alias Grace", new Date(2000, 1, 1), new ArrayList<>(Arrays.asList("Horror")), new ArrayList<>(Arrays.asList(new Author(7,"Alan", "Messi")))),
            new Book(7, "345678911", "The handmaids tale", new Date(2010, 1, 1), new ArrayList<>(Arrays.asList("Horror")), new ArrayList<>(Arrays.asList(new Author(8,"Robel","Brre")))),
            new Book(8, "345678901", "Shuggie Bain", new Date(2020, 1, 1), new ArrayList<>(Arrays.asList("Horror")), new ArrayList<>(Arrays.asList(new Author(2,"Ali", "Nabizada")))),
            new Book(9, "345678912", "Microserfs", new Date(2000, 1, 1), new ArrayList<>(Arrays.asList("Horror")), new ArrayList<>(Arrays.asList(new Author(1,"Alaa", "Abdulrazzaq")))),
    };
}
