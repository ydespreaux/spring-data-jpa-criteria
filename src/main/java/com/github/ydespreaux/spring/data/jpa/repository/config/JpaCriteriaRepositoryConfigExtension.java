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

package com.github.ydespreaux.spring.data.jpa.repository.config;

import com.github.ydespreaux.spring.data.jpa.repository.support.JpaCriteriaRepository;
import com.github.ydespreaux.spring.data.jpa.repository.support.JpaCriteriaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Yoann Despréaux
 * @since 1.0.0
 */
public class JpaCriteriaRepositoryConfigExtension extends JpaRepositoryConfigExtension {

    @Override
    public String getRepositoryFactoryClassName() {
        return JpaCriteriaRepositoryFactoryBean.class.getName();
    }

    @Override
    protected Collection<Class<?>> getIdentifyingTypes() {
        return Arrays.asList(JpaCriteriaRepository.class, JpaRepository.class);
    }

}
