/**
 * Observable interface for the Observer Pattern
 */
package com.mygdx.game.Observer;

import java.util.ArrayList;

public class Observable<T> {
    private ArrayList<Observer<T>> observerList = new ArrayList<Observer<T>>();

    /**
     * Add an observer to the list.
     *
     * @param observer the object observing {@code this}
     */
    public void addObserver(Observer<T> observer) {
        observerList.add(observer);
    }

    /**
     * Notify all observers of {@code this}.
     *
     * @param arg passed to the observers.
     */
    public void notifyObservers(T arg) {
        for (Observer<T> observer : observerList) {
            observer.update(arg);
        }
    }
}
