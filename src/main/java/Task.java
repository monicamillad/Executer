
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@Setter
public class Task<T> {

    private Supplier<T> supplier;
    private Consumer<T> consumer;
    private T value;
}
