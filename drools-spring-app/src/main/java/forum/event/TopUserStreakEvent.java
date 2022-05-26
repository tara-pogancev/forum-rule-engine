package forum.event;

import java.io.Serializable;
import java.util.Date;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

@Role(Role.Type.EVENT)
public class TopUserStreakEvent implements Serializable {	

	private static final long serialVersionUID = 1L;
    private String userId;
    
	public TopUserStreakEvent() {
		super();
	}

	public TopUserStreakEvent(String userId) {
		super();
		this.userId = userId;
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
