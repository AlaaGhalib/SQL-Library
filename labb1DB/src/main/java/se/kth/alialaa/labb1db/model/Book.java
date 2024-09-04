package se.kth.alialaa.labb1db.model;

import java.sql.Array;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Representation of a book.
 * 
 * @author anderslm@kth.se
 */
public class Book {
    
    private int bookId;
    private String isbn; // should check format
    private String title;
    private Date published;
    private String storyLine = "";
    private ArrayList<String> genres;
    private ArrayList<Author> authors;
    
    public Book(int bookId, String isbn, String title, Date published, ArrayList<String> genres, ArrayList<Author> authors) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.published = published;
        this.authors = new ArrayList<>();
        this.genres = genres;
        fillAuthor(authors);
    }
    
    public int getBookId() { return bookId; }

    public String getIsbn() { return isbn; }

    public String getTitle() { return title; }

    public Date getPublished() { return published; }

    public String getStoryLine() { return storyLine; }

    public ArrayList<Author> getAuthors() {
        ArrayList<Author> copy = new ArrayList<>();
        for (Author author: authors) {
            copy.add(new Author(author.getAuthorId(),author.getName(),author.getLastName()));
        }
        return copy;
    }
    
    public void setStoryLine(String storyLine) {
        this.storyLine = storyLine;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    private void fillAuthor(ArrayList<Author> authors) {
        for (Author author: authors) {
            this.authors.add(new Author(author.getAuthorId(),author.getName(),author.getLastName()));
        }
    }
    
    @Override
    public String toString() {
        return title + ", " + isbn + ", " + published + ", " + authors.toString() + ", " + genres;
    }
}
