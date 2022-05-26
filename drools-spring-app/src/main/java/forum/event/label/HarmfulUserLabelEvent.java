package forum.event.label;

import java.io.Serializable;
import java.util.Date;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

@Role(Role.Type.EVENT)
public class HarmfulUserLabelEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private String userId;
    
	public HarmfulUserLabelEvent() {
		super();
	}

	public HarmfulUserLabelEvent(String userId) {
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


