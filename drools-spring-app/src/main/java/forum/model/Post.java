package forum.model;

import java.util.List;

import java.util.ArrayList;

import org.joda.time.DateTime;

import lombok.Data;

@Data
public class Post {
	
	private String postId;
	private String postOwnerId;
	private String postContent;
	private DateTime timestamp;
	private Integer likes = 0;
	private Integer dislikes = 0;
	private Integer reports = 0;
	private List<PostLabelEnum> postLabels = new ArrayList<>();
	
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

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getPostOwnerId() {
		return postOwnerId;
	}

	public void setPostOwnerId(String postOwnerId) {
		this.postOwnerId = postOwnerId;
	}

	public String getPostContent() {
		return postContent;
	}

	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}

	public Integer getReports() {
		return reports;
	}

	public void setReports(Integer reports) {
		this.reports = reports;
	}

	public List<PostLabelEnum> getPostLabels() {
		return postLabels;
	}

	public void setPostLabels(List<PostLabelEnum> postLabels) {
		this.postLabels = postLabels;
	}
	
	public void removeLabel(PostLabelEnum label) {
		this.postLabels.remove(label);
	}
	
	public void addLabel(PostLabelEnum label) {
		if (!this.postLabels.contains(label)) {
			this.postLabels.add(label);
		}
	}
	
	
}
