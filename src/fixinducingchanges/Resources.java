package fixinducingchanges;

public class Resources
{	
	public static int DB_LIMIT = 2000;
	
	

	public static int convertCharToLine(int charNum, String file) {
		int index = -1;
		int lineCount = 1;

		for(char c: file.toCharArray()) {
			index++;
			if(index == charNum) 
				break;
			if(c == '\n')
				lineCount++;
		}

		return lineCount;
	}
}
