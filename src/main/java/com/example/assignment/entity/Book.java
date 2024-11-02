package com.example.assignment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    private String isbn;
    private String title;
    private String author;
    private Integer pageCount;
    private String coverUrl;

    @JsonBackReference
    @ManyToMany(mappedBy = "books", cascade = CascadeType.ALL)
    private List<ReadingList> readingLists;
}
