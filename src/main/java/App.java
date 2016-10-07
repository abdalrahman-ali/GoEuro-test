import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class App
{
	public static void main(String[] args)
	{

		String url = "http://api.goeuro.com/api/v2/position/suggest/en/";

		JSONArray array = null;
		try
		{
			url += args[0];
			array = new JSONArray(
					IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
		} catch( ConnectException | UnknownHostException e)
		{
			System.out.println(
					"Couldn't connect to the API URL, please check the internet"
							+ " connection.\nfollowing is the stacktrace:\n");
			e.printStackTrace();
			return;
		} catch( IOException e)
		{
			System.out.println(
					"IOException occurred.\nfollowing is the stacktrace:\n");
			e.printStackTrace();
			return;
		} catch( ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Atleast one argument should be provided.");
			return;
		}

		try
		{
			FileWriter fileWriter = new FileWriter("Output.csv");

			if(array.length() == 0)
			{
				fileWriter.close();
				System.out.println("No Results [empty JSON responce].");
				return;
			}

			for(int i = 0; i < array.length(); i++)
			{
				JSONObject current = array.getJSONObject(i);
				JSONObject current_geoLocation = (JSONObject) current
						.get("geo_position");

				String line = current.get("_id") + ", " + current.get("name")
						+ ", " + current.get("type") + ", "
						+ current_geoLocation.get("latitude") + ", "
						+ current_geoLocation.get("longitude");

				fileWriter.append(line + "\n");

			}
			fileWriter.flush();
			fileWriter.close();
		} catch( IOException e)
		{
			System.out.println(
					"An error occurred trying to write the output file.\nfollowing is the stacktrace:\n");
			e.printStackTrace();
		}

	}
}
