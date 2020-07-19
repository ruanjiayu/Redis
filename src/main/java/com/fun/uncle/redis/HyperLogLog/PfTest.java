package com.fun.uncle.redis.HyperLogLog;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description: 对于HyperLogLog实现原理
 * @Author: fan
 * @DateTime: 2020/7/19 11:38 下午
 * @Version: 0.0.1-SNAPSHOT
 */
public class PfTest {

    static class BitKeeper {
        private int maxbits;

        /**
         * 获取指定数字内的随机数
         */
        public void random() {
            long val = ThreadLocalRandom.current().nextLong(2L << 32);
            int bits = lowZeros(val);
            if (bits > this.maxbits) {
                this.maxbits = bits;
            }
        }

        /**
         * 获取尾数1最后出现的位置，然后减1
         * @param value
         * @return
         */
        private int lowZeros(long value) {
            int i = 1;
            for (; i< 32; i++ ) {
                if (value >> i << i != value) {
                    break;
                }
            }
            return i -1;
        }
    }

    static class Experiment {
        private int n;
        private BitKeeper keeper;

        public Experiment(int n) {
            this.n = n;
            this.keeper = new BitKeeper();
        }

        public void work() {
            for (int i = 0; i < n; i++) {
                this.keeper.random();
            }
        }

        public void debug() {
            System.out.printf("%d %.2f %d\n", this.n, Math.log(this.n)/Math.log(2), this.keeper.maxbits);
        }
    }

    public static void main(String[] args) {
        for (int i = 1000; i < 100_000; i += 100) {
            Experiment experiment = new Experiment(i);
            experiment.work();
            experiment.debug();
        }
    }
}
