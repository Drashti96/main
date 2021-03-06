//@@author A0126240W-reused
package seedu.whatnow.model.task;

import seedu.whatnow.commons.exceptions.IllegalValueException;

/**
 * Represents a Task's name in WhatNow. Guarantees: immutable; is valid as
 * declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_NAME_CONSTRAINTS = "Task names should not contain quotation marks \"";
    public static final String NAME_VALIDATION_REGEX = "[^\"]+";

    public final String fullName;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException
     *             if given name string is invalid.
     */
    public Name(String name) throws IllegalValueException {
        assert name != null;
        String nameRemovedTrailSpace = name.trim();
        if (!isValidName(nameRemovedTrailSpace)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        this.fullName = nameRemovedTrailSpace;
    }

    /**
     * Returns true if a given string is a valid task name.
     */
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Name // instanceof handles nulls
                        && this.fullName.equals(((Name) other).fullName)); // state
                                                                           // check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
