package forum.event.label;

import java.io.Serializable;
import java.util.Date;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

@Role(Role.Type.EVENT)
@Timestamp("executionTime")
public class HarmfulPostLabelEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private Date executionTime;
    private String postId;
    
	public HarmfulPostLabelEvent() {
		super();
	}

	public HarmfulPostLabelEvent(String postId) {
		super();
		this.postId = postId;
		this.executionTime = new Date();
	}    

	public Date getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Date executionTime) {
		this.executionTime = executionTime;
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
