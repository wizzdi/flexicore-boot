package com.flexicore.exceptions;

/**
 * Created by Asaf on 21/12/2016.
 */
public class PhaseErrorDetected extends Exception {

    public PhaseErrorDetected() {
    }

    public PhaseErrorDetected(String message) {
        super(message);
    }

    public PhaseErrorDetected(String message, Throwable cause) {
        super(message, cause);
    }

    public PhaseErrorDetected(Throwable cause) {
        super(cause);
    }
}
