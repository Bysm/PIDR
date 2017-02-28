package pidr.app.managers;

import java.util.Observable;

public class AManager extends Observable {
	
	private static AManager instance = null;
	private int progress = 0;
	
	protected AManager() {}
	
    public static synchronized AManager getInstance() {
    	if (instance == null) {
    		instance = new AManager();
        }
        return instance;
    }
    
    public int getProgress() {
		return progress;
	}
    
    public void setProgress(int progress) {
		this.progress = progress;
	}
    
}
