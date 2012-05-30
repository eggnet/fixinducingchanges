package fixinducingchanges;

public class Resources
{
	public static String bugDBName = "bugzilla-eclipse";
	public static String repoDBName = "agilefant";
	public static String repoBranch = "master";
	
	public static int convertLineStartToCharStart(int lineStart, String file) {
		int index = -1;
		int lineCount = 1;
		
		for(char c: file.toCharArray()) {
			if(c == '\n')
				lineCount++;
			else if(lineCount == lineStart) {
				index++;
				break;
			}
			index++;
		}
		
		return index;
	}
	
	public static int convertLineEndToCharEnd(int lineEnd, String file) {
		int index = -1;
		int lineCount = 1;
		
		for(char c: file.toCharArray()) {
			if(c == '\n' && lineCount == lineEnd) {
				index++;
				break;
			}
			if(c == '\n')
				lineCount++;
			index++;
		}
		
		return index;
	}
	
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
