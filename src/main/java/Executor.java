import java.util.function.Consumer;
import java.util.function.Supplier;

public class Executor {

    public static final Integer NUM_OF_THREADS = 2;

    private QueueManager queueManager;

    Executor(){

        queueManager = new QueueManager();
        queueManager.start();
    }

    public <T> void  applyTask(Supplier<T>supplier, Consumer<T>consumer){

        Task<T> task = new Task<>();

        task.setSupplier(supplier);
        task.setConsumer(consumer);

        for( int i=0 ; i<NUM_OF_THREADS ; i++ ){

            if( queueManager.getTaskAppliers()[i].getTasks().isEmpty() ){

                queueManager.getTaskAppliers()[i].getTasks().add(task);

                return;
            }
        }

        queueManager.getPendingTasks().add(task);
    }

    public void terminate(){
        queueManager.terminate();
        queueManager.interrupt();
    }

    public static void main(String[] args) throws InterruptedException {

        Executor executor = new Executor();

        Supplier<String> supplier1 = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Task1";
        } ;

        Consumer<String> consumer = (s) -> System.out.println("Hello from " + s);

        Supplier<String> supplier2 = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Task2";
        } ;

        Supplier<String> supplier3 = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Task3";
        } ;

        executor.applyTask(supplier1,consumer);
        executor.applyTask(supplier2,consumer);
        executor.applyTask(supplier3,consumer);

        Thread.sleep(4000);

        executor.terminate();

        System.out.println("Finished !");
    }
}

