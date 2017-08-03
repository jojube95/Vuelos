package application.vista;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import application.Main;
import application.data.Data;
import application.model.Airport;
import application.model.AirportFlights;
import application.util.DateTimeUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

public class VentanaPrincipalController {
	public static Stage secundarioStage;
	@FXML
    private PieChart vuelCompGen;
	@FXML
    private PieChart vuelAvEsp;
	@FXML
    private LineChart<String, Integer> vuelDiaGen;
	@FXML
    private BarChart<?, ?> vuelRetEsp;
    @FXML
    private PieChart vuelAerGen;
    @FXML
    private PieChart vuelCompEsp;
    @FXML
    private PieChart vuelAvGen;
    @FXML
    private LineChart<?, ?> vuelDiaEsp;
    @FXML
    private BarChart<String, Integer> vuelRetGen;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField hasta;
    @FXML
    private TextField nVuel;
    @FXML
    private TextField desde;
    @FXML
    private TableColumn<Airport, Integer> cVuelos;
    @FXML
    private TextField inter;
    @FXML
    private TableColumn<Airport, Double> cVuelDia;
    @FXML
    private TextField nac;
    @FXML
    private TableColumn<Airport, Integer> cInter;
    @FXML
    private TextField nAer;
    @FXML
    private TableColumn<Airport, Integer> cNacion;
    @FXML
    private TableView<Airport> tAero;
    @FXML
    private TableColumn<Airport, Double> cDelay;
    @FXML
    private TableColumn<Airport, String> cNombre;
    @FXML
    private TextField tCompAMost;
    @FXML
    private TextField tModAMost;
    @FXML
    private TextField tAerAMost;
    @FXML
    private Button bCompAMost;
    @FXML
    private Button bModAMost;
    @FXML
    private Button bAerAMost;
    @FXML
    private MenuItem mSa;
        
    //Cargar los datos de vuelos:
	static final Data data = Data.getInstance();
	ObservableList<Airport> airportData = FXCollections.observableArrayList(data.getAirportList());
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
    void initialize() {
      	
    	
    	
    	//Agragar la lista de aeropuertos
    	tAero.setItems(airportData);
    	
    	//Mostrar en la columna nombre:
    	cNombre.setCellValueFactory(new Callback<CellDataFeatures<Airport, String>, ObservableValue<String>>() {
    	     public ObservableValue<String> call(CellDataFeatures<Airport, String> p) {
    	         return new ReadOnlyObjectWrapper(p.getValue().getName());
    	     }
    	  });
    	//Mostrar en la columna vuelos:
    	cVuelos.setCellValueFactory(new Callback<CellDataFeatures<Airport, Integer>, ObservableValue<Integer>>() {
    	     public ObservableValue<Integer> call(CellDataFeatures<Airport, Integer> p) {
    	         // p.getValue() returns the Person instance for a particular TableView row
    	         return new ReadOnlyObjectWrapper(data.getAirportFlights(p.getValue()).getNumFlights());
    	     }
    	  });
    	//Mostrar en la columna nacionales:
    	cNacion.setCellValueFactory(new Callback<CellDataFeatures<Airport, Integer>, ObservableValue<Integer>>() {
    	     public ObservableValue<Integer> call(CellDataFeatures<Airport, Integer> p) {
    	         // p.getValue() returns the Person instance for a particular TableView row
    	         return new ReadOnlyObjectWrapper(data.getAirportFlights(p.getValue()).getNumNationalFlights());
    	     }
    	  });
    	//Mostrar en la columna internacionales:
    	cInter.setCellValueFactory(new Callback<CellDataFeatures<Airport, Integer>, ObservableValue<Integer>>() {
    	     public ObservableValue<Integer> call(CellDataFeatures<Airport, Integer> p) {
    	         // p.getValue() returns the Person instance for a particular TableView row
    	         return new ReadOnlyObjectWrapper(data.getAirportFlights(p.getValue()).getNumInternationalFlights());
    	     }
    	  });
    	//Mostrar en la columna delay:
    	cDelay.setCellValueFactory(new Callback<CellDataFeatures<Airport, Double>, ObservableValue<Double>>() {
    	     public ObservableValue<Double> call(CellDataFeatures<Airport, Double> p) {
    	         // p.getValue() returns the Person instance for a particular TableView row
    	         return new ReadOnlyObjectWrapper(redondear((data.getAirportFlights(p.getValue()).getDelay())));
    	     }
    	  });
    	//Mostrar en la columna vuelos/dias:
    	cVuelDia.setCellValueFactory(new Callback<CellDataFeatures<Airport, Double>, ObservableValue<Double>>() {
    	     public ObservableValue<Double> call(CellDataFeatures<Airport, Double> p) {
    	         // p.getValue() returns the Person instance for a particular TableView row
    	    	 LocalDateTime primero = data.getAirportFlights(p.getValue()).getFrom();
    	    	 LocalDateTime ultimo = data.getAirportFlights(p.getValue()).getTo();
    	    	 int ini = primero.getDayOfYear() + primero.getDayOfMonth();
    	    	 int fin = ultimo.getDayOfYear() + ultimo.getDayOfMonth();;
    	    	 @SuppressWarnings("unused")
				int numDias = fin - ini;
    	    	 double compare = (double)ultimo.compareTo(primero);
     	    	 int numVuelos = data.getAirportFlights(p.getValue()).getNumFlights();
     	    	 double res = numVuelos/compare;
     	    	 double res2 = res-(res*(8.15/100));
    	         return new ReadOnlyObjectWrapper(redondear(res2-(res2*(0.36/100))));
    	     }
    	  });
    	
    	//Añadir listener a la tabla de aeropuertos:
    	tAero.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> mostrarGraficasAeropuerto(newValue));
    	    	
