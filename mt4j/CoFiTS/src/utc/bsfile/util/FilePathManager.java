package utc.bsfile.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FilePathManager {

	//Singleton
	static private FilePathManager instance = null;
	
	static public FilePathManager getInstance(){
		if (instance == null)
			instance = new FilePathManager();
		return instance;
	}
	
	
	static public void releaseInstance(){
		instance = null;
	}
	
	//Constructeurs
	private FilePathManager(){}
	
	//Méthodes
	
	public void createFolder(String path){
		File filepath = new File(path);
		createFolder(filepath);
	}
	
	
	/**
	 * @param filePath - path where the folder should be created
	 * Creates folders recursivelyaccording to the path in argument
	 */
	public void createFolder(File filePath){
		if (!filePath.exists()){
			filePath.mkdirs();
		} else {
			System.err.println(filePath.getAbsolutePath() + " already exists");
		}
	}
	
	
	public void deletePath(String path){
		File filePath = new File(path);
		deletePath(filePath);
	}
	
	
	/**
	 * @param filePath - path to the file/folder to be deleted
	 * Deletes recursively the folder passed in argument, or the file
	 */
	public void deletePath(File filePath){
		if (filePath.exists())
		{
			if (filePath.isDirectory()){
				deleteFolder(filePath);
			} else {
				deleteFile(filePath);
			}
		} else {
			System.err.println(filePath + " does not exists");
		}
	}


	private void deleteFolder(File filePath) {
		
		for (File file : filePath.listFiles()){
			if (file.isDirectory()){
				deleteFolder(file);
			} else {
				deleteFile(file);
			}
		}
		
		filePath.delete();		
	}


	private void deleteFile(File filePath) {
		filePath.delete();		
	}
	
	
	public void moveFile(String oldPath, String newPath){
		File oldPathFile = new File(oldPath), newPathFile = new File(newPath);
		moveFile(oldPathFile, newPathFile);
	}


	/**
	 * @param oldPathFile - path where the file/folder to be moved is located
	 * @param newPathFile - path to replace the oldPathFile
	 * Rename or move the file/folder passed in first argument to the second argument path
	 */
	public void moveFile(File oldPathFile, File newPathFile) {
		
		if(!oldPathFile.renameTo(newPathFile)){
			System.err.println("Failed to rename file : " + oldPathFile.getAbsolutePath() + " to : " + newPathFile.getAbsolutePath());
		}
		
	}
	
	
	public void createTextFile(String path, String content){
		createTextFile(new File(path), content);
	}
	
	
	/**
	 * @param filePath - The path where to create the file
	 * @param content - Text content of the new file
	 * Create a new text file and write some content in it
	 */
	public void createTextFile(File filePath, String content){
			//If file does not exists, create it			
			if (filePath.exists()) {
				System.err.println("The file : " + filePath.getAbsolutePath() + " already exists");
			} else {
				try {
					filePath.createNewFile();
				} catch (IOException e) {
					System.err.println("Unabled to create file : " + filePath.getAbsolutePath());
					e.printStackTrace();
				}
			}
 
			
	}
	
	
	public void appendTextFile(String filename, String content){
		appendTextFile(new File(filename), content);
	}
	
	
	/**
	 * @param filePath - The file to append text to
	 * @param content - text to be appended to file
	 * Append text to a file
	 */
	public void appendTextFile(File filePath, String content){
		if (filePath.exists()){
			System.err.println("File : " + filePath.getAbsolutePath() + " does not exist");
		} else {
			try { 
				FileWriter fw = new FileWriter(filePath.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(content);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void createBinaryFile(String filename, byte[] content){
		createBinaryFile(new File(filename), content);
	}
	
	/**
	 * @param filePath - The path where to create the file
	 * @param content - Binary content of the new file
	 * Create a new binary file and write some content in it
	 */
	public void createBinaryFile(File filePath, byte[] content){
		//If file does not exists, create it			
		if (filePath.exists()) {
			System.err.println("The file : " + filePath.getAbsolutePath() + " already exists");
		} else {
			try {
				filePath.createNewFile();
			} catch (IOException e) {
				System.err.println("Unabled to create File : " + filePath.getAbsolutePath());
				e.printStackTrace();
			}
			
			appendBinaryFile(filePath, content);
		}
	}
	
	
	public void appendBinaryFile(String filename, byte[] content){
		appendBinaryFile(new File(filename), content);
	}
	
	/**
	 * @param filePath - The file to append content to
	 * @param content - content to be appended to file
	 * Append binary content to a file
	 */
	public void appendBinaryFile(File filePath, byte[] content){
		
		if(!filePath.exists()){
			System.err.println("File : " + filePath.getAbsolutePath() + " does not exist");
		} else {
			FileOutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(filePath, true);
				outputStream.write(content);
				outputStream.close();
			} catch (FileNotFoundException e) {
				System.err.println("Unabled to open file : " + filePath.getAbsolutePath());
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Unabled to write into/close file : " + filePath.getAbsolutePath());
				e.printStackTrace();
			}
		}
	}
}
