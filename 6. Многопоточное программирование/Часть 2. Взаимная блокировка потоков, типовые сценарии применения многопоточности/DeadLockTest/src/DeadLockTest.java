public class DeadLockTest
{
    public static void main(String [] args)
    {
        new WorkerThread1().start();
        new WorkerThread2().start();
    }

    public static void sleep(long delay)
    {
        try
        {
            Thread.sleep(delay);
        }
        catch (InterruptedException ex)
        {
        }
    }

    static Object st_objLock1 = new Object();
    static Object st_objLock2 = new Object();
    static int iDelay = 1;
    static class WorkerThread1 extends Thread
    {
        @Override
        public void run()
        {
            System.out.println("WorkerThread1 is started");
            DeadLockTest.sleep(iDelay);

            synchronized(st_objLock1)
            {
                System.out.println("WorkerThread1 got lock on st_objLock1");
                DeadLockTest.sleep(iDelay);
                synchronized(st_objLock2)
                {
                    System.out.println("WorkerThread1 got lock on st_objLock2");
                    DeadLockTest.sleep(iDelay);
                }
                System.out.println("WorkerThread1 released lock on st_objLock2");
                DeadLockTest.sleep(iDelay);
            }
            System.out.println("WorkerThread1 released lock on st_objLock1");
        }

    }

    static class WorkerThread2 extends Thread
    {
        @Override
        public void run()
        {
            System.out.println("WorkerThread2 is started");
            DeadLockTest.sleep(iDelay);
            synchronized(st_objLock2)
            {
                System.out.println("WorkerThread2 got lock on st_objLock2");
                DeadLockTest.sleep(iDelay);
                synchronized(st_objLock1)
                {
                    System.out.println("WorkerThread2 got lock on st_objLock1");
                    DeadLockTest.sleep(iDelay);
                }
                System.out.println("WorkerThread2 released lock on st_objLock1");
                DeadLockTest.sleep(iDelay);
            }
            System.out.println("WorkerThread2 released lock on st_objLock2");
        }

    }

}