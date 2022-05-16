package forum.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import lombok.Data;

@Data
public class RulesResponse {
	
	public String message;
	public DateTime timestamp = new DateTime();
	public List<EntityTypeEnum> refresh = new ArrayList<EntityTypeEnum>();
	
	public RulesResponse(String message) {
		super();
		this.message = message;
	}
	
}
