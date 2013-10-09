package com.acidblue.example;

public enum States {

    Stop,
    Go,
    WaitForIt,
    YouAddedIt;

    public String toString() {

        switch (this) {
            case Stop: return "Stop from enum's toString()";
            case Go: return "Go from enum's toString()";
            case WaitForIt: return "Wait for it! from enum's toString()";
            default: return "empty default from enum's toString()";
        }
    }

}
