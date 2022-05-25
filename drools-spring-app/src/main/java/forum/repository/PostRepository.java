package forum.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import forum.KieSessionSingleton;
import forum.model.Post;
import forum.model.User;

public class PostRepository {
	
	private List<Post> posts = new ArrayList<>();	
	
	private PostRepository() {		
		SimpleDateFormat formatter =new SimpleDateFormat("dd/MM/yyyy");  
		
		Post p1 = new Post("Zack", "Piece of cake, I'll make first in no time!");
		p1.setLikes(23);
		p1.setDislikes(2);
		try {
			p1.setTimestamp(formatter.parse("01/03/2022"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		posts.add(p1);
		KieSessionSingleton.getInstance().insert(p1);

		Post p2 = new Post("Sephiroth", "And it's Hojo of Shinra that produced these monsters. Mutated living organisms produced by Mako energy. That's what these monsters really are.");
		p2.setLikes(67);
		p2.setDislikes(0);
		try {
			p2.setTimestamp(formatter.parse("12/01/2022"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		posts.add(p2);
		KieSessionSingleton.getInstance().insert(p2);
		
		Post p3 = new Post("Cloud", "Anyone may claim that he will act in the direst times, yet only a brave man acts in times great and small.");
		p3.setLikes(56);
		p3.setDislikes(13);
		try {
			p3.setTimestamp(formatter.parse("22/02/2022"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		posts.add(p3);
		KieSessionSingleton.getInstance().insert(p3);
		
		Post p4 = new Post("Angeal", "You're a little more important than my sword,... but just a little.");
		p4.setLikes(12);
		p4.setDislikes(2);
		try {
			p4.setTimestamp(formatter.parse("20/04/2022"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		posts.add(p4);
		KieSessionSingleton.getInstance().insert(p4);
		
		Post p5 = new Post("Genesis", "Infinite in mystery, is the gift of the goddess, we seek it thus, and take to the sky. Ripples form on the water's surface, the wandering soul knows no rest...");
		p5.setLikes(39);
		p5.setDislikes(8);
		try {
			p5.setTimestamp(formatter.parse("15/05/2022"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		posts.add(p5);
		KieSessionSingleton.getInstance().insert(p5);
		
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
			if (post.getPostId().toLowerCase().equals(id.toLowerCase())) {
				return post;
			}
		}
		
		return null;
	}
	
	public Post updatePost(Post post) {
		for (Post postFromList: posts) {
			if (postFromList.getPostId().toLowerCase().equals(post.getPostId().toLowerCase())) {
				int index = posts.indexOf(postFromList);
				posts.set(index, post);
				return post;
			}
		}
		
		return null;
	}
	
	public void create(Post post) {
		this.posts.add(post);
	}
	
	public List<Post> getTop10Percent() {
		List<Post> retVal = new ArrayList<>();
		List<Post> latestPosts = getPostsLast5Minutes();
		
		Integer totalSize = latestPosts.size();
		Integer retValSize = (int) (1.0 + (0.1 * totalSize));
		
		if (totalSize != 0) {
			Collections.sort(latestPosts, (p1, p2) -> p1.getLikes() - p2.getLikes());
			Collections.reverse(latestPosts);
			for (int i = 0; i <= retValSize; i++) {
				if (i < totalSize) {
					retVal.add(latestPosts.get(i));
				}
			}
		}
		
		return retVal;
	}
	
	public List<Post> getPostsLast5Minutes() {
		List<Post> retVal = new ArrayList<>();
		Date yesterday = new Date(System.currentTimeMillis() - (5 * 60 * 1000));
		
		for (Post p: posts) {
			if (p.getTimestamp().after(yesterday)) {
				retVal.add(p);
			};
		}
		
		return retVal;
	}

}
