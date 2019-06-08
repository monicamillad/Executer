import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
public class TaskApplier extends Thread {

    ArrayList<Task> tasks ;

    TaskApplier(){
        tasks = new ArrayList<>();
    }

    @Override
    public void run(){

        while( !this.isInterrupted() ){

            while( !tasks.isEmpty() ){

                System.out.println(this.getName());

                tasks.get(0).setValue(tasks.get(0).getSupplier().get());
                tasks.get(0).getConsumer().accept(tasks.get(0).getValue());

                tasks.clear();
            }
        }
    }
}
