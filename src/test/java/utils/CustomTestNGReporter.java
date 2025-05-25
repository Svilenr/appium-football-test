package utils;

import org.testng.*;
import java.io.*;

public class CustomTestNGReporter implements ITestListener {

    private static PrintWriter writer;

    // Called when the whole suite starts: wipe/create file
    @Override
    public void onStart(ITestContext context) {
        try {
            writer = new PrintWriter(new FileWriter("custom-test-output.txt", false)); // overwrite each time
            writer.println("Custom Test Report:");
            writer.flush();
        } catch (IOException e) {
            System.err.println("Could not create custom test output file: " + e.getMessage());
        }
    }

    // Called after EACH test method
    @Override
    public void onTestSuccess(ITestResult result) {
        String line = formatResult(result, "PASSED");
        System.out.println(line); // Console
        writeLine(line); // File
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String line = formatResult(result, "FAILED", result.getThrowable());
        System.out.println(line); // Console
        writeLine(line); // File
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String line = formatResult(result, "SKIPPED", result.getThrowable());
        System.out.println(line);
        writeLine(line);
    }

    // Always close file at the end
    @Override
    public void onFinish(ITestContext context) {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

    // --- Helpers ---
    private void writeLine(String line) {
        if (writer != null) {
            writer.println(line);
            writer.flush();
        }
    }

    private String formatResult(ITestResult result, String status) {
        return String.format("[%s] %s.%s (%d ms)",
                status,
                result.getTestClass().getName(),
                result.getMethod().getMethodName(),
                result.getEndMillis() - result.getStartMillis());
    }

    private String formatResult(ITestResult result, String status, Throwable t) {
        String base = formatResult(result, status);
        if (t != null) {
            base += "\n    ERROR: " + t.getMessage();
        }
        return base;
    }

    // No-op for unused events
    @Override
    public void onTestStart(ITestResult result) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
    }
}