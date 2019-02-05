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

package com.github.ydespreaux.spring.data.jpa.repository.support;

import com.github.ydespreaux.spring.data.jpa.query.Criteria;
import com.github.ydespreaux.spring.data.jpa.query.QueryOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * *
 *
 * @param <T>
 * @author Yoann Despréaux
 * @since 1.1.0
 */
public interface JpaCriteriaExecutor<T> {

    /**
     * @param criteria
     * @return
     */
    default Optional<T> findOne(@Nullable Criteria criteria) {
        return findOne(criteria, QueryOptions.DEFAULT);
    }

    /**
     * @param criteria
     * @param options
     * @return
     */
    Optional<T> findOne(@Nullable Criteria criteria, QueryOptions options);

    /**
     * @param criteria
     * @return
     */
    default List<T> findAll(@Nullable Criteria criteria) {
        return findAll(criteria, QueryOptions.DEFAULT);
    }

    /**
     * @param criteria
     * @param options
     * @return
     */
    List<T> findAll(@Nullable Criteria criteria, QueryOptions options);

    /**
     * @param criteria
     * @param pageable
     * @return
     */
    default Page<T> findAll(@Nullable Criteria criteria, Pageable pageable) {
        return findAll(criteria, pageable, QueryOptions.DEFAULT);
    }

    /**
     * @param criteria
     * @param pageable
     * @param options
     * @return
     */
    Page<T> findAll(@Nullable Criteria criteria, Pageable pageable, QueryOptions options);

    /**
     * @param criteria
     * @param sort
     * @return
     */
    default List<T> findAll(@Nullable Criteria criteria, Sort sort) {
        return findAll(criteria, sort, QueryOptions.DEFAULT);
    }

    /**
     * @param criteria
     * @param sort
     * @param options
     * @return
     */
    List<T> findAll(@Nullable Criteria criteria, Sort sort, QueryOptions options);

    /**
     * @param criteria
     * @return
     */
    default long count(@Nullable Criteria criteria) {
        return count(criteria, QueryOptions.DEFAULT);
    }

    /**
     * @param criteria
     * @param options
     * @return
     */
    long count(@Nullable Criteria criteria, QueryOptions options);
}
