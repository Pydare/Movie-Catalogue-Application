package io.javabrains.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    //had to remove the restTemplate injection from the requestmapping method, to prevent re-initialization of the resttemplate during get requests
    //this is why we have a bean annotation for one time initialization (singletons)
    @Autowired
    private RestTemplate restTemplate;

    //web client builder sorta has the same purpose as the comment made aboove rest template
    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId){

        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/"+userId,UserRating.class);

        return ratings.getUserRating().stream().map(rating -> {
            //makes a call to the api and unmarshalls the object. ie, it converts the string into the object type
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
            

            //putting them all together
            return new CatalogItem(movie.getName(),"Test",rating.getRating()); })
            .collect(Collectors.toList());
        
    }
}

// using webclient instead of rest template as above
            //Movie movie = webClientBuilder.build().get().uri("http://localhost:8081/movies/"+rating.getMovieId()).retrieve().bodyToMono(Movie.class).block();
           