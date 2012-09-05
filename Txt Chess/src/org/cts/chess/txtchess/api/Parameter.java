package org.cts.chess.txtchess.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * 
 * @author Mohan
 */
public class Parameter {

	private String name;
	private Set<String> options;
	private String defaultOption;
	private boolean optional;
	private Condition condition;
	private String helpText;

	@SuppressWarnings("unchecked")
	public Parameter(String name, String[] options, String defaultOption,
			boolean optional, Condition condition,String helpText) {
		this.name = name;
		this.options = options == null ? Collections.EMPTY_SET : new HashSet<String>(Arrays.asList(options));
		this.defaultOption = defaultOption == null ? "" : defaultOption;
		this.optional = optional;
		this.condition = (condition == null) ? Condition.DEFAULT : condition;
		this.helpText=helpText;
		for (String string : this.options) {
			assert this.condition.validate(string) : "Provided Option  not satisfied for condition";
		}
		assert this.condition.validate(this.defaultOption) : "Provided default option not satisfied for Condition";
	}

	public String getName() {
		return name;
	}

	public Set<String> getOptions() {
		return options;
	}

	public String getDefaultOption() {
		return defaultOption;
	}

	public boolean isOptional() {
		return optional;
	}

	public Condition getCondition() {
		return condition;
	}

	public String getHelpText() {
		return helpText;
	}

	public String detectParam(String possibleParam)
			throws ParameterNotPresentException {

		if (possibleParam == null || !getCondition().validate(possibleParam)) {
			if (isOptional()) {
				return getDefaultOption();
			} else {
				throw new ParameterNotPresentException(this);
			}
		}
		return possibleParam;
	}
}
