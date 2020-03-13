package com.liurui.redis.demo6;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class AppTests {
    @Benchmark
    public void test(){
        System.out.println("hello");
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AppTests.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
