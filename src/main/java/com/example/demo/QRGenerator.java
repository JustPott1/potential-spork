package com.example.demo;

import java.nio.file.Path;

public abstract class QRGenerator {
    public abstract void generate(String text, Path output) throws Exception;
}
