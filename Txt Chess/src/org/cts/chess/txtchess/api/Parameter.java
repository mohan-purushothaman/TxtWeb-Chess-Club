package org.cts.chess.txtchess.api;


/**
 * Respresents a parameter to a txtweb service ( support class for ParameterHandlerServlet )
 * 
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
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
