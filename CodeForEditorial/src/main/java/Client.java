public class Client {


    public static void main(String[] args) {
        System.out.println("I am the main class");

        ScalerThread t1 = new ScalerThread(new Adder());
        t1.start();

        ScalerThread t2 = new ScalerThread(new Subtractor());
        t2.start();

    }

}