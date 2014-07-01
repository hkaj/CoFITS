package ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import main.RemoveAck;
import uc1.CreateAProjectBehaviour;
import uc1.RemoveAProjectBehaviour;
import uc3.AddAUserBehaviour;
import uc4.CreateASessionBehaviour;
import uc4.RemoveASessionBehaviour;
import agent.MainAgent;

public class MyFrame extends JFrame implements ActionListener{
	
	public JTextField first = new JTextField();
	public JTextField second = new JTextField();
	public JTextField third = new JTextField();
	public JLabel ans = new JLabel(" un projet");
	public JComboBox liste;
	public JComboBox IsAdmin;
	public JLabel QIsAdmin = new JLabel("Administrateur ?");
	public JButton ok = new JButton("OK");
	public int where = 0; //0 est project, 1 est session, 2 est ajouter un utilisateur
	public MainAgent myAgent;

	public MyFrame(MainAgent a){
		super();
		
		myAgent = a;
		
		build();
	}
 
	private void build(){
		
		
        
        where = 0;
		
		setTitle("CoFITS min"); 
		setSize(320,240); 
		setLocationRelativeTo(null);
		setResizable(false); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setLayout(new FlowLayout());
		
		Object[] elements = new Object[]{"Ajouter", "Supprimer"};
		liste = new JComboBox(elements);
		add(liste);
		
		add(ans);
		
		first.setColumns(25); 
		first.addFocusListener(new FocusListener() {
		    @Override
			public void focusGained(FocusEvent e) {
		    	first.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				
			}
		});
		
		add(first);		
		//placeholder : project name
		
		second.setColumns(25);
		second.addFocusListener(new FocusListener() {
		    @Override
			public void focusGained(FocusEvent e) {
		    	second.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				
			}
		});
		
		add(second);
		//placeholder : project description
		
		third.setColumns(25);
		add(third);
		third.addFocusListener(new FocusListener() {
		    @Override
			public void focusGained(FocusEvent e) {
		    	third.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				
			}
		});
		
		third.setVisible(false);
		
		liste.addItemListener(new ItemListener() {
            //
            // Listening if a new items of the combo box has been selected.
            //

			@Override
			public void itemStateChanged(ItemEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();

				Object item = e.getItem();


                if (e.getStateChange() == ItemEvent.SELECTED) {
                	if(item.toString().equals("Ajouter")){
                		switch (where) {
						case 0:
							CreateAProjectUI();
							break;
						case 1:
							CreateASessionUI();
							break;
						case 2:
							addAUserUI();
							break;
						default:
							break;
						}
                	} else if(item.toString().equals("Supprimer")){
                		System.out.println("delete");
                		switch (where) {
						case 0:
							RemoveAProjectUI();
							break;
						case 1:
							RemoveASessionUI();
							break;
						default:
							break;
						}
                	}
                }
			}
        });

		add(QIsAdmin);
		QIsAdmin.setVisible(false);
		
		Object[] yORn = new Object[]{"Oui", "Non"};
		IsAdmin = new JComboBox(yORn);
		add(IsAdmin);
		IsAdmin.setVisible(false);
		
		CreateAProjectUI();		
		add(ok);
		ok.addActionListener(this);	
	}

	public void CreateAProjectUI(){
		liste.setVisible(true);
		ans.setText(" un projet");
		
		first.setVisible(true);
		first.setText("Nom du projet");
		
		second.setVisible(true);
		second.setText("Description du projet");
		
		third.setVisible(false);
		IsAdmin.setVisible(false);
		QIsAdmin.setVisible(false);
	}
	
	public void RemoveAProjectUI(){
		liste.setVisible(true);
		ans.setText(" un projet");

		first.setVisible(true);
		first.setText("ID du projet");
		
		second.setVisible(false);
		third.setVisible(false);
		IsAdmin.setVisible(false);
		QIsAdmin.setVisible(false);
	}
	
	public void addAUserUI(){
		liste.setVisible(false);
		ans.setText("Ajouter un utilisateur");

		first.setVisible(true);
		first.setText("Login à ajouter");
		
		second.setVisible(true);
		second.setText("ID du projet");
		
		third.setVisible(false);
		IsAdmin.setVisible(true);
		QIsAdmin.setVisible(true);		
	}
	
	public void CreateASessionUI(){
		ans.setText(" une session");
		liste.setVisible(true);
		
		first.setVisible(true);
		first.setText("ID du projet");
		
		second.setVisible(false);
		third.setVisible(false);
		IsAdmin.setVisible(false);
		QIsAdmin.setVisible(false);	
	}
	
	public void RemoveASessionUI() {
		ans.setText(" une session");
		liste.setVisible(true);

		first.setVisible(true);
		first.setText("ID du projet");
		
		second.setVisible(true);
		second.setText("ID de la session");
		
		third.setVisible(false);
		IsAdmin.setVisible(false);
		QIsAdmin.setVisible(false);	
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		 
		if(source == ok){
			AddBehaviour(where);
		}
	}
	
	public void AddBehaviour(int toDo){
		
		if(where == 0){
			System.out.println("projet");
			if(liste.getSelectedIndex()==0){
				System.out.println("ajouter ");
				try{
				CreateAProjectBehaviour b1 = new CreateAProjectBehaviour(first.getText(), second.getText());
				myAgent.addBehaviour(b1);
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			else if(liste.getSelectedIndex()==1){
				System.out.println("remove");
				RemoveAProjectBehaviour b1 = new RemoveAProjectBehaviour(first.getText());
				myAgent.addBehaviour(b1);
			}
			else{
				System.out.println("unknown situation");
			}
			CreateASessionUI();
			where++;			
		} else if(where == 1){
			System.out.println("session");
			if(liste.getSelectedIndex()==0){
				System.out.println("ajouter");
				CreateASessionBehaviour b1 = new CreateASessionBehaviour(first.getText());
				myAgent.addBehaviour(b1);
			}
			else if(liste.getSelectedIndex()==1){
				System.out.println("remove");
				RemoveASessionBehaviour b1 = new RemoveASessionBehaviour(first.getText(), second.getText());
				myAgent.addBehaviour(b1);
			}
			else{
				System.out.println("unknown situation");
			}
			addAUserUI();
			where++;
		} else if(where == 2){
			System.out.println("utilisateur");
			if(liste.getSelectedIndex()==0){
				System.out.println("ajouter");
				String admin = new String("false");
				if(IsAdmin.getSelectedIndex() == 0)
					admin = "true";
				AddAUserBehaviour b1 = new AddAUserBehaviour(first.getText(), admin, second.getText());
				myAgent.addBehaviour(b1);
			}
			else{
				System.out.println("unknown situation");
			}
			CreateAProjectUI();
			where = 0;
		}
		
	}
	
}
