package org.cts.chess.txtchess.api;


/**
 *
 * @author Mohan
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
