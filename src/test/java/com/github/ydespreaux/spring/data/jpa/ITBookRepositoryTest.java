/* * Copyright (C) 2018 Yoann Despréaux * * This program eq free software; you can redistribute it and/or modify * it under the terms of the GNU General Public License as published by * the Free Software Foundation; either version 2 of the License, or * (at your option) any later version. * * This program eq distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License * along with this program; see the file COPYING . If not, write to the * Free Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA. * * Please send bugreports with examples or suggestions to yoann.despreaux@believeit.fr */package com.github.ydespreaux.spring.data.jpa;import com.github.ydespreaux.spring.data.jpa.configuration.JpaConfiguration;import com.github.ydespreaux.spring.data.jpa.configuration.entities.Book;import com.github.ydespreaux.spring.data.jpa.query.Criteria;import com.github.ydespreaux.spring.data.jpa.query.QueryOptions;import org.junit.Assert;import org.junit.Before;import org.junit.Test;import org.junit.runner.RunWith;import org.springframework.boot.test.context.SpringBootTest;import org.springframework.data.domain.Page;import org.springframework.data.domain.PageRequest;import org.springframework.data.domain.Pageable;import org.springframework.data.domain.Sort;import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;import org.springframework.transaction.annotation.Propagation;import org.springframework.transaction.annotation.Transactional;import java.time.LocalDate;import java.util.List;import java.util.Optional;import static org.hamcrest.Matchers.equalTo;import static org.hamcrest.Matchers.is;/** * @author Yoann Despréaux * @since 1.0.0 */@RunWith(SpringJUnit4ClassRunner.class)@SpringBootTest(classes = JpaConfiguration.class)public class ITBookRepositoryTest extends AbstractJpaCriteriaRepositoryTest {    @Before    public void onSetup() {        cleanData();        insertData();    }    @Test    public void findAllByGenreAndPriceWithPageable() {        Criteria criteria = new Criteria("genre").eq(Book.Genre.FANTASTIQUE)                .or("genre").eq(Book.Genre.FICTION)                .and("price").between(8, 10);        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 2));        Assert.assertThat(result.getTotalElements(), is(equalTo(1L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(1)));        assertThat(this.amieProdigieuse, result.getContent().get(0));    }    @Test    public void countByGenreAndPrice() {        Criteria criteria = new Criteria("genre").eq(Book.Genre.FANTASTIQUE)                .or("genre").eq(Book.Genre.FICTION)                .and("price").between(8, 10);        long count = this.bookRepository.count(criteria);        Assert.assertThat(count, is(equalTo(1L)));    }    @Test    public void findAllByGenreAndPrice() {        Criteria criteria = new Criteria("genre").eq(Book.Genre.FANTASTIQUE)                .or("genre").eq(Book.Genre.FICTION)                .and("price").between(8, 10);        List<Book> result = this.bookRepository.findAll(criteria);        Assert.assertThat(result.size(), is(equalTo(1)));        assertThat(this.amieProdigieuse, result.get(0));    }    @Test    public void findAllByTitleWithMultiCriteresWithPageable() {        Criteria criteria = new Criteria("title").contains("Walking Dead")                .and("description").contains("La Colline");        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 2));        Assert.assertThat(result.getTotalElements(), is(equalTo(1L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(1)));        assertThat(this.walkingDead28, result.getContent().get(0));    }    @Test    public void findOneByTitleWithMultiCriteres() {        Criteria criteria = new Criteria("title").contains("Walking Dead")                .and("description").contains("La Colline");        Optional<Book> result = this.bookRepository.findOne(criteria);        Assert.assertThat(result.isPresent(), is(true));        assertThat(this.walkingDead28, result.get());    }    @Test    public void findOneByTitleEmptyResult() {        Criteria criteria = new Criteria("title").eq("Oui Oui");        Optional<Book> result = this.bookRepository.findOne(criteria);        Assert.assertThat(result.isPresent(), is(false));    }    @Test    public void findAllByTitleWithOrCriteresWithPageable() {        Criteria criteria = new Criteria("title").contains("Walking Dead")                .or("description").contains("La Colline");        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 2));        Assert.assertThat(result.getTotalElements(), is(equalTo(2L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(2)));        assertThat(this.walkingDead28, result.getContent().get(0));        assertThat(this.walkingDead29, result.getContent().get(1));    }    @Test    public void findAllByTitleWithMultiCriteres_or_withSort() {        Criteria criteria = new Criteria("title").contains("Walking Dead")                .or("description").contains("La Colline");        List<Book> result = this.bookRepository.findAll(criteria, Sort.by(Sort.Direction.ASC, "title"));        Assert.assertThat(result.size(), is(equalTo(2)));        assertThat(this.walkingDead28, result.get(0));        assertThat(this.walkingDead29, result.get(1));    }    @Test    public void findAllByTitleWithEqWithPageable() {        Criteria criteria = new Criteria("title").eq("Sans défense");        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 2));        Assert.assertThat(result.getTotalElements(), is(equalTo(1L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(1)));        assertThat(this.sansDefense, result.getContent().get(0));    }    @Test    public void findAllByGenreWithEqWithPageable() {        Criteria criteria = new Criteria("genre").eq(Book.Genre.THRILLER);        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(3L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(3)));        assertThat(this.complot, result.getContent().get(0));        assertThat(this.leCri, result.getContent().get(1));        assertThat(this.sansDefense, result.getContent().get(2));    }    @Test    public void findAllByGenreWithNotEqWithPageable() {        Criteria criteria = new Criteria("genre").notEq(Book.Genre.THRILLER);        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 2));        Assert.assertThat(result.getTotalElements(), is(equalTo(3L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(2)));        assertThat(this.amieProdigieuse, result.getContent().get(0));        assertThat(this.walkingDead28, result.getContent().get(1));        Page<Book> result1 = this.bookRepository.findAll(criteria, createPageable(1, 2));        Assert.assertThat(result1.getTotalElements(), is(equalTo(3L)));        Assert.assertThat(result1.getNumberOfElements(), is(equalTo(1)));        assertThat(this.walkingDead29, result1.getContent().get(0));    }    @Test    public void findAllByGenreWithInWithPageable() {        Criteria criteria = new Criteria("genre").in(Book.Genre.THRILLER, Book.Genre.FICTION);        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(4L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(4)));        assertThat(this.complot, result.getContent().get(0));        assertThat(this.leCri, result.getContent().get(1));        assertThat(this.amieProdigieuse, result.getContent().get(2));        assertThat(this.sansDefense, result.getContent().get(3));    }    @Test    public void findAllByGenreWithNotInWithPageable() {        Criteria criteria = new Criteria("genre").notIn(Book.Genre.THRILLER, Book.Genre.FICTION);        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(2L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(2)));        assertThat(this.walkingDead28, result.getContent().get(0));        assertThat(this.walkingDead29, result.getContent().get(1));    }    @Test    public void findAllByAuthorWithNullWithPageable() {        Criteria criteria = new Criteria("author").isNull();        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 2));        Assert.assertThat(result.getTotalElements(), is(equalTo(2L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(2)));        assertThat(this.walkingDead28, result.getContent().get(0));        assertThat(this.walkingDead29, result.getContent().get(1));    }    @Test    public void findAllByAuthorWithNotNullWithPageable() {        Criteria criteria = new Criteria("author").isNotNull();        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(4L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(4)));        assertThat(this.complot, result.getContent().get(0));        assertThat(this.leCri, result.getContent().get(1));        assertThat(this.amieProdigieuse, result.getContent().get(2));        assertThat(this.sansDefense, result.getContent().get(3));    }    @Test    public void findAllByPriceWithBetweenWithPageable() {        Criteria criteria = new Criteria("price").between(10.0, 20.0);        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(2L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(2)));        assertThat(this.walkingDead28, result.getContent().get(0));        assertThat(this.walkingDead29, result.getContent().get(1));    }    @Test    public void findAllByPriceWithGreaterWithPageable() {        Criteria criteria = new Criteria("price").greaterThan(21.90);        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(0L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(0)));    }    @Test    public void findAllByPriceWithGreaterOrEqualsWithPageable() {        Criteria criteria = new Criteria("price").greaterThanEqual(21.90);        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(1L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(1)));        assertThat(this.sansDefense, result.getContent().get(0));    }    @Test    public void findAllByPriceWithLessWithPageable() {        Criteria criteria = new Criteria("price").lessThan(8.30);        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(1L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(1)));        assertThat(this.leCri, result.getContent().get(0));    }    @Test    public void findAllByPriceWithLessOrEqualsWithPageable() {        Criteria criteria = new Criteria("price").lessThanEqual(8.30);        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(2L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(2)));        assertThat(this.leCri, result.getContent().get(0));        assertThat(this.amieProdigieuse, result.getContent().get(1));    }    @Test    public void findAllByDescriptionWithContainsWithPageable() {        Criteria criteria = new Criteria("description").contains("Rick");        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(2L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(2)));        assertThat(this.walkingDead28, result.getContent().get(0));        assertThat(this.walkingDead29, result.getContent().get(1));    }    @Test    public void findAllByTitleWithStartWith() {        Criteria criteria = new Criteria("title").startsWith("Walking");        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(2L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(2)));        assertThat(this.walkingDead28, result.getContent().get(0));        assertThat(this.walkingDead29, result.getContent().get(1));    }    @Test    public void findByTitleWithEndWith() {        Criteria criteria = new Criteria("title").endsWith("reste");        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(1L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(1)));        assertThat(this.amieProdigieuse, result.getContent().get(0));    }    @Test    public void findByPublicationWithBetweenWithPageable() {        Criteria criteria = new Criteria("publication").between(buildPublication(2018, 1, 1), buildPublication(2018, 2, 1));        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(2L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(2)));        assertThat(this.leCri, result.getContent().get(0));        assertThat(this.amieProdigieuse, result.getContent().get(1));    }    @Test    public void findByAuthorWithPageable() {        Criteria criteria = new Criteria("author.lastName").contains("Beuglet");        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(2L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(2)));        assertThat(this.complot, result.getContent().get(0));        assertThat(this.leCri, result.getContent().get(1));    }    @Test    public void findByEditor() {        Criteria criteria = new Criteria("editor.label").contains("delcourt");        Page<Book> result = this.bookRepository.findAll(criteria, createPageable(0, 5));        Assert.assertThat(result.getTotalElements(), is(equalTo(2L)));        Assert.assertThat(result.getNumberOfElements(), is(equalTo(2)));        assertThat(this.walkingDead28, result.getContent().get(0));        assertThat(this.walkingDead29, result.getContent().get(1));    }    @Test    public void findBookById() {        Optional<Book> result = this.bookRepository.findById(this.amieProdigieuse.getId());        Assert.assertThat(result.isPresent(), is(true));        assertThat(this.amieProdigieuse, result.get());    }    @Test    @Transactional(propagation = Propagation.NEVER)    public void findAllWithAllAssociationAndSort() {        List<Book> result = this.bookRepository.findAll(null, Sort.by(Sort.Direction.ASC, "title"), new QueryOptions().withAssociation("author", "editor"));        Assert.assertThat(result.size(), is(equalTo(6)));        assertThat(result.get(0), this.complot);        assertThat(result.get(1), this.leCri);        assertThat(result.get(2), this.amieProdigieuse);        assertThat(result.get(3), this.sansDefense);        assertThat(result.get(4), this.walkingDead28);        assertThat(result.get(5), this.walkingDead29);    }    private void assertThat(Book reference, Book other) {        Assert.assertThat(other.getTitle(), is(equalTo(reference.getTitle())));        Assert.assertThat(other.getDescription(), is(equalTo(reference.getDescription())));        Assert.assertThat(other.getEditor(), is(equalTo(reference.getEditor())));        Assert.assertThat(other.getPrice(), is(equalTo(reference.getPrice())));        Assert.assertThat(other.getGenre(), is(equalTo(reference.getGenre())));        Assert.assertThat(other.getId(), is(equalTo(reference.getId())));        Assert.assertThat(other.getPublication(), is(equalTo(reference.getPublication())));    }    /**     * @param year     * @param month     * @param day     * @return     */    private LocalDate buildPublication(int year, int month, int day) {        return LocalDate.of(year, month, day);    }    /**     * @return     */    private Pageable createPageable(int page, int size) {        return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "title"));    }}