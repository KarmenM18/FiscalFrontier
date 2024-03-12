/**
 * Observer for an Observer Pattern implementation
 */
package com.mygdx.game.Observer;

public interface Observer<T> {
    void update(T arg);
}