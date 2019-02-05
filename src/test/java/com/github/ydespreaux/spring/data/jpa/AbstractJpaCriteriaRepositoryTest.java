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

import com.github.ydespreaux.spring.data.jpa.configuration.entities.Author;
import com.github.ydespreaux.spring.data.jpa.configuration.entities.Book;
import com.github.ydespreaux.spring.data.jpa.configuration.entities.Editor;
import com.github.ydespreaux.spring.data.jpa.configuration.repository.AuthorRepository;
import com.github.ydespreaux.spring.data.jpa.configuration.repository.BookRepository;
import com.github.ydespreaux.spring.data.jpa.configuration.repository.EditorRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;

public abstract class AbstractJpaCriteriaRepositoryTest {

    @Autowired
    protected AuthorRepository authorRepository;

    @Autowired
    protected BookRepository bookRepository;

    @Autowired
    protected EditorRepository editorRepository;

    protected Editor pocket;
    protected Editor gallimard;
    protected Editor belfond;
    protected Editor delcourt;

    protected Author nicolasBeuglet = null;
    protected Author elenaFerrante = null;
    protected Author harlanCoben = null;

    protected Book amieProdigieuse = null;
    protected Book complot = null;
    protected Book leCri = null;
    protected Book sansDefense = null;
    protected Book walkingDead28 = null;
    protected Book walkingDead29 = null;


    protected void cleanData() {
        this.bookRepository.deleteAllInBatch();
        this.authorRepository.deleteAllInBatch();
        this.editorRepository.deleteAllInBatch();
    }

    protected void insertData() {
        this.generateData();
        this.editorRepository.saveAll(Arrays.asList(
                pocket, gallimard, belfond, delcourt
        ));
        this.authorRepository.saveAll(Arrays.asList(
                nicolasBeuglet, elenaFerrante, harlanCoben
        ));
        this.bookRepository.saveAll(Arrays.asList(
                walkingDead28, walkingDead29
        ));
    }

    private void generateData() {

        pocket = createEditor("Pocket");
        gallimard = createEditor("Gallimard");
        belfond = createEditor("Belfond");
        delcourt = createEditor("Delcourt");

        nicolasBeuglet = createAuthor("Nicolas", "Beuglet");
        elenaFerrante = createAuthor("Elena", "Ferrante");
        harlanCoben = createAuthor("Harlan", "Coben");

        leCri = this.createBook(
                "Le cri",
                "À quelques kilomètres d'Oslo, en Norvège, l'hôpital psychiatrique de Gaustad dresse sa masse sombre parmi les pins enneigés. Appelée sur place pour un suicide, l'inspectrice Sarah Geringën pressent d'emblée que rien ne concorde. Le patient 488, ainsi surnommé suivant les chiffres cicatrisés qu'il porte sur le front, s'est figé dans la mort, un cri muet aux lèvres – un cri de peur primale. Soumise à un compte à rebours implacable, Sarah va découvrir une vérité vertigineuse sur l'une des questions qui hante chacun d'entre nous : la vie...",
                Book.Genre.THRILLER,
                pocket,
                8.20,
                buildPublication(2018, 1, 11));
        complot = this.createBook(
                "Complot",
                "Un archipel isolé au nord de la Norvège, battu par les vents. Et, au bord de la falaise, le corps nu et martyrisé d'une femme. Les blessures qui déchirent sa chair semblent être autant de symboles mystérieux. ",
                Book.Genre.THRILLER,
                pocket,
                9.20,
                buildPublication(2018, 5, 16));
        nicolasBeuglet.addBook(leCri);
        nicolasBeuglet.addBook(complot);

        amieProdigieuse = this.createBook(
                "L’amie Prodigieuse - Tome 3 : Celle qui fuit et celle qui reste",
                "Après L'amie prodigieuse et Le nouveau nom, Celle qui fuit et celle qui reste est la suite de la formidable saga dans laquelle Elena Ferrante raconte cinquante ans d'histoire italienne et d'amitié entre ses deux héroïnes, Elena et Lila. Pour Elena, comme pour l'Italie, une période de grands bouleversements s'ouvre. Nous sommes à la fin des années soixante, les événements de 1968 s'annoncent, les mouvements féministes et protestataires s'organisent, et Elena, diplômée de l'Ecole normale de Pise et entourée d'universitaires,...",
                Book.Genre.FICTION,
                gallimard,
                8.30,
                buildPublication(2018, 1, 25));
        elenaFerrante.addBook(amieProdigieuse);

        sansDefense = this.createBook(
                "Sans défense",
                "Deux enfants kidnappés. Un inconnu qui réapparaît. Après dix ans d'angoisse, le cauchemar ne fait que commencer...",
                Book.Genre.THRILLER,
                belfond,
                21.90,
                buildPublication(2018, 3, 1));
        harlanCoben.addBook(sansDefense);

        walkingDead28 = this.createBook("Walking Dead - Tome 28 : Walking Dead",
                "La Colline a été dévastée et la communauté qui l'habitait a du fuir les lieux, sous l'impulsion de Maggie. Dwight a rejoint Rick, en lui affirmant que les Chuchoteurs ont été anéantis. Malheureusement, même si Beta - qui a pris la tête des Chuchoteurs - a perdu une bataille, il lance une horde de rôdeurs sur Alexandria. La guerre est peut-être terminée, mais la survie d'Alexandria est en jeu...",
                Book.Genre.FANTASTIQUE,
                delcourt,
                14.95,
                buildPublication(2017, 10, 14));

        walkingDead29 = this.createBook("Walking Dead - Tome 29 : La Ligne blanche",
                "Carl ne parvient pas à admettre la mort d'Andrea. Tandis que Rick fait au mieux, Maggie n'accepte pas sa décision de laisser Negan en liberté et le fait étroitement surveillé. Eugene contacte Stephanie par radio et ils conviennent de se rencontrer. À la suite de ces tragiques événements, Rick envisage d'établir une communauté dans l'Ohio. Une nouvelle ère débute pour les survivants de l'apocalypse...",
                Book.Genre.FANTASTIQUE,
                delcourt,
                14.95,
                buildPublication(2018, 3, 7));

    }


    private Editor createEditor(String label) {
        Editor editor = new Editor();
        editor.setLabel(label);
        return editor;
    }

    /**
     * @param firstName
     * @param lastName
     * @return
     */
    private Author createAuthor(String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        return author;
    }

    /**
     * @param title
     * @param description
     * @param genre
     * @param editor
     * @param price
     * @param publication
     * @return
     */
    private Book createBook(String title, String description, Book.Genre genre, Editor editor, Double price, LocalDate publication) {
        return Book.builder()
                .title(title)
                .editor(editor)
                .description(description)
                .genre(genre)
                .price(price)
                .publication(publication)
                .build();
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return
     */
    private LocalDate buildPublication(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }
}
