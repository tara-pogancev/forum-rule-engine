package forum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import forum.model.Post;
import forum.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
	
	private PostRepository postRepository = PostRepository.getInstance();
	
    @GetMapping()
    public List<Post> getAll() {
       return postRepository.getAllPosts();
    }

}
