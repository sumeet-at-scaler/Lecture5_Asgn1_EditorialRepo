public class Client {
    public static void main(String[] args) {
        System.out.println("I am the main class");

        Thread t1 = new Thread(new Adder());
        t1.start();

        Thread t2 = new Thread(new Subtractor());
        t2.start();
    }
}