package swingTableFromCSVFile;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;

public class Diagramm extends JFrame {
	protected String title = "";
	protected int x = 100;
	protected int y = 100;
	protected int width = 600;
	protected int height = 400;

	public Diagramm(String title) {
		super();
		this.title = title;
		this.setBounds(x, y, width, height);
		this.setTitle(this.title);
		this.setVisible(true);

	}

	// Merke: Ein Objekt malt sich selbst!!!
	// Dazu braucht es die Methode paint (oder paintComponent)
	public void paint(Graphics g) {
		// in paint: zumeist die paint-Methode der Elternklasse aufrufen
		super.paint(g);
		// g = "Zeichnungs-Kontext"
		// in paint: nahezu immer g in ein Graphics2D-Objekt umwandeln ("casten")
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawString("COVID-19", 100, 100);
	}
}