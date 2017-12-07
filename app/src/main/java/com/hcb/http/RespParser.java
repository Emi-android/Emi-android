package com.hcb.http;

import java.io.InputStream;

public interface RespParser<T> {
	T parse(InputStream is);
}
