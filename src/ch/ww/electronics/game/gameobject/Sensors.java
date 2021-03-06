package ch.ww.electronics.game.gameobject;

import java.util.ArrayList;

public class Sensors{
	private Animal animal;
	public Sensors(Animal animal){
		this.animal=animal;
	}
	public ArrayList<Animal> getEyeInput(){
		ArrayList<Animal> animals= new ArrayList<>();
		animal.getGame().getLevel().getObjectsInLevel().forEach((GameObject go) -> {
			if(go instanceof Animal) {
				Animal a = (Animal) go;
				if(this.animal!=a)
					if(this.animal.distanceTo(a) <= animal.getDNA().get(DNA.VIEWRANGE)) 
						animals.add(a);
			}
		});
		animals.sort((a1, a2) -> {return (int) Math.signum(animal.distanceTo(a1) - animal.distanceTo(a2));});
		return(animals);
	}
	
	
}