    	//TextField numero aeropuertos:
    	nAer.setText(String.valueOf((airportData.size())));
    	
    	//Iniciar textField numero vuelos:
    	int numVuelos = 0;
    	for(int i = 0; i < airportData.size(); i++){
    		numVuelos = numVuelos + cVuelos.getCellData(i);
    	}
    	nVuel.setText(String.valueOf(numVuelos));
		//TextField nacionales:
    	int numNacionales = 0;
    	for(int i = 0; i < airportData.size(); i++){
    		numNacionales = numNacionales + cNacion.getCellData(i);
    	}
    	nac.setText(String.valueOf(numNacionales));
    	//TextField internacionales:
    	int numInternacionales = 0;
    	for(int i = 0; i < airportData.size(); i++){
    		numInternacionales = numInternacionales + cInter.getCellData(i);
    	}
    	inter.setText(String.valueOf(numInternacionales));
    	//TextField desde:
    	desde.setText(DateTimeUtils.format(data.getFromDate()));
    	//TextField hasta:
    	hasta.setText(DateTimeUtils.format(data.getToDate()));
    	
    	
    	//Diagrama de linieas Vuelos/Dia general:
    	XYChart.Series vuelos = new XYChart.Series();
    	XYChart.Series nacionales = new XYChart.Series();
    	XYChart.Series internacionales = new XYChart.Series();
    	vuelos.setName("Vuelos");
    	nacionales.setName("Nacionales");
    	internacionales.setName("Internacionales");
    	iniciarVuelDiaGenTot(vuelos);
    	iniciarVuelDiaGenNac(nacionales);
    	iniciarVuelDiaGenInt(internacionales);
    	vuelDiaGen.getData().add(vuelos);
    	vuelDiaGen.getData().add(nacionales);
    	vuelDiaGen.getData().add(internacionales);
    	
    	
		
		
		
    	//Diagrama de barras vuelos/retraso general:
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		vuelRetEsp.setTitle("Retrasos");
		xAxis.setLabel("Retrasos");
		yAxis.setLabel("Vuelos");
		XYChart.Series series1 = new XYChart.Series<>();
		series1.setName("-10");
		XYChart.Series series2 = new XYChart.Series<>();
		series2.setName("10-30");
		XYChart.Series series3 = new XYChart.Series<>();
		series3.setName("+30");
			//Insertar todos los vuelos en un nuevo map<Retraso, Vuelos>
		Hashtable<String,Integer> dicRet = new Hashtable<String,Integer>(11);
		dicRet.put("-10", 0);
		dicRet.put("10-30", 0);
		dicRet.put("+30", 0);
		for(int i = 0; i < airportData.size(); i++){
				//Obtener los maps de los vuelos del aeropuerto
			ObservableMap<String, application.model.Flight> fhLlegadas = data.getAirportFlights(airportData.get(i)).getArrivals().getFlights();
			ObservableMap<String, application.model.Flight> fhSalidas = data.getAirportFlights(airportData.get(i)).getDepartures().getFlights();
	    		//Obtener las llaves de los vuelos en listas
			Object[] llavesLlegadas = fhLlegadas.keySet().toArray();
			Object[] llavesSalidas = fhSalidas.keySet().toArray();
	    	
			//System.out.println("Primer for:");
			for(int j = 0; j < llavesLlegadas.length; j++){
				//System.out.println("Iteracion: "+i);
				if(fhLlegadas.get(llavesLlegadas[j]).getDelay()<10.0){
					Integer numVuelos2 = dicRet.get("-10");
					if(numVuelos2 == null){
						numVuelos2 = 0;
					}
					dicRet.put("-10", numVuelos2 + 1);
					//System.out.println("Insertado: <-10,"+dic3.get("-10")+">");
				}
				else{
					if(fhLlegadas.get(llavesLlegadas[j]).getDelay()>30.0){
						Integer numVuelos2 = dicRet.get("+30");
						if(numVuelos2 == null){
							numVuelos2 = 0;
						}
						dicRet.put("+30", numVuelos2 + 1);
						//System.out.println("Insertado: <+30,"+dic3.get("+30")+">");
					}
					else{
						if(fhLlegadas.get(llavesLlegadas[j]).getDelay()<=30.0 && fhLlegadas.get(llavesLlegadas[j]).getDelay()>=10.0){
						Integer numVuelos2 = dicRet.get("10-30");
						if(numVuelos2 == null){
							numVuelos2 = 0;
						}
						dicRet.put("10-30", numVuelos2 + 1);
						//System.out.println("Insertado: <10-30,"+dic3.get("10-30")+">");
						}
					}
				}
			}
			//System.out.println("Segundo for:");
			for(int j = 0; j < llavesSalidas.length; j++){
				//System.out.println("Iteracion: "+i);
				if(fhSalidas.get(llavesSalidas[j]).getDelay()<10.0){
					Integer numVuelos2 = dicRet.get("-10");
					if(numVuelos2 == null){
						numVuelos2 = 1;
					}
					dicRet.put("-10", numVuelos2 + 1);
					//System.out.println("Insertado: <-10,"+dic3.get("-10")+">");
				}
				else{
					if(fhSalidas.get(llavesSalidas[j]).getDelay()>30.0){
						Integer numVuelos2 = dicRet.get("+30");
						if(numVuelos2 == null){
							numVuelos2 = 1;
						}
						dicRet.put("+30", numVuelos2 + 1);
						//System.out.println("Insertado: <+30,"+dic3.get("+30")+">");
					}
					else{
						if(fhSalidas.get(llavesSalidas[j]).getDelay()<=30.0 && fhSalidas.get(llavesSalidas[j]).getDelay()>=10.0){
						Integer numVuelos2 = dicRet.get("10-30");
						if(numVuelos2 == null){
							numVuelos2 = 1;
						}
						dicRet.put("10-30", numVuelos2 + 1);
						//System.out.println("Insertado: <10-30,"+dic3.get("10-30")+">");
						}
					}
				}
			}
		}
		
