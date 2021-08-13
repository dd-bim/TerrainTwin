package com.Microservices.SchedulerService.domain.model;

public class WorkingVar {
    
    public static volatile boolean isWorking = false;

    public void setWorkingVar(boolean working) {
        isWorking = working;
    }

    public boolean getWorkingVar() {
        return isWorking;
    }
}
