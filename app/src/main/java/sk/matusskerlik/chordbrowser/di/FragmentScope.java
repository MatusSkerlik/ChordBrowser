/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.di;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Scope
@Retention(RUNTIME)
public @interface FragmentScope {
}
