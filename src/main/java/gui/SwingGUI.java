package gui;

import states.Context;
import states.EventListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Container;
import java.awt.GridLayout;

/**
 * @author tommens
 * This class extends the HeadlessGUI to draw a real GUI on the screen
 */
public class SwingGUI extends HeadlessGUI {
    // When running tests in headless CI we want to avoid creating a real JFrame.
    // Toggle this flag in tests using `SwingGUI.setShowRealGUI(false)`.
    private static boolean SHOW_REAL_GUI = true;

    public SwingGUI(EventListener o) { super(o); }

    public static void setShowRealGUI(boolean show) { SHOW_REAL_GUI = show; }
    
    protected void initGUI() {
        super.initGUI();
        if (!SHOW_REAL_GUI) {
            // Build the UI on a temporary panel so tests exercise the layout code
            buildUI(new JPanel());
            return;
        }

        JFrame myFrame = new JFrame("Chronometer");
        Container myContent = myFrame.getContentPane();
        buildUI(myContent);
        myFrame.pack();
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
    }
    
    /**
     * Extracted UI construction so tests can exercise layout code
     * by passing a dummy Container (e.g., JPanel) without showing a frame.
     */
    protected void buildUI(Container myContent) {
        // grid layout with 2 rows and 3 columns
        myContent.setLayout(new GridLayout(2,3,1,1));
        // filling first row of grid (3 columns) with text information
        myContent.add(myText1,0);
        myContent.add(myText2,1);
        myContent.add(myText3,2);
        // filling second row of grid (3 columns) with buttons
        myContent.add(b1);
        myContent.add(b2);
        myContent.add(b3);
    }
       
}
