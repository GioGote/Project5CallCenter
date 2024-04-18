/*
    You can import any additional package here.
 */
import java.util.concurrent.ThreadLocalRandom;
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

    }


    /*
        The greeter class.
        to greet a customer.
     */
    public static class Greeter implements Runnable{

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
    }

    /*
        Create the greeter and agents threads first, and then create the customer threads.
     */
    public static void main(String[] args){

	//Insert a random sleep between 0 and 150 miliseconds after submitting every customer task,
     // to simulate a random interval between customer arrivals.

    }

}
