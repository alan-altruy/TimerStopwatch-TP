package states.timer;

import states.ClockState;

public abstract class ActiveTimer extends AbstractTimer {

	//This is a composite state, so we need to defer its
	//creation to its initial substate RunningTimer
	
	// use Singleton design pattern
    private static ActiveTimer instance = null;
    public static ActiveTimer instance() {
        if (instance == null) instance = RunningTimer.instance();
        return instance;
    }

    // the composite state has one outgoing transition
    // which will be inherited by all its substates
    @Override
    public ClockState right() {
    	return transition(IdleTimer.instance());
    }
    public String getRightText() { return "reset"; }

}