package forum.event;

import java.io.Serializable;
import java.util.Date;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import forum.model.Post;
import forum.model.PostLabelEnum;
import forum.repository.PostRepository;

@Role(Role.Type.EVENT)
@Expires("24h")
public class ReportPostEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private String userId;
    private String postId;
    
	public ReportPostEvent() {
		super();
	}

	public ReportPostEvent(String userId, String postId) {
		super();
		this.userId = userId;
		this.postId = postId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}		
	
	public boolean isJustified() {
		Post post = PostRepository.getInstance().getPostById(postId);
		return (post.getPostLabels().contains(PostLabelEnum.POOR_CONTENT) || post.getPostLabels().contains(PostLabelEnum.HARMFUL));
	}

}
