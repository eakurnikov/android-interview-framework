package com.example.myapplication.concurrency;

/**
 * TASK: Classic walking robot.
 * The robot has two legs: left and right which are the threads.
 * On each step the leg prints it's name: "left" or "right".
 * We want to sync them to be able to see: “rightleftrightleftrightleftrightleft”
 */

public class Robot {

    private static class Foot implements Runnable {
        private final String name;

        Foot(String name) {
            this.name = name;
        }

        @Override
        public void run() {
        }

        private void step() {
            System.out.println(name);
        }
    }

    public void run() {
        Robot.Foot left = new Robot.Foot("left");
        Robot.Foot right = new Robot.Foot("right");

        new Thread(left).start();
        new Thread(right).start();
    }

    public static void main(String[] args) {
        new Robot().run();
    }
}
