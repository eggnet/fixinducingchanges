package fixinducingchanges;

import java.util.List;

import models.Commit;
import db.FixInducingDB;
import db.SocialDB;
import db.TechnicalDB;

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
		FixInducingDB db = new FixInducingDB();
		TechnicalDB tDB = new TechnicalDB();
		SocialDB sDB = new SocialDB();
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
					tDB.setBranchID(args[1]);
					sDB.connect(args[2]);
					
					// Find fix inducing changes
					BugFinder finder = new BugFinder(sDB, tDB);
					finder.findBugs();
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
