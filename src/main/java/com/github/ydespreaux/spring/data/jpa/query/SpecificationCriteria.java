/*
 * Copyright (C) 2018 Yoann Despréaux
 *
 * This program eq free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program eq distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; see the file COPYING . If not, write to the
 * Free Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Please send bugreports with examples or suggestions to yoann.despreaux@believeit.fr
 */

package com.github.ydespreaux.spring.data.jpa.query;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.*;
import java.util.*;

import static com.github.ydespreaux.spring.data.jpa.query.Criteria.CriteriaEntry;

/**
 * @author Yoann Despréaux
 * @since 1.0.0
 */
public class SpecificationCriteria<T> implements Specification<T> {

    private static final long serialVersionUID = 3295157927853086841L;

    private transient Criteria criteria;

    private transient Map<String, Join<?, ?>> joinMap = new HashMap<>();

    public SpecificationCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<T> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        if (this.criteria == null) {
            return null;
        }
        return toPredicate(this.criteria, root, cb);
    }

    /**
     * @param criteria
     * @param root
     * @param cb
     * @return
     */
    protected Predicate toPredicate(Criteria criteria, Root<T> root, CriteriaBuilder cb) {
        List<Predicate> restrictions = new ArrayList<>();
        for (Criteria chainedCriteria : criteria.getCriteriaChain()) {
            Predicate predicate = toPredicate(chainedCriteria, root, cb);
            if (predicate != null) {
                restrictions.add(predicate);
            }
        }
        List<CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        for (CriteriaEntry entry : entries) {
            Predicate predicate = toPredicate(entry, root, cb);
            if (predicate != null) {
                restrictions.add(predicate);
            }
        }
        if (!restrictions.isEmpty()) {
            if (restrictions.size() > 1) {
                return criteria.isAnd() ? cb.and(toArray(restrictions)) : cb.or(toArray(restrictions));
            }
            return restrictions.get(0);
        }
        return null;
    }


    private Predicate[] toArray(List<Predicate> predicates) {
        return predicates.toArray(new Predicate[predicates.size()]);
    }

    /**
     * @param entry
     * @param root
     * @param cb
     * @return
     */
    protected Predicate toPredicate(CriteriaEntry entry, Root<T> root, CriteriaBuilder cb) {
        Path<?> path = getPath(root, root.getJavaType().getSimpleName(), splitProperties(entry.getField().getName()));
        return toPredicate(cb, path, entry);
    }

    /**
     * @param root
     * @param properties
     * @param <Y>
     * @return
     */
    protected <Y> Path<Y> getPath(From<T, ?> root, String currentPath, String... properties) {
        if (properties.length == 1) {
            return root.get(properties[0]);
        }
        String path = currentPath + "." + properties[0];
        Join<T, ?> join = (Join<T, ?>) joinMap.get(path);
        if (join == null) {
            join = root.join(properties[0], JoinType.LEFT);
            joinMap.put(path, join);
        }
        String[] newProperties = new String[properties.length - 1];
        System.arraycopy(properties, 1, newProperties, 0, newProperties.length);
        return getPath(join, path, newProperties);
    }

    /**
     * @param builder
     * @param path
     * @param entry
     * @param <Y>
     * @return
     */
    private <Y> Predicate toPredicate(CriteriaBuilder builder, Path<Y> path, Criteria.CriteriaEntry entry) {
        assertSearchCriteriaValue(entry);
        Object value = entry.getValue();
        switch (entry.getKey()) {
            case EQUALS:
                return builder.equal(path, value);
            case NOT_EQUALS:
                return builder.notEqual(path, value);
            case NOT_NULL:
                return builder.isNotNull(path);
            case NULL:
                return builder.isNull(path);
            case BETWEEN:
                Comparable[] values = (Comparable[]) value;
                return builder.between(castPath(path, Comparable.class), values[0], values[1]);
            case GREATER_EQUAL:
                return builder.greaterThanOrEqualTo(castPath(path, Comparable.class), (Comparable) value);
            case GREATER:
                return builder.greaterThan(castPath(path, Comparable.class), (Comparable) value);
            case LESS_EQUAL:
                return builder.lessThanOrEqualTo(castPath(path, Comparable.class), (Comparable) value);
            case LESS:
                return builder.lessThan(castPath(path, Comparable.class), (Comparable) value);
            case IN:
                return path.in((Iterable<?>) value);
            case NOT_IN:
                return builder.not(path.in((Iterable<?>) value));
            case CONTAINS:
                return builder.like(builder.upper(castPath(path, String.class)), "%" + ((String) value).toUpperCase() + "%");
            case ENDS_WITH:
                return builder.like(builder.upper(castPath(path, String.class)), "%" + ((String) value).toUpperCase());
            case STARTS_WITH:
                return builder.like(builder.upper(castPath(path, String.class)), ((String) value).toUpperCase() + "%");
        }
        return null;
    }

    /**
     * @param entry
     */
    private void assertSearchCriteriaValue(Criteria.CriteriaEntry entry) {
        if (entry.getKey() != Criteria.OperationKey.NOT_NULL && entry.getKey() != Criteria.OperationKey.NULL) {
            Objects.requireNonNull(entry.getValue(), "CriteriaEntry.value must not be null!!");
        }
    }

    /**
     * @param path
     * @param valueType
     * @param <T>
     * @param <V>
     * @return
     */
    private <T, V> Expression<V> castPath(Path<T> path, Class<V> valueType) {
        if (valueType.isAssignableFrom(path.getJavaType())) {
            return (Path<V>) path;
        }
        throw new IllegalArgumentException(String.format("Could not convert java type [%s] to [%s]", valueType.getName(), path.getJavaType().getName()));
    }

    /**
     * @param propertyPath
     * @return
     */
    private String[] splitProperties(String propertyPath) {
        return propertyPath.split("\\.");
    }

}