package swingTableFromCSVFile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

public class Csv2TableModel implements ActionListener {
	static String dataFileName = "";

	static JTable table = null;

	public static void main(String[] args) {
		try {
			// Read a csv file, present it
			// save it to another file named 'generatedOutput.csv'
//            dataFileName = "data/hospitalsFromOsm.countries.csv";
//            dataFileName = "data/covid19.csv";
			dataFileName = "data/covid2.csv";
			FileReader fin = new FileReader(dataFileName);
			DefaultTableModel ourDataModel = createTableModel(fin, null);
			JFrame mainFrame = new JFrame();
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// create Layout for Main Pane

			mainFrame.setLayout(new BorderLayout());
			table = new JTable(ourDataModel);
			//mainFrame.add(new JScrollPane(table), BorderLayout.CENTER);
			var mainLayout = new JPanel(new GridLayout(2, 1));
			var upperRow = new JPanel(new GridLayout(1, 2));
			var topRight = new JPanel(new BorderLayout());

			// defaultPanes
			var mid = new JPanel();

//          Behälter für unten schaffen
			var panelUnten = createButtonPane(ourDataModel);
			topRight.add(new JScrollPane(table), BorderLayout.CENTER);
			topRight.add(panelUnten, BorderLayout.PAGE_END);

			var chart = createAndShowGUI(ourDataModel);

			//mainFram befüllen
			upperRow.add(new JScrollPane(chart));
			upperRow.add(topRight);
			mainLayout.add(upperRow);
			mainLayout.add(new JScrollPane(mid));
			//mainLayout.add(panelUnten, BorderLayout.PAGE_END);

			mainFrame.setContentPane(mainLayout);
			mainFrame.pack();

//          und dann dem JFrame adden 
			//mainFrame.add(panelUnten, BorderLayout.PAGE_END);

			mainFrame.setSize(1600, 800);
			mainFrame.setVisible(true);

			FileWriter out = new FileWriter("data/generatedOutput.csv");
			defaultTableModelToStream(ourDataModel, out);
			out.close();
			// createClassDefinitionForTableModel(ourDataModel, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static ArrayList<Double> extractColumnValuesAsDoubleList(DefaultTableModel ourDataModel, int spaltenNummer) {
		ArrayList<Double> werteVonSpalte3 = new ArrayList<Double>();
		for (int zeilenNummer = 0; zeilenNummer < ourDataModel.getRowCount(); zeilenNummer++) {
			String s = (String) ourDataModel.getValueAt(zeilenNummer, spaltenNummer);
			s = s.replace(",", ".");
			double wert = Double.parseDouble(s);
			werteVonSpalte3.add(wert);
		}
		return werteVonSpalte3;
	}

	/**
	 *
	 * @param dtm The DefaultTableModel to save to stream
	 * @param out The stream to which to save
	 *
	 */
	public static void defaultTableModelToStream(DefaultTableModel dtm, Writer out) throws IOException {
		final String LINE_SEP = System.getProperty("line.separator"); // Zeilenumbruch
		int numCols = dtm.getColumnCount();
		int numRows = dtm.getRowCount();

		// Write headers
		String sep = "";

//        schleife für spalten 
		for (int i = 0; i < numCols; i++) {
			out.write(sep);
			out.write(dtm.getColumnName(i)); // getColumnName gibt datennamen an
												// und frägt wie sie heißen
			sep = ",";
		}

		out.write(LINE_SEP);

//        Ausgabe der Datei 

		for (int r = 0; r < numRows; r++) {
			sep = "";

			for (int c = 0; c < numCols; c++) {
				out.write(sep);
				out.write(dtm.getValueAt(r, c).toString());
				sep = ",";
			}

			out.write(LINE_SEP);
		}
	}

	/**
	 *
	 *
	 * @param in      A CSV input stream to parse
	 * @param headers A Vector containing the column headers. If this is null, it's
	 *                assumed that the first row contains column headers
	 *
	 * @return A DefaultTableModel containing the CSV values as type String
	 */
	public static DefaultTableModel createTableModel(Reader in, Vector<Object> headers) {
		DefaultTableModel model = null;
		Scanner s = null;

		try {
			Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
			s = new Scanner(in);

			while (s.hasNextLine()) {
				rows.add(new Vector<Object>(Arrays.asList(s.nextLine().split("\\s*;\\s*", // zerhackt die datei mit ;
						-1))));
			}

			if (headers == null) { // noch keine Spaltenüberschriften
				headers = rows.remove(0); // die erste zeile als überschrift
//                Datenmodell befüllt
				model = new DefaultTableModel(rows, headers); // rows = daten; headers = überschrift
			} else {
				model = new DefaultTableModel(rows, headers);
			}

			return model;
		} finally {
			s.close();
		}
	}

	
	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed");
	}

