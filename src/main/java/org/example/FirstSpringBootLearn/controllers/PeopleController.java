package org.example.FirstSpringBootLearn.controllers;

import jakarta.validation.Valid;
import org.example.FirstSpringBootLearn.models.Book;
import org.example.FirstSpringBootLearn.models.Person;
import org.example.FirstSpringBootLearn.services.BookService;
import org.example.FirstSpringBootLearn.services.PeopleService;
import org.example.FirstSpringBootLearn.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    private final PersonValidator personValidator;

    private final BookService bookService;

//    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator, BookService bookService) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
        this.bookService = bookService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,
                       Model model) {
        List<Book> books = peopleService.getBooksByPersonId(id);
        List<Boolean> overdueList = books.stream()
                .map(book -> bookService.isOverdue(book.getId()))
                .collect(Collectors.toList());

        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", books);
        model.addAttribute("overdue", overdueList);
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }
    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/new";

        peopleService.save(person);
        return "redirect:/people";
    }
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors())
            return "people/edit";

        peopleService.update(id, person);
        return "redirect:/people";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
