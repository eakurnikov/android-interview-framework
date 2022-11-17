package com.example.myapplication.concurrency;

/**
 * TASK: H2O generator.
 * We have thread Hydrogen and thread Oxygen, they are randomly fed to the H2OMachine.
 * The task is to sync them in such a way that they are grouped like H2O
 * and then they have the release method called at the same time.
 */

interface Hydrogen {
    void release();
}

interface Oxygen {
    void release();
}

class H2OMachine {

    public void hydrogen(Hydrogen hydrogen) throws InterruptedException {
    }

    public void oxygen(Oxygen oxygen) throws InterruptedException {
    }
}
