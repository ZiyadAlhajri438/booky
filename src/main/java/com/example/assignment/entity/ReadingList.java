package com.example.assignment.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "reading_lists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingList {
    @Id
    private String name;

    @ManyToMany
    @JoinTable(
            name = "reading_list_books",
            joinColumns = @JoinColumn(name = "reading_list_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @JsonManagedReference
    private List<Book> books;
}