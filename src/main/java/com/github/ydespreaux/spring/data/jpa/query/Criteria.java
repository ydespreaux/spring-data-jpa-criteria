/*
 * Copyright (C) 2018 Yoann Despréaux
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
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

import lombok.Getter;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Criteria is the central class when constructing jpa queries. It follows more or less a fluent API style, which allows to
 * easily chain together multiple criteria.
 *
 * @author Yoann Despréaux
 * @since 1.0.0
 */
public class Criteria {

    private static final String OR_OPERATOR = "OR";
    private static final String AND_OPERATOR = "AND";


    private Field field;
    private String conjunctionOperator;
    private List<Criteria> criteriaChained = new ArrayList<>(1);
    private List<CriteriaEntry> queryCriteria = new ArrayList<>(1);

    /**
     * Creates a new Criteria with provided field name
     *
     * @param fieldName
     */
    public Criteria(String fieldName) {
        this(new Field(fieldName));
    }

    /**
     * Creates a new Criteria for the given field
     *
     * @param field
     */
    public Criteria(Field field) {
        Assert.notNull(field, "Field for criteria must not be null");
        Assert.hasText(field.getName(), "Field.name for criteria must not be null/empty");
        this.field = field;
        this.conjunctionOperator = AND_OPERATOR;
    }

    /**
     * @param field
     * @param criteriaChained
     */
    protected Criteria(Field field, List<Criteria> criteriaChained, List<CriteriaEntry> queryCriteria, String conjunction) {
        Assert.notNull(field, "Field for criteria must not be null");
        Assert.hasText(field.getName(), "Field.name for criteria must not be null/empty");
        this.field = field;
        this.conjunctionOperator = conjunction;
        this.criteriaChained.addAll(criteriaChained);
        this.queryCriteria.addAll(queryCriteria);
    }

    /**
     * Chain using {@code AND}
     *
     * @param field
     * @return
     */
    public Criteria and(Field field) {
        if (this.isAnd()) {
            return new Criteria(field, this.criteriaChained, this.queryCriteria, AND_OPERATOR);
        } else {
            return new Criteria(field, Arrays.asList(this), Collections.emptyList(), AND_OPERATOR);
        }
    }

    /**
     * Chain using {@code AND}
     *
     * @param fieldName
     * @return
     */
    public Criteria and(String fieldName) {
        return and(new Field(fieldName));
    }

    /**
     * Chain using {@code AND}
     *
     * @param criteria
     * @return
     */
    public Criteria and(Criteria criteria) {
        this.criteriaChained.add(criteria);
        return this;
    }

    /**
     * Chain using {@code AND}
     *
     * @param criterias
     * @return
     */
    public Criteria and(Criteria... criterias) {
        this.criteriaChained.addAll(Arrays.asList(criterias));
        return this;
    }

    /**
     * Chain using {@code AND}
     *
     * @param criterias
     * @return
     */
    public Criteria and(List<Criteria> criterias) {
        this.criteriaChained.addAll(criterias);
        return this;
    }

    /**
     * Chain using {@code OR}
     *
     * @param field
     * @return
     */
    public Criteria or(Field field) {
        Assert.notNull(field, "Cannot chain 'null' field.");
        return new Criteria(field, this.criteriaChained, this.queryCriteria, OR_OPERATOR);
    }

    /**
     * Chain using {@code OR}
     *
     * @param criteria
     * @return
     */
    public Criteria or(Criteria criteria) {
        Assert.notNull(criteria, "Cannot chain 'null' criteria.");
        return new Criteria(criteria.field, Arrays.asList(this, criteria), Collections.emptyList(), OR_OPERATOR);
    }

    /**
     * Chain using {@code OR}
     *
     * @param fieldName
     * @return
     */
    public Criteria or(String fieldName) {
        return or(new Field(fieldName));
    }

