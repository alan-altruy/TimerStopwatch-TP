package gui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import states.timer.AbstractTimer;

class TestSwingGUIActions extends TestGUIAbstract {

    @Test
    @DisplayName("SwingGUI: clicking b2 in SetTimer increases memTimer by 5")
    void testSwingSetTimerInc() {
        // disable showing the real JFrame so the test can run in CI/headless
        SwingGUI.setShowRealGUI(false);
        g = new SwingGUI(c);
        c.right();
        g.updateUI(c);
        g.b2.doClick();
        g.updateUI(c);
        assertEquals(5, AbstractTimer.getMemTimer());
    }

}
