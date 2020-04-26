package org.akxy.zhky.manage.redis.service;

/**
 * @ClassName: Function.java
 * @Description:
 * @date: 2018年6月14日
 */
public interface Function<T,E> {
	public T callback(E e);
}
