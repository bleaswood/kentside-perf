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

@State(Scope.Thread)
public class Buff {

    // A (assumed) 40 byte area, here defined as a struct
    // with a similar layout intended in the various buffers

    class Struct {
        double dummy0;
        int currInt;
        int totalInt;
        double currDoub;
        double totalDoub;
        double dummy1;
    };

    static final int BUFF_SIZE = 40;

    static final int CURR_INT = 8;
    static final int TOTAL_INT = 12;
    static final int CURR_DOUB = 16;
    static final int TOTAL_DOUB = 24;

    private Unsafe unsafe;

    Struct structBuff = new Struct();
    ByteBuffer indirBuff;
    ByteBuffer directBuff;
    ByteBuffer fileBuff;
    long unsafeBuff;

    @Setup(Level.Trial)
    public void setup() throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        unsafe = (Unsafe) field.get(null);

        indirBuff = ByteBuffer.allocate(BUFF_SIZE);
        directBuff = ByteBuffer.allocateDirect(BUFF_SIZE);
        fileBuff = new RandomAccessFile("/tmp/file.dat","rw")
                .getChannel().map(MapMode.READ_WRITE,0,BUFF_SIZE);
        unsafeBuff = unsafe.allocateMemory(BUFF_SIZE);
    }

    @TearDown(Level.Trial)
    public void tearDown() throws Exception {
        unsafe.freeMemory(unsafeBuff);
    }

    /**
     * Benchmark           Mode  Cnt          Score         Error  Units
     * Buff.directBuffer  thrpt   60   87965063.219 ±  436715.342  ops/s
     * Buff.fileBuffer    thrpt   60   84783487.385 ± 1035606.932  ops/s
     * Buff.indirBuffer   thrpt   60   30445087.855 ±  151718.597  ops/s
     * Buff.structBuffer  thrpt   60  147551312.706 ± 4210402.308  ops/s
     * Buff.unsafeBuffer  thrpt   60  134638820.598 ± 6035020.444  ops/s
     */

    @Benchmark
    public void structBuffer() {
        structBuff.totalInt = structBuff.totalInt + structBuff.currInt;
        structBuff.totalDoub = structBuff.totalDoub + structBuff.currDoub;
    }

    @Benchmark
    public void indirBuffer() {
        indirBuff.putInt(
                TOTAL_INT, 
                indirBuff.getInt(TOTAL_INT) + indirBuff.getInt(CURR_INT));
        indirBuff.putDouble(
                TOTAL_DOUB, 
                indirBuff.getDouble(TOTAL_DOUB) + indirBuff.getDouble(CURR_DOUB));
    }

    @Benchmark
    public void directBuffer() {
        directBuff.putInt(
                TOTAL_INT, 
                directBuff.getInt(TOTAL_INT) + directBuff.getInt(CURR_INT));
        directBuff.putDouble(
                TOTAL_DOUB, 
                directBuff.getDouble(TOTAL_DOUB) + directBuff.getDouble(CURR_DOUB));
    }

    @Benchmark
    public void fileBuffer() {
        fileBuff.putInt(
                TOTAL_INT, 
                fileBuff.getInt(TOTAL_INT) + fileBuff.getInt(CURR_INT));
        fileBuff.putDouble(
                TOTAL_DOUB, 
                fileBuff.getDouble(TOTAL_DOUB) + fileBuff.getDouble(CURR_DOUB));
    }

    @Benchmark
    public void unsafeBuffer() {
        unsafe.putInt(
                unsafeBuff + TOTAL_INT,
                unsafe.getInt(unsafeBuff + TOTAL_INT) + 
                        unsafe.getInt(unsafeBuff + CURR_INT));
        unsafe.putDouble(
                unsafeBuff + TOTAL_DOUB,
                unsafe.getDouble(unsafeBuff + TOTAL_DOUB) + 
                        unsafe.getDouble(unsafeBuff + CURR_DOUB));
    }

}
