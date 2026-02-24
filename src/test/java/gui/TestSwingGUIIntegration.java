package gui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import states.Context;
import states.stopwatch.AbstractStopwatch;
import states.timer.AbstractTimer;

class TestSwingGUIIntegration extends TestGUIAbstract {

    @Test
    @DisplayName("SwingGUI stopwatch flow: start, tick, split, timeout return")
    void testStopwatchFlow() {
        // use SwingGUI in headless mode
        SwingGUI.setShowRealGUI(false);
        g = new SwingGUI(c);

        // switch to stopwatch mode
        g.b1.doClick(); // left
        g.updateUI(c);
        assertEquals("stopwatch", g.myText2.getText());

        // start stopwatch
        g.b2.doClick(); // up -> run
        g.updateUI(c);
        // tick 3 times
        c.tick(); g.updateUI(c);
        c.tick(); g.updateUI(c);
        c.tick(); g.updateUI(c);
        assertEquals("totalTime = 3", g.myText1.getText());

        // split (lap)
        g.b2.doClick(); // up -> split
        g.updateUI(c);
        assertEquals("lapTime = 3", g.myText1.getText());

        // while in lap mode, totalTime continues to increase but display shows lapTime
        c.tick(); // totalTime -> 4
        g.updateUI(c);
        assertEquals("lapTime = 3", g.myText1.getText());

        // after 5 ticks, it should return to running and display totalTime
        for (int i = 0; i < 5; i++) { c.tick(); }
        g.updateUI(c);
        assertTrue(g.myText1.getText().startsWith("totalTime = "));
    }

    @Test
    @DisplayName("SwingGUI timer flow: set, inc, run, tick to ringing")
    void testTimerFlow() {
        SwingGUI.setShowRealGUI(false);
        g = new SwingGUI(c);

        // ensure timer initial
        g.updateUI(c);
        assertEquals("timer", g.myText2.getText());

        // go to set timer
        g.b3.doClick(); // right
        g.updateUI(c);
        assertEquals("SetTimer", g.myText3.getText());

        // inc memTimer twice (each up adds 5)
        g.b2.doClick(); g.updateUI(c);
        g.b2.doClick(); g.updateUI(c);
        assertEquals("memTimer = 10", g.myText1.getText());
        assertEquals(10, AbstractTimer.getMemTimer());

        // done -> back to idle
        g.b3.doClick(); g.updateUI(c);
        assertEquals("IdleTimer", g.myText3.getText());

        // run the timer
        g.b2.doClick(); // up -> run (if memTimer > 0)
        g.updateUI(c);
        assertEquals("RunningTimer", g.myText3.getText());

        // tick 10 times -> should reach zero and transition to RingingTimer
        for (int i = 0; i < 10; i++) { c.tick(); }
        g.updateUI(c);
        assertEquals("RingingTimer", g.myText3.getText());
    }

}
