package executor;

import rx.Observer;
import rx.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Executor {

    private Integer numOfThreads;
    private Integer maxNumOfTasks;
    private Integer numberOfTerminatedThreads;

    private ArrayList<TaskApplier> taskAppliers;

    protected static BlockingQueue<Task> finishedTasksQueue ;

    private static PublishSubject<Task> finishedTasks;

    private Boolean isRunning;

    public Executor() {
        this(2,100);
    }

    public Executor(Integer numOfThreads, Integer maxNumOfTasks){
        isRunning = true;
        this.numOfThreads = numOfThreads;
        this.maxNumOfTasks = maxNumOfTasks;
        numberOfTerminatedThreads = 0;

        TaskApplier.tasks = new ArrayBlockingQueue<>(maxNumOfTasks);

        taskAppliers = new ArrayList<>();

        for( int i=0 ; i<numOfThreads ; i++ ){
            taskAppliers.add(new TaskApplier());
            taskAppliers.get(i).start();
        }

        finishedTasks = PublishSubject.create();

        finishedTasksQueue = new ArrayBlockingQueue<>(maxNumOfTasks);
    }



    public static void notifyFinishedTask (Task task) {
        finishedTasks.onNext(task);
    }

    public void getNotifiedOnFinishedTask (Observer observer) {
        finishedTasks.subscribe(observer);
    }

    public Stream<Task> getFinishedTasksStream () {

        Iterable<Task> collection = () -> new Iterator<Task>() {

            @Override
            public boolean hasNext() {

                if (finishedTasksQueue.isEmpty()) {
                    isRunning = false;
                } else {
                    Task task = finishedTasksQueue.peek();
                    if (Objects.nonNull(task) && task instanceof TerminatingTask) {
                        isRunning = false;
                    }
                }

                return isRunning;
            }

            @Override
            public Task next() {

                if (hasNext()) {
                    try {
                        Task task = finishedTasksQueue.take();
                        return task;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    throw new NoSuchElementException();
                }

            }
        };

        //note: here the default implementation of the spliterator is used
        // a new one could be created with specific characteristics and the iterator will be passed to it
        return StreamSupport.stream(collection.spliterator(), false);
    }

    public <T> Executor applyTask(Supplier<T> supplier, Consumer<T> consumer){

        Task<T> task = new Task<>();

        task.setSupplier(supplier);
        task.setConsumer(consumer);

        TaskApplier.tasks.add(task);

        return this;
    }

    public <T> CompletableFuture applyTask (Supplier<T> supplier){

        return CompletableFuture.supplyAsync(supplier);
    }

    public void terminateOneApplier() {
        TerminatingTask terminatingTask = new TerminatingTask();
        TaskApplier.tasks.add(terminatingTask);
        numberOfTerminatedThreads++;
    }

    public void terminateAllAppliers(){
        int n = numOfThreads-numberOfTerminatedThreads;
        for (int i=0 ; i<n ; i++) {
            terminateOneApplier();
        }
    }

    public void terminate () {
        terminateAllAppliers();
        finishedTasks.onCompleted();
        TerminatingTask terminatingTask = new TerminatingTask();
        finishedTasksQueue.add(terminatingTask);
    }
}

