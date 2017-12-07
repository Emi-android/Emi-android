package com.proj.emi.bean;

public class DeviceInfo {

	private String brand;// 厂商
	private String model;// 机型
	private String imei;
	private String imsi;
	private String locale;
	private int osVer;
	private int screenWidth;
	private int screenHeight;
	private int ramSize;// RAM容量
	private int romSize;// ROM容量

	public String getBrand() {
		return brand;
	}

	public DeviceInfo setBrand(String brand) {
		this.brand = brand;
		return this;
	}

	public int getOsVer() {
		return osVer;
	}

	public DeviceInfo setOsVer(int osVer) {
		this.osVer = osVer;
		return this;
	}

	public String getImei() {
		return imei;
	}

	public DeviceInfo setImei(String imei) {
		this.imei = imei;
		return this;
	}

	public String getImsi() {
		return imsi;
	}

	public DeviceInfo setImsi(String imsi) {
		this.imsi = imsi;
		return this;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public DeviceInfo setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
		return this;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public DeviceInfo setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
		return this;
	}

	public String getModel() {
		return model;
	}

	public int getRamSize() {
		return ramSize;
	}

	public DeviceInfo setModel(String model) {
		this.model = model;
		return this;
	}

	public DeviceInfo setRamSize(final int ramSize) {
		this.ramSize = ramSize;
		return this;
	}

	public int getRomSize() {
		return romSize;
	}

	public DeviceInfo setRomSize(final int romSize) {
		this.romSize = romSize;
		return this;
	}

	public String getLocale() {
		return locale;
	}

	public DeviceInfo setLocale(String locale) {
		this.locale = locale;
		return this;
	}

	@Override
	public String toString() {
		return "DeviceInfo [brand=" + brand + ", model=" + model + ", imei="
		        + imei + ", imsi=" + imsi + ", locale=" + locale + ", osVer="
		        + osVer + ", screenWidth=" + screenWidth + ", screenHeight="
		        + screenHeight + ", ramSize=" + ramSize + ", romSize="
		        + romSize + "]";
	}

}
