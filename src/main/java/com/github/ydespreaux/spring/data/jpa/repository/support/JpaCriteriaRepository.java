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

package com.github.ydespreaux.spring.data.jpa.repository.support;

import com.github.ydespreaux.spring.data.jpa.query.Criteria;
import com.github.ydespreaux.spring.data.jpa.query.QueryOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.List;

/**
 * @param <T> Entity type
 * @param <K> Identifier type
 * @author Yoann Despréaux
 * @since 1.0.0
 */
public interface JpaCriteriaRepository<T, K extends Serializable> extends JpaRepository<T, K>, JpaSpecificationExecutor<T> {
    /**
     * @param criteria
     * @return
     */
    default T findOne(Criteria criteria) {
        return findOne(criteria, QueryOptions.DEFAULT);
    }

    /**
     * @param criteria
     * @param options
     * @return
     */
    T findOne(Criteria criteria, QueryOptions options);

    /**
     * @param criteria
     * @return
     */
    default List<T> findAll(Criteria criteria) {
        return findAll(criteria, QueryOptions.DEFAULT);
    }

    /**
     * @param criteria
     * @param options
     * @return
     */
    List<T> findAll(Criteria criteria, QueryOptions options);

    /**
     * @param criteria
     * @param pageable
     * @return
     */
    default Page<T> findAll(Criteria criteria, Pageable pageable) {
        return findAll(criteria, pageable, QueryOptions.DEFAULT);
    }

    /**
     * @param criteria
     * @param pageable
     * @param options
     * @return
     */
    Page<T> findAll(Criteria criteria, Pageable pageable, QueryOptions options);

    /**
     * @param criteria
     * @param sort
     * @return
     */
    default List<T> findAll(Criteria criteria, Sort sort) {
        return findAll(criteria, sort, QueryOptions.DEFAULT);
    }

    /**
     * @param criteria
     * @param sort
     * @param options
     * @return
     */
    List<T> findAll(Criteria criteria, Sort sort, QueryOptions options);

    /**
     * @param criteria
     * @return
     */
    default long count(Criteria criteria) {
        return count(criteria, QueryOptions.DEFAULT);
    }

    /**
     * @param criteria
     * @param options
     * @return
     */
    long count(Criteria criteria, QueryOptions options);

}
