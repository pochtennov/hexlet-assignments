package exercise.controller;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO show(@PathVariable Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));

        var comments = commentRepository.findByPostId(id);

        return toPostDTO(post, comments);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    // Пользователь автоматически преобразуется в JSON
    public List<PostDTO> showAll() {
        var posts = postRepository.findAll();

        return posts.stream().map(post -> {
            var comments = commentRepository.findByPostId(post.getId());
            return toPostDTO(post, comments);
        }).toList();
    }


    private PostDTO toPostDTO(Post post, List<Comment> comments) {
        var dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        dto.setComments(comments.stream()
                .map(this::toCommentDto)
                .toList()
        );
        return dto;
    }
    private CommentDTO toCommentDto(Comment comment) {
        var dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setBody(comment.getBody());
        return dto;
    }
}