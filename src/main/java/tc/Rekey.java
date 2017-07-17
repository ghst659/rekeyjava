package tc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Class to provide and retain key mappings.
 */
public class Rekey<K> {
    private Map<K,K> old2new = new HashMap<>();
    private Map<K,K> new2old = new HashMap<>();
    private Supplier<K> generator = null;

    /**
     * Constructor taking a Supplier function for new K values.
     * @param gen A Supplier function for K values.
     */
    public Rekey(Supplier<K> gen) {
        this.generator = gen;
    }

    /**
     * Retrieve the new value for an old value.
     * @param oldKey The old value for which to retrieve the new value.
     * @return The new value.
     */
    public synchronized K newKey(K oldKey) {
        K result = this.old2new.getOrDefault(oldKey,null);
        if (result != null) {
            assert oldKey == this.new2old.get(result);
        } else {
            result = this.generator.get();
            assert ! this.new2old.containsKey(result);
            this.old2new.put(oldKey, result);
            this.new2old.put(result, oldKey);
        }
        return result;
    }

    /**
     * Retrieve the old value for a new value.
     * @param newKey The new value for which to retrieve the old value.
     * @return The old value.
     */
    public synchronized K oldKey(K newKey) {
        K result = this.new2old.getOrDefault(newKey, null);
        if (result != null) {
            assert this.old2new.containsKey(result);
        }
        return result;
    }

    /**
     * Retrieve a ;set of all old keys in the map.
     * @return A set of K values.
     */
    public synchronized Set<K> oldSet() {
        Set<K> result = new HashSet<>(this.old2new.keySet());
        return result;
    }

    /**
     * Retrieve a set of all new keys in the map.
     * @return A set of K values.
     */
    public synchronized Set<K> newSet() {
        Set<K> result = new HashSet<>(this.new2old.keySet());
        return result;
    }
}
