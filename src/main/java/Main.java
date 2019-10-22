import executor.Executor;
import executor.Task;
import rx.Observer;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Main {

    public static Observer<Task> getObserver() {

        return new Observer<Task>() {
            @Override
            public void onCompleted() {
                System.out.println("There is no more tasks");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("There is something went wrong");
            }

            @Override
            public void onNext(Task task) {
                System.out.println("There is a task that has finished");
            }
        };
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

//        Observer observer = getObserver();
//
//        executor.getNotifiedOnFinishedTask(observer);

        Runnable runnable = () -> {

            Stream<Task> stream = executor.getFinishedTasksStream();

            stream.forEach(task -> {
                if (Objects.nonNull(task)) {
                    System.out.println("There is a task that has finished");
                }
            });

            System.out.println("There is no more tasks");

        };

        Thread thread = new Thread(runnable);

        thread.start();

        executor
                .applyTask(supplier1,consumer)
                .applyTask(supplier2,consumer)
                .applyTask(supplier3,consumer);

        Thread.sleep(4000);

        executor.terminate();

        System.out.println("Finished !");


    }
}
