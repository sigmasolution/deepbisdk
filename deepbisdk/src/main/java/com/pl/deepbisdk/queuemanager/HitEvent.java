package com.pl.deepbisdk.queuemanager;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HitEvent implements Serializable {

    private long timemillis;

    @SerializedName("event")
    @Expose
    private Event event;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("app")
    @Expose
    private App app;

    @SerializedName("utcoffset")
    @Expose
    private int utcoffset;

    @SerializedName("session")
    @Expose
    private Session session;

    @SerializedName("page")
    @Expose
    private Page page;

    @SerializedName("sdk")
    @Expose
    private Sdk sdk;

    @SerializedName("user")
    @Expose
    private User user;

    public HitEvent() {
    }

    public HitEvent(String eventType, String pageTitle) {
        event = new Event();
        event.setType(eventType);
        page = new Page();
        page.setTitle(pageTitle);
    }

    public long getTimemillis() {
        return timemillis;
    }

    public void setTimemillis(long timemillis) {
        this.timemillis = timemillis;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public int getUtcoffset() {
        return utcoffset;
    }

    public void setUtcoffset(int utcoffset) {
        this.utcoffset = utcoffset;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Sdk getSdk() {
        return sdk;
    }

    public void setSdk(Sdk sdk) {
        this.sdk = sdk;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // SUB-Class
    public static class Event {

        @SerializedName("type")
        @Expose
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class App {

        @SerializedName("name")
        @Expose
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Session {

        @SerializedName("id")
        @Expose
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class Page {

        @SerializedName("title")
        @Expose
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class Sdk {

        @SerializedName("version")
        @Expose
        private String version;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    public static class Consents {

        @SerializedName("gdpr")
        @Expose
        private boolean gdpr;

        public boolean isGdpr() {
            return gdpr;
        }

        public void setGdpr(boolean gdpr) {
            this.gdpr = gdpr;
        }
    }

    public static class Attention {

        @SerializedName("total")
        @Expose
        private int total;

        @SerializedName("active")
        @Expose
        private int active;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }
    }

    public static class SoftwarePlatformName {

        @SerializedName("platformname")
        @Expose
        private String platformname;


        @SerializedName("platformversion")
        @Expose
        private String platformversion;


        @SerializedName("platformvendor")
        @Expose
        private String platformvendor;

        public String getPlatformname() {
            return platformname;
        }

        public void setPlatformname(String platformname) {
            this.platformname = platformname;
        }

        public String getPlatformversion() {
            return platformversion;
        }

        public void setPlatformversion(String platformversion) {
            this.platformversion = platformversion;
        }

        public String getPlatformvendor() {
            return platformvendor;
        }

        public void setPlatformvendor(String platformvendor) {
            this.platformvendor = platformvendor;
        }
    }

    public static class SoftwarePlatform {

        @SerializedName("name")
        @Expose
        private SoftwarePlatformName name;

        public SoftwarePlatformName getName() {
            return name;
        }

        public void setName(SoftwarePlatformName name) {
            this.name = name;
        }
    }

    public static class HardwarePlatformName {

        @SerializedName("hardwarename")
        @Expose
        private String hardwarename;

        public HardwarePlatformName() {}

        public HardwarePlatformName(String name) {
            this.hardwarename = name;
        }

        public String getHardwarename() {
            return hardwarename;
        }

        public void setHardwarename(String hardwarename) {
            this.hardwarename = hardwarename;
        }
    }

    public static class HardwarePlatformDevice {

        @SerializedName("issmartphone")
        @Expose
        private boolean issmartphone;

        @SerializedName("devicetype")
        @Expose
        private String devicetype;

        @SerializedName("istablet")
        @Expose
        private boolean istablet;

        @SerializedName("issmallscreen")
        @Expose
        private boolean issmallscreen;

        @SerializedName("ismobile")
        @Expose
        private boolean ismobile;

        public boolean isIssmartphone() {
            return issmartphone;
        }

        public void setIssmartphone(boolean issmartphone) {
            this.issmartphone = issmartphone;
        }

        public String getDevicetype() {
            return devicetype;
        }

        public void setDevicetype(String devicetype) {
            this.devicetype = devicetype;
        }

        public boolean isIstablet() {
            return istablet;
        }

        public void setIstablet(boolean istablet) {
            this.istablet = istablet;
        }

        public boolean isIssmallscreen() {
            return issmallscreen;
        }

        public void setIssmallscreen(boolean issmallscreen) {
            this.issmallscreen = issmallscreen;
        }

        public boolean isIsmobile() {
            return ismobile;
        }

        public void setIsmobile(boolean ismobile) {
            this.ismobile = ismobile;
        }
    }

    public static class HardwarePlatformScreen {

        @SerializedName("screenpixelswidth")
        @Expose
        private int screenpixelswidth;

        @SerializedName("screenpixelsheight")
        @Expose
        private int screenpixelsheight;

        public int getScreenpixelswidth() {
            return screenpixelswidth;
        }

        public void setScreenpixelswidth(int screenpixelswidth) {
            this.screenpixelswidth = screenpixelswidth;
        }

        public int getScreenpixelsheight() {
            return screenpixelsheight;
        }

        public void setScreenpixelsheight(int screenpixelsheight) {
            this.screenpixelsheight = screenpixelsheight;
        }
    }

    public static class HardwarePlatform {
        @SerializedName("name")
        @Expose
        private HardwarePlatformName name;

        @SerializedName("device")
        @Expose
        private HardwarePlatformDevice device;

        @SerializedName("screen")
        @Expose
        private HardwarePlatformScreen screen;

        public HardwarePlatformName getName() {
            return name;
        }

        public void setName(HardwarePlatformName name) {
            this.name = name;
        }

        public HardwarePlatformDevice getDevice() {
            return device;
        }

        public void setDevice(HardwarePlatformDevice device) {
            this.device = device;
        }

        public HardwarePlatformScreen getScreen() {
            return screen;
        }

        public void setScreen(HardwarePlatformScreen screen) {
            this.screen = screen;
        }
    }

    public static class Device {
        @SerializedName("softwarplatform")
        @Expose
        private SoftwarePlatform softwarplatform;

        @SerializedName("hardwarePlatform")
        @Expose
        private HardwarePlatform hardwarePlatform;

        @SerializedName("ip")
        @Expose
        private String ip;

        public SoftwarePlatform getSoftwarplatform() {
            return softwarplatform;
        }

        public void setSoftwarplatform(SoftwarePlatform softwarplatform) {
            this.softwarplatform = softwarplatform;
        }

        public HardwarePlatform getHardwarePlatform() {
            return hardwarePlatform;
        }

        public void setHardwarePlatform(HardwarePlatform hardwarePlatform) {
            this.hardwarePlatform = hardwarePlatform;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }

    public static class UserLocation {
        @SerializedName("longitude")
        @Expose
        private double longitude;

        @SerializedName("lattitude")
        @Expose
        private double lattitude;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLattitude() {
            return lattitude;
        }

        public void setLattitude(double lattitude) {
            this.lattitude = lattitude;
        }
    }

    public static class User {
        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("consents")
        @Expose
        private Consents consents;

        @SerializedName("attention")
        @Expose
        private Attention attention;

        @SerializedName("device")
        @Expose
        private Device device;

        @SerializedName("location")
        @Expose
        private UserLocation location;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Consents getConsents() {
            return consents;
        }

        public void setConsents(Consents consents) {
            this.consents = consents;
        }

        public Attention getAttention() {
            return attention;
        }

        public void setAttention(Attention attention) {
            this.attention = attention;
        }

        public Device getDevice() {
            return device;
        }

        public void setDevice(Device device) {
            this.device = device;
        }

        public UserLocation getLocation() {
            return location;
        }

        public void setLocation(UserLocation location) {
            this.location = location;
        }
    }

}
