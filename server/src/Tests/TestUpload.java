package Tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import Constants.RequestConstants;
import ModelObjects.Document;
import Requests.UploadRequest;

public class TestUpload
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		final Document doc = new Document(0,"fichier_texte",".txt");
		
		final Path path = Paths.get(RequestConstants.clientAgentDirectory+doc.getName()+doc.getType());
		byte[] content = null;
		try
		{
			content= Files.readAllBytes(path);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		final UploadRequest request = new UploadRequest(doc, content);
		final String requestJson = request.toJSON();
		System.out.println("Après convertion :"+requestJson);
	}

}
