
import java.util.function.Consumer;
import java.util.function.Supplier;


public class Task<T> {

    private Supplier<T> supplier;
    private Consumer<T> consumer;
    private T value;

    public void setSupplier(Supplier<T> supplier){
        this.supplier = supplier;
    }

    public void setConsumer(Consumer<T> consumer){
        this.consumer = consumer;
    }

    public void setValue (T value){
        this.value = value;
    }

    public T getValue(){
        return this.value;

    }

    public Supplier<T> getSupplier(){ return this.supplier;}
    public Consumer<T> getConsumer(){ return this.consumer;}
}
