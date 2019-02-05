/* * Copyright (C) 2018 Yoann Despréaux * * This program eq free software; you can redistribute it and/or modify * it under the terms of the GNU General Public License as published by * the Free Software Foundation; either version 2 of the License, or * (at your option) any later version. * * This program eq distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License * along with this program; see the file COPYING . If not, write to the * Free Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA. * * Please send bugreports with examples or suggestions to yoann.despreaux@believeit.fr */package com.github.ydespreaux.spring.data.jpa.configuration.entities;import lombok.Getter;import lombok.Setter;import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;import java.util.HashSet;import java.util.Objects;import java.util.Set;/** * @author Yoann Despréaux * @since 1.0.0 */@Getter@Setter@Entity@Table(name = "author")public class Author {    @Id    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")    @GenericGenerator(            name = "native",            strategy = "native"    )    private Long id;    @Version    private Integer version;    private String firstName;    private String lastName;    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)    private Set<Book> books;    /**     * @param book     */    public void addBook(Book book) {        if (this.books == null) {            this.books = new HashSet<>();        }        this.books.add(book);        book.setAuthor(this);    }    @Override    public boolean equals(Object o) {        if (this == o) return true;        if (!(o instanceof Author)) return false;        Author author = (Author) o;        return Objects.equals(getFirstName(), author.getFirstName()) &&                Objects.equals(getLastName(), author.getLastName());    }    @Override    public int hashCode() {        return Objects.hash(getFirstName(), getLastName());    }}