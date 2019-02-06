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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Yoann Despréaux
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
public class CriteriaTest {

    @Test
    public void eqNot() {
        Criteria criteria = new Criteria("field").notEq("value");
        assertThat(criteria.toString(), is(equalTo("(field NOT_EQUALS value)")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.NOT_EQUALS)));
        assertThat(entries.get(0).getValue(), is(equalTo("value")));
    }

    @Test
    public void eq() {
        Criteria criteria = new Criteria("field").eq("value");
        assertThat(criteria.toString(), is(equalTo("(field EQUALS value)")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.EQUALS)));
        assertThat(entries.get(0).getValue(), is(equalTo("value")));
    }

    @Test
    public void isNull() {
        Criteria criteria = new Criteria("field").isNull();
        assertThat(criteria.toString(), is(equalTo("(field NULL)")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.NULL)));
        assertThat(entries.get(0).getValue(), is(nullValue()));
    }

    @Test
    public void isNotNull() {
        Criteria criteria = new Criteria("field").isNotNull();
        assertThat(criteria.toString(), is(equalTo("(field NOT_NULL)")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.NOT_NULL)));
        assertThat(entries.get(0).getValue(), is(nullValue()));
    }

    @Test
    public void between() {
        Criteria criteria = new Criteria("field").between("value1", "value2");
        assertThat(criteria.toString(), is(equalTo("(field BETWEEN value1 to value2)")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.BETWEEN)));
        assertThat(entries.get(0).getValue(), is(equalTo(new Object[]{"value1", "value2"})));
    }

    @Test
    public void contains() {
        Criteria criteria = new Criteria("field").contains("value");
        assertThat(criteria.toString(), is(equalTo("(field CONTAINS value)")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.CONTAINS)));
        assertThat(entries.get(0).getValue(), is(equalTo("value")));
    }

    @Test
    public void endsWith() {
        Criteria criteria = new Criteria("field").endsWith("value");
        assertThat(criteria.toString(), is(equalTo("(field ENDS_WITH value)")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.ENDS_WITH)));
        assertThat(entries.get(0).getValue(), is(equalTo("value")));
    }

    @Test
    public void startsWith() {
        Criteria criteria = new Criteria("field").startsWith("value");
        assertThat(criteria.toString(), is(equalTo("(field STARTS_WITH value)")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.STARTS_WITH)));
        assertThat(entries.get(0).getValue(), is(equalTo("value")));
    }

    @Test
    public void greaterThan() {
        Criteria criteria = new Criteria("field").greaterThan(10);
        assertThat(criteria.toString(), is(equalTo("(field GREATER 10)")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.GREATER)));
        assertThat(entries.get(0).getValue(), is(equalTo(10)));
    }

    @Test
    public void greaterThanEqual() {
        Criteria criteria = new Criteria("field").greaterThanEqual(10);
        assertThat(criteria.toString(), is(equalTo("(field GREATER_EQUAL 10)")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.GREATER_EQUAL)));
        assertThat(entries.get(0).getValue(), is(equalTo(10)));
    }

    @Test
    public void lessThan() {
        Criteria criteria = new Criteria("field").lessThan(10);
        assertThat(criteria.toString(), is(equalTo("(field LESS 10)")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.LESS)));
        assertThat(entries.get(0).getValue(), is(equalTo(10)));
    }

    @Test
    public void lessThanEqual() {
        Criteria criteria = new Criteria("field").lessThanEqual(10);
        assertThat(criteria.toString(), is(equalTo("(field LESS_EQUAL 10)")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.LESS_EQUAL)));
        assertThat(entries.get(0).getValue(), is(equalTo(10)));
    }

    @Test
    public void inCriteriaWithObjects() {
        Criteria criteria = new Criteria("field").in("value1", "value2");
        assertThat(criteria.toString(), is(equalTo("(field IN [value1, value2])")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.IN)));
        assertThat(((List<Object>) entries.get(0).getValue()).get(0), is(equalTo("value1")));
        assertThat(((List<Object>) entries.get(0).getValue()).get(1), is(equalTo("value2")));
    }

    @Test
    public void inCriteriaWithIterable() {
        Iterable<String> values = Arrays.asList("value1", "value2");
        Criteria criteria = new Criteria("field").in(values);
        assertThat(criteria.toString(), is(equalTo("(field IN [value1, value2])")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.IN)));
        assertThat(entries.get(0).getValue(), is(equalTo(values)));
    }

    @Test
    public void notInCriteriaWithObjects() {
        Criteria criteria = new Criteria("field").notIn("value1", "value2");
        assertThat(criteria.toString(), is(equalTo("(field NOT_IN [value1, value2])")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.NOT_IN)));
        assertThat(((List<Object>) entries.get(0).getValue()).get(0), is(equalTo("value1")));
        assertThat(((List<Object>) entries.get(0).getValue()).get(1), is(equalTo("value2")));
    }

    @Test
    public void notInCriteriaWithIterable() {
        Iterable<String> values = Arrays.asList("value1", "value2");
        Criteria criteria = new Criteria("field").notIn(values);
        assertThat(criteria.toString(), is(equalTo("(field NOT_IN [value1, value2])")));
        assertThat(criteria.getField().getName(), is(equalTo("field")));
        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.NOT_IN)));
        assertThat(entries.get(0).getValue(), is(equalTo(values)));
    }

    @Test
    public void simpleCriteria() {
        Criteria criteria = new Criteria("field").eq("value");
        assertThat(criteria.toString(), is(equalTo("(field EQUALS value)")));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.EQUALS)));
        assertThat(entries.get(0).getValue(), is(equalTo("value")));
    }

    @Test
    public void multiAndConjunctionCriteria() {
        Criteria criteria = new Criteria("field_1").eq("value_1")
                .and("field_2").notEq("value_2")
                .and("field_3").contains("value_3");
        assertThat(criteria.toString(), is(equalTo("(field_1 EQUALS value_1) AND (field_2 NOT_EQUALS value_2) AND (field_3 CONTAINS value_3)")));

        assertThat(criteria.isAnd(), is(true));
        assertThat(criteria.isOr(), is(false));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));

        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.get(0).getField().getName(), is(equalTo("field_1")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.EQUALS)));
        assertThat(entries.get(0).getValue(), is(equalTo("value_1")));

        assertThat(entries.get(1).getField().getName(), is(equalTo("field_2")));
        assertThat(entries.get(1).getKey(), is(equalTo(Criteria.OperationKey.NOT_EQUALS)));
        assertThat(entries.get(1).getValue(), is(equalTo("value_2")));

        assertThat(entries.get(2).getField().getName(), is(equalTo("field_3")));
        assertThat(entries.get(2).getKey(), is(equalTo(Criteria.OperationKey.CONTAINS)));
        assertThat(entries.get(2).getValue(), is(equalTo("value_3")));
    }

    @Test
    public void multiOrConjunctionCriteria() {
        Criteria criteria = new Criteria("field_1").eq("value_1")
                .or("field_2").notEq("value_2")
                .or("field_3").contains("value_3");
        assertThat(criteria.toString(), is(equalTo("(field_1 EQUALS value_1) OR (field_2 NOT_EQUALS value_2) OR (field_3 CONTAINS value_3)")));

        assertThat(criteria.isAnd(), is(false));
        assertThat(criteria.isOr(), is(true));
        assertThat(criteria.getCriteriaChain().isEmpty(), is(true));

        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();

        assertThat(entries.size(), is(equalTo(3)));

        assertThat(entries.get(0).getField().getName(), is(equalTo("field_1")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.EQUALS)));
        assertThat(entries.get(0).getValue(), is(equalTo("value_1")));

        assertThat(entries.get(1).getField().getName(), is(equalTo("field_2")));
        assertThat(entries.get(1).getKey(), is(equalTo(Criteria.OperationKey.NOT_EQUALS)));
        assertThat(entries.get(1).getValue(), is(equalTo("value_2")));

        assertThat(entries.get(2).getField().getName(), is(equalTo("field_3")));
        assertThat(entries.get(2).getKey(), is(equalTo(Criteria.OperationKey.CONTAINS)));
        assertThat(entries.get(2).getValue(), is(equalTo("value_3")));
    }

    @Test
    public void combinedCriteria_0() {
        Criteria criteria = new Criteria("attribut1").eq("value1")
                .or(new Criteria("attribut_2").notEq("value2").and("attribut_3").contains("value3"));
        assertThat(criteria.toString(), is(equalTo("((attribut1 EQUALS value1)) OR ((attribut_2 NOT_EQUALS value2) AND (attribut_3 CONTAINS value3))")));
        assertThat(criteria.getField().getName(), is(equalTo("attribut_3")));
        assertThat(criteria.isOr(), is(true));

        List<Criteria> criterias = criteria.getCriteriaChain();
        assertThat(criterias.size(), is(equalTo(2)));
        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.isEmpty(), is(true));

        Criteria criteria_1 = criterias.get(0);
        assertThat(criteria_1.getField().getName(), is(equalTo("attribut1")));
        assertThat(criteria_1.isAnd(), is(true));
        List<Criteria.CriteriaEntry> entries_1 = criteria_1.getQueryCriteriaEntries();
        assertThat(entries_1.size(), is(equalTo(1)));
        assertThat(entries_1.get(0).getField().getName(), is(equalTo("attribut1")));
        assertThat(entries_1.get(0).getKey(), is(equalTo(Criteria.OperationKey.EQUALS)));
        assertThat(entries_1.get(0).getValue(), is(equalTo("value1")));

        Criteria criteria_2 = criterias.get(1);
        assertThat(criteria_2.getField().getName(), is(equalTo("attribut_3")));
        assertThat(criteria_2.isAnd(), is(true));
        List<Criteria.CriteriaEntry> entries_2 = criteria_2.getQueryCriteriaEntries();
        assertThat(entries_2.size(), is(equalTo(2)));
        assertThat(entries_2.get(0).getField().getName(), is(equalTo("attribut_2")));
        assertThat(entries_2.get(0).getKey(), is(equalTo(Criteria.OperationKey.NOT_EQUALS)));
        assertThat(entries_2.get(0).getValue(), is(equalTo("value2")));
        assertThat(entries_2.get(1).getField().getName(), is(equalTo("attribut_3")));
        assertThat(entries_2.get(1).getKey(), is(equalTo(Criteria.OperationKey.CONTAINS)));
        assertThat(entries_2.get(1).getValue(), is(equalTo("value3")));
    }

    @Test
    public void combinedCriteria_1() {
        Criteria criteria = new Criteria("field_1").eq("value_1")
                .or("field_2").eq("value_2")
                .and("field_3").eq("value_3");
        assertThat(criteria.toString(), is(equalTo("((field_1 EQUALS value_1) OR (field_2 EQUALS value_2)) AND (field_3 EQUALS value_3)")));
        assertThat(criteria.getField().getName(), is(equalTo("field_3")));
        assertThat(criteria.isAnd(), is(true));

        List<Criteria> criterias = criteria.getCriteriaChain();
        assertThat(criterias.size(), is(equalTo(1)));
        assertThat(criterias.get(0).getField().getName(), is(equalTo("field_2")));
        assertThat(criterias.get(0).isOr(), is(true));
        List<Criteria.CriteriaEntry> entries_1 = criterias.get(0).getQueryCriteriaEntries();
        assertThat(entries_1.size(), is(equalTo(2)));
        assertThat(entries_1.get(0).getField().getName(), is(equalTo("field_1")));
        assertThat(entries_1.get(0).getKey(), is(equalTo(Criteria.OperationKey.EQUALS)));
        assertThat(entries_1.get(0).getValue(), is(equalTo("value_1")));
        assertThat(entries_1.get(1).getField().getName(), is(equalTo("field_2")));
        assertThat(entries_1.get(1).getKey(), is(equalTo(Criteria.OperationKey.EQUALS)));
        assertThat(entries_1.get(1).getValue(), is(equalTo("value_2")));

        List<Criteria.CriteriaEntry> entries = criteria.getQueryCriteriaEntries();
        assertThat(entries.size(), is(equalTo(1)));
        assertThat(entries.get(0).getField().getName(), is(equalTo("field_3")));
        assertThat(entries.get(0).getKey(), is(equalTo(Criteria.OperationKey.EQUALS)));
        assertThat(entries.get(0).getValue(), is(equalTo("value_3")));

    }

}
