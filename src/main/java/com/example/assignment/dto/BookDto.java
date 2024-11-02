package com.example.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private String isbn;
    private String title;
    private String author;
    private Integer pageCount;
    private String coverUrl;
}
