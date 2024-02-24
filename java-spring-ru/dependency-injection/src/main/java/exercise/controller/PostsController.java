package exercise.controller;

import exercise.exception.ResourceAlreadyExistsException;
import exercise.model.Post;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import java.util.List;

import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/posts")
public class PostsController {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    public PostsController(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping(path = "")
    public List<Post> index() {
        return postRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Post show(@PathVariable long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
    }

    @PostMapping(path = "")
    public ResponseEntity<Post> create(@RequestBody Post postData) {
        Example<Post> example = Example.of(postData);
        var existingPost = postRepository.findOne(example);
        if (existingPost.isPresent()) {
            throw new ResourceAlreadyExistsException("Post already exists");
        } else {
            postRepository.save(postData);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(postData);
    }

    @PutMapping(path = "/{id}")
    public Post update(@PathVariable long id, @RequestBody Post postData) {
        var post =  postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));

        post.setTitle(postData.getTitle());
        post.setBody(postData.getBody());

        postRepository.save(post);

        return post;
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {

        postRepository.deleteById(id);
        commentRepository.deleteByPostId(id);
    }
}