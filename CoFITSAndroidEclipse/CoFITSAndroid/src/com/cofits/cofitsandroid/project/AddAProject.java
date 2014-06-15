package com.cofits.cofitsandroid.project;

import com.cofits.cofitsandroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddAProject extends Activity{

	Button createProject;
	String projectName = "";
	String projectDescription = "";
	
	EditText nameProject, descriptionProject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_project);
		
		  createProject = (Button) findViewById(R.id.create_project_button);
	      createProject.setOnClickListener(createProjectListener);
	      
	      nameProject = (EditText) findViewById(R.id.project_name);
	      descriptionProject = (EditText) findViewById(R.id.project_description);
		
	}
	
    private View.OnClickListener createProjectListener = new View.OnClickListener() {
        public void onClick(View v) {
        	
        	String name = nameProject.getText().toString();
        	String description = descriptionProject.getText().toString();
        	
        	System.out.println(name);
        	System.out.println(description);
        	
        	//todoagent
        	
        	
        	onBackPressed();
        }
       };
	
	
}
