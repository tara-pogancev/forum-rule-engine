package forum.event.label;

import java.io.Serializable;
import java.util.Date;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

@Role(Role.Type.EVENT)
@Timestamp("executionTime")
public class SuspendedUserLabelEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private Date executionTime;
    private String userId;
    
	public SuspendedUserLabelEvent() {
		super();
	}

	public SuspendedUserLabelEvent(String userId) {
		super();
		this.userId = userId;
		this.executionTime = new Date();
	}    

	public Date getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Date executionTime) {
		this.executionTime = executionTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}		

}


