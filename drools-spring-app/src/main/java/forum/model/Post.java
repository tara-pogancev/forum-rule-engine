package forum.model;

import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.kie.api.definition.type.Modifies;

import forum.repository.PostRepository;
import lombok.Data;

@Data
public class Post {
	
	private String postId;
	private String postOwnerId;
	private String postContent;
	private Date timestamp;
	private Integer likes = 0;
	private Integer dislikes = 0;
	private Integer reports = 0;
	private List<PostLabelEnum> postLabels = new ArrayList<>();
	
	public static String generatePostId(String username) {
		Date currentTime = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyDDmmhhMMss");  
		String strDate = dateFormat.format(currentTime); 
		return username.toLowerCase() + strDate;
	}
	
	public static String generatePostId(String username, Date timestamp) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyDDmmhhMMss");  
		String strDate = dateFormat.format(timestamp); 
		return username.toLowerCase() + strDate;
	}
	
	@Modifies({ "likes" })
	public void likePost() {
		this.likes++; 
	}

	@Modifies({ "dislikes" })
	public void dislikePost() {
		this.dislikes++; 
	}
	
	@Modifies({ "reports" })
	public void reportPost() {
		this.reports++; 
	}

	public Post(String postOwnerId, String postContent, Date timestamp) {
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
		this.timestamp = new Date();
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

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
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
	
	@Modifies({ "postLabels" })
	public void removeLabel(PostLabelEnum label) {
		this.postLabels.remove(label);
	}
	
	@Modifies({ "postLabels" })
	public void addLabel(PostLabelEnum label) {
		if (!this.postLabels.contains(label)) {
			this.postLabels.add(label);
		}
	}	
	
	public void update() {
		PostRepository.getInstance().updatePost(this);
	}
	
	public boolean isPoorContent() {
		return (((likes*1.0)/(dislikes*1.0)) < (3.0/2.0));
	}	

	@Modifies({ "postLabels" })
	public void doHarmfulPostAnalysis() {
		String contentSample = postContent.toLowerCase();
		if (contentSample.contains("poor") || 
				contentSample.contains("harm") ||
				contentSample.contains("bad")) {
			addLabel(PostLabelEnum.HARMFUL);			
		}
	}
		
}
