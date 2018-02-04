package multithreadregister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author lucas.burdell
 */
public class ExecutorRegister {
    
    private static final ExecutorRegister singleton = new ExecutorRegister();
    private final HashSet<ExecutorService> registry = new LinkedHashSet<>();
    private ExecutorRegister(){
        
    }
    
    public void addToRegistry(ExecutorService service) {
        registry.add(service);
    }
    
    public void shutdown(){
        for (ExecutorService executorService : registry) {
            if (executorService != null) 
                executorService.shutdownNow();
            SecurityManager manager = new SecurityManager();
        }
    }
    
}
