package seedu.whatnow.model.task;

import java.util.Objects;

import seedu.whatnow.commons.util.CollectionUtil;
import seedu.whatnow.model.tag.UniqueTagList;

/**
 * Represents a Task in WhatNow.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {

    private Name name;
    private TaskDate taskDate;
    private TaskDate startDate;
    private TaskDate endDate;
    private String taskTime;
    private String startTime;
    private String endTime;
    private UniqueTagList tags;
    private String status;
    private String taskType;
    
    private static final String FLOATING = "floating";
    private static final String NOT_FLOATING = "not_floating";
    
    /**
     * Every field must be present and not null.
     */
    public Task(Name name, TaskDate taskDate, TaskDate startDate, TaskDate endDate, String taskTime, String startTime, String endTime, UniqueTagList tags, String status, String taskType) {
        assert !CollectionUtil.isAnyNull(name, tags);
        this.name = name;
        this.tags = new UniqueTagList(tags);
        this.status = status;
        this.taskType = FLOATING;
             
        if (taskDate != null && taskDate.getDate() != null) {
            this.taskDate = taskDate;
            this.taskType = NOT_FLOATING;
        }
        
        if (startDate != null && startDate.getDate() != null) {
            this.startDate = startDate;
            this.taskType = NOT_FLOATING;
        }
            
        if (endDate != null && endDate.getDate() != null) {
            this.endDate = endDate;
            this.taskType = NOT_FLOATING;
        }
            
        if (taskTime != null) {
            this.taskTime = taskTime;
            this.taskType = NOT_FLOATING;
        }
            
        if (startTime != null) {
            this.startTime = startTime;
            this.taskType = NOT_FLOATING;
        }
            
        if (endTime != null) {
            this.endTime = endTime;
            this.taskType = NOT_FLOATING;
        }
        
        if (taskType != null) {
            this.taskType = taskType;
        }
    }
    
    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getTaskDate(), source.getStartDate(), source.getEndDate(), source.getTaskTime(), source.getStartTime(), source.getEndTime(), source.getTags(), source.getStatus(), source.getTaskType());
    }
   
    @Override
    public Name getName() {
        return name;
    }

    @Override
    public TaskDate getTaskDate() {
    	return taskDate;
    }
    
    @Override
    public TaskDate getStartDate() {
        return startDate;
    }
    
    @Override
    public TaskDate getEndDate() {
        return endDate;
    }
    
    @Override
    public String getTaskTime() {
        return taskTime;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public String getEndTime() {
        return endTime;
    }

    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }
    
    @Override
    public String getStatus() {
        return status;
    }
    
    @Override
    public String getTaskType() {
        return taskType;
    }
    
    public void setName(Name name) {
        this.name = name;
    }
    
    public void setTaskDate(TaskDate taskDate) {
        this.taskDate = taskDate;
    }
    
    public void setStartDate(TaskDate startDate) {
        this.startDate = startDate;
    }
    
    public void setEndDate(TaskDate endDate) {
        this.endDate = endDate;
    }
    
    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    /**
     * Replaces this task's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, taskDate, tags, status, taskType);
    }

    @Override
    public String toString() {
        return getAsText();
    }

}
