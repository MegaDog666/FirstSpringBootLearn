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
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    @Query("select b from Book b where b.owner.id = :personId")
    List<Book> getBooksById(@Param("personId") int personId);

    Optional<Person> getPersonByFullName(String fullName);
}
