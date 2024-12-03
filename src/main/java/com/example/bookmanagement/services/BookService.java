package com.example.bookmanagement.services;

import com.example.bookmanagement.model.Book;
import com.example.bookmanagement.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new RuntimeException("ISBN already exists.");
        }
        book.setIsbn(generateIsbn());
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Long id, Book book) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found.");
        }
        book.setId(id);
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found.");
        }
        bookRepository.deleteById(id);
    }

    public String generateIsbn() {
        String isbnPrefix = "978030640615"; // Some prefix, could be dynamic
        String isbnBase = isbnPrefix + (int) (Math.random() * 100000); // Generate a random 6-digit base
        int checkDigit = calculateIsbnCheckDigit(isbnBase);
        return isbnBase + checkDigit;
    }

    private int calculateIsbnCheckDigit(String isbnBase) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbnBase.charAt(i));
            sum += (i % 2 == 0 ? digit : digit * 3);
        }
        int remainder = sum % 10;
        return remainder == 0 ? 0 : 10 - remainder;
    }
}