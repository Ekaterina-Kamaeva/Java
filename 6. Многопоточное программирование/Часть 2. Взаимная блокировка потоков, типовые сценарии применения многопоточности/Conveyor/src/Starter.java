public class Starter
{
    public static void main(String [] args) throws InterruptedException {
        Dispatcher dispatcher = new Dispatcher();
        new ExpressionGenerator(10, dispatcher, 100).start();
        new ExpressionGenerator(20, dispatcher, 200).start();
        new ExpressionGenerator(30, dispatcher, 300).start();
        new ExpressionGenerator(40, dispatcher, 4000).start();
//      new ExpressionGenerator(30, dispatcher, 10).start();
//      new ExpressionGenerator(40, dispatcher, 100).start();
        Thread.sleep(5000);
    }

}
