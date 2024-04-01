package com.mygdx.game.Observer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObservableTest {
    static int count = 0; // Count calls of notifyObserver
    static String stringVal = "";

    @Test
    void addObserver() {
        // Test notifications
        count = 0;
        Observable<Void> voidObservable = new Observable<>();
        Observer<Void> voidObserver = arg -> count++;
        voidObservable.addObserver(voidObserver);
        voidObservable.notifyObservers(null);
        assertEquals(1, count);

        // Test multiple notifications
        count = 0;
        Observer<Void> voidObserver2 = arg -> count++;
        voidObservable.addObserver(voidObserver2);
        voidObservable.notifyObservers(null);
        assertEquals(2, count);
    }

    @Test
    void notifyObservers() {
        // Test data types
        count = 0;
        stringVal = "";

        Observable<Integer> intObservable = new Observable<>();
        Observer<Integer> intObserver = arg -> count = arg;
        intObservable.addObserver(intObserver);
        intObservable.notifyObservers(5);
        assertEquals(5, count);

        Observable<String> strObservable = new Observable<>();
        Observer<String> strObserver = arg -> stringVal = arg;
        strObservable.addObserver(strObserver);
        strObservable.notifyObservers("result");
        assertEquals("result", stringVal);
    }
}