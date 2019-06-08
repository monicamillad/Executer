import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Setter
@Getter
@AllArgsConstructor
public class QueueManager extends Thread {

    private BlockingQueue<Task> pendingTasks;

    private TaskApplier[] taskAppliers;

    QueueManager(){

        pendingTasks = new ArrayBlockingQueue<>(100);
        taskAppliers = new TaskApplier[Executor.NUM_OF_THREADS];

        for( int i=0 ; i<Executor.NUM_OF_THREADS ; i++ ){
            taskAppliers[i] = new TaskApplier();
            taskAppliers[i].start();
        }
    }

    @Override
    public void run(){

        while( !this.isInterrupted() ){

            while( !pendingTasks.isEmpty() ){

                for( int i=0 ; i<Executor.NUM_OF_THREADS ; i++ ){

                    if( taskAppliers[i].getTasks().isEmpty() ){
                        try {
                            taskAppliers[i].getTasks().add(pendingTasks.take());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        break ;
                    }
                }
            }
        }
    }

    public void terminate(){
        for( int i=0 ; i<Executor.NUM_OF_THREADS ; i++ ){
            taskAppliers[i].interrupt();
        }
    }
}
