/**
 * DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE Version 2, December 2004
 * 
 * Copyright (C) 2014 http://popofibo.com <popo.fibo@gmail.com>
 * 
 * Everyone is permitted to copy and distribute verbatim or modified copies of
 * this code, and changing it is allowed as long as the name is changed.
 * 
 * DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE TERMS AND CONDITIONS FOR COPYING,
 * DISTRIBUTION AND MODIFICATION
 * 
 * 0. You just DO WHAT THE FUCK YOU WANT TO.
 * 
 */
package com.popofibo.plugins.jaxb;

import java.text.Normalizer;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.Outline;
import com.sun.xml.bind.api.impl.NameConverter;

/**
 * {@link Plugin} that normalized the names of JAXB generated artifacts
 * 
 * @author popofibo
 */
public class NormalizeElements extends Plugin {

	/**
	 * Set the command line option
	 */
	@Override
	public String getOptionName() {
		return "normalize";
	}

	/**
	 * Usage content of the option
	 */
	@Override
	public String getUsage() {
		return "  -normalize    :  normalize the classes and method names generated by removing the accented characters";
	}

	/**
	 * Set the name converted option to a delegated custom implementation of
	 * NameConverter.Standard
	 */
	@Override
	public void onActivated(Options opts) throws BadCommandLineException {
		opts.setNameConverter(new NonAsciiConverter(), this);
	}

	/**
	 * Always return true
	 */
	@Override
	public boolean run(Outline model, Options opt, ErrorHandler errorHandler)
			throws SAXException {
		return true;
	}

}

/**
 * 
 * @author popofibo
 * 
 */
class NonAsciiConverter extends NameConverter.Standard {

	/**
	 * Override the generated class name
	 */
	@Override
	public String toClassName(String s) {
		String origStr = super.toClassName(s);
		return normalize(origStr);
	}

	/**
	 * Override the generated property name
	 */
	@Override
	public String toPropertyName(String s) {
		String origStr = super.toPropertyName(s);
		return normalize(origStr);
	}

	/**
	 * Override the generated variable name
	 */
	@Override
	public String toVariableName(String s) {
		String origStr = super.toVariableName(s);
		return normalize(origStr);
	}

	/**
	 * Override the generated interface name
	 */
	@Override
	public String toInterfaceName(String s) {
		String origStr = super.toInterfaceName(s);
		return normalize(origStr);
	}

	/**
	 * Match the accented characters within a String choosing Canonical
	 * Decomposition option of the Normalizer, regex replaceAll using non POSIX
	 * character classes for ASCII
	 * 
	 * @param accented
	 * @return normalized String
	 */
	private String normalize(String accented) {
		String normalized = Normalizer.normalize(accented, Normalizer.Form.NFD);
		normalized = normalized.replaceAll("[^\\p{ASCII}]", "");
		return normalized;
	}
}
