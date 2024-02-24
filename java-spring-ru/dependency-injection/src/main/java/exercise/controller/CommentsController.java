package exercise.controller;

import exercise.exception.ResourceAlreadyExistsException;
import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    private final CommentRepository commentRepository;

    public CommentsController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping(path = "")
    public List<Comment> index() {
        return commentRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Comment show(@PathVariable long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));
    }

    @PostMapping(path = "")
    public ResponseEntity<Comment> create(@RequestBody Comment commentData) {
        Example<Comment> example = Example.of(commentData);
        var existingComment = commentRepository.findOne(example);
        if (existingComment.isPresent()) {
            throw new ResourceAlreadyExistsException("Comment already exists");
        } else {
            commentRepository.save(commentData);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(commentData);
    }

    @PutMapping(path = "/{id}")
    public Comment update(@PathVariable long id, @RequestBody Comment commentData) {
        var comment =  commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));

        comment.setPostId(commentData.getPostId());
        comment.setBody(commentData.getBody());

        commentRepository.save(comment);

        return comment;
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {

        commentRepository.deleteById(id);
    }
}
