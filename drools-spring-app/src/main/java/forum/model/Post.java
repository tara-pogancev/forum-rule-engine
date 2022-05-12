package forum.model;

import java.util.List;

import java.util.ArrayList;

import org.joda.time.DateTime;

import lombok.Data;

@Data
public class Post {
	
	public String postId;
	public String postOwnerId;
	public String postContent;
	public DateTime timestamp;
	public Integer likes = 0;
	public Integer dislikes = 0;
	public Integer reports = 0;
	public List<PostLabelEnum> postLabels = new ArrayList<>();
	
	public static String generatePostId(String username) {
		DateTime currentTime = new DateTime();
		return username.toLowerCase() + currentTime.toString("yyyyDDmmhhMMss");
	}
	
	public static String generatePostId(String username, DateTime timestamp) {
		return username.toLowerCase() + timestamp.toString("yyyyDDmmhhMMss");
	}
	
	public void likePost() {
		this.likes++; 
	}

	
	public void dislikePost() {
		this.dislikes++; 
	}
	
	public void reportPost() {
		this.reports++; 
	}

	public Post(String postOwnerId, String postContent, DateTime timestamp) {
		super();
		this.postOwnerId = postOwnerId;
		this.postContent = postContent;
		this.timestamp = timestamp;
		
		this.postId = generatePostId(postOwnerId, timestamp);
	}

	public Post(String postOwnerId, String postContent) {
		super();
		this.postOwnerId = postOwnerId;
		this.postContent = postContent;

		this.postId = generatePostId(postOwnerId);
		this.timestamp = new DateTime();
	}
	
	
	
	
	
	
}
