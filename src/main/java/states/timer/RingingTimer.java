package states.timer;

import states.ClockState;

public class RingingTimer extends ActiveTimer {

    private boolean isRinging = true; // flag to indicate if the timer is still ringing
 	
	// use Singleton design pattern
	private RingingTimer() {}; // make constructor invisible to clients
    private static RingingTimer instance = null;
    public static RingingTimer instance() {
        if(instance == null) instance = new RingingTimer();        
        return instance;
    }
    
    @Override
    public ClockState doIt() {
    	java.awt.Toolkit.getDefaultToolkit().beep();
    	return this;
    }

    @Override
    public void entry() {
        this.isRinging = true; // reset the ringing flag when entering the state
    }

    @Override
    public void exit() {
        this.isRinging = false; // stop ringing when exiting the state
    }

    public String getDisplayString() {
    	// display decreasing values starting from memTimer counting down to 0
        return "Time's up !";
    }
    
}
