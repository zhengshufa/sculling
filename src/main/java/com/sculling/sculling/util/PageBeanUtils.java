package com.sculling.sculling.util;

import lombok.extern.slf4j.Slf4j;


/**
 * @author hpdata
 * @DATE 2023/8/2416:24
 */
@Slf4j
public class PageBeanUtils {


	/**
	 * 将源PageBean的属性拷贝到目标PageBean中，并返回目标PageBean对象
	 *
	 * @param source      源PageBean对象
	 * @param targetClass 目标PageBean的Class
	 * @return 目标PageBean对象
	 */
	public static <S, T> Page<T> copyProperties(Page<S> source, Class<T> targetClass, BaseMapStruct mapStruct) {
		Page<T> target = new Page<>();
		target.setRecords(mapStruct.toDto(source.getRecords()));
		target.setCurrent(source.getCurrent());
		target.setSize(source.getSize());
		target.setPages(source.getPages());
		target.setTotal(source.getTotal());
		return target;
	}

}
