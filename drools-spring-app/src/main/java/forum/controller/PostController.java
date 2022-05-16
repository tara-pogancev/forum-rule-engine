package forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import forum.model.Post;
import forum.model.RulesResponse;
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
	
    @PutMapping("/like/{id}/{userId}")
    public Post likePost(@PathVariable String id, @PathVariable String userId) {
       return postService.getClassifiedPost(null);
    }
    
    @PutMapping("/dislike/{id}/{userId}")
    public RulesResponse dislikePost(@PathVariable String id, @PathVariable String userId) {
       return postService.dislikePost(id, userId);
    }

}
