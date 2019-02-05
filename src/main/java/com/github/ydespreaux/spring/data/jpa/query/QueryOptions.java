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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Yoann Despréaux
 * @since 1.1.0
 */
public class QueryOptions {

    public static final QueryOptions DEFAULT = new QueryOptions();
    @Getter
    private final Set<String> associations = new HashSet<>();
    @Getter
    private boolean distinct = false;

    public QueryOptions distinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    public QueryOptions withAssociation(String association) {
        this.associations.add(association);
        return this;
    }

    public QueryOptions withAssociation(String... associations) {
        this.associations.addAll(Arrays.asList(associations));
        return this;
    }

    /**
     * @return
     */
    public boolean hasAssocations() {
        return !this.associations.isEmpty();
    }
}
