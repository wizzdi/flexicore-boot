package com.flexicore.jobs.messages;


public class JobPhaseChangedMessage extends JobMessage{
    private String oldPhase;
    private String newPhase;

    public String getOldPhase() {
        return oldPhase;
    }

    public JobPhaseChangedMessage setOldPhase(String oldPhase) {
        this.oldPhase = oldPhase;
        return this;
    }

    public String getNewPhase() {
        return newPhase;
    }

    public JobPhaseChangedMessage setNewPhase(String newPhase) {
        this.newPhase = newPhase;
        return this;
    }

    @Override
    public JobPhaseChangedMessage setId(String id) {
        return (JobPhaseChangedMessage) super.setId(id);
    }
}
