package com.renzaifei.hextechlib.ui.components;

public final class UiAnimation {
    private UiAnimation() {}

    public static double approach(double current, double target, double seconds, double speed) {
        if (seconds <= 0.0 || speed <= 0.0) return current;
        double value = current + (target - current) * -Math.expm1(-seconds * speed);
        return Math.abs(target - value) < 0.001 ? target : value;
    }

    public static double entranceProgress(double elapsedMillis, int index) {
        double delay = Math.clamp(index, 0, 8) * 45.0;
        double progress = Math.clamp((elapsedMillis - delay) / 280.0, 0.0, 1.0);
        return 1.0 - Math.pow(1.0 - progress, 3.0);
    }
}
