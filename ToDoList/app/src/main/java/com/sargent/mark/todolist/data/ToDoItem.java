package com.sargent.mark.todolist.data;

/**
 * Created by mark on 7/4/17.
 */

public class ToDoItem {
    private String description;
    private String dueDate;
    private boolean undone;

    public ToDoItem(String description, String dueDate) {
        this.description = description;
        this.dueDate = dueDate;
        this.undone = true;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    //adding status methods
    public boolean getStatus(){return undone;}

    public boolean setStatus(boolean undone){return this.undone = undone; }
}
