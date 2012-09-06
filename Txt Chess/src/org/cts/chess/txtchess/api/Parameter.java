package org.cts.chess.txtchess.api;


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
	private String defaultOption;
	private boolean optional;
	private Condition condition;
	private String helpText;

	public Parameter(String name, String defaultOption,
			boolean optional, Condition condition,String helpText) {
		this.name = name;
		this.defaultOption = defaultOption == null ? "" : defaultOption;
		this.optional = optional;
		this.condition = (condition == null) ? Condition.DEFAULT : condition;
		this.helpText=helpText;
		assert this.condition.validate(this.defaultOption) : "Provided default option not satisfied for Condition";
	}

	public String getName() {
		return name;
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
