package org.cts.chess.txtchess.api;


/**
 * A condition interface which is being used to validate Parameters
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public interface Condition {
	
    Condition DEFAULT=new Condition() {

        @Override
        public boolean validate(String input) {
            return true;
        }
    };
    
    Condition NUMBER=new Condition() {
		
		@Override
		public boolean validate(String input) {
			if(input.equals("*"))
			{
				return true;
			}
			for(int i=0;i<input.length();i++)
			{
				char c=input.charAt(i);
				if(c<'0'||c>'9')
				{
					return false;
				}
			}
			return true;
		}
	};
    public boolean validate(String input);
}
