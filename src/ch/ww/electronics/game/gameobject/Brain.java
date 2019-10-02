package ch.ww.electronics.game.gameobject;

import java.util.ArrayList;
import java.util.Random;

import javax.print.attribute.standard.MediaSize.Engineering;

import ch.ww.electronics.game.level.Level;
import ch.ww.electronics.util.MutableVector2D;
import ch.ww.electronics.util.Vector2D;

public class Brain {
	public enum Status {IDLE, CHASING, SEARCHING_FOOD};
	
	private final Animal animal;
	private final Random r;
	private final DNA dna;
	
	private final Sensors sensors;
	private double facingangle;
	
	private Status status;
	
	private Animal target;
	
	public Brain(Animal animal) {
		this.animal = animal;
		this.sensors = new Sensors(this.animal);
		r = animal.getRandom();
		dna = animal.getDNA();
		this.target = null;
		status = Status.IDLE;
	}

	public void think() {
		ArrayList<Animal> nearby = sensors.getEyeInput();
		if(animal.getEnergy()/dna.getMaxEnergy() < 1 && status != Status.CHASING) {
			status = Status.SEARCHING_FOOD;
		}
		status=Status.IDLE;
		switch(status) {
		case IDLE:
			idle(nearby);
			break;
		case CHASING:
			chasing(nearby);
			break;
		case SEARCHING_FOOD:
			searchingFood(nearby);
			break;
		default:
			throw new RuntimeException("Should not reach this step");
		}
	}
	
	private void idle(ArrayList<Animal> nearby) {
		if(animal.getMotion().getLength() == 0 && animal.getLevel().getRandom().nextDouble() < 0.1) {
			double maxspeed = dna.getSize();
			double speed=animal.getGame().getRandom().nextDouble()*maxspeed;
			double  angle=animal.getGame().getRandom().nextDouble()*2*Math.PI;
			animal.setMotion(new Vector2D(speed*Math.sin(angle),speed*Math.cos(angle)));
		} else if(animal.getLevel().getRandom().nextDouble() < 0.1) {
			animal.setMotion(new Vector2D(0,0));
		}
		if(getAnimal().getEnergy()/getAnimal().getDNA().getMaxEnergy()>0.8){
			Animal baby = new Animal(animal.getLevel(), animal.getX(), animal.getY());
			baby.getDNA().variate(0);
			baby.setEnergy(baby.getEnergy()/2);
			getAnimal().setEnergy(getAnimal().getEnergy()/3);
			
		}
	}
	
	private void chasing(ArrayList<Animal> nearby) {
		if(this.animal.isTouching(target)) {
			animal.getLevel().fight(this.animal, target);
			System.out.println("Kill");
			status = Status.IDLE;
		} else {
			MutableVector2D v = new MutableVector2D(target.getX() - this.animal.getX(), target.getY() - this.animal.getY());
			double factor = dna.getMaxSpeed() / v.getLength();
			v = new MutableVector2D(v.getX() * factor, v.getY() * factor);
			animal.setMotion(v);
		}
	}
	
	private void searchingFood(ArrayList<Animal> nearby) {
		if(nearby.size() > 0) {
			target = nearby.get(animal.getRandom().nextInt(nearby.size()));
			status = Status.CHASING;
		} else {
			status = Status.IDLE;
			target = null;
		}
	}
	
	public Animal getAnimal() {
		return animal;
	}
	public double getFacingAngle(){
		return (facingangle);
	}
}