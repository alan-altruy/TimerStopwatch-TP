package gui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import states.timer.AbstractTimer;

class TestHeadlessGUIActions extends TestGUIAbstract {

    @Test
    @DisplayName("Clicking left switches mode to stopwatch")
    void testClickLeftSwitchesToStopwatch() {
        g.updateUI(c);
        assertEquals("TIMER", g.myText2.getText());
        g.b1.doClick(); // should call c.left()
        g.updateUI(c);
        assertEquals("STOPWATCH", g.myText2.getText());
    }

    @Test
    @DisplayName("SetTimer: up (inc) increases memTimer by 5")
    void testSetTimerIncrementByClick() {
        c.right(); // go to SetTimer
        g.updateUI(c);
        assertEquals("memTimer = 0", g.myText1.getText());
        g.b2.doClick(); // should call c.up() => memTimer += 5
        g.updateUI(c);
        assertEquals("memTimer = 5", g.myText1.getText());
        assertEquals(5, AbstractTimer.getMemTimer());
    }

    @Test
    @DisplayName("SetTimer: left (reset) resets memTimer to 0")
    void testSetTimerResetByLeftClick() {
        c.right();
        g.updateUI(c);
        // increment
        g.b2.doClick();
        g.updateUI(c);
        assertEquals(5, AbstractTimer.getMemTimer());
        // reset via left
        g.b1.doClick();
        g.updateUI(c);
        assertEquals(0, AbstractTimer.getMemTimer());
        assertEquals("memTimer = 0", g.myText1.getText());
    }

}
