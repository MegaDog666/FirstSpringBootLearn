package org.example.FirstSpringBootLearn.controllers;

import jakarta.validation.Valid;
import org.example.FirstSpringBootLearn.models.Book;
import org.example.FirstSpringBootLearn.models.Person;
import org.example.FirstSpringBootLearn.services.BookService;
import org.example.FirstSpringBootLearn.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final PeopleService peopleService;

    private final BookService booksService;


    @Autowired
    public BookController(BookService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "book_per_page", required = false) Integer booksPerPage) {

        if (page != null && booksPerPage != null && sortByYear)
            model.addAttribute("books", booksService.findWithPaginationAndSorting(page, booksPerPage));
        else if (page != null && booksPerPage != null)
            model.addAttribute("books", booksService.findWithPagination(page, booksPerPage));
        else if (sortByYear)
            model.addAttribute("books", booksService.getSortedByYear());
        else
            model.addAttribute("books", booksService.findAll());
        return "books/index";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id));

        Optional<Person> bookOwner = booksService.getBookOwner(id);

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }
    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        booksService.save(book);
        return "redirect:/books";
    }
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);
        return "redirect:/books";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        booksService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(value = "title", required = false) String title) {
        model.addAttribute("books", booksService.findByTitleStartingWith(title));
        return "books/search";
    }

}
