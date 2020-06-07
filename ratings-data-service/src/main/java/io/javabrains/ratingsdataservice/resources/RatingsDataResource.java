package io.javabrains.ratingsdataservice.resources;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.ratingsdataservice.models.Rating;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsDataResource {

    @RequestMapping("/{movieId}")
    public Rating getRating(@PathVariable String movieId){
        return new Rating(movieId, 4);
    }

}