package com.flexicore.interfaces;

import com.flexicore.exceptions.PhaseErrorDetected;

import java.util.Set;

/**
 * Created by Asaf on 21/12/2016.
 */
public interface Phaser {

    void waitForPhase(String phase) throws InterruptedException, PhaseErrorDetected;
    void setCurrentPhase(String phaseName);
    String getCurrentPhase();

    void waitForOneOfPhase(Set<String> phases) throws InterruptedException, PhaseErrorDetected;
}
