package com.donann.utils;

import java.util.UUID;

public class UUIDUtil{
	
	public static String getuuid() {
		UUID uuid=UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}
}
