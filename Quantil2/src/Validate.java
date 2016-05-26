import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * <h1>Validate</h1>
 * The Validate Class is a utility class for the search log application
 * It is used to validate the command before executing the command
 * <p>
 * All the functions are static functions except the validateCommand and getDate method. 
 * 
 * @author Akshat Misra
 * @version 1.0
 * @since   2016-04-25 
 */
public class Validate {

	/**
	 * The Command entered by the user is validated. 
	 * @param command
	 * @return Boolean after validating all the parameters
	 */
	public static boolean validateCommand(String command) 
	{
		boolean isValid = true;
		String[] cmd = command.split(" ");

		if (cmd.length == 7) {
			isValid = isValid && validateIP(cmd[1]);
			isValid = isValid && validateCpuId(cmd[2].trim());
			isValid = isValid && validateStartTime(cmd[3]+" "+cmd[4]);
			isValid = isValid && validateEndTime(cmd[5]+" "+cmd[6]);
			if((cmd[3]+" "+cmd[4]).equals(cmd[5]+" "+cmd[6]))
			{
				System.out.println("Start and End Time cannot be same there should be a gap of 1 minute");
				System.out.println("Re-Enter the Query in the form <QUERY IP cpu_id time_start time_end> : or \"Exit\" to terminate");
				isValid = false;
			}
		} else {
			System.out.println("Invalid Query");
			System.out.println("Re-Enter the Query in the form <QUERY IP cpu_id time_start time_end> : or \"Exit\" to terminate");
		}
		return isValid;
	}

	/**
	 * Validate IP based on the ip in Log file
	 * @param ip
	 * @return 
	 */
	private static boolean validateIP(String ip) 
	{
		if(SearchLog.ipaddr.contains(ip))
			return true;
		System.out.println("The Ip address is invalid :");
		System.out.println("Re-Enter the Query in the form <QUERY IP cpu_id time_start time_end> : or \"Exit\" to terminate");
		return false;
	}

	/**
	 * Cpu Id can be either 1 or 0 
	 * @param cpuId
	 * @return
	 */
	private static boolean validateCpuId(String cpuId) 
	{
		try
		{
			int cpu_Id = Integer.parseInt(cpuId);
			if(cpu_Id >1 || cpu_Id <0)
			{
				System.out.println("Cpu Id is not valid : Id can be 0 or 1 and You entered : " + cpu_Id);
				System.out.println("Re-Enter the Query in the form <QUERY IP cpu_id time_start time_end> : or \"Exit\" to terminate");
				return false;
			}
		}
		catch(NumberFormatException e)
		{
			System.out.println("Cpu Id is not a number : " + cpuId);
			System.out.println("Re-Enter the Query in the form <QUERY IP cpu_id time_start time_end> : or \"Exit\" to terminate");
			return false;
		}
		return true;
	}

	/**
	 * Validate the Start time, 
	 * @param startTime
	 * @return false if the end time is not in range else true
	 * @exception ParseException
	 * @see ParseException
	 */
	private static boolean validateStartTime(String startTime) 
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date;
		try {
			date = df.parse(startTime);
			long epoch = date.getTime()/1000;
			if(epoch < SearchLog.epoch || epoch > SearchLog.maxepoch)
			{
				System.out.println("start time is not in range in the log file");
				System.out.println("Please Enter valid start time between "+ getDate(SearchLog.epoch) +" - "+ 
																			getDate(SearchLog.maxepoch-120) );
				System.out.println("Re-Enter the Query in the form <QUERY IP cpu_id time_start time_end> : or \"Exit\" to terminate");
				return false;
			}
		} catch (ParseException e) {
			//e.printStackTrace();
			System.out.println("Start time is not in valid format : Please follow \"yyyy-MM-dd HH:mm\" format");
			System.out.println("Re-Enter the Query in the form <QUERY IP cpu_id time_start time_end> : or \"Exit\" to terminate");
			return false;
		}
		return true;
	}

	/**
	 * Validate the end time
	 * @param endTime
	 * @return false if the end time is not in range else true.
	 * @exception ParseException
	 * @see ParseException
	 */
	private static boolean validateEndTime(String endTime) 
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date;
		try {
			date = df.parse(endTime);
			long epoch = date.getTime()/1000;
			if(epoch < SearchLog.epoch || epoch> SearchLog.maxepoch)
			{
				System.out.println("End time is not in range in the log file");
				System.out.println("Please Enter valid end time between "+ getDate(SearchLog.epoch + 60) +" - "+ 
						getDate(SearchLog.maxepoch-60) );
				System.out.println("Re-Enter the Query in the form <QUERY IP cpu_id time_start time_end> : or \"Exit\" to terminate");
				return false;
			}
		} catch (ParseException e) {
			System.out.println("End time is not in valid format : Please follow \"yyyy-MM-dd HH:mm\" format");
			System.out.println("Re-Enter the Query in the form <QUERY IP cpu_id time_start time_end> : or \"Exit\" to terminate");
			return false;
		}
		return true;
	}
	
	/**
	 * Converts the epoch to Date format using SimpleDateFormat("yyyy-MM-dd HH:mm")
	 * @param time as long value
	 * @return Date as String
	 */
	public static String getDate(long time) 
	{
		Date date = new Date(time*1000);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formatted = format.format(date);
        return formatted;
	}

}
