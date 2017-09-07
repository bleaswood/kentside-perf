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

@State(Scope.Thread)
public class Poly {

    interface Calc {
        int calc(int num);
    }

    abstract class AbCalc implements Calc {
    }

    class Inc extends AbCalc {
        public int calc(int num) {
            return num + 1;
        }
    }

    class Dec extends AbCalc {
        public int calc(int num) {
            return num - 1;
        }
    }

    class Left extends AbCalc {
        public int calc(int num) {
            return num << 1;
        }
    }

    Calc[] mono = new AbCalc[] {new Inc(), new Inc(), new Inc()};
    Calc[] poly = new AbCalc[] {new Inc(), new Dec(), new Left()};
    Calc[] poly2 = new AbCalc[] {new Dec(), new Left(), new Inc()};
    Calc[] poly3 = new AbCalc[] {new Left(), new Inc(), new Dec()};

    public int curr;

    private void naiveInt(Calc[] calcs) {
        for (int i = 0; i < calcs.length; ++i) {
            curr = calcs[i].calc(curr);
        }
    }

    private void naiveIntRev(Calc[] calcs) {
        for (int i = calcs.length; i-- != 0; ) {
            curr = calcs[i].calc(curr);
        }
    }

    private void naiveAbs(AbCalc[] calcs) {
        for (int i = 0; i < calcs.length; ++i) {
            curr = calcs[i].calc(curr);
        }
    }

    private void smartInt(Calc[] calcs) {
        switch (calcs.length) {
        default:
            for (int i = calcs.length; i-- != 3; ) {
                curr = calcs[i].calc(curr);
            }
        case 3:
            curr = calcs[2].calc(curr);
        case 2:
            curr = calcs[1].calc(curr);
        case 1:
            curr = calcs[0].calc(curr);
        case 0:
        }
    }

    /**
     * # Run complete. Total time: 00:16:17
     * 
     * Benchmark              Mode  Cnt          Score         Error  Units
     * Poly.naiveMonoAbs     thrpt   60  114355044.952 ± 3530348.841  ops/s
     * Poly.naiveMonoInt     thrpt   60  129283878.820 ± 3151661.549  ops/s
     * Poly.naiveMonoIntRev  thrpt   60  108136651.163 ± 2197639.810  ops/s
     * Poly.naivePolyAbs     thrpt   60   29852252.993 ±  792719.547  ops/s
     * Poly.naivePolyInt     thrpt   60   23124987.171 ±  476445.364  ops/s
     * Poly.smartMonoInt     thrpt   60  143561007.528 ± 3594286.739  ops/s
     * Poly.smartPolyInt     thrpt   60  151052131.765 ± 4132430.532  ops/s
     * Poly.triSmartPolyInt  thrpt   60   13955638.775 ±  255146.160  ops/s
     **/

    @Benchmark
    public void naiveMonoInt() {
        naiveInt(mono);
    }

    @Benchmark
    public void naiveMonoIntRev() {
        naiveIntRev(mono);
    }

    @Benchmark
    public void naivePolyInt() {
        naiveInt(poly);
    }

    @Benchmark
    public void naiveMonoAbs() {
        naiveAbs((AbCalc[]) mono);
    }

    @Benchmark
    public void naivePolyAbs() {
        naiveAbs((AbCalc[]) poly);
    }

    @Benchmark
    public void smartMonoInt() {
        smartInt(mono);
    }

    @Benchmark
    public void smartPolyInt() {
        smartInt(poly);
    }

    @Benchmark
    public void triSmartPolyInt() {
        smartInt(poly);
        smartInt(poly2);
        smartInt(poly3);
    }

}
