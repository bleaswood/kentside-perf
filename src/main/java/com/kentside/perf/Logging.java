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

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.apache.logging.log4j.LogManager;
import org.slf4j.LoggerFactory;

@State(Scope.Thread)
public class Logging {
    static final org.slf4j.Logger slfLogger = LoggerFactory.getLogger(Logging.class);
    static final org.apache.logging.log4j.Logger logLogger = LogManager.getLogger(Logging.class);

    private int fact(int n) {
        switch (n) {
        case 0:
            return 0;
        case 1:
        case 2:
            return 1;
        default:
            return fact(n - 1) + fact(n - 2);
        }
    }

    /**
     * # Run complete. Total time: 00:20:41
     * 
     * Benchmark           Mode  Cnt          Score          Error  Units
     * Logging.empty      thrpt   60  706976348.293 ± 10474418.443  ops/s
     * Logging.logCalc    thrpt   60     158924.936 ±      358.391  ops/s
     * Logging.logCheck   thrpt   60  283950635.420 ±  3111685.480  ops/s
     * Logging.logLambda  thrpt   60  279087727.031 ±  1581454.555  ops/s
     * Logging.logParam   thrpt   60  234757083.539 ±  3389246.428  ops/s
     * Logging.logSimple  thrpt   60  279251981.284 ±  3456223.315  ops/s
     * Logging.slfCalc    thrpt   60     158933.274 ±      355.057  ops/s
     * Logging.slfCheck   thrpt   60  268382858.117 ±  1314996.830  ops/s
     * Logging.slfParam   thrpt   60  218913313.845 ±  2383826.205  ops/s
     * Logging.slfSimple  thrpt   60  258048949.103 ±  1042583.475  ops/s
     */

    @Benchmark
    public void empty() {
    }

    @Benchmark
    public void logSimple() {
        logLogger.trace("Simple string");
    }

    @Benchmark
    public void slfSimple() {
        slfLogger.trace("Simple string");
    }

    @Benchmark
    public void logCalc() {
        logLogger.trace("Factorial is " + fact(15));
    }

    @Benchmark
    public void slfCalc() {
        slfLogger.trace("Factorial is " + fact(15));
    }

    @Benchmark
    public void logParam() {
        logLogger.trace("{} + {} = {}", 1, 1, 2);
    }

    @Benchmark
    public void slfParam() {
        slfLogger.trace("{} + {} = {}", 1, 1, 2);
    }

    @Benchmark
    public void logCheck() {
        if (logLogger.isTraceEnabled()) {
            logLogger.trace("Factorial is " + fact(15));
        }
    }

    @Benchmark
    public void slfCheck() {
        if (slfLogger.isTraceEnabled()) {
            slfLogger.trace("Factorial is " + fact(15));
        }
    }

    @Benchmark
    public void logLambda() {
        logLogger.trace(() -> "Factorial is " + fact(15));
    }
}

