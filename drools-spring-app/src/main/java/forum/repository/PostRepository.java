package forum.repository;

import java.util.ArrayList;
import java.util.List;

import forum.model.Post;
import forum.model.User;

public class PostRepository {
	
	private List<Post> posts = new ArrayList<>();	
	
	private PostRepository() {
		UserRepository userRepository = UserRepository.getInstance();
		
		for (User user: userRepository.getAllUsers()) {
			Post tempPost = new Post(user.username, "My name is "+ user.name +". This is my first post. It better be amazing!");
			posts.add(tempPost);
		}
		
	}
	
	private static PostRepository instance = new PostRepository();	
	
	public static PostRepository getInstance() {
		return instance;
	}
	
	public List<Post> getAllPosts() {
		return posts;
	}
	
	public Post getPostById(String id) {
		for (Post post: posts) {
			if (post.postId.toLowerCase().equals(id.toLowerCase())) {
				return post;
			}
		}
		
		return null;
	}
	
	public Post updatePost(Post post) {
		for (Post postFromList: posts) {
			if (postFromList.postId.toLowerCase().equals(post.postId.toLowerCase())) {
				int index = posts.indexOf(postFromList);
				posts.set(index, post);
				return post;
			}
		}
		
		return null;
	}

}