    /**
     * Crates new CriteriaEntry with equals
     *
     * @param value
     * @return
     */
    public Criteria is(Object value) {
        queryCriteria.add(new CriteriaEntry(this.field, OperationKey.EQUALS, value));
        return this;
    }

    /**
     * Crates new CriteriaEntry with not equals
     *
     * @param value
     * @return
     */
    public Criteria isNot(Object value) {
        queryCriteria.add(new CriteriaEntry(this.field, OperationKey.NOT_EQUALS, value));
        return this;
    }

    /**
     * Crates new CriteriaEntry with null
     *
     * @return
     */
    public Criteria isNull() {
        queryCriteria.add(new CriteriaEntry(this.field, OperationKey.NULL, null));
        return this;
    }

    /**
     * Crates new CriteriaEntry with not null
     *
     * @return
     */
    public Criteria isNotNull() {
        this.addQueryCriteria(new CriteriaEntry(this.field, OperationKey.NOT_NULL, null));
        return this;
    }

    /**
     * Crates new CriteriaEntry with contains
     *
     * @param search
     * @return
     */
    public Criteria contains(String search) {
        assertNoBlankQuery(search);
        queryCriteria.add(new CriteriaEntry(this.field, OperationKey.CONTAINS, search));
        return this;
    }

    /**
     * Creates new CriteriaEntry for startsWith
     *
     * @param search
     * @return
     */
    public Criteria startsWith(String search) {
        assertNoBlankQuery(search);
        queryCriteria.add(new CriteriaEntry(this.field, OperationKey.STARTS_WITH, search));
        return this;
    }

    /**
     * Creates new CriteriaEntry for endsWith
     *
     * @param search
     * @return
     */
    public Criteria endsWith(String search) {
        assertNoBlankQuery(search);
        queryCriteria.add(new CriteriaEntry(this.field, OperationKey.ENDS_WITH, search));
        return this;
    }

    /**
     * Creates new CriteriaEntry for {@code RANGE [lowerBound TO upperBound]}
     *
     * @param lowerBound
     * @param upperBound
     * @return
     */
    public Criteria between(Comparable<?> lowerBound, Comparable<?> upperBound) {
        if (lowerBound == null && upperBound == null) {
            throw new InvalidDataAccessApiUsageException("Range [* TO *] is not allowed");
        }
        queryCriteria.add(new CriteriaEntry(this.field, OperationKey.BETWEEN, new Comparable<?>[]{lowerBound, upperBound}));
        return this;
    }

    /**
     * Creates new CriteriaEntry for {@code <= upperBound}
     *
     * @param upperBound
     * @return
     */
    public Criteria lessThanEqual(Comparable<?> upperBound) {
        if (upperBound == null) {
            throw new InvalidDataAccessApiUsageException("UpperBound can't be null");
        }
        queryCriteria.add(new CriteriaEntry(this.field, OperationKey.LESS_EQUAL, upperBound));
        return this;
    }

    /**
     * Creates new CriteriaEntry for {@code < upperBound}
     *
     * @param upperBound
     * @return
     */
    public Criteria lessThan(Comparable<?> upperBound) {
        if (upperBound == null) {
            throw new InvalidDataAccessApiUsageException("UpperBound can't be null");
        }
        queryCriteria.add(new CriteriaEntry(this.field, OperationKey.LESS, upperBound));
        return this;
    }

    /**
     * Creates new CriteriaEntry for {@code >= lowerBound}
     *
     * @param lowerBound
     * @return
     */
    public Criteria greaterThanEqual(Comparable<?> lowerBound) {
        if (lowerBound == null) {
            throw new InvalidDataAccessApiUsageException("LowerBound can't be null");
        }
        queryCriteria.add(new CriteriaEntry(this.field, OperationKey.GREATER_EQUAL, lowerBound));
        return this;
    }

