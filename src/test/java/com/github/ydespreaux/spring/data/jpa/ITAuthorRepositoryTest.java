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

package com.github.ydespreaux.spring.data.jpa;

import com.github.ydespreaux.spring.data.jpa.configuration.JpaConfiguration;
import com.github.ydespreaux.spring.data.jpa.configuration.entities.Author;
import com.github.ydespreaux.spring.data.jpa.query.Criteria;
import com.github.ydespreaux.spring.data.jpa.query.QueryOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JpaConfiguration.class)
public class ITAuthorRepositoryTest extends AbstractJpaCriteriaRepositoryTest {

    @Before
    public void onSetup() {
        cleanData();
        insertData();
    }

    @Test
    public void countByBookDescription() {
        long count = this.authorRepository.count(new Criteria("books.description").contains("Norvège"));
        assertThat(count, is(equalTo(2L)));
    }

    @Test
    public void findAllByBookDescription() {
        List<Author> authors = this.authorRepository.findAll(new Criteria("books.description").contains("Norvège"));
        assertThat(authors.size(), is(equalTo(2)));
        assertThatAuthor(authors.get(0), this.nicolasBeuglet);
        assertThatAuthor(authors.get(1), this.nicolasBeuglet);
    }

    @Test
    public void findAllByBookDescriptionWithPageable() {
        Page<Author> result = this.authorRepository.findAll(new Criteria("books.description").contains("Norvège"), createPageable(0, 5));
        assertThat(result.getTotalElements(), is(equalTo(2L)));
        assertThatAuthor(result.getContent().get(0), this.nicolasBeuglet);
        assertThatAuthor(result.getContent().get(1), this.nicolasBeuglet);
    }

    @Test
    public void countByBookDescriptionWithDistinct() {
        long count = this.authorRepository.count(new Criteria("books.description").contains("Norvège"), new QueryOptions().distinct(true));
        assertThat(count, is(equalTo(1L)));
    }

    @Test
    public void findAllByBookTitleWithDistinct() {
        List<Author> authors = this.authorRepository.findAll(new Criteria("books.description").contains("Norvège"), new QueryOptions().distinct(true));
        assertThat(authors.size(), is(equalTo(1)));
        assertThatAuthor(authors.get(0), this.nicolasBeuglet);
    }

    @Test
    public void findAllByBookDescriptionWithDistinctAndPageable() {
        Page<Author> result = this.authorRepository.findAll(new Criteria("books.description").contains("Norvège"), createPageable(0, 5), new QueryOptions().distinct(true));
        assertThat(result.getTotalElements(), is(equalTo(1L)));
        assertThatAuthor(result.getContent().get(0), this.nicolasBeuglet);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void findAllWithAssociation() {
        List<Author> result = this.authorRepository.findAll(null, new QueryOptions().withAssociation("books"));
        assertThat(result.size(), is(equalTo(3)));
        assertThat(result.contains(this.elenaFerrante), is(true));
        assertThat(result.contains(this.harlanCoben), is(true));
        assertThat(result.contains(this.nicolasBeuglet), is(true));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void findAllWithAssociationAndSort() {
        List<Author> result = this.authorRepository.findAll(null, Sort.by(Sort.Direction.ASC, "lastName"), new QueryOptions().withAssociation("books"));
        assertThat(result.size(), is(equalTo(3)));
        assertThatAuthor(result.get(0), this.nicolasBeuglet);
        assertThatAuthor(result.get(1), this.harlanCoben);
        assertThatAuthor(result.get(2), this.elenaFerrante);
    }

    @Test
    public void countWithAssociation() {
        Long count = this.authorRepository.count(null, new QueryOptions().withAssociation("books"));
        assertThat(count, is(equalTo(3L)));
    }

    /**
     * @param actual
     * @param expected
     */
    private void assertThatAuthor(Author actual, Author expected) {
        assertThat(expected.getFirstName(), is(equalTo(actual.getFirstName())));
        assertThat(expected.getLastName(), is(equalTo(actual.getLastName())));
        assertThat(expected.getId(), is(equalTo(actual.getId())));
    }

    /**
     * @return
     */
    private Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
    }

}
