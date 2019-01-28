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

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Yoann Despréaux
 * @since 0.0.3
 */
@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Book implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;
    @Column(name = "editor", length = 255)
    private String editor;
    @Column(name = "publication")
    private LocalDate publication;
    @Version
    private Integer version;

    public enum Genre {
        THRILLER, FANTASTIQUE, FICTION
    }

    /**
     * Properties path for search
     */
    public enum BookPropertyPath {

        TITLE("title"),
        DESCRIPTION("description"),
        GENRE("genre"),
        PRICE("price"),
        AUTHOR("author"),
        AUTHOR_FIRSTNAME("author.firstName"),
        AUTHOR_LASTNAME("author.lastName"),
        EDITOR("editor"),
        PUBLICATION("publication");

        private final String propertyPath;

        BookPropertyPath(String propertyPath) {
            this.propertyPath = propertyPath;
        }

        public String getPropertyPath() {
            return this.propertyPath;
        }
    }

}
