package org.consulo.php.lang.psi.resolve.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.consulo.php.lang.psi.PhpClass;
import org.consulo.php.lang.psi.PhpFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date Jun 17, 2008 10:56:45 PM
 */
public class PhpType implements Serializable
{
	Collection<PhpClass> classes = new ArrayList<PhpClass>();

	public PhpType(PhpClass... classes)
	{
		this.classes.addAll(Arrays.asList(classes));
	}

	public void addClasses(Collection<PhpClass> classes)
	{
		this.classes.addAll(classes);
	}

	public void addClass(PhpClass klass)
	{
		classes.add(klass);
	}

	@Nullable
	public PhpClass getType()
	{
		if(classes.size() == 1)
		{
			return classes.iterator().next();
		}
		return null;
	}

	@NotNull
	public Collection<PhpClass> getTypes()
	{
		return classes;
	}

	public Collection<PhpFunction> getMethods()
	{
		List<PhpFunction> methods = new ArrayList<PhpFunction>();
		for(PhpClass klass : classes)
		{
			Collections.addAll(methods, klass.getFunctions());
		}
		return methods;
	}

	public String toString()
	{
		StringBuilder str = new StringBuilder();
		for(PhpClass klass : classes)
		{
			str.append(klass.getName()).append("|");
		}
		String s = str.toString();
		if(classes.size() > 0)
		{
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}
}
