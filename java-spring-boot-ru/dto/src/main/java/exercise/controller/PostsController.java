package exercise.controller;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class PostsController {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostDTO> getPosts() {
        var post = postRepository.findAll();
        return post.stream().map(this::toPostDto).toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO getPost(@PathVariable long id) {
        var post = postRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Post with id " + id + " not found"));
        return toPostDto(post);
    }

    private CommentDTO toCommentDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setBody(comment.getBody());
        commentDTO.setId(comment.getId());
        return commentDTO;
    }

    private PostDTO toPostDto(Post post) {
        List<Comment> comment = commentRepository.findByPostId(post.getId());
        List<CommentDTO> commentDto = comment.stream().map(this::toCommentDto).toList();
        PostDTO postDto = new PostDTO();
        postDto.setBody(post.getBody());
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setComments(commentDto);
        return postDto;
    }
}
// END
