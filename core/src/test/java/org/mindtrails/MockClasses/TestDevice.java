package org.mindtrails.MockClasses;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;

public class TestDevice implements Device {
    @Override
    public boolean isNormal() {
        return true;
    }

    @Override
    public boolean isMobile() {
        return false;
    }

    @Override
    public boolean isTablet() {
        return false;
    }

    @Override
    public DevicePlatform getDevicePlatform() {
        return null;
    }
}
