package se.kth.alialaa.labb1db.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface declares methods for querying a Books database.
 * Different implementations of this interface handles the connection and
 * queries to a specific DBMS and database, for example a MySQL or a MongoDB
 * database.
 *
 * NB! The methods in the implementation must catch the SQL/MongoDBExceptions thrown
 * by the underlying driver, wrap in a BooksDbException and then re-throw the latter
 * exception. This way the interface is the same for both implementations, because the
 * exception type in the method signatures is the same. More info in BooksDbException.java.
 * 
 * @author anderslm@kth.se
 */
public interface BooksDbInterface {
    
    /**
     * Connect to the database.
     * @param database
     * @return true on successful connection.
     */
    public boolean connect(String database) throws BooksDbException;
    
    public void disconnect() throws BooksDbException;
    
    public List<Book> searchBooksByTitle(String title) throws BooksDbException;
    public List<Book> searchBooksByISBN(String ISBN) throws BooksDbException;
    public List<Book> searchBooksByAuthor(String author) throws BooksDbException;
    public List<Book> getAllBooks(String searchtitle) throws BooksDbException;
    public abstract void insert(Book book) throws BooksDbException;

    public abstract void delete(Book boook);
    public abstract void queries();

}
