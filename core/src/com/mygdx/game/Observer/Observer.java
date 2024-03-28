package com.mygdx.game.Observer;

/**
 * Implementation of the Observer Pattern - The Observer can listen to an Observable.
 *
 * @param <T> The value type passed by the Observable
 */
public interface Observer<T> {
    void update(T arg);
}