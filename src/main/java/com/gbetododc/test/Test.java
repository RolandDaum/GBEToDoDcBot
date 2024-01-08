package com.gbetododc.Test;

import java.util.function.Consumer;

public class Test {
        public static void main(String[] args) {
            test(cbString -> {
                System.out.println(cbString);
            });
        }
        public static void test(Consumer<String> callback) {
            String cbString = "Hello";
            callback.accept(cbString);
        }
    }