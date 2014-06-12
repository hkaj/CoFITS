package utc.bsfile.util;

import java.io.BufferedWriter;
import java.io.File;
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
		try { 
			//If file does not exists, create it			
			if (!filePath.exists()) {
				filePath.createNewFile();
			}
 
			FileWriter fw = new FileWriter(filePath.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
