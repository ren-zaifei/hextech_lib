package com.renzaifei.hextechlib.ui.components;

public final class SmoothScroll {
    private final double maxScroll;
    private double position;
    private double target;

    public SmoothScroll(double maxScroll) {
        this.maxScroll = Math.max(0.0, maxScroll);
    }

    public boolean scrollBy(double amount) {
        double previous = target;
        target = Math.clamp(target + amount, 0.0, maxScroll);
        return previous != target;
    }

    public void reveal(double left, double right, double viewportWidth) {
        if (left < target) {
            target = Math.clamp(left, 0.0, maxScroll);
        } else if (right > target + viewportWidth) {
            target = Math.clamp(right - viewportWidth, 0.0, maxScroll);
        }
    }

    public void update(double seconds) {
        position = UiAnimation.approach(position, target, seconds, 16.0);
    }

    public double position() {
        return position;
    }

    public double target() {
        return target;
    }
}
