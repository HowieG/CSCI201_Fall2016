package game_logic;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Overwrite
{
	private BufferedReader br;
	private PrintWriter pw;
	private String filename;
	private int lineThatRatingStartsOn;
	private int numRatings;
	private int totalRatings;

	public Overwrite(String filename, int lineThatRatingStartsOn, int numRatings, int totalRating)
	{
		this.filename = filename;
		this.lineThatRatingStartsOn = lineThatRatingStartsOn;
		this.numRatings = numRatings;
		this.totalRatings = totalRating;
		overwriteFile();
	}

	public void overwriteFile()
	{
		try
		{
			br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			int lineCounter = 1;

			//copy all but the last two lines
			while (line != null && lineCounter != lineThatRatingStartsOn)
			{
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
				lineCounter++;
			}
			//append the new numRatings and totalRatings values
			sb.append(numRatings + "\n");
			sb.append(totalRatings);
			String everything = sb.toString();

			//append everything to the file (overwrite)
			pw = new PrintWriter(filename);
			pw.println(everything);
			pw.close();

		}
		catch (FileNotFoundException fnfe)
		{
			log("fnfe: " + fnfe.getMessage());
		}
		catch (IOException ioe)
		{
			log("ioe: " + ioe.getMessage());
		}
		finally
		{
			//close reader/writer
			close(br);
			close(pw);
		}
	}

	public void close(Closeable stream)
	{
		try
		{
			if (stream != null)
			{
				stream.close();
			}
		}
		catch (IOException ioe)
		{
			log("ioe: Issue closing file");
		}
	}

	private void log(String message)
	{
		System.out.println(message);
	}

}
