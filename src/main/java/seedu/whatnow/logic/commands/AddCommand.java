package seedu.whatnow.logic.commands;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.whatnow.commons.exceptions.IllegalValueException;
import seedu.whatnow.model.tag.Tag;
import seedu.whatnow.model.tag.UniqueTagList;
import seedu.whatnow.model.task.*;
import seedu.whatnow.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.whatnow.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Adds a task to WhatNow.
 */
public class AddCommand extends UndoAndRedo {

	public static final String COMMAND_WORD = "add";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to WhatNow. "
			+ "Parameters: TASK_NAME [t/TAG]...\n"
			+ "Example: " + COMMAND_WORD
			+ " Buy groceries 18 January t/highPriority\n"
			+ " Buy dinner 18/10/2016 t/lowPriority\n";

	public static final String MESSAGE_SUCCESS = "New task added: %1$s";
	public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in WhatNow";
	private static final String STATUS_INCOMPLETE = "incomplete";

	private final Task toAdd;
	
	public AddCommand(String name, String date, String startDate, String endDate, String time, String startTime, String endTime, Set<String> tags) throws IllegalValueException, ParseException {
	    TaskTime validateTime = null;
	    TaskDate validateDate = null;
	    
	    if (time != null || startTime != null || endTime != null) {
	        validateTime = new TaskTime(time, startTime, endTime, date, startDate, endDate);
	        if (date == null && startDate == null && endDate == null)
	            date = validateTime.getDate();
	    }
	    
	    if (date != null || startDate != null || endDate != null) {
	        validateDate = new TaskDate(date, startDate, endDate);
	    }
	    
	    final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }    
        
        this.toAdd = new Task(new Name(name), date, startDate, endDate, time, startTime, endTime, new UniqueTagList(tagSet), STATUS_INCOMPLETE, null);
	}

	@Override
	public CommandResult execute() {
		assert model != null;
		try {
			model.addTask(toAdd);
			model.getUndoStack().push(this);
			return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
		} catch (UniqueTaskList.DuplicateTaskException e) {
			return new CommandResult(MESSAGE_DUPLICATE_TASK);
		}
	}

	@Override
	public CommandResult undo() {
		assert model != null;
		try {
			model.deleteTask(toAdd);
			return new CommandResult(String.format(UndoCommand.MESSAGE_SUCCESS));
		} catch (TaskNotFoundException pnfe) {
			return new CommandResult(String.format(UndoCommand.MESSAGE_FAIL));
		} 
	}

	@Override
	public CommandResult redo() {
		assert model != null;
		try {
			model.addTask(toAdd);
			return new CommandResult(String.format(RedoCommand.MESSAGE_SUCCESS));
		} catch (DuplicateTaskException e) {
			return new CommandResult(String.format(RedoCommand.MESSAGE_FAIL));
		}
	}
}
