package fixinducingchanges;

import db.FixInducingDB;
import db.SocialDb;

public class Main
{
	/**
	 * This is the entry point on the fix inducing changes project.
	 * Need to pass in two parameters [START] [END] which are
	 * two commit IDs for which to find all fix inducing changes
	 * in their span.
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("FixInducingChanges tool developed by eggnet.");
		FixInducingDB tDB = new FixInducingDB();
		SocialDb sDB 	 = new SocialDb();
		try {
			if (args.length < 3 )
			{
				System.out.println("Retry: FixInducingChanges [technicalDB] [branch] [socialDB]");
				throw new ArrayIndexOutOfBoundsException();
			}
			else
			{
				try 
				{
					// Setup the DB connections
					tDB.connect(args[0]);
					tDB.setBranchName(args[1]);
					sDB.connect(args[2]);
					
					// Find fix inducing changes
					BugFinder finder = new BugFinder(sDB, tDB);
					finder.findBugs();
					
					
					// Close the DB connections
					tDB.close();
					sDB.close();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				} 
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Please consult the usage above.");
		}
	}
}
