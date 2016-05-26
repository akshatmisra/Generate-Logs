
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <h1>GenerateLog</h1>
 * The Generate Log program implements an application that
 * Generates the Log file for 1 day from 2014-10-31 00:00 till 2014-10-31 23:59
 * <p>
 * All the functions are private functions except the constructor, main and generateLog
 * To include the information about the generateLog method in javadoc it is made public
 * 
 * @author Akshat Misra
 * @version 1.0
 * @since   2016-04-25 
 */
public class GenerateLog {

	static String fileName = "log.txt";
	static long epoch = 0;
	static long maxepoch = 0;
	static ArrayList<String> ipaddr = new ArrayList<String>();
	static long timecounter = 0;

	/**
	 * Constructor takes the argument and creates the directory if not already created. 
	 * In case of an IOException, terminates the program
	 * @param path Directory location provided as a command line parameter while running the program
	 * @exception IOException if the directory cannot be created
	 * @see IOException
	 */
	public GenerateLog(String path) {
		Path p = Paths.get(path);
		// method should not follow symbolic links in the file system to
		// determine if the path exists.
		if (!Files.exists(p, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
			try {
				Files.createDirectories(p);
			} catch (IOException e) {
				// e.printStackTrace();
				System.out.println("Cannot create directory");
				System.exit(-1);
			}
		}

		fileName = path + "/log.txt";
	}

	/**
	 * Creates the GenerateLog class object
	 * @param args 1 argument required directory location. 
	 * if more arguments are passed, program will exit with "Arguments not valid" message.
	 * if no argument is passed, program will exit with "Please provide the Directory path" message.
	 */
	public static void main(String args[]) {
		if(args.length == 1)
		{
			GenerateLog generateLog = new GenerateLog(args[0]);
			generateLog.initialize();
		}
		else if (args.length >1)
		{
			System.out.println("Arguments not valid");
			System.exit(-1);
		}
		else
		{
			System.out.println("Please provide the Directory path");
			System.exit(-1);
		}
			
	}

	/**
	 * Creates the file in the directory passed as an argument
	 * Logs the entries for each server in log.txt by calling generateLog method for both CPU id
	 * @exception ParseException if the dates are not parsed
	 */
	private void initialize() {
		File f = new File(fileName);
		if (f.exists()) {
			f.delete();
		}

		String str = "2014-11-31 00:00";
		String str2 = "2014-11-31 23:59";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date;
		try {
			date = df.parse(str);
			epoch = date.getTime() / 1000;
			date = df.parse(str2);
			maxepoch = date.getTime() / 1000;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		generateIp();
		timecounter = epoch;
		while (timecounter <= maxepoch) {
			System.out.println("Writing to a file + " + fileName);

			for (String ip : ipaddr) {
				this.generateLog(String.valueOf(timecounter), ip, 0, generateUsage());
				this.generateLog(String.valueOf(timecounter), ip, 1, generateUsage());
			}
			timecounter = generateTime();
		}
		System.out.println("File write complete");
	}

	/**
	 * Generates the time for every minute
	 * @return Next Timestamp
	 */
	private static long generateTime() {
		timecounter += 60;
		return timecounter;
	}

	/**
	 * Generates the Ip Address for 1000 servers
	 */
	private static void generateIp() {
		for (int j = 0; j < 3; j++) {
			for (int i = 1; i < 255; i++) {
				ipaddr.add("192.168." + j + "." + i);
			}
		}
		for (int i = 1; i < 239; i++) {
			ipaddr.add("192.168.3." + i);
		}
	}

	/**
	 * Generates the CPU usage for every core
	 * @return Random usage value
	 */
	private static int generateUsage() {
		Random r = new Random();
		int low = 0;
		int high = 100;
		return r.nextInt(high - low) + low;
	}

	/**
	 * Enters the log entries to the log file log.txt
	 * @param time Timestamp of the log
	 * @param ip Server IP address
	 * @param cpuId CPU Id
	 * @param usage CPU usage
	 * @exception FileNotFoundException
	 * @exception IOException
	 * @see FileNotFoundException
	 * @see IOException
	 */
	public void generateLog(String time, String ip, int cpuId, int usage) {
		// log for all 1000 servers in a single file
		PrintWriter pw = null;
		try {

			pw = new PrintWriter(new FileWriter(fileName, true));

			StringBuilder sb = new StringBuilder();
			sb.append(time + " ");
			sb.append(ip + " ");
			sb.append(String.valueOf(cpuId) + " ");
			sb.append(String.valueOf(usage));
			pw.println(sb.toString());
			pw.flush();

		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			System.out.println("File not found exception");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("IOException");
		} finally {
			if (pw != null)
				pw.close();
		}
	}

}
