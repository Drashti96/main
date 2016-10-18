package guitests;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ClearCommandTest extends WhatNowGuiTest {

    @Test
    public void clear() {

        //verify a non-empty list can be cleared
        System.out.println("task length: " + td.getTypicalTasks().length);
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));
        assertClearCommandSuccess();

        //verify other commands can work after a clear command
        commandBox.runCommand(td.h.getAddCommand());
        assertTrue(taskListPanel.isListMatching(td.h));
        commandBox.runCommand("delete 1");
        assertListSize(0);

        //verify clear command works when the list is empty
        assertClearCommandSuccess();
    }

    private void assertClearCommandSuccess() {
        commandBox.runCommand("clear");
        assertListSize(0);
        assertResultMessage("WhatNow has been cleared!");
    }
}
