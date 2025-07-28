package exercise.controller;

import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RequestMapping("/posts")
@RestController
public class PostsController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findById(@PathVariable Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post with id " + id +" not found"));
        return ResponseEntity.of(postRepository.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Post post) {
        postRepository.save(post);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable Long id, @RequestBody Post post) {
        Post updatePost = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        updatePost.setBody(post.getBody());
        updatePost.setTitle(post.getTitle());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        postRepository.deleteById(id);
        commentRepository.deleteByPostId(id);
    }

}
// END