    /**
     * Creates new CriteriaEntry for {@code > lowerBound}
     *
     * @param lowerBound
     * @return
     */
    public Criteria greaterThan(Comparable<?> lowerBound) {
        if (lowerBound == null) {
            throw new InvalidDataAccessApiUsageException("LowerBound can't be null");
        }
        queryCriteria.add(new CriteriaEntry(this.field, OperationKey.GREATER, lowerBound));
        return this;
    }

    /**
     * Creates new CriteriaEntry for multiple values {@code (arg0 arg1 arg2 ...)}
     *
     * @param values
     * @return
     */
    public Criteria in(Object... values) {
        return in(toCollection(values));
    }

    /**
     * Creates new CriteriaEntry for multiple values {@code (arg0 arg1 arg2 ...)}
     *
     * @param values the collection containing the values to match against
     * @return
     */
    public Criteria in(Iterable<?> values) {
        Assert.notNull(values, "Collection of 'in' values must not be null");
        queryCriteria.add(new CriteriaEntry(this.field, OperationKey.IN, values));
        return this;
    }

    /**
     * @param values
     * @return
     */
    private List<Object> toCollection(Object... values) {
        if (values.length == 0 || (values.length > 1 && values[1] instanceof Collection)) {
            throw new InvalidDataAccessApiUsageException("At least one element "
                    + (values.length > 0 ? ("of argument of type " + values[1].getClass().getName()) : "")
                    + " has to be present.");
        }
        return Arrays.asList(values);
    }

    /**
     * Creates new CriteriaEntry for multiple values {@code (arg0 arg1 arg2 ...)}
     *
     * @param values
     * @return
     */
    public Criteria notIn(Object... values) {
        return notIn(toCollection(values));
    }

    /**
     * Creates new CriteriaEntry for multiple values {@code (arg0 arg1 arg2 ...)}
     *
     * @param values
     * @return
     */
    public Criteria notIn(Iterable<?> values) {
        Assert.notNull(values, "Collection of 'NotIn' values must not be null");
        queryCriteria.add(new CriteriaEntry(this.field, OperationKey.NOT_IN, values));
        return this;
    }

    /**
     * Field targeted by this Criteria
     *
     * @return
     */
    public Field getField() {
        return this.field;
    }

    /**
     * @return all Criteria chained
     */
    public List<Criteria> getCriteriaChain() {
        return Collections.unmodifiableList(this.criteriaChained);
    }

    /**
     * @return all criteria query
     */
    public List<CriteriaEntry> getQueryCriteriaEntries() {
        return Collections.unmodifiableList(this.queryCriteria);
    }

    public boolean isAnd() {
        return AND_OPERATOR.equals(this.conjunctionOperator);
    }

    public boolean isOr() {
        return OR_OPERATOR.equals(this.conjunctionOperator);
    }


    protected void assertNoBlankQuery(String searchString) {
        if (StringUtils.isEmpty(searchString)) {
            throw new InvalidDataAccessApiUsageException("Cannot constructQuery with null expression");
        }
    }

    /**
     * Add criteria query
     *
     * @param entry criteria
     */
    protected void addQueryCriteria(CriteriaEntry entry) {
        this.queryCriteria.add(entry);
    }

    /**
     * Add criterias
     *
     * @param entries criteria list
     */
    protected void addAllQueryCriteria(Collection<CriteriaEntry> entries) {
        this.queryCriteria.addAll(entries);
    }

    /**
     *
     */
    public enum OperationKey {
        EQUALS, NOT_EQUALS, CONTAINS, STARTS_WITH, ENDS_WITH, BETWEEN, IN, NOT_IN, LESS, LESS_EQUAL, GREATER, GREATER_EQUAL, NULL, NOT_NULL;
    }


    /**
     *
     */
    @Getter
    public static class CriteriaEntry {

        private Field field;
        private OperationKey key;
        private Object value;

        public CriteriaEntry(Field field, OperationKey key, Object value) {
            this.field = field;
            this.key = key;
            this.value = value;
        }


        @Override
        public String toString() {
            return "CriteriaEntry{" +
                    "field=" + field.getName() +
                    ", key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

}
