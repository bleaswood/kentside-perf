/*
 * This file is part of the Kentside Java Benchmark Suite
 *
 * Copyright (C) 2017 Helm Solutions Ltd (kentside@yandex.com)
 *
 * The Kentside Java Benchmark Suite is free software: you can redistribute
 * it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * The Kentside Java Benchmark Suite is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Kentside Java Benchmark Suite.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.kentside.perf;

import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel.MapMode;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import sun.misc.Unsafe;

@State(Scope.Benchmark)
public class Real {

    int intMult = 130;
    long longMult = 13000l;
    float floatMult = 1.3f;
    double doubleMult = 1.3d;

    int intAdd = 70;
    long longAdd = 7000l;
    float floatAdd = 0.7f;
    double doubleAdd = 0.7d;

    int intDiv = 120;
    long longDiv = 12000l;
    float floatDiv = 1.2f;
    double doubleDiv = 1.2d;

    int intAccrual;
    long longAccrual;
    float floatAccrual;
    float cRulesAccrual;
    double doubleAccrual;

    /*
     * Benchmark         Mode  Cnt         Score        Error  Units
     * Real.cRulesCalc  thrpt   50  45456132.317 ± 100699.559  ops/s
     * Real.doubleCalc  thrpt   50  59905955.657 ± 711916.043  ops/s
     * Real.floatCalc   thrpt   50  66451655.177 ± 674519.581  ops/s
     * Real.intCalc     thrpt   50  30560818.001 ±  38008.271  ops/s
     * Real.longCalc    thrpt   50  21097882.987 ± 202415.754  ops/s
     */

    @Benchmark
    public void intCalc() {
        intAccrual = (intAccrual * intMult / 100 + intAdd) * 100 / intDiv;
    }

    @Benchmark
    public void longCalc() {
        longAccrual = (longAccrual * longMult / 10000 + longAdd) * 10000 / longDiv;
    }

    @Benchmark
    public void floatCalc() {
        floatAccrual = (floatAccrual * floatMult + floatAdd) / floatDiv;
    }

    @Benchmark
    public void cRulesCalc() {
        cRulesAccrual = (float) ((((double) cRulesAccrual) * floatMult + floatAdd) / floatDiv);
    }

    @Benchmark
    public void doubleCalc() {
        doubleAccrual = (doubleAccrual * doubleMult + doubleAdd) / doubleDiv;
    }

}
