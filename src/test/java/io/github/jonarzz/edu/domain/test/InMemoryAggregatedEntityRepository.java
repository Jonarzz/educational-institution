package io.github.jonarzz.edu.domain.test;

import static lombok.AccessLevel.PROTECTED;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = PROTECTED, makeFinal = true)
public abstract class InMemoryAggregatedEntityRepository<K, V> {

    Map<K, Collection<V>> entitiesByAggregatingId = new HashMap<>();

    protected abstract Function<V, UUID> aggregatedEntityIdGetter();

    protected V getByEntityId(UUID aggregatedEntityId) {
        return entitiesByAggregatingId.values()
                                      .stream()
                                      .flatMap(Collection::stream)
                                      .filter(entity -> aggregatedEntityId.equals(aggregatedEntityIdGetter()
                                                                                          .apply(entity)))
                                      .findFirst()
                                      .orElseThrow(() -> new IllegalStateException("Not found entity with ID "
                                                                                   + aggregatedEntityId));
    }

    protected Collection<V> getByAggregatingId(K aggregatingId) {
        return entitiesByAggregatingId.getOrDefault(aggregatingId, Set.of());
    }

    protected V firstMatching(K aggregatingId, Predicate<V> predicate) {
        return getByAggregatingId(aggregatingId)
                .stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow();
    }

    protected void replace(V object) {
        for (var entities : entitiesByAggregatingId.values()) {
            for (var entity : entities) {
                if (entity.equals(object)) {
                    entities.remove(entity);
                    entities.add(object);
                    return;
                }
            }
        }
    }

    protected void add(K aggregatingId, V object) {
        entitiesByAggregatingId.computeIfAbsent(
                aggregatingId,
                id -> new HashSet<>()
        ).add(object);
    }

}
