package forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import forum.model.Post;
import forum.service.PostService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
	
	@Autowired
	private PostService postService;
	
    @GetMapping()
    public List<Post> getAll() {
       return postService.getAllPosts();
    }    
	
    @GetMapping("/{id}")
    public Post getById(@PathVariable String id) {
       return postService.getClassifiedPost(null);
    }


}
