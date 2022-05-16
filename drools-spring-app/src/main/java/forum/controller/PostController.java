package forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import forum.controller.dto.PostDto;
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
    
    @GetMapping("/{postId}")
    public Post getById(@PathVariable String postId) {
       return postService.getById(postId);
    }    
		
    @PutMapping("/like/{userId}/{postId}")
    public RulesResponse likePost(@PathVariable String postId, @PathVariable String userId) {
       return postService.likePost(userId, postId);
    }
    
    @PutMapping("/dislike/{userId}/{postId}")
    public RulesResponse dislikePost(@PathVariable String postId, @PathVariable String userId) {
       return postService.dislikePost(userId, postId);
    }
    
    @PutMapping("/report/{userId}/{postId}")
    public RulesResponse reportPost(@PathVariable String postId, @PathVariable String userId) {
       return postService.reportPost(userId, postId);
    }
    
    @PostMapping()
    public RulesResponse createPost(@RequestBody PostDto newPost) {
       return postService.create(newPost);
    }

}
