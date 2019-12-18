package examples;

public class Fighter {
	
	//Attributs
	private String name;
	private int damage;
	private int life;
	private String type;

	//Constructeur
	public Fighter(String name, int damage) {
		this.name = name;
		this.damage = damage;
		this.life = 100;
	}

	//Getter
	public String getName() {
		return this.name;
	}

	public int getDamage() {
		return this.damage;
	}

	public int getLife() {
		return this.life;
	}

	public String getType() {
		return this.type;
	}

	//Setter
	public void setName(String name) {
		this.name = name;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public void setType(String type) {
		this.type = type;
	}

	//Méthodes instanciées

	public String takeHit(Fighter name) {
		this.life = this.life - name.getDamage();
		if (this.life > 0) {
			return (this.name + " : " + this.getLife() + " points de vie restants");
		} else
			return (" KO pour " + this.name + " !");
	}

		//Affichage alternatif takeHit()
	/*public void takeHit(Monster name) {
		this.life = this.life - name.getDamage();
		String lifeLayout = "♥ " + Tools.repeat("|", this.life/3);
		if (this.life > 0) {
			System.out.println(BashColor.setRed(lifeLayout, 'b') + " " + this.name);
		} else
			System.out.println(this.name + " is KO !");
	}*/

	/*public void goToDungeon() {
		int[] loot = {1, 1, 1, 2, 2, 2, 3, 3, 3, 3};
		int random = ((int)(Math.random() * (loot.length)));
		if (loot[random] == 1) {
			setLife(this.life*150/100);
			setDamage(this.damage*8);
			System.out.println("\nCongratz !!! You've found the secret ring (+50% life, damage x 8)");
		} else if (loot[random] == 2) {
			setLife(this.life - this.life*20/100);
			System.out.println("\nOuch, too much monsters in there ! (-20% life)");
		} else {
			System.out.println("\nNothing in there... But your monster is fine !");
		}
	}*/

}
