package GestionRequetes;

import java.util.ArrayList;
import java.util.Iterator;
//idée : Ajout d'un DP state pour passer d'un mode ajout à un mode lecture
public class ConteneurResultats<T> //Utiliser un générique ici ou renvoyer un objet puis faire un cast ?
{
	ArrayList<T> res;
	
	public ConteneurResultats(int taille)
	{
		super();
		this.res.ensureCapacity(taille);
	}
	
	public void ajouter(T obj)
	{
		this.res.add(obj);
	}
	
	public Iterator<T> iterateur()
	{
		return this.res.iterator();
	}
}
