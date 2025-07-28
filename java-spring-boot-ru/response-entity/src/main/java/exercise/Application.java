package exercise;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import exercise.model.Post;
import lombok.Setter;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    @Setter
    private static  List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts() {
        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPost(@PathVariable String id) {
        Optional<Post> optPost = posts.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (optPost.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optPost.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> savePost(@RequestBody Post post) {
        posts.add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable String id, @RequestBody Post post) {
       Optional<Post> optPost = posts.stream().filter(p -> p.getId().equals(post.getId())).findFirst();
       if(optPost.isPresent()) {
           Post updatePost = optPost.get();
           updatePost.setId(post.getId());
           updatePost.setTitle(post.getTitle());
           updatePost.setBody(post.getBody());
           return ResponseEntity.status(HttpStatus.OK).body(updatePost);
       }
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    // END

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
