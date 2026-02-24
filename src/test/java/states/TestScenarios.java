package states;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import states.stopwatch.*;
import states.timer.*;

class TestScenarios {

	Context c;
	
    @BeforeEach
	void setup() {
    	c = new Context();
     	//before each test, reset the timer values to avoid interference between tests:
    	AbstractTimer.resetInitialValues();
    	AbstractStopwatch.resetInitialValues();
    }

	// The original full integration scenario is kept but split below into
	// smaller BDD-style scenarios to make behavior clearer and testable.

	@Test
	@DisplayName("Scenario: Setting timer memory and starting it - Given Idle timer, When user sets memory then starts, Then timer runs")
	void bdd_setAndStartTimer() {
		// Given
		assertEquals(IdleTimer.Instance(), c.currentState);
		assertEquals(0, AbstractTimer.getMemTimer());

		// When: set memTimer to 2 (press right and tick twice while in Set state)
		c.right(); // enter SetTimer mode
		c.tick();
		assertSame(SetTimer.Instance(), c.currentState);
		c.tick();
		assertEquals(2, AbstractTimer.getMemTimer());

		// stop adjusting and start the timer
		c.right(); // stop incrementing memTimer
		c.tick();
		c.up(); // start running the timer (moves mem -> timer)

		// Then: timer was initialized from memTimer and decreases on tick
		assertEquals(2, AbstractTimer.getTimer(), "timer initialized from memTimer");
		c.tick();
		assertEquals(1, AbstractTimer.getTimer(), "timer decremented after tick");
	}

	@Test
	@DisplayName("Scenario: Stopwatch lap recording - Given Reset stopwatch, When user starts and records a lap, Then total and lap times update")
	void bdd_stopwatchLapRecording() {
		// Given: switch to stopwatch mode
		assertEquals(IdleTimer.Instance(), c.currentState);
		c.left(); // go to stopwatch mode
		c.tick();
		assertSame(ResetStopwatch.Instance(), c.currentState);
		assertEquals(0, AbstractStopwatch.getTotalTime());
		assertEquals(0, AbstractStopwatch.getLapTime());

		// When: start stopwatch and then record a lap
		c.up(); // start running
		c.tick();
		assertSame(RunningStopwatch.Instance(), c.currentState);
		assertEquals(1, AbstractStopwatch.getTotalTime());
		c.up(); // record lap
		c.tick();

		// Then: total time advanced, lap time recorded
		assertSame(LaptimeStopwatch.Instance(), c.currentState);
		assertEquals(2, AbstractStopwatch.getTotalTime(), "total time after lap");
		assertEquals(1, AbstractStopwatch.getLapTime(), "lap time recorded");
	}

	@Test
	@DisplayName("Scenario: Mode switching preserves timer state and leads to ringing - Given a paused timer, When user leaves then returns and resumes, Then timer rings at 0")
	void bdd_modeSwitchPreservesAndRings() {
		// Given: prepare a timer with memTimer=2 and run for one tick then pause
		c.right(); c.tick(); c.tick(); // set memTimer to 2
		c.right(); c.tick(); // stop adjusting
		c.up(); // start -> timer=2
		c.tick(); // timer -> 1
		c.up(); // pause -> PausedTimer
		c.tick();
		assertSame(PausedTimer.Instance(), c.currentState);
		assertEquals(1, AbstractTimer.getTimer());

		// When: switch to stopwatch and back (history should remember paused timer), then resume
		c.left(); c.tick(); // go to stopwatch
		c.up(); c.tick(); // start stopwatch briefly
		c.left(); c.tick(); // back to timer
		assertSame(PausedTimer.Instance(), c.currentState);

		c.up(); // resume timer -> RunningTimer
		assertSame(RunningTimer.Instance(), c.currentState);
		c.tick(); // this tick should bring timer to 0 and cause ringing

		// Then: ringing state reached and timer is 0
		assertSame(RingingTimer.Instance(), c.currentState);
		assertEquals(0, AbstractTimer.getTimer());
	}

	// Keep the original comprehensive integration test for completeness
	@Test
	@DisplayName("Integration: Complete scenario of events and transitions")
	void completeScenario() {
		assertEquals(IdleTimer.Instance(),c.currentState);
		assertEquals(0,AbstractTimer.getMemTimer());

		c.right(); // start incrementing the memTimer variable
		c.tick();
		assertSame(SetTimer.Instance(),c.currentState);
		assertEquals(1,AbstractTimer.getMemTimer());
		assertEquals(0,AbstractTimer.getTimer());

		c.tick();
		assertEquals(2,AbstractTimer.getMemTimer());
		assertEquals(0,AbstractTimer.getTimer());

		c.right(); // stop incrementing the memTimer variable
		c.tick();
		assertEquals(2,AbstractTimer.getMemTimer());
		assertEquals(0,AbstractTimer.getTimer());

		c.up(); // start running the timer
		assertEquals(2, AbstractTimer.getTimer(),"value of timer ");
		c.tick();
		assertEquals(2, AbstractTimer.getMemTimer(),"value of memTimer ");
		assertEquals(1, AbstractTimer.getTimer(),"value of timer ");

		c.up(); // pause the timer
		c.tick();
		assertSame(PausedTimer.Instance(), c.currentState);
		assertEquals(2, AbstractTimer.getMemTimer(),"value of memTimer ");
		assertEquals(1, AbstractTimer.getTimer(),"value of timer ");

		c.left(); // go to stopwatch mode
		c.tick();
		assertSame(ResetStopwatch.Instance(), c.currentState);
		assertEquals(0, AbstractStopwatch.getTotalTime(),"value of totalTime ");
		assertEquals(0, AbstractStopwatch.getLapTime(),"value of lapTime ");

		c.up(); //start running the stopwatch
		c.tick();
		assertSame(RunningStopwatch.Instance(), c.currentState);
		assertEquals(1, AbstractStopwatch.getTotalTime(),"value of totalTime ");
		assertEquals(0, AbstractStopwatch.getLapTime(),"value of lapTime ");

		c.up(); // record stopwatch laptime
		c.tick();
		assertSame(LaptimeStopwatch.Instance(), c.currentState);
		assertEquals(2, AbstractStopwatch.getTotalTime(),"value of totalTime ");
		assertEquals(1, AbstractStopwatch.getLapTime(),"value of lapTime ");

		c.left(); // go back to timer mode (remembering history state)
		c.tick();
		assertSame(PausedTimer.Instance(), c.currentState);
		assertEquals(2, AbstractTimer.getMemTimer(),"value of memTimer ");
		assertEquals(1, AbstractTimer.getTimer(),"value of timer ");

		c.up(); // continue running timer
		assertSame(RunningTimer.Instance(), c.currentState);
		c.tick();
		//automatic switch to ringing timer since timer has reached 0...
		assertSame(RingingTimer.Instance(), c.currentState);
		assertEquals(2, AbstractTimer.getMemTimer(),"value of memTimer ");
		assertEquals(0, AbstractTimer.getTimer(),"value of timer ");

		c.right(); // return to idle timer state
		c.tick();
		assertSame(IdleTimer.Instance(), c.currentState);
		assertEquals(2, AbstractTimer.getMemTimer(),"value of memTimer ");
		assertEquals(0, AbstractTimer.getTimer(),"value of timer ");
	}

}
