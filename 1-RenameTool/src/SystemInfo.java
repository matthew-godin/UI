import java.util.ArrayList;
import java.util.HashMap;

class SystemInfo {
	ArrayList<String> keys = new ArrayList<>();
    HashMap<String, String> settings = new HashMap<>();

	SystemInfo() {
		keys.add("java.vendor"); 
		keys.add("java.version"); 
		keys.add("os.name"); 
		keys.add("os.arch"); 
		keys.add("os.version");
				
		for (String key : keys) {
			settings.put(key, System.getProperty(key));
		}
	}

	public String toString() {
        StringBuilder s = new StringBuilder();
		settings.forEach((key, value) -> s.append(key).append(": ").append(value).append("\n"));
        return s.toString();
    }
}