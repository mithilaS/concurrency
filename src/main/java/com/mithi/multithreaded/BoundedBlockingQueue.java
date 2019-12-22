package com.mithi.multithreaded;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBlockingQueue{

    private int  capacity ;
    private Condition empty ;
    private  Condition full;
    private Lock lock = new ReentrantLock();
    private LinkedList<Integer> list;

    public BoundedBlockingQueue(int capacity) {
        this.capacity= capacity;
        list= new LinkedList();
        empty = lock.newCondition();
        full = lock.newCondition();
    }

    public void enqueue(int element) throws InterruptedException {
        // if not full then add else block
        lock.lock();
            try {
                System.out.println(" In enqueue: "+ Thread.currentThread().getName());
                while (capacity == list.size()) {
                    full.await();
                }
                    list.addLast(element);
                    System.out.println("Added : " + element + " t: " + Thread.currentThread().getName());
                    empty.signalAll();

            }finally{
                lock.unlock();
            }
    }

    public int dequeue() throws InterruptedException {
        lock.lock();
        System.out.println(" In deque: " + Thread.currentThread().getName());
        try{
            while(list.size()==0){
                empty.await();
            }
            int num =list.removeFirst();
            System.out.println("Removed : "+ num + " t: " + Thread.currentThread().getName());
            full.signalAll();
            return num;
        }finally{
            lock.unlock();
        }

    }

    public int size() {
        return list.size();
    }


}
