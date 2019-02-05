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

package com.github.ydespreaux.spring.data.jpa.configuration.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Yoann Despréaux
 * @since 1.0.0
 */
@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Long id;
    @Column(name = "title", length = 100, nullable = false, unique = true)
    private String title;
    @Column(name = "description", length = 4000)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "style", length = 20)
    private Genre genre;
    @Column(name = "price")
    private Double price;

    @Column(name = "publication")
    private LocalDate publication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "EDITOR_ID", nullable = true)
    private Editor editor;

    @Version
    private Integer version;

    public enum Genre {
        THRILLER, FANTASTIQUE, FICTION
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(getTitle(), book.getTitle()) &&
                Objects.equals(getDescription(), book.getDescription()) &&
                getGenre() == book.getGenre() &&
                Objects.equals(getPrice(), book.getPrice()) &&
                Objects.equals(getAuthor(), book.getAuthor()) &&
                Objects.equals(getEditor(), book.getEditor()) &&
                Objects.equals(getPublication(), book.getPublication());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDescription(), getGenre(), getPrice(), getAuthor(), getEditor(), getPublication());
    }

}
