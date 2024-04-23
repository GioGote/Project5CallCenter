/*
    You can import any additional package here.
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class CallCenter {

    /*
       N is the total number of customers that each agent will serve in
       this simulation.
       (Note that an agent can only serve one customer at a time.)
     */
    private static final int CUSTOMERS_PER_AGENT = 5;

    /*
       NUMBER_OF_AGENTS specifies the total number of agents.
     */
    private static final int NUMBER_OF_AGENTS = 3;

    /*
       NUMBER_OF_CUSTOMERS specifies the total number of customers to create
       for this simulation.
     */
    private static final int NUMBER_OF_CUSTOMERS = NUMBER_OF_AGENTS * CUSTOMERS_PER_AGENT;

    /*
      NUMBER_OF_THREADS specifies the number of threads to use for this simulation.
      (The number of threads should be greater than the number of agents and greeter combined
      to allow simulating multiple concurrent customers.)
     */
    private static final int NUMBER_OF_THREADS = 10;

    // Qs
    private static Queue<Integer> waitQ = new LinkedList<Integer>();
    private static Queue<Integer> dispatchQ = new LinkedList<Integer>();

    // Locks
    private static ReentrantLock waitLock = new ReentrantLock();
    private static ReentrantLock dispatchLock = new ReentrantLock();

    // Conditions
    private static Condition waitersWaiting = waitLock.newCondition();

    // Semaphores
    private static Semaphore dispatchSem = new Semaphore(0, true);

    /*
       The Agent class.
     */
    public static class Agent implements Runnable {

        //The ID of the agent.
        private final int ID;

        //Feel free to modify the constructor
        public Agent(int i) {
            ID = i;
        }

        // # of customers served by agent
        private int customersServed = 0;

        /*
        Your Agent implementation must call the method below
        to serve each customer.
        Do not modify this method.
         */
        public void serve(int customerID) {
            System.out.println("Agent " + ID + " is serving customer " + customerID);
            try {
                /*
                   Simulate busy serving a customer by sleeping for a random amount of time.
                */
                sleep(ThreadLocalRandom.current().nextInt(10, 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while(customersServed < CUSTOMERS_PER_AGENT) {
                int customerID = -1;
                try {
                    dispatchSem.acquire();
                } catch(Exception error) {
                    // nope
                }

                dispatchLock.lock();
                try {
                    customerID = dispatchQ.remove();
                }
                finally {
                    dispatchLock.unlock();
                }

                if(customerID != -1) {
                    serve(customerID);
                    this.customersServed++;
                }
            }
            // Below line is for testing
            System.out.println("Agent " + this.ID + " out! ^-^");
        }
    }


    /*
        The greeter class.
        to greet a customer.
     */
    public static class Greeter implements Runnable {
        private int customersServed = 0;

        public void run() {
            while(customersServed < NUMBER_OF_CUSTOMERS) {
                int customerID = -1;
                waitLock.lock();
                try {
                    while(waitQ.isEmpty()) {
                        waitersWaiting.await();
                    }
                    customerID = waitQ.remove();
                } catch(Exception error) {
                    // nope
                }
                finally {
                    waitLock.unlock();
                }
                dispatchLock.lock();
                try {
                    dispatchQ.add(customerID);
                    System.out.println(
                            "Greeting customer " + customerID + ": " + "Your place in queue is " + dispatchQ.size() + "! :3"
                    );
                }
                finally {
                    dispatchLock.unlock();
                }
                this.customersServed++;

                dispatchSem.release();
            }
            // Below line is for testing
            System.out.println("Greeter out ✌");
        }
    }


    /*
        The customer class.
        add itself to the first queue.
     */
    public static class Customer implements Runnable {

        //The ID of the customer.
        private final int ID;


        //Feel free to modify the constructor
        public Customer (int i){
            ID = i;
        }

        public void run() {
            waitLock.lock();
            try {
                waitQ.add(ID);
                waitersWaiting.signal();
            }
            finally {
                waitLock.unlock();
            }
        }
    }

    /*
        Create the greeter and agents threads first, and then create the customer threads.
     */
    public static void main(String[] args) throws Exception {
        // Make the random
        Random randy = new Random();

        // Set up the es with the greeter and the agents
        ExecutorService es = Executors.newFixedThreadPool(10);
        es.submit(new Greeter());
        for(int i = 1; i <= NUMBER_OF_AGENTS; i++) {
            es.submit(new Agent(i));
        }

	    //Insert a random sleep between 0 and 150 miliseconds after submitting every customer task,
        // to simulate a random interval between customer arrivals.
        // Summon the customers forth
        for(int i = 1; i <= NUMBER_OF_CUSTOMERS; i++) {
            es.submit(new Customer(i));
            sleep(randy.nextInt(0, 150));
        }

        // End it!
        es.shutdown();
    }

}
