package cn.rkang.wxgate.util;

import java.lang.annotation.ElementType;

import javax.validation.Path;
import javax.validation.TraversableResolver;

/**
 * 使用hibernate validator，在tomcat下部署没有问题，发布到weblogic(11g)在使用验证时报错：java.lang.AbstractMethodError
 * http://ljhzzyx.blog.163.com/blog/static/3838031220132159362372/
 */
public class ExtTraversableResolver implements TraversableResolver {

	@Override
	public final boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType,
			Path pathToTraversableObject, ElementType elementType) {
		return true;
	}

	@Override
	public final boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType,
			Path pathToTraversableObject, ElementType elementType) {
		return true;
	}
}