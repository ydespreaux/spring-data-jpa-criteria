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

package com.github.ydespreaux.spring.data.jpa;

import com.github.ydespreaux.spring.data.jpa.configuration.JpaConfiguration;
import com.github.ydespreaux.spring.data.jpa.configuration.entities.Author;
import com.github.ydespreaux.spring.data.jpa.query.Criteria;
import com.github.ydespreaux.spring.data.jpa.query.QueryOptions;
import org.junit.Assert;
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
        Assert.assertThat(count, is(equalTo(2L)));
    }

    @Test
    public void findAllByBookDescription() {
        List<Author> authors = this.authorRepository.findAll(new Criteria("books.description").contains("Norvège"));
        Assert.assertThat(authors.size(), is(equalTo(2)));
        assertThat(authors.get(0), this.nicolasBeuglet);
        assertThat(authors.get(1), this.nicolasBeuglet);
    }

    @Test
    public void findAllByBookDescriptionWithPageable() {
        Page<Author> result = this.authorRepository.findAll(new Criteria("books.description").contains("Norvège"), createPageable(0, 5));
        Assert.assertThat(result.getTotalElements(), is(equalTo(2L)));
        assertThat(result.getContent().get(0), this.nicolasBeuglet);
        assertThat(result.getContent().get(1), this.nicolasBeuglet);
    }

    @Test
    public void countByBookDescriptionWithDistinct() {
        long count = this.authorRepository.count(new Criteria("books.description").contains("Norvège"), new QueryOptions().distinct(true));
        Assert.assertThat(count, is(equalTo(1L)));
    }

    @Test
    public void findAllByBookTitleWithDistinct() {
        List<Author> authors = this.authorRepository.findAll(new Criteria("books.description").contains("Norvège"), new QueryOptions().distinct(true));
        Assert.assertThat(authors.size(), is(equalTo(1)));
        assertThat(authors.get(0), this.nicolasBeuglet);
    }

    @Test
    public void findAllByBookDescriptionWithDistinctAndPageable() {
        Page<Author> result = this.authorRepository.findAll(new Criteria("books.description").contains("Norvège"), createPageable(0, 5), new QueryOptions().distinct(true));
        Assert.assertThat(result.getTotalElements(), is(equalTo(1L)));
        assertThat(result.getContent().get(0), this.nicolasBeuglet);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void findAllWithAssociation() {
        List<Author> result = this.authorRepository.findAll(null, new QueryOptions().withAssociation("books"));
        Assert.assertThat(result.size(), is(equalTo(3)));
        Assert.assertThat(result.contains(this.elenaFerrante), is(true));
        Assert.assertThat(result.contains(this.harlanCoben), is(true));
        Assert.assertThat(result.contains(this.nicolasBeuglet), is(true));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void findAllWithAssociationAndSort() {
        List<Author> result = this.authorRepository.findAll(null, Sort.by(Sort.Direction.ASC, "lastName"), new QueryOptions().withAssociation("books"));
        Assert.assertThat(result.size(), is(equalTo(3)));
        assertThat(result.get(0), this.nicolasBeuglet);
        assertThat(result.get(1), this.harlanCoben);
        assertThat(result.get(2), this.elenaFerrante);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void findAllWithAllAssociationAndSort() {
        List<Author> result = this.authorRepository.findAll(null, Sort.by(Sort.Direction.ASC, "lastName"), new QueryOptions().withAssociation("books", "books.editor"));
        Assert.assertThat(result.size(), is(equalTo(3)));
        assertThat(result.get(0), this.nicolasBeuglet);
        assertThat(result.get(1), this.harlanCoben);
        assertThat(result.get(2), this.elenaFerrante);
    }

    @Test
    public void countWithAssociation() {
        Long count = this.authorRepository.count(null, new QueryOptions().withAssociation("books"));
        Assert.assertThat(count, is(equalTo(3L)));
    }

    /**
     * @param actual
     * @param expected
     */
    private void assertThat(Author actual, Author expected) {
        Assert.assertThat(expected.getFirstName(), is(equalTo(actual.getFirstName())));
        Assert.assertThat(expected.getLastName(), is(equalTo(actual.getLastName())));
        Assert.assertThat(expected.getId(), is(equalTo(actual.getId())));
    }

    /**
     * @return
     */
    private Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
    }

}
