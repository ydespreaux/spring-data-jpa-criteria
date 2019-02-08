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

import com.github.ydespreaux.testcontainers.mysql.MySQLContainer;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.testcontainers.containers.JdbcDatabaseContainer;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ITAuthorRepositoryTest.class,
        ITBookRepositoryTest.class
})
public class ITSuiteTest {

    @ClassRule
    public static final JdbcDatabaseContainer mySqlContainer = new MySQLContainer()
            .withDatabaseName("db_library")
            .withUsername("user")
            .withPassword("password")
            .withSqlScriptDirectory("scripts")
            .withStartupTimeoutSeconds(240);

    public static String editorPocket = "Pocket";
    public static String editorGallimard = "Gallimard";
    public static String editorBelfond = "Belfond";
    public static String editorDelcourt = "Delcourt";
    public static String nicolasBeuglet = "Beuglet";
    public static String elenaFerrante = "Ferrante";
    public static String harlanCoben = "Coben";
    public static String amieProdigieuse = "L'amie Prodigieuse - Tome 3 : Celle qui fuit et celle qui reste";
    public static String complot = "Complot";
    public static String leCri = "Le cri";
    public static String sansDefense = "Sans defense";
    public static String walkingDead28 = "Walking Dead - Tome 28 : Walking Dead";
    public static String walkingDead29 = "Walking Dead - Tome 29 : La Ligne blanche";
}
