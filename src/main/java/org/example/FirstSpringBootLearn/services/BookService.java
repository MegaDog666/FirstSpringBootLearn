package org.example.FirstSpringBootLearn.services;

import org.example.FirstSpringBootLearn.dao.BookDAO;
import org.example.FirstSpringBootLearn.models.Book;
import org.example.FirstSpringBootLearn.models.Person;
import org.example.FirstSpringBootLearn.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    private final BookDAO bookDAO;

    @Autowired
    public BookService(BookRepository bookRepository, BookDAO bookDAO) {
        this.bookRepository = bookRepository;
        this.bookDAO = bookDAO;
    }


    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = bookRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public Optional<Person> getBookOwner(int id) {
        return bookRepository.findOwnerById(id);
    }

    @Transactional
    public void assign(int bookId, Person selectedPerson) {
        bookDAO.assign(bookId, selectedPerson);
    }

    @Transactional
    public void release(int id) {
        bookDAO.release(id);
    }

    public List<Book> getSortedByYear() {
        return bookRepository.findAll(Sort.by("year"));
    }

    public List<Book> findWithPagination(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public List<Book> findWithPaginationAndSorting(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size, Sort.by("year"))).getContent();
    }

    public List<Book> findByTitleStartingWith(String searchBook) {
        return bookRepository.findByTitleStartingWith(searchBook);
    }

    public boolean isOverdue(int id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null)
            return false;

        LocalDateTime now = LocalDateTime.now();

        if (book.getBorrowedAt() == null)
            return false;

        return book.getBorrowedAt().isAfter(now.plusDays(10));
    }
}