	public static JPanel createAndShowGUI(DefaultTableModel data)
	{
		var colorList = new Color[] {Color.BLUE, Color.RED, Color.YELLOW, Color.ORANGE, Color.MAGENTA, Color.CYAN};
		HistogramPanel panel = new HistogramPanel();
		var random = new Random();

		for (var i = 0; i < data.getRowCount(); i++) {
			var name = (String) data.getValueAt(i, 0);
			var amount = (String) data.getValueAt(i, 1);
			panel.addHistogramColumn(name, Integer.parseInt(amount), colorList[random.nextInt(colorList.length)]);
		}

		panel.layoutHistogram();

		return panel;
	}

	public static JPanel createButtonPane(DefaultTableModel ourDataModel) {
		JPanel panelUnten = new JPanel();

//          Befüllen
		JButton addButton = new JButton("add");
		JButton deleteButton = new JButton("delete");
		JButton outputButton = new JButton("output");

//          Listener zuordnen jeder Button ein Listener
		addButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("innen");
				Vector<Object> v = new Vector<Object>();
				int anzahlElementeInZeile = ourDataModel.getColumnCount();
//					 v befüllen, so dass eine Zeile gefüllt werden kann
				for (int i = 0; i < anzahlElementeInZeile; i++) {
					v.add(new String("Test"));
				}
//					neue Zeile hinzufügen
				ourDataModel.addRow(v);
			}
		});
		deleteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("innen Delete");
				if (table.getSelectedRow() != -1) { // Ist eine Zeile selektiert?
					ourDataModel.removeRow(table.getSelectedRow());
				}

			}
		});
		outputButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("innen output");
//					Spalte auslesen
				int spaltenNummer = 2;
				ArrayList<Double> werteVonSpalte2 = extractColumnValuesAsDoubleList(ourDataModel, spaltenNummer);

				spaltenNummer = 3;
				ArrayList<Double> werteVonSpalte3 = extractColumnValuesAsDoubleList(ourDataModel, spaltenNummer);

				System.out.println("-----------Ergebnis-----------------");
//					Berechnung in ArrayList speichern
				ArrayList<Double> ergebnisListe = new ArrayList<Double>();
				for (int i = 0; i < werteVonSpalte2.size(); i++) {
					double ergebnisInEinerZeile = werteVonSpalte2.get(i) / (werteVonSpalte3.get(i)*10);
					// runden auf 2 Nachkommastellen
					// siehe https://wiki.byte-welt.net/wiki/Flie%C3%9Fkommazahlen_mit_Java_runden
					ergebnisInEinerZeile = ergebnisInEinerZeile * 100;
					ergebnisInEinerZeile = ((long) ergebnisInEinerZeile) / 100.0;
					ergebnisListe.add(ergebnisInEinerZeile); // in ergebnisListe eintragen
				}

//					for (Double d : ergebnisListe) { // Testausgabe
//						System.out.println(d);
//					}

				// datei wird als neue zeile mit output hinzugefügt
				ourDataModel.addColumn("relative Häufigkeit", ergebnisListe.toArray());

				Diagramm balkenDiagramm = new Diagramm("COVID-Inzidenzen");

//					JFrame balkendiagramm = new JFrame("Balkendiagramm");
//					balkendiagramm.setBounds(200, 200, 600, 400);
//					balkendiagramm.setVisible(true);
			}

		});


		panelUnten.add(addButton);
		panelUnten.add(deleteButton);
		panelUnten.add(outputButton);

		return panelUnten;
	}
}

