package executor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;

@AllArgsConstructor
@Getter
@Setter
public class TaskApplier extends Thread {

    protected static BlockingQueue<Task> tasks;
    private volatile Boolean isRunning;

    TaskApplier(){
        isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            System.out.println(this.getName());

            Task task = null;
            try {
                task = tasks.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (task instanceof TerminatingTask) {
                isRunning = false;
            } else if(Objects.nonNull(task)) {
                task.setValue(task.getSupplier().get());
                task.getConsumer().accept(task.getValue());
                Executor.notifyFinishedTask(task);
                Executor.finishedTasksQueue.add(task);
            }
        }
    }

}
