package org.example.FirstSpringBootLearn.dao;

import jakarta.persistence.EntityManager;
import org.example.FirstSpringBootLearn.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookDAO {

    private final EntityManager entityManager;

    @Autowired
    public BookDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void assign(int bookId, Person selectedPerson) {
        LocalDateTime now = LocalDateTime.now();

        entityManager.createNativeQuery("UPDATE book SET person_id = ?, borrowed_at = ? WHERE id = ?")
                .setParameter(1, selectedPerson.getId())
                .setParameter(2, now)
                .setParameter(3, bookId)
                .executeUpdate();
    }

    public void release(int id) {
        LocalDateTime now = LocalDateTime.now();

        entityManager.createNativeQuery("UPDATE book SET person_id = NULL, borrowed_at = null WHERE id = ?")
                .setParameter(1, id)
                .executeUpdate();
    }
}
