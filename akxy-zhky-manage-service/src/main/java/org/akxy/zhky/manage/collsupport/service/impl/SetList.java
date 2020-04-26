package org.akxy.zhky.manage.collsupport.service.impl;

import java.util.LinkedList;

class SetList<T> extends LinkedList<T>{

	private static final long serialVersionUID = 3612971767507405567L;

	@Override
	public boolean add(T object) {
		if (size() == 0) {
			return super.add(object);
		} else {
			int count = 0;
			for (T t : this) {
				if (t.equals(object)) {
					count++;
					break;
				}
			}
			if (count == 0) {
				return super.add(object);
			} else {
				return false;
			}
		}
	}
}