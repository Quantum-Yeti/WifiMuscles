package me.theoria.wifimuscles.utils;

import junit.framework.TestCase;

import me.theoria.wifimuscles.core.utils.CalculationUtils;

public class CalculationUtilsTest extends TestCase {

    public void testIntIPToString() {
        assertEquals("192.168.1.1", CalculationUtils.intIPToString(0xC0A80101));
        assertEquals("0.0.0.0", CalculationUtils.intIPToString(0x00000000));
        assertEquals("255.255.255.255", CalculationUtils.intIPToString(0xFFFFFFFF));
    }

    public void testSpeedConvert() {
        assertEquals("1.00 Gbps", CalculationUtils.speedConvert(1000000));
        assertEquals("500.00 Mbps", CalculationUtils.speedConvert(500000));
        assertEquals("200 Kbps", CalculationUtils.speedConvert(200));
        assertEquals("1.00 Mbps", CalculationUtils.speedConvert(1000));
    }

    public void testFqToGhz() {
        assertEquals("2.4 GHz", CalculationUtils.fqToGhz(2400));
        assertEquals("5 GHz", CalculationUtils.fqToGhz(5000));
        assertEquals("6 GHz", CalculationUtils.fqToGhz(6000));
        assertEquals("Unknown Band", CalculationUtils.fqToGhz(1000));
    }

    public void testConvertRssiToLevel() {
        assertEquals(5, CalculationUtils.convertRssiToLevel(-50));
        assertEquals(4, CalculationUtils.convertRssiToLevel(-60));
        assertEquals(3, CalculationUtils.convertRssiToLevel(-70));
        assertEquals(2, CalculationUtils.convertRssiToLevel(-80));
        assertEquals(1, CalculationUtils.convertRssiToLevel(-90));

    }

    public void testFormatLeaseDuration() {
    }

    public void testCalculateChannel() {
    }

    public void testGetWifiStandardName() {
    }

    public void testCalculateInterference() {
    }
}