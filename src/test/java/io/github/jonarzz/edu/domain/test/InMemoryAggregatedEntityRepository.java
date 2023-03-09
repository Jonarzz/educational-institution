package io.github.jonarzz.edu.domain.test;

import static lombok.AccessLevel.*;

import lombok.experimental.*;

import java.util.*;
import java.util.function.*;

@FieldDefaults(level = PROTECTED, makeFinal = true)
public abstract class InMemoryAggregatedEntityRepository<V> {

    Map<UUID, Collection<V>> entitiesByAggregatingId = new HashMap<>();

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

    protected Collection<V> getByAggregatingId(UUID aggregatingId) {
        return entitiesByAggregatingId.getOrDefault(aggregatingId, Set.of());
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

    protected void add(UUID aggregatingId, V object) {
        entitiesByAggregatingId.computeIfAbsent(
                aggregatingId,
                id -> new HashSet<>()
        ).add(object);
    }

}
