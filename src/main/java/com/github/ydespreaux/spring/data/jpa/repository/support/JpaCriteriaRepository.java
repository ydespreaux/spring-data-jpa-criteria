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

import com.github.ydespreaux.spring.data.jpa.ModelConverter;
import com.github.ydespreaux.spring.data.jpa.query.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;
import java.util.Optional;

/**
 * @param <T> Entity type
 * @param <K> Identifier type
 * @author Yoann Despréaux
 * @since 0.0.3
 */
public interface JpaCriteriaRepository<T, K> extends JpaRepositoryImplementation<T, K> {

    /**
     * @param criteria
     * @return
     */
    Long count(Criteria criteria);

    /**
     * @param criteria
     * @return
     */
    List<T> findAll(Criteria criteria);

    /**
     * @param criteria
     * @param pageable
     * @return
     */
    Page<T> findAll(Criteria criteria, Pageable pageable);

    /**
     * @param criteria
     * @param converter
     * @param fetchAssociation
     * @param <D>
     * @return
     */
    <D> List<D> findAll(Criteria criteria, ModelConverter<D, T> converter, String... fetchAssociation);

    /**
     * @param criteria
     * @param pageable
     * @param converter
     * @param fetchAssociation
     * @param <D>
     * @return
     */
    <D> Page<D> findAll(Criteria criteria, Pageable pageable, ModelConverter<D, T> converter, String... fetchAssociation);

    /**
     * @param id
     * @param converter
     * @param fetchAssociation
     * @param <D>
     * @return
     */
    <D> Optional<D> findById(K id, ModelConverter<D, T> converter, String... fetchAssociation);
}