			//Recuperar los datos e insertarlos en la grafica
		String[] retrasos = dicRet.keySet().toArray(new String[0]);
		
		int ret1 = dicRet.get(retrasos[0]);
		int ret2 = dicRet.get(retrasos[1]);
		int ret3 = dicRet.get(retrasos[2]);
		
		series1.getData().add(new XYChart.Data("Retraso", ret1));
		series2.getData().add(new XYChart.Data("Retraso", ret2));
		series3.getData().add(new XYChart.Data("Retraso", ret3));
    	vuelRetGen.getData().addAll(series1, series2, series3);
		
		
    	
					
    			
    	assert nVuel != null : "fx:id=\"nVuel\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert cVuelDia != null : "fx:id=\"cVuelDia\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert tCompAMost != null : "fx:id=\"tCompAMost\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert nac != null : "fx:id=\"nac\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert cInter != null : "fx:id=\"cInter\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert cNacion != null : "fx:id=\"cNacion\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert tModAMost != null : "fx:id=\"tModAMost\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert hasta != null : "fx:id=\"hasta\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert vuelCompGen != null : "fx:id=\"vuelCompGen\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert desde != null : "fx:id=\"desde\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert cVuelos != null : "fx:id=\"cVuelos\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert vuelAvEsp != null : "fx:id=\"vuelAvEsp\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert inter != null : "fx:id=\"inter\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert vuelDiaGen != null : "fx:id=\"vuelDiaGen\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert vuelRetEsp != null : "fx:id=\"vuelRetEsp\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert bAerAMost != null : "fx:id=\"bAerAMost\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert vuelAerGen != null : "fx:id=\"vuelAerGen\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert tAerAMost != null : "fx:id=\"tAerAMost\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert nAer != null : "fx:id=\"nAer\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert bCompAMost != null : "fx:id=\"bCompAMost\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert vuelCompEsp != null : "fx:id=\"vuelCompEsp\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert tAero != null : "fx:id=\"tAero\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert vuelAvGen != null : "fx:id=\"vuelAvGen\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert bModAMost != null : "fx:id=\"bModAMost\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert vuelDiaEsp != null : "fx:id=\"vuelDiaEsp\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert cDelay != null : "fx:id=\"cDelay\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert vuelRetGen != null : "fx:id=\"vuelRetGen\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";
        assert cNombre != null : "fx:id=\"cNombre\" was not injected: check your FXML file 'VentanaPrincipal.fxml'.";

    }
    	
	    public void pulsarMostrarCompañias(ActionEvent e) throws IOException{
	    	String compañiasAMostrar = tCompAMost.getText();
	    	System.out.println(compañiasAMostrar);
	  		int n = Integer.parseInt(tCompAMost.getText());
	  		if(n<=0){
	  			Alert alert = new Alert(AlertType.ERROR);
	  			alert.setTitle("Error");
	  			alert.setHeaderText("Error al introducir compañías");
	  			alert.setContentText("Introducir un valor mayor a 0");
	  			alert.showAndWait();
	  		}
	  		iniciarVuelCompGen(n);
	  		
  		}
	    
	    public void pulsarMostrarModelos(ActionEvent e) throws IOException{
	  		int n = Integer.parseInt((tModAMost.getText()));
	  		if(n<=0){
	  			Alert alert = new Alert(AlertType.ERROR);
	  			alert.setTitle("Error");
	  			alert.setHeaderText("Error al introducir modelos");
	  			alert.setContentText("Introducir un valor mayor a 0");
	  			alert.showAndWait();
	  		}
	  		iniciarVuelModGen(n);
	  	}
	    
	    public void pulsarMostrarAeropuertos(ActionEvent e) throws IOException{
	  		int n = Integer.parseInt(tAerAMost.getText());
	  		if(n<=0){
	  			Alert alert = new Alert(AlertType.ERROR);
	  			alert.setTitle("Error");
	  			alert.setHeaderText("Error al introducir aeropuertos");
	  			alert.setContentText("Introducir un valor mayor a 0");
	  			alert.showAndWait();
	  		}
	  		iniciarVuelAerGen(n);
	  			
	  	}
    	
	    public double redondear( double numero) {
    		return Math.round(numero*Math.pow(10,1))/Math.pow(10,1);
      }
	    	    	    
	    public void pulsarSalir(ActionEvent e) throws IOException{
	    	Main.principalStage.close();
	    }
    	
    	@SuppressWarnings("rawtypes")
		public static ArrayList<Map.Entry<String, Integer>> sortValue(Hashtable<String, Integer> t){
    	       //Transfer as List and sort it
    	       @SuppressWarnings("unchecked")
			ArrayList<Map.Entry<String, Integer>> l = new ArrayList(t.entrySet());
    	       Collections.sort(l, new Comparator<Map.Entry<String, Integer>>(){

    	         public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
    	            return o2.getValue().compareTo(o1.getValue());
    	        }});
    	       
    	       return l;
    	    }
       	
    	@SuppressWarnings({ "rawtypes", "unchecked" })
		public void iniciarVuelDiaGenTot(XYChart.Series series){
    		LocalDate fechaInicial = data.getFromDate();
        	LocalDate fechaFinal = data.getToDate();
        	int numVuelosFecha = 0;
        	while(fechaInicial.compareTo(fechaFinal) <= 0){
        		for(int i = 0; i < airportData.size(); i++){
        			AirportFlights af = data.getAirportFlights(airportData.get(i), fechaInicial);
        			if(af != null){
        				numVuelosFecha = numVuelosFecha + af.getNumFlights();
        			}
        		}
        		series.getData().add(new XYChart.Data(fechaInicial.toString(), numVuelosFecha));
        		fechaInicial = fechaInicial.plusDays(1);
        		numVuelosFecha = 0;
        	}
    	}
    	
    	@SuppressWarnings({ "rawtypes", "unchecked" })
		public void iniciarVuelDiaGenNac(XYChart.Series series){
    		LocalDate fechaInicial = data.getFromDate();
        	LocalDate fechaFinal = data.getToDate();
        	int numVuelosFecha = 0;
        	while(fechaInicial.compareTo(fechaFinal) <= 0){
        		for(int i = 0; i < airportData.size(); i++){
	        			AirportFlights af = data.getAirportFlights(airportData.get(i), fechaInicial);
        			if(af != null){
        				numVuelosFecha = numVuelosFecha + af.getNumNationalFlights();
        			}
        		}
        		series.getData().add(new XYChart.Data(fechaInicial.toString(), numVuelosFecha));
        		fechaInicial = fechaInicial.plusDays(1);
        		numVuelosFecha = 0;
        	}
    	}

    	@SuppressWarnings({ "rawtypes", "unchecked" })
		public void iniciarVuelDiaGenInt(XYChart.Series series){
    		LocalDate fechaInicial = data.getFromDate();
        	LocalDate fechaFinal = data.getToDate();
        	int numVuelosFecha = 0;
        	while(fechaInicial.compareTo(fechaFinal) <= 0){
        		for(int i = 0; i < airportData.size(); i++){
        			AirportFlights af = data.getAirportFlights(airportData.get(i), fechaInicial);
        			if(af != null){
        				numVuelosFecha = numVuelosFecha + af.getNumInternationalFlights();
        			}
        		}
        		series.getData().add(new XYChart.Data(fechaInicial.toString(), numVuelosFecha));
        		fechaInicial = fechaInicial.plusDays(1);
        		numVuelosFecha = 0;
        	}
    	}

    	@SuppressWarnings({ "rawtypes", "unchecked" })
		public void iniciarVuelDiaEspTot(XYChart.Series series, Airport aeropuerto){
    		LocalDate fechaInicial = data.getAirportFlights(aeropuerto).getFrom().toLocalDate();
        	LocalDate fechaFinal = data.getAirportFlights(aeropuerto).getTo().toLocalDate();
        	int numVuelosFecha = 0;
        	while(fechaInicial.compareTo(fechaFinal) <= 0){
    			AirportFlights af = data.getAirportFlights(aeropuerto, fechaInicial);
    			if(af != null){
    				numVuelosFecha = af.getNumFlights();
    				series.getData().add(new XYChart.Data(fechaInicial.toString(), numVuelosFecha));
    			}
    			fechaInicial = fechaInicial.plusDays(1);
    			numVuelosFecha = 0;
    		}
    	}
	    	
    	@SuppressWarnings({ "rawtypes", "unchecked" })
		public void iniciarVuelDiaEspNac(XYChart.Series series, Airport aeropuerto){
    		LocalDate fechaInicial = data.getAirportFlights(aeropuerto).getFrom().toLocalDate();
        	LocalDate fechaFinal = data.getAirportFlights(aeropuerto).getTo().toLocalDate();
        	int numVuelosFecha = 0;
        	while(fechaInicial.compareTo(fechaFinal) <= 0){
    			AirportFlights af = data.getAirportFlights(aeropuerto, fechaInicial);
    			if(af != null){
    				numVuelosFecha = af.getNumNationalFlights();
    				series.getData().add(new XYChart.Data(fechaInicial.toString(), numVuelosFecha));
    			}
    			fechaInicial = fechaInicial.plusDays(1);
    			numVuelosFecha = 0;
    		}
    	}
    	    	
    	@SuppressWarnings({ "rawtypes", "unchecked" })
		public void iniciarVuelDiaEspInt(XYChart.Series series, Airport aeropuerto){
    		LocalDate fechaInicial = data.getAirportFlights(aeropuerto).getFrom().toLocalDate();
        	LocalDate fechaFinal = data.getAirportFlights(aeropuerto).getTo().toLocalDate();
        	int numVuelosFecha = 0;
        	while(fechaInicial.compareTo(fechaFinal) <= 0){
        		AirportFlights af = data.getAirportFlights(aeropuerto, fechaInicial);
    			if(af != null){
    				numVuelosFecha = af.getNumInternationalFlights();
    				series.getData().add(new XYChart.Data(fechaInicial.toString(), numVuelosFecha));
    			}
    			fechaInicial = fechaInicial.plusDays(1);
    			numVuelosFecha = 0;
    		}
    	}
    
    	public void iniciarVuelCompGen(int n){
    		//Diagrama de tarta Vuelos/Compañía:
    		ObservableList<PieChart.Data> pieChartComp = FXCollections.observableArrayList();
    			//Insertar todos los vuelos en un nuevo map<Compañía, Vuelos>
    		Hashtable<String,Integer> dicComp = new Hashtable<String,Integer>(Integer.parseInt(nVuel.getText()));
    		for(int i = 0; i < airportData.size(); i++){
    				//Obtener los maps de los vuelos del aeropuerto
    			ObservableMap<String, application.model.Flight> fhLlegadas = data.getAirportFlights(airportData.get(i)).getArrivals().getFlights();
    			ObservableMap<String, application.model.Flight> fhSalidas = data.getAirportFlights(airportData.get(i)).getDepartures().getFlights();
    	    		//Obtener las llaves de los vuelos en listas
    			Object[] llavesLlegadas = fhLlegadas.keySet().toArray();
    			Object[] llavesSalidas = fhSalidas.keySet().toArray();
    				//Insertar los datos <Compañía, Vuelos> en el dicComp
    			for(int j = 0; j < llavesLlegadas.length; j++){
    				//System.out.println("Iteracion: "+j);
    				Integer numVuelosComp = dicComp.get(fhLlegadas.get(llavesLlegadas[j]).getCompany());
    				if(numVuelosComp == null){
    					numVuelosComp = 0;
    				}
    				dicComp.put(fhLlegadas.get(llavesLlegadas[j]).getCompany(), numVuelosComp + 1);
    				//System.out.println("Insertado: <"+fhLlegadas.get(llavesLlegadas[j]).getCompany()+","+dic.get(fhLlegadas.get(llavesLlegadas[j]).getCompany())+">");
    			}
    				//System.out.println("Segundo for:");
    			for(int j = 0; j < llavesSalidas.length; j++){
    				//System.out.println("Iteracion: "+i);
    				Integer numVuelosComp = dicComp.get(fhSalidas.get(llavesSalidas[j]).getCompany());
    				if(numVuelosComp == null){
    					numVuelosComp = 1;
    				}
    				dicComp.put(fhSalidas.get(llavesSalidas[j]).getCompany(), numVuelosComp + 1);
    			}
    		}
    			//Recuperar del dicComp e insertar en la grafica:
    		@SuppressWarnings("rawtypes")
			ArrayList compañiasOrd = sortValue(dicComp);
    		System.out.println("Numero de compañias: "+compañiasOrd.size());
    		if(n > compañiasOrd.size()){
    			n = compañiasOrd.size();
    		}
    		for(int j = 0; j<n; j++){
    			@SuppressWarnings("unchecked")
				Map.Entry<String, Integer> entradaHash = (Entry<String, Integer>) compañiasOrd.get(j);
    			String compañia = entradaHash.getKey();
    			Integer vuelosCompañia = entradaHash.getValue();
                pieChartComp.add(new PieChart.Data(compañia, vuelosCompañia)); 	
    		}
    		vuelCompGen.setData(pieChartComp);
    		System.out.println("Boton mostrar compañias pulsado");
    	}
    	
    	@SuppressWarnings("unchecked")
		public void iniciarVuelModGen(int n){
    		//Diagrama tarta vuelos/avion:
    		ObservableList<PieChart.Data> pieChartAv = FXCollections.observableArrayList();
    		//Insertar todos los vuelos en un nuevo map<Compañía, Vuelos>
    		Hashtable<String,Integer> dicAv = new Hashtable<String,Integer>(Integer.parseInt(nVuel.getText()));
    		for(int i = 0; i < airportData.size(); i++){
    				//Obtener los maps de los vuelos del aeropuerto
    			ObservableMap<String, application.model.Flight> fhLlegadas = data.getAirportFlights(airportData.get(i)).getArrivals().getFlights();
    			ObservableMap<String, application.model.Flight> fhSalidas = data.getAirportFlights(airportData.get(i)).getDepartures().getFlights();
    	    		//Obtener las llaves de los vuelos en listas
    			Object[] llavesLlegadas = fhLlegadas.keySet().toArray();
    			Object[] llavesSalidas = fhSalidas.keySet().toArray();
    				//Insertar los datos <Compañía, Vuelos> en el dicComp
    			for(int j = 0; j < llavesLlegadas.length; j++){
    				//System.out.println("Iteracion: "+j);
    				if(fhLlegadas.get(llavesLlegadas[j]).getPlane()!=null){
    				Integer numVuelosAv = dicAv.get(fhLlegadas.get(llavesLlegadas[j]).getPlane());
    				if(numVuelosAv == null){
    					numVuelosAv = 0;
    				}
    				dicAv.put(fhLlegadas.get(llavesLlegadas[j]).getPlane(), numVuelosAv + 1);
    				//System.out.println("Insertado: <"+fhLlegadas.get(llavesLlegadas[j]).getCompany()+","+dic.get(fhLlegadas.get(llavesLlegadas[j]).getCompany())+">");
    				}
    			}
    				//System.out.println("Segundo for:");
    			for(int j = 0; j < llavesSalidas.length; j++){
    				//System.out.println("Iteracion: "+i);
    				if(fhSalidas.get(llavesSalidas[j]).getPlane()!=null){
    				Integer numVuelosAv = dicAv.get(fhSalidas.get(llavesSalidas[j]).getPlane());
    				if(numVuelosAv == null){
    					numVuelosAv = 1;
    				}
    				dicAv.put(fhSalidas.get(llavesSalidas[j]).getPlane(), numVuelosAv + 1);
    				}
    			}
    		}
    			//Recuperar del dicComp e insertar en la grafica:
    		@SuppressWarnings("rawtypes")
			ArrayList avionesOrd = sortValue(dicAv);
    		//System.out.println("Numero de compañias: "+avionesOrd.size());
    		if(n > avionesOrd.size()){
    			n = avionesOrd.size();
    		}
    		for(int j = 0; j<n; j++){
    			Map.Entry<String, Integer> entradaHash = (Entry<String, Integer>) avionesOrd.get(j);
    			String avion = entradaHash.getKey();
    			Integer vuelosAvion = entradaHash.getValue();
    	        pieChartAv.add(new PieChart.Data(avion, vuelosAvion)); 	
    		}
    		vuelAvGen.setData(pieChartAv);
    		System.out.println("Boton mostrar modelos pulsado");
    	}
    	
    	public void iniciarVuelAerGen(int n){
    		//Diagrama de tarta Vuelos/Aeropuerto general:
        	ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(/*	new PieChart.Data("Grapefruit", 13)*/);
        	Hashtable<String,Integer> dicAer = new Hashtable<String,Integer>(airportData.size());
        		//Insertar los datos en el Hashtable:
        	for(int i = 0; i < airportData.size(); i++){
        		dicAer.put(airportData.get(i).getName(), data.getAirportFlights(airportData.get(i)).getNumFlights());
        	}
        		//Recuperar del Hashtable e insertar en la grafica:
    		@SuppressWarnings("rawtypes")
			ArrayList aeropuertosOrd = sortValue(dicAer);
    		if(n > aeropuertosOrd.size()){
    			n = aeropuertosOrd.size();
    		}
    		for(int j = 0; j<n; j++){
    			@SuppressWarnings("unchecked")
				Map.Entry<String, Integer> entradaHash = (Entry<String, Integer>) aeropuertosOrd.get(j);
    			String aeropuerto = entradaHash.getKey();
    			Integer vuelosAeropuerto = entradaHash.getValue();
    	        pieChartData.add(new PieChart.Data(aeropuerto, vuelosAeropuerto)); 	
    		}
        	
    		vuelAerGen.setData(pieChartData);
    		System.out.println("Boton mostrar aeropuertos pulsado");
    	}
    	
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void mostrarGraficasAeropuerto(Airport aeropuerto){
    		//Iniciar la grafica vuelos/dia:
    		vuelDiaEsp.getData().clear();
    		vuelCompEsp.getData().clear();
    		vuelAvEsp.getData().clear();
    		vuelRetEsp.getData().clear();
    		
    		XYChart.Series vuelos = new XYChart.Series();
        	XYChart.Series nacionales = new XYChart.Series();
        	XYChart.Series internacionales = new XYChart.Series();
        	vuelos.setName("Vuelos");
        	nacionales.setName("Nacionales");
        	internacionales.setName("Internacionales");
        	iniciarVuelDiaEspTot(vuelos, aeropuerto);
        	iniciarVuelDiaEspNac(nacionales, aeropuerto);
        	iniciarVuelDiaEspInt(internacionales, aeropuerto);
        	vuelDiaEsp.getData().add(vuelos);
        	vuelDiaEsp.getData().add(nacionales);
        	vuelDiaEsp.getData().add(internacionales);
        	
        	
        	//Iniciar la grafica vuelos/compañía:
        	ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        		//Crear los maps de vuelos
        	ObservableMap<String, application.model.Flight> fhLlegadas = data.getAirportFlights(aeropuerto).getArrivals().getFlights();
			ObservableMap<String, application.model.Flight> fhSalidas = data.getAirportFlights(aeropuerto).getDepartures().getFlights();
        		//Obtener las llaves de los vuelos en listas
			Object[] llavesLlegadas = fhLlegadas.keySet().toArray();
			Object[] llavesSalidas = fhSalidas.keySet().toArray();
				//Insertar todos los vuelos en un nuevo map<Compañía, Vuelos>
			Hashtable<String,Integer> dic = new Hashtable<String,Integer>(llavesLlegadas.length + llavesSalidas.length);
			//System.out.println("Primer for:");
			for(int i = 0; i < llavesLlegadas.length; i++){
				//System.out.println("Iteracion: "+i);
				Integer numVuelos = dic.get(fhLlegadas.get(llavesLlegadas[i]).getCompany());
				if(numVuelos == null){
					numVuelos = 0;
				}
				dic.put(fhLlegadas.get(llavesLlegadas[i]).getCompany(), numVuelos + 1);
				//System.out.println("Insertado: <"+fhLlegadas.get(llavesLlegadas[i]).getCompany()+","+dic.get(fhLlegadas.get(llavesLlegadas[i]).getCompany())+">");
			}
			//System.out.println("Segundo for:");
			for(int i = 0; i < llavesSalidas.length; i++){
				//System.out.println("Iteracion: "+i);
				Integer numVuelos = dic.get(fhSalidas.get(llavesSalidas[i]).getCompany());
				if(numVuelos == null){
					numVuelos = 1;
				}
				dic.put(fhSalidas.get(llavesSalidas[i]).getCompany(), numVuelos + 1);
				//System.out.println("Insertado: <"+fhSalidas.get(llavesSalidas[i]).getCompany()+","+dic.get(fhSalidas.get(llavesSalidas[i]).getCompany())+">");
			}
				//Recuperar todos los <Compañía, Vuelos> e insertarlos en la grafica:
			String[] compañias = dic.keySet().toArray(new String[0]);
			for(int i = 0; i<compañias.length;i++){
	            //System.out.println(i);
	            Integer numVuelosCompañia = dic.get(compañias[i]);
	            //System.out.println("Compañia: "+compañias[i]);
	            //System.out.println("Vuelos: "+numVuelosCompañia);
	            pieChartData.add(new PieChart.Data(compañias[i], numVuelosCompañia)); 	
			}
        	vuelCompEsp.setData(pieChartData);
    		
        	//Iniciar grafica vuelos por avión:
        	ObservableList<PieChart.Data> pieChartData2 = FXCollections.observableArrayList();
        		//Insertar todos los vuelos en un nuevo map<Avión, Vuelos>
			Hashtable<String,Integer> dic2 = new Hashtable<String,Integer>(llavesLlegadas.length + llavesSalidas.length);
			//System.out.println("Primer for:");
			for(int i = 0; i < llavesLlegadas.length; i++){
				//System.out.println("Iteracion: "+i);
				if(fhLlegadas.get(llavesLlegadas[i]).getPlane()!=null){
					Integer numVuelos2 = dic2.get(fhLlegadas.get(llavesLlegadas[i]).getPlane());
					if(numVuelos2 == null){
						numVuelos2 = 0;
					}
					dic2.put(fhLlegadas.get(llavesLlegadas[i]).getPlane(), numVuelos2 + 1);
					//System.out.println("Insertado: <"+fhLlegadas.get(llavesLlegadas[i]).getPlane()+","+dic2.get(fhLlegadas.get(llavesLlegadas[i]).getPlane())+">");
				}
			}
			//System.out.println("Segundo for:");
			for(int i = 0; i < llavesSalidas.length; i++){
				//System.out.println("Iteracion: "+i);
				if(fhSalidas.get(llavesSalidas[i]).getPlane()!=null){
					Integer numVuelos2 = dic2.get(fhSalidas.get(llavesSalidas[i]).getPlane());
					if(numVuelos2 == null){
						numVuelos2 = 1;
					}
					dic2.put(fhSalidas.get(llavesSalidas[i]).getPlane(), numVuelos2 + 1);
					//System.out.println("Insertado: <"+fhSalidas.get(llavesSalidas[i]).getPlane()+","+dic2.get(fhSalidas.get(llavesSalidas[i]).getPlane())+">");
				}
			}
				//Recuperar todos los <Avión, Vuelos> e insertarlos en la grafica:
			String[] aviones = dic2.keySet().toArray(new String[0]);
			for(int i = 0; i<aviones.length;i++){
	            //System.out.println(i);
	            Integer numVuelosAvion = dic2.get(aviones[i]);
	            //System.out.println("Avion: "+aviones[i]);
	            //System.out.println("Vuelos: "+numVuelosAvion);
	            pieChartData2.add(new PieChart.Data(aviones[i], numVuelosAvion)); 	
			}
	    	vuelAvEsp.setData(pieChartData2);
	    	
	    	//Iniciar grafica vuelos por retraso
    		CategoryAxis xAxis = new CategoryAxis();
    		NumberAxis yAxis = new NumberAxis();
    		vuelRetEsp.setTitle("Retrasos");
    		xAxis.setLabel("Retrasos");
    		yAxis.setLabel("Vuelos");
    		XYChart.Series series1 = new XYChart.Series<>();
    		series1.setName("-10");
    		XYChart.Series series2 = new XYChart.Series<>();
    		series2.setName("10-30");
    		XYChart.Series series3 = new XYChart.Series<>();
    		series3.setName("+30");
	    		
	    		//Insertar todos los vuelos en un nuevo map<Retraso, Vuelos>
			Hashtable<String,Integer> dic3 = new Hashtable<String,Integer>(llavesLlegadas.length + llavesSalidas.length);
			dic3.put("-10", 0);
			dic3.put("10-30", 0);
			dic3.put("+30", 0);
			//System.out.println("Primer for:");
			for(int i = 0; i < llavesLlegadas.length; i++){
				//System.out.println("Iteracion: "+i);
				if(fhLlegadas.get(llavesLlegadas[i]).getDelay()<10){
					Integer numVuelos2 = dic3.get("-10");
					if(numVuelos2 == null){
						numVuelos2 = 0;
					}
					dic3.put("-10", numVuelos2 + 1);
					//System.out.println("Insertado: <-10,"+dic3.get("-10")+">");
				}
				else{
					if(fhLlegadas.get(llavesLlegadas[i]).getDelay()>30){
						Integer numVuelos2 = dic3.get("+30");
						if(numVuelos2 == null){
							numVuelos2 = 0;
						}
						dic3.put("+30", numVuelos2 + 1);
						//System.out.println("Insertado: <+30,"+dic3.get("+30")+">");
					}
					else{
						if(fhLlegadas.get(llavesLlegadas[i]).getDelay() >= 10 && fhLlegadas.get(llavesLlegadas[i]).getDelay() <=30){
						Integer numVuelos2 = dic3.get("10-30");
						if(numVuelos2 == null){
							numVuelos2 = 0;
						}
						dic3.put("10-30", numVuelos2 + 1);
						//System.out.println("Insertado: <10-30,"+dic3.get("10-30")+">");
						}
					}
				}
			}
			//System.out.println("Segundo for:");
			for(int i = 0; i < llavesSalidas.length; i++){
				//System.out.println("Iteracion: "+i);
				if(fhSalidas.get(llavesSalidas[i]).getDelay()<10){
					Integer numVuelos2 = dic3.get("-10");
					if(numVuelos2 == null){
						numVuelos2 = 1;
					}
					dic3.put("-10", numVuelos2 + 1);
					//System.out.println("Insertado: <-10,"+dic3.get("-10")+">");
				}
				else{
					if(fhSalidas.get(llavesSalidas[i]).getDelay()>30){
						Integer numVuelos2 = dic3.get("+30");
						if(numVuelos2 == null){
							numVuelos2 = 1;
						}
						dic3.put("+30", numVuelos2 + 1);
						//System.out.println("Insertado: <+30,"+dic3.get("+30")+">");
					}
					else{
						if(fhSalidas.get(llavesSalidas[i]).getDelay() <=30 && fhSalidas.get(llavesSalidas[i]).getDelay() >=10){
						Integer numVuelos2 = dic3.get("10-30");
						if(numVuelos2 == null){
							numVuelos2 = 1;
						}
						dic3.put("10-30", numVuelos2 + 1);
						//System.out.println("Insertado: <10-30,"+dic3.get("10-30")+">");
						}
					}
				}
			}
				//Recuperar los datos e insertarlos en la grafica
			String[] retrasos = dic3.keySet().toArray(new String[0]);
			for(int i = 0; i<retrasos.length;i++){
	            System.out.println(i);
	            //Integer numVuelosRetrasos = dic3.get(retrasos[i]);
	            //System.out.println("Retraso: "+retrasos[i]);
	            //System.out.println("Vuelos: "+numVuelosRetrasos);
	             	
			}
			int ret1 = dic3.get(retrasos[0]);
			int ret2 = dic3.get(retrasos[1]);
			int ret3 = dic3.get(retrasos[2]);
			
			series1.getData().add(new XYChart.Data(aeropuerto.getName(), ret1));
			series2.getData().add(new XYChart.Data(aeropuerto.getName(), ret2));
			series3.getData().add(new XYChart.Data(aeropuerto.getName(), ret3));
	    	vuelRetEsp.getData().addAll(series1, series2, series3);
    	}
}
