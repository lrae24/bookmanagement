package com.example.bookmanagement;


import com.example.bookmanagement.model.Book;
import com.example.bookmanagement.repository.BookRepository;
import com.example.bookmanagement.services.BookService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {

    @MockBean
    private BookRepository bookRepository; // Mock the repository

    @Autowired
    private BookService bookService; // Autowire the service to be tested

    @Test
    public void testCreateBook() {
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        book.setIsbn("9780306406157");

        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);
        Book savedBook = bookService.createBook(book);

        Assert.assertNotNull(savedBook);
        Assert.assertEquals("The Great Gatsby", savedBook.getTitle());
        Mockito.verify(bookRepository, Mockito.times(1)).save(Mockito.any(Book.class));
    }

    @Test
    public void testGetAllBooks() {
        Book book1 = new Book("Book 1", "Author 1");
        Book book2 = new Book("Book 2", "Author 2");

        Mockito.when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.getAllBooks();

        Assert.assertNotNull(books);
        Assert.assertEquals(2, books.size());
    }

    @Test
    public void testGetBookById() {
        Long bookId = 1L;
        Book book = new Book("Book", "Author");

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.getBookById(bookId);

        Assert.assertTrue(foundBook.isPresent());
        Assert.assertEquals("Book", foundBook.get().getTitle());
    }

    @Test
    public void testUpdateBook() {
        Long bookId = 1L;
        Book existingBook = new Book("Old Title", "Old Author");
        existingBook.setId(bookId);

        Book updatedBook = new Book("New Title", "New Author");
        updatedBook.setId(bookId);

        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(updatedBook);

        Book result = bookService.updateBook(bookId, updatedBook);

        Assert.assertNotNull(result);
        Assert.assertEquals("New Title", result.getTitle());
    }

    @Test
    public void testDeleteBook() {
        Long bookId = 1L;

        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);
        bookService.deleteBook(bookId);

        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(bookId);
    }
}
