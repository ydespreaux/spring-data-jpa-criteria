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
import com.github.ydespreaux.spring.data.jpa.configuration.repository.AuthorRepository;
import com.github.ydespreaux.spring.data.jpa.query.Criteria;
import com.github.ydespreaux.spring.data.jpa.query.QueryOptions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JpaConfiguration.class)
public class ITAuthorRepositoryTest {

    @Autowired
    protected AuthorRepository authorRepository;

    @Test
    public void countByBookDescription() {
        long count = this.authorRepository.count(new Criteria("books.description").contains("Norvege"));
        Assert.assertThat(count, is(equalTo(2L)));
    }

    @Test
    public void findAllByBookDescription() {
        List<Author> result = this.authorRepository.findAll(new Criteria("books.description").contains("Norvege"));
        Assert.assertThat(result.size(), is(equalTo(2)));
        Assert.assertThat(result.get(0).getLastName(), is(equalTo(ITSuiteTest.nicolasBeuglet)));
        Assert.assertThat(result.get(1).getLastName(), is(equalTo(ITSuiteTest.nicolasBeuglet)));
    }

    @Test
    public void findAllByBookDescriptionWithPageable() {
        Page<Author> result = this.authorRepository.findAll(new Criteria("books.description").contains("Norvege"), createPageable(0, 5));
        Assert.assertThat(result.getTotalElements(), is(equalTo(2L)));
        Assert.assertThat(result.getContent().get(0).getLastName(), is(equalTo(ITSuiteTest.nicolasBeuglet)));
        Assert.assertThat(result.getContent().get(1).getLastName(), is(equalTo(ITSuiteTest.nicolasBeuglet)));
    }

    @Test
    public void countByBookDescriptionWithDistinct() {
        long count = this.authorRepository.count(new Criteria("books.description").contains("Norvege"), new QueryOptions().distinct(true));
        Assert.assertThat(count, is(equalTo(1L)));
    }

    @Test
    public void findAllByBookTitleWithDistinct() {
        List<Author> result = this.authorRepository.findAll(new Criteria("books.description").contains("Norvege"), new QueryOptions().distinct(true));
        Assert.assertThat(result.size(), is(equalTo(1)));
        Assert.assertThat(result.get(0).getLastName(), is(equalTo(ITSuiteTest.nicolasBeuglet)));
    }

    @Test
    public void findAllByBookDescriptionWithDistinctAndPageable() {
        Page<Author> result = this.authorRepository.findAll(new Criteria("books.description").contains("Norvege"), createPageable(0, 5), new QueryOptions().distinct(true));
        Assert.assertThat(result.getTotalElements(), is(equalTo(1L)));
        Assert.assertThat(result.getContent().get(0).getLastName(), is(equalTo(ITSuiteTest.nicolasBeuglet)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void findAllWithAssociation() {
        List<Author> result = this.authorRepository.findAll(null, new QueryOptions().withAssociation("books"));
        Assert.assertThat(result.size(), is(equalTo(4)));
        List<String> lastNames = result.stream().map(Author::getLastName).collect(Collectors.toList());
        Assert.assertThat(lastNames.contains(ITSuiteTest.elenaFerrante), is(true));
        Assert.assertThat(lastNames.contains(ITSuiteTest.harlanCoben), is(true));
        Assert.assertThat(lastNames.contains(ITSuiteTest.nicolasBeuglet), is(true));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void findAllWithAssociationAndDistinct() {
        List<Author> result = this.authorRepository.findAll(null, new QueryOptions().withAssociation("books").distinct(true));
        Assert.assertThat(result.size(), is(equalTo(3)));
        List<String> lastNames = result.stream().map(Author::getLastName).collect(Collectors.toList());
        Assert.assertThat(lastNames.contains(ITSuiteTest.elenaFerrante), is(true));
        Assert.assertThat(lastNames.contains(ITSuiteTest.harlanCoben), is(true));
        Assert.assertThat(lastNames.contains(ITSuiteTest.nicolasBeuglet), is(true));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void findAllWithAssociationAndSort() {
        List<Author> result = this.authorRepository.findAll(null, new Sort(Sort.Direction.ASC, "lastName"), new QueryOptions().withAssociation("books"));
        Assert.assertThat(result.size(), is(equalTo(4)));
        Assert.assertThat(result.get(0).getLastName(), is(equalTo(ITSuiteTest.nicolasBeuglet)));
        Assert.assertThat(result.get(1).getLastName(), is(equalTo(ITSuiteTest.nicolasBeuglet)));
        Assert.assertThat(result.get(2).getLastName(), is(equalTo(ITSuiteTest.harlanCoben)));
        Assert.assertThat(result.get(3).getLastName(), is(equalTo(ITSuiteTest.elenaFerrante)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void findAllWithAssociationAndSortAndDistinct() {
        List<Author> result = this.authorRepository.findAll(null, new Sort(Sort.Direction.ASC, "lastName"), new QueryOptions().withAssociation("books").distinct(true));
        Assert.assertThat(result.size(), is(equalTo(3)));
        Assert.assertThat(result.get(0).getLastName(), is(equalTo(ITSuiteTest.nicolasBeuglet)));
        Assert.assertThat(result.get(1).getLastName(), is(equalTo(ITSuiteTest.harlanCoben)));
        Assert.assertThat(result.get(2).getLastName(), is(equalTo(ITSuiteTest.elenaFerrante)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void findAllWithAllAssociationAndSort() {
        List<Author> result = this.authorRepository.findAll(null, new Sort(Sort.Direction.ASC, "lastName"), new QueryOptions().withAssociation("books", "books.editor"));
        Assert.assertThat(result.size(), is(equalTo(4)));
        Assert.assertThat(result.get(0).getLastName(), is(equalTo(ITSuiteTest.nicolasBeuglet)));
        Assert.assertThat(result.get(1).getLastName(), is(equalTo(ITSuiteTest.nicolasBeuglet)));
        Assert.assertThat(result.get(2).getLastName(), is(equalTo(ITSuiteTest.harlanCoben)));
        Assert.assertThat(result.get(3).getLastName(), is(equalTo(ITSuiteTest.elenaFerrante)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void findAllWithAllAssociationAndSortAndDistinct() {
        List<Author> result = this.authorRepository.findAll(null, new Sort(Sort.Direction.ASC, "lastName"), new QueryOptions().withAssociation("books", "books.editor").distinct(true));
        Assert.assertThat(result.size(), is(equalTo(3)));
        Assert.assertThat(result.get(0).getLastName(), is(equalTo(ITSuiteTest.nicolasBeuglet)));
        Assert.assertThat(result.get(1).getLastName(), is(equalTo(ITSuiteTest.harlanCoben)));
        Assert.assertThat(result.get(2).getLastName(), is(equalTo(ITSuiteTest.elenaFerrante)));
    }

    @Test
    public void countWithAssociation() {
        Long count = this.authorRepository.count(null, new QueryOptions().withAssociation("books"));
        Assert.assertThat(count, is(equalTo(3L)));
    }

    /**
     * @return
     */
    private Pageable createPageable(int page, int size) {
        return new PageRequest(page, size, new Sort(Sort.Direction.ASC, "lastName", "firstName"));
    }

}
