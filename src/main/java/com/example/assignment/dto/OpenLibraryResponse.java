package com.example.assignment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OpenLibraryResponse {
    @JsonProperty("title")
    private String title;
    @JsonProperty("number_of_pages")
    private Integer numberOfPages;
    @JsonProperty("authors")
    private List<Authors> authors;
    @JsonProperty("covers")
    private List<String> covers;
}