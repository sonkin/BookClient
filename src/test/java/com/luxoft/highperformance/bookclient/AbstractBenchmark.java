package com.luxoft.highperformance.bookclient;

import org.junit.Test;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

abstract public class AbstractBenchmark {

    private final static Integer WARMUP_ITERATIONS = 2;
    private final static Integer MEASUREMENT_ITERATIONS = 3;

    @Test
    public void executeJmhRunner() throws RunnerException {
        Options opt = new OptionsBuilder()
                // set the class name regex for benchmarks to search for to the current class
                .include("\\." + this.getClass().getSimpleName() + "\\.")
                .warmupIterations(WARMUP_ITERATIONS)
                .measurementIterations(MEASUREMENT_ITERATIONS)
                // do not use forking or the benchmark methods
                // will not see references stored within its class
                .forks(0)
                // do not use multiple threads
                .threads(4)
//                .shouldFailOnError(true)
//                .resultFormat(ResultFormatType.JSON)
//                .result("/dev/null") // set this to a valid filename if you want reports
                .shouldFailOnError(true)
                .shouldDoGC(true)
//                .jvmArgs("-Xmx128m")
                .build();

        new Runner(opt).run();
    }
}
