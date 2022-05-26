package forum.event.label;

import java.io.Serializable;
import java.util.Date;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

@Role(Role.Type.EVENT)
public class PotentiallyHarmfulPostLabelEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private String postId;
    
	public PotentiallyHarmfulPostLabelEvent() {
		super();
	}

	public PotentiallyHarmfulPostLabelEvent(String postId) {
		super();
		this.postId = postId;
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

}
