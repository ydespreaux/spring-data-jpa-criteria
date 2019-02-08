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
import com.github.ydespreaux.spring.data.jpa.query.SpecificationCriteria;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.*;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

/**
 * @param <T>
 * @author Yoann Despréaux
 * @since 1.0.0
 */
public class SimpleJpaCriteriaRepository<T, K extends Serializable> extends SimpleJpaRepository<T, K> implements JpaCriteriaRepository<T, K> {

    protected final EntityManager em;

    /**
     * @param entityInformation
     * @param em
     */
    public SimpleJpaCriteriaRepository(JpaEntityInformation<T, ?> entityInformation, final EntityManager em) {
        super(entityInformation, em);
        this.em = em;
    }

    /**
     * @param domainClass
     * @param em
     */
    public SimpleJpaCriteriaRepository(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    /**
     *
     * @param query
     * @return
     */
    private static long executeCountQuery(TypedQuery<Long> query) {
        Assert.notNull(query, "TypedQuery must not be null!");
        List<Long> totals = query.getResultList();
        long total = 0L;

        Long element;
        for (Iterator var4 = totals.iterator(); var4.hasNext(); total += element == null ? 0L : element) {
            element = (Long) var4.next();
        }

        return total;
    }

    /**
     * @param criteria
     * @return
     */
    @Override
    public long count(Criteria criteria, QueryOptions options) {
        return executeCountQuery(this.getCountQuery(new SpecificationCriteria<>(criteria), this.getDomainClass(), options));
    }

    @Override
    public T findOne(Criteria criteria, QueryOptions options) {
        try {
            TypedQuery<T> query = this.getTypedQuery(new SpecificationCriteria<>(criteria), null, options);
            return query.getSingleResult();
        } catch (NoResultException var3) {
            return null;
        }
    }

    /**
     * @param criteria
     * @return
     */
    @Override
    public List<T> findAll(Criteria criteria, QueryOptions options) {
        return this.findAll(criteria, (Sort) null, options);
    }

    /**
     *
     * @param criteria
     * @param sort
     * @return
     */
    @Override
    public List<T> findAll(Criteria criteria, Sort sort, QueryOptions options) {
        TypedQuery<T> query = this.getTypedQuery(new SpecificationCriteria<>(criteria), sort, options);
        return query.getResultList();
    }

    /**
     * @param criteria
     * @param pageable
     * @return
     */
    @Override
    public Page<T> findAll(Criteria criteria, Pageable pageable, QueryOptions options) {
        Specification<T> specification = new SpecificationCriteria<>(criteria);
        TypedQuery<T> query = this.getTypedQuery(specification, pageable == null ? null : pageable.getSort(), options);
        return (Page) (pageable == null ? new PageImpl(query.getResultList()) : this.readPage(query, this.getDomainClass(), pageable, specification, options));
    }

    /**
     * @param spec
     * @param sort
     * @return
     */
    private TypedQuery<T> getTypedQuery(Specification<T> spec, Sort sort, QueryOptions options) {
        return this.getTypedQuery(spec, getDomainClass(), sort, options);
    }

    /**
     * @param spec
     * @param domainClass
     * @param sort
     * @return
     */
    private <S extends T> TypedQuery<S> getTypedQuery(Specification<S> spec, Class<S> domainClass, Sort sort, QueryOptions options) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<S> criteriaQuery = builder.createQuery(domainClass);
        Root<S> root = applySpecification(spec, domainClass, criteriaQuery);
        criteriaQuery.select(root);
        if (options.isDistinct()) {
            criteriaQuery.distinct(true);
        }
        if (sort != null) {
            criteriaQuery.orderBy(toOrders(sort, root, builder));
        }
        TypedQuery<S> query = this.applyMetadata(this.em.createQuery(criteriaQuery));
        if (options.hasAssocations()) {
            query.setHint(QueryHints.LOADGRAPH, applyFetchAssociations(domainClass, options.getAssociations()));
        }
        return query;

    }

    /**
     *
     * @param query
     * @param <S>
     * @return
     */
    private <S> TypedQuery<S> applyMetadata(TypedQuery<S> query) {
        CrudMethodMetadata metadata = getRepositoryMethodMetadata();
        if (metadata == null) {
            return query;
        } else {
            LockModeType type = metadata.getLockModeType();
            return type == null ? query : query.setLockMode(type);
        }
    }

    /**
     * @param spec
     * @param domainClass
     * @param query
     * @param <S>
     * @param <U>
     * @return
     */
    private <S, U extends T> Root<U> applySpecification(Specification<U> spec, Class<U> domainClass, CriteriaQuery<S> query) {
        Assert.notNull(domainClass, "Domain class must not be null!");
        Assert.notNull(query, "CriteriaQuery must not be null!");
        Root<U> root = query.from(domainClass);
        if (spec == null) {
            return root;
        }
        CriteriaBuilder builder = em.getCriteriaBuilder();
        Predicate predicate = spec.toPredicate(root, query, builder);
        if (predicate != null) {
            query.where(predicate);
        }
        return root;
    }

    /**
     *
     * @param query
     * @param domainClass
     * @param pageable
     * @param spec
     * @param options
     * @param <S>
     * @return
     */
    private <S extends T> Page<S> readPage(TypedQuery<S> query, Class<S> domainClass, Pageable pageable, Specification<S> spec, QueryOptions options) {
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return PageableExecutionUtils.getPage(query.getResultList(), pageable, () -> executeCountQuery(this.getCountQuery(spec, domainClass, options)));
    }

    /**
     *
     * @param spec
     * @param domainClass
     * @param options
     * @param <S>
     * @return
     */
    private <S extends T> TypedQuery<Long> getCountQuery(Specification<S> spec, Class<S> domainClass, QueryOptions options) {
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<S> root = this.applySpecification(spec, domainClass, query);
        if (options.isDistinct()) {
            query.select(builder.countDistinct(root));
        } else {
            query.select(builder.count(root));
        }
        query.orderBy(Collections.emptyList());
        return this.em.createQuery(query);
    }

    /**
     *
     * @param domainClass
     * @param associations
     * @param <S>
     * @return
     */
    private <S extends T> EntityGraph<S> applyFetchAssociations(Class<S> domainClass, Set<String> associations) {
        final EntityGraph<S> fetchGraph = em.createEntityGraph(domainClass);
        Map<String, Subgraph<?>> joinMap = new HashMap<>();
        for (String association : associations) {
            String[] fields = association.split("\\.");
            applyFetchAssociation(fetchGraph, fields, joinMap);
        }
        return fetchGraph;
    }

    /**
     *
     * @param joinMap
     * @param root
     * @param fields
     */
    private void applyFetchAssociation(EntityGraph<?> root, String[] fields, Map<String, Subgraph<?>> joinMap) {
        StringBuilder path = new StringBuilder();
        Subgraph<?> currentSubGraph = null;
        for (int i = 0; i < fields.length; i++) {
            String attribut = fields[i];
            if (path.length() > 0) {
                path.append(".");
            }
            path.append(attribut);
            String keyGraph = path.toString();
            if (!joinMap.containsKey(keyGraph)) {
                joinMap.put(keyGraph, currentSubGraph != null ? currentSubGraph.addSubgraph(attribut) : root.addSubgraph(attribut));
            }
            currentSubGraph = joinMap.get(keyGraph);
        }
    }

}
