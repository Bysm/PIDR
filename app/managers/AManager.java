package app.managers;

public class AManager {
	
	private static AManager instance = null;
	protected AManager() {}
	
    public static synchronized AManager getInstance() {
    	if (instance == null) {
    		instance = new AManager();
        }
        return instance;
    }
    
}
