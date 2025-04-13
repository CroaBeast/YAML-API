package me.croabeast.file;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

/**
 * A concrete implementation of {@link Mappable} backed by a {@link HashMap}.
 * <p>
 * {@code HashMappable} associates integer keys with collections of elements of type {@code T}.
 * It extends {@code HashMap<Integer, C>} and implements the {@code Mappable} interface, providing
 * utility methods for ordering and copying the mapping. In addition, it stores a {@link Supplier} to
 * generate new instances of the collection type {@code C} when needed.
 * </p>
 *
 * @param <T> the type of elements stored in the collections.
 * @param <C> the type of collection that holds the elements.
 * @see Mappable
 */
@Getter
public class HashMappable<T, C extends Collection<T>> extends HashMap<Integer, C> implements Mappable<T, C, HashMappable<T, C>> {

    /**
     * A supplier for creating new instances of the collection type {@code C}.
     */
    private final Supplier<C> supplier;

    /**
     * Constructs an empty {@code HashMappable} with the specified collection supplier.
     *
     * @param supplier a {@link Supplier} used to create new collections of type {@code C}; must not be {@code null}
     */
    public HashMappable(Supplier<C> supplier) {
        super();
        this.supplier = Objects.requireNonNull(supplier);
    }

    /**
     * Constructs a new {@code HashMappable} with the mappings copied from the provided map.
     *
     * @param supplier a {@link Supplier} used to create new collections of type {@code C}; must not be {@code null}
     * @param map      the map whose entries are to be placed in this mappable
     */
    public HashMappable(Supplier<C> supplier, Map<Integer, ? extends C> map) {
        super(map);
        this.supplier = Objects.requireNonNull(supplier);
    }

    /**
     * Orders the entries of this {@code HashMappable} using the provided comparator.
     * <p>
     * This method creates a new ordered map (a {@link TreeMap}) based on the given comparator,
     * copies all entries into it, and then returns a new {@code HashMappable} instance
     * containing the ordered entries.
     * </p>
     *
     * @param comparator the comparator used for ordering the keys; must not be {@code null}
     * @return a new {@code HashMappable} instance with entries ordered according to the comparator
     */
    @NotNull
    public HashMappable<T, C> order(Comparator<Integer> comparator) {
        TreeMap<Integer, C> map = new TreeMap<>(comparator);
        forEach((k, v) -> {
            C collection = supplier.get();
            collection.addAll(v);
            map.put(k, collection);
        });
        return new HashMappable<>(supplier, map);
    }

    /**
     * Creates a shallow copy of this {@code HashMappable}.
     * <p>
     * The returned copy contains the same keys and values as this instance. Note that the collections
     * associated with each key are also copied using the supplier, but their elements are not deep-cloned.
     * </p>
     *
     * @return a new {@code HashMappable} instance with the same data as this instance.
     */
    @NotNull
    public HashMappable<T, C> copy() {
        HashMappable<T, C> mappable = new HashMappable<>(supplier);
        forEach((k, v) -> {
            C collection = supplier.get();
            collection.addAll(v);
            mappable.put(k, collection);
        });
        return mappable;
    }

    /**
     * Retrieves all stored values across all keys combined into a single collection.
     * <p>
     * This method collects all elements from each collection in the mapping and returns a new collection
     * containing all these elements.
     * </p>
     *
     * @return a collection containing all values from the mapping
     */
    @NotNull
    public C getStoredValues() {
        return getStoredValues(supplier);
    }

    /**
     * Returns this instance as a {@code HashMappable}.
     *
     * @return this {@code HashMappable} instance
     */
    @NotNull
    public HashMappable<T, C> instance() {
        return this;
    }
}
