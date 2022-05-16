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

	public RulesResponse(List<String> firedRules) {
		super();
		if (firedRules.size() == 0) {
			this.message = null;
		} else if (firedRules.size() == 0) {
			this.message = "1 rule fired: " + firedRules.get(0);
		} else {
			this.message = firedRules.size() + " rules fired: ";
			for (int i =0; i<firedRules.size(); i++)  {
				if (i != 0) {
					this.message += ", " + firedRules.get(i);
				} else {
					this.message += firedRules.get(i);
				}
			}
		}
	}
	
}
