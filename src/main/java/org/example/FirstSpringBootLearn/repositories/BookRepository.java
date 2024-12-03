package org.example.FirstSpringBootLearn.repositories;

import org.example.FirstSpringBootLearn.models.Book;
import org.example.FirstSpringBootLearn.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT p FROM Book b JOIN b.owner p WHERE b.id = :bookId")
    Optional<Person> findOwnerById(@Param("bookId")int id);

    List<Book> findByTitleStartingWith(String searchBook);
}
