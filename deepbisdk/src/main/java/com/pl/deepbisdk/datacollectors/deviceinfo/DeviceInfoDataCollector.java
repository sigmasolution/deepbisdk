package com.pl.deepbisdk.datacollectors.deviceinfo;

import android.os.Build;

import com.pl.deepbisdk.BuildConfig;
import com.pl.deepbisdk.DeepBiManager;
import com.pl.deepbisdk.datacollectors.BaseDataCollector;
import com.pl.deepbisdk.queuemanager.HitEvent;
import com.pl.deepbisdk.utilities.NetworkUtils;
import com.pl.deepbisdk.utilities.RandomString;
import com.pl.deepbisdk.utilities.TimeUtils;
import com.pl.deepbisdk.utilities.Utility;

import java.util.Date;
import java.util.TimeZone;

public class DeviceInfoDataCollector extends BaseDataCollector {

    @Override
    public void putData(HitEvent event) {
        HitEvent.User user;
        if (event.getUser() == null) {
            user = new HitEvent.User();
            event.setUser(user);
        } else {
            user = event.getUser();
        }
        user.setDevice(generateHitDevice());

        // Consent
        HitEvent.Consents consents = new HitEvent.Consents();
        consents.setGdpr(true);
        user.setConsents(consents);

        // ID
        user.setId(DeepBiManager.DEVICE_ID);

        // Time
        event.setTime(TimeUtils.getTimeISO8601());

        // Session
        HitEvent.Session session = new HitEvent.Session();
        session.setId(DeepBiManager.SESSION_ID);
        event.setSession(session);

        // Sdk
        HitEvent.Sdk sdk = new HitEvent.Sdk();
        sdk.setVersion(BuildConfig.VERSION_NAME);
        event.setSdk(sdk);

        // App Info
        HitEvent.App app = new HitEvent.App();
        app.setName("DeepBi");
        event.setApp(app);

        // UTC offset
        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUtc = tz.getOffset(now.getTime()) / 1000 / 60;
        event.setUtcoffset(offsetFromUtc);

        // Attention
        HitEvent.Attention attention = new HitEvent.Attention();

        user.setAttention(attention);
    }

    private HitEvent.Device generateHitDevice() {
        HitEvent.Device device = new HitEvent.Device();
        device.setIp(NetworkUtils.getIPAddress(true));

        // Hardware Info
        HitEvent.HardwarePlatform hardwarePlatform = new HitEvent.HardwarePlatform();
        HitEvent.HardwarePlatformName hardwarePlatformName = new HitEvent.HardwarePlatformName();
        hardwarePlatformName.setHardwarename(Utility.getDeviceName());
        hardwarePlatform.setName(hardwarePlatformName);

        HitEvent.HardwarePlatformDevice hardwarePlatformDevice = new HitEvent.HardwarePlatformDevice();
        hardwarePlatformDevice.setDevicetype(DeepBiManager.isTablet() ? "Tablet" : "Smartphone");
        hardwarePlatformDevice.setIstablet(DeepBiManager.isTablet());
        hardwarePlatformDevice.setIssmartphone(!DeepBiManager.isTablet());
        hardwarePlatformDevice.setIsmobile(true);
        hardwarePlatformDevice.setIssmallscreen(false);
        hardwarePlatform.setDevice(hardwarePlatformDevice);

        HitEvent.HardwarePlatformScreen hardwarePlatformScreen = new HitEvent.HardwarePlatformScreen();
        int[] size = Utility.getScreenSizePixel();
        hardwarePlatformScreen.setScreenpixelsheight(size[1]);
        hardwarePlatformScreen.setScreenpixelswidth(size[0]);
        hardwarePlatform.setScreen(hardwarePlatformScreen);

        device.setHardwarePlatform(hardwarePlatform);

        // Software Info
        HitEvent.SoftwarePlatform softwarePlatform = new HitEvent.SoftwarePlatform();
        HitEvent.SoftwarePlatformName softwarePlatformName = new HitEvent.SoftwarePlatformName();
        softwarePlatformName.setPlatformname("Android");
        softwarePlatformName.setPlatformvendor(Build.VERSION.RELEASE);
        softwarePlatformName.setPlatformversion("Google Inc");
        softwarePlatform.setName(softwarePlatformName);
        device.setSoftwarplatform(softwarePlatform);


        return device;
    }
}
