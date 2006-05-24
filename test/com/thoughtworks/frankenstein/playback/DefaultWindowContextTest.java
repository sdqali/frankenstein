package com.thoughtworks.frankenstein.playback;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

/**
 * Ensures behaviour of the window context.
 */
public class DefaultWindowContextTest extends TestCase {
    private DefaultWindowContext windowContext;
    private KeyboardFocusManager defaultKeyboardFocusManager;

    protected void setUp() throws Exception {
        super.setUp();
        windowContext = new DefaultWindowContext();
        defaultKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        KeyboardFocusManager.setCurrentKeyboardFocusManager(defaultKeyboardFocusManager);
    }

    public void testContextIsInternalFrameWhenInternalFrameIsShown() {
        JInternalFrame internalFrame = new JInternalFrame();
        JButton comp = new JButton();
        internalFrame.getContentPane().add(comp);
        windowContext.setActiveWindow(comp);
        assertSame(internalFrame, windowContext.activeWindow());
    }

    public void testContextIsJFrameWhenJFrameIsShown() {
        JFrame frame = new JFrame();
        JButton comp = new JButton();
        frame.getContentPane().add(comp);
        windowContext.setActiveWindow(comp);
        assertSame(frame, windowContext.activeWindow());
    }

    public void testContextIsJDialogWhenJDialogIsShown() {
        JDialog dialog = new JDialog();
        JButton comp = new JButton();
        dialog.getContentPane().add(comp);
        windowContext.setActiveWindow(comp);
        assertSame(dialog, windowContext.activeWindow());
    }

    public void testSetsActiveWindowWhenFocusChanges() {
        JFrame frame = new JFrame();
        KeyboardFocusManager.setCurrentKeyboardFocusManager(new TestKeyboardFocusManager(frame));
        windowContext.propertyChange(new PropertyChangeEvent(this, "focusOwner", null, frame));
        assertSame(frame, windowContext.activeWindow());
    }

    public void testSetsActiveWindowToNullIfThereIsNoRootPaneContainer() {
        JButton button = new JButton();
        KeyboardFocusManager.setCurrentKeyboardFocusManager(new TestKeyboardFocusManager(button));
        windowContext.propertyChange(new PropertyChangeEvent(this, "focusOwner", null, button));
        assertNull(windowContext.activeWindow());
    }

    public void testWaitForDialogWithActiveWindowAsTargetDialog() throws InterruptedException {
        JFrame frame = new JFrame();
        JDialog testDialog = new JDialog(frame, "testDialog");
        KeyboardFocusManager.setCurrentKeyboardFocusManager(new TestKeyboardFocusManager(testDialog));
        windowContext.propertyChange(new PropertyChangeEvent(this, "focusOwner", null, testDialog));
        windowContext.waitForDialog("testDialog", 10);
        assertEquals(testDialog, windowContext.activeWindow());
        testDialog.dispose();
        frame.dispose();
    }

    public void testWaitForActiveDialogWhenDialogIsNotReady() throws InterruptedException {
        final JDialog testDialog = new JDialog(new JFrame(), "testDialog");
        KeyboardFocusManager.setCurrentKeyboardFocusManager(new TestKeyboardFocusManager(testDialog));
        new Thread(new Runnable() {
            public void run() {
                windowContext.propertyChange(new PropertyChangeEvent(this, "focusOwner", null, testDialog));
            }
        }).start();
        windowContext.waitForDialog("testDialog", 10);
        assertEquals(testDialog, windowContext.activeWindow());
        testDialog.dispose();
    }

    private class TestKeyboardFocusManager extends DefaultKeyboardFocusManager {
        private Component focusOwner;

        public TestKeyboardFocusManager(Component focusOwner) {
            this.focusOwner = focusOwner;
        }

        public Component getFocusOwner() {
            return focusOwner;
        }

    }
}
