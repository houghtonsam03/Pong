import java.io.FileInputStream;
import java.util.*;

public class Settings {
    protected Map<String,Object> properties = new HashMap<>();

    public Settings(String filename) {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(filename));

            for (String key : props.stringPropertyNames()) {
                String value = props.getProperty(key);
                
                if (value.matches("-?\\d+")) properties.put(key,Integer.parseInt(value)); // Integer
                else if (value.matches("-?\\d+\\.\\d+")) properties.put(key, Double.parseDouble(value)); // Double
                else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) properties.put(key, Boolean.parseBoolean(value)); // Boolean
                else properties.put(key,value); // String
            }
        }
        catch (Exception e ) {
            System.out.println("Could not load settings from " + filename);
            e.printStackTrace();
        }
    }
    public Object getProperty(String key) {
        return properties.get(key);
    }
    public Set<String> getPropertyKeys() {
        return properties.keySet();
    }
}
