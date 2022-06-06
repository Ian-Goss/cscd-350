package cs350s22.component.ui.parser;

import java.io.*;
import java.util.*;

import cs350s22.component.sensor.mapper.A_Mapper;
import cs350s22.component.sensor.reporter.A_Reporter;
import cs350s22.component.sensor.watchdog.A_Watchdog;
import cs350s22.support.*;
import cs350s22.test.MySensor;

public class Parser{
	//Variable List
	public A_ParserHelper parserHelper;
	public String commandText;
	
	//CONSTRUCTORS
	public Parser(A_ParserHelper parserHelper, String commandText){
		this.parserHelper = parserHelper;
		this.commandText = commandText;
	}
	
	public void parse() throws ParseException, IOException{
		String input = this.commandText;
		String parserCheck = null;
		String output = null;
		
		//Split off leading term into 2-slot array
		String[] splitArray = input.split(" ", 2);
	    parserCheck = splitArray[0];
	    
	    if(splitArray.length > 1) {
	    	output = splitArray[1];
	    }
	    
	    //Navigation Tree
		if(parserCheck.equals("CREATE")){
		    treeCreate(output);
		}
		else if(parserCheck.equals("SEND")){
		    treeSend(output);
		}
		
		//Immediate Action
		//CLOCK Methods
		if(parserCheck.equals("@CLOCK")){
		    Clock myClock = Clock.getInstance();    //Get a hold of the clock
			
			//Check if we're turning the clock on or off
			if(output.equals("PAUSE")){
			    myClock.isActive(false);            //Turn the clock off
			}
			else if(output.equals("RESUME")){
			    myClock.isActive(true);             //Turn the clock on
			}
			
			//Check for anything else
			else{
			    String[] actionArray = output.split(" ", 2);
				
				//Are we running "ONESTEP"
				if(actionArray[0].equals("ONESTEP")){
					int counter = Integer.parseInt(actionArray[1]);     //Convert VALUE to int
				    myClock.onestep(counter);							//Run appropriate method
				}
				
				//Are we running "SET RATE"
				else if(actionArray[0].equals("SET")){
					String[] setRate = actionArray[1].split(" ", 2);	//Split remaining string to isolate VALUE
					int counter = Integer.parseInt(setRate[1]);			//Convert VALUE to int
					myClock.setRate(counter);							//Run appropriate method
				}
			}
		}
		
		if(parserCheck.equals("@EXIT")){
			this.parserHelper.exit();
		}
		
		if(parserCheck.equals("@RUN")){
		    this.parserHelper.run(output);
		}
		if(parserCheck.equals("@CONFIGURE")){
			//Insert Code Here
		}
		if(parserCheck.equals("BUILD")){
			//Insert Code Here
		}
	}

	public void treeCreate(String input) {
		String parserCheck = null;
		String output = null;
		
		//Split off leading term into 2-slot array
		String[] splitArray = input.split(" ", 2);
	    parserCheck = splitArray[0];
	    
	    if(splitArray.length > 1) {
	    	output = splitArray[1];
	    }
	    
	    //Navigation Tree
	  	if(parserCheck.equals("ACTUATOR")){
	  	    //Insert Code Here
	  		//SymbolTable<A_Reporter>.get(sensor);
	  	}
	  	else if(parserCheck.equals("CONTROLLER")) {
	  		//Insert Code Here
	  	}
	  	else if(parserCheck.equals("DEPENDENCY")) {
	  		//Insert Code Here
	  	}
	  	else if(parserCheck.equals("MAPPER")) {
	  		//Insert Code Here
	  	}
	  	else if(parserCheck.equals("REPORTER")) {
	  		//Insert Code Here
	  	}
	  	else if(parserCheck.equals("SENSOR")) {
	  		createSensor(output);
	  	}
	  	else if(parserCheck.equals("WATCHDOG")) {
	  		//Insert Code Here
	  	}
	}
	
	public void treeSend(String input) {
		String parserCheck = null;
		String output = null;
		
		//Split off leading term into 2-slot array
		String[] splitArray = input.split(" ", 2);
	    parserCheck = splitArray[0];
	    
	    if(splitArray.length > 1) {
	    	output = splitArray[1];
	    }
	    
	    //Navigation Tree
	    
	}
	
	public void createSensor(String input){
		
		String output = null;
		
		//Split off leading term into first slot of array
		String[] splitArray = input.split(" ", 0);
		
	    String sensorType = splitArray[0];						//The first term is always the type
	    Identifier sensorName = Identifier.make(splitArray[1]);	//The second term is always the name
	    
	    //If the resulting array is only two terms long
	    if(splitArray.length == 2){
	    	MySensor s = new MySensor(sensorName);
	    	System.out.println("Sensor Created");
	    	parserHelper.getSymbolTableSensor().add(sensorName, s);
	    	System.out.println("Sensor Added to Table");
	    }
	    
	    
	    
	    else{
	    	Identifier myGroup = null;
	    	List<Identifier> sensorGroups= null;
	    	
	    	List<A_Reporter> sensorReporters = null;
	    	List<A_Watchdog> sensorWatchdogs = null;
	    	A_Mapper sensorMapper = null;
	    	
	    	int reporterSlot = 0;
	    	int watchdogSlot = 0;
	    	int mapperSlot = 0;
	    	
	    	for(int scan = 3; scan < splitArray.length; scan++) {
	    		if(splitArray[scan].equals("REPORTERS")) {
	    			reporterSlot = scan;
	    		}
	    		if(splitArray[scan].equals("WATCHDOGS")) {
	    			watchdogSlot = scan;
	    		}
	    		if(splitArray[scan].equals("MAPPER")) {
	    			mapperSlot = scan;
	    		}
	    	}
	    	
	    	for(int i = 3; i < splitArray.length; i++) {
	    		if(splitArray[i].equals("GROUP")){		//Once we find the "GROUP" term
	    			
	    			myGroup = Identifier.make(splitArray[i+1]);
	    			sensorGroups.add(myGroup);
	    			
	    			
	    		}
	    		//parserHelper.getSymbolTableSensor().add(sensorName, s);
	    		//parserHelper.getSymbolTableReporter().get(variableName);
	    		if(splitArray[i].equals("REPORTERS")) {
	    			if(watchdogSlot != 0) {
	    				for(int j = i+1; j < watchdogSlot; j++) {
	    					Identifier val = Identifier.make(splitArray[j]);
	    					
	    					A_Reporter thisReporter = parserHelper.getSymbolTableReporter().get(val);
	    					sensorReporters.add(thisReporter);
	    					
	    				}
	    			}
	    			else if (mapperSlot != 0) {
	    				for(int j = i+1; j < mapperSlot; j++) {
	    					Identifier val = Identifier.make(splitArray[j]);
	    					
	    					A_Reporter thisReporter = parserHelper.getSymbolTableReporter().get(val);
	    					sensorReporters.add(thisReporter);
	    				}
	    			}
	    			else {
	    				for(int j = i+1; j < splitArray.length; j++) {
	    					Identifier val = Identifier.make(splitArray[j]);
	    					
	    					A_Reporter thisReporter = parserHelper.getSymbolTableReporter().get(val);
	    					sensorReporters.add(thisReporter);
	    				}
	    			}
	    		}
	    		if(splitArray[i].equals("WATCHDOGS")) {
	    			if (mapperSlot != 0) {
	    				for(int j = i+1; j < mapperSlot; j++) {
	    					Identifier val = Identifier.make(splitArray[j]);
	    					
	    					A_Watchdog thisWatchdog = parserHelper.getSymbolTableWatchdog().get(val);
	    					sensorWatchdogs.add(thisWatchdog);
	    				}
	    			}
	    			else {
	    				for(int j = i+1; j < splitArray.length; j++) {
	    					Identifier val = Identifier.make(splitArray[j]);
	    					
	    					A_Watchdog thisWatchdog = parserHelper.getSymbolTableWatchdog().get(val);
	    					sensorWatchdogs.add(thisWatchdog);
	    				}
	    			}
	    		}
	    		if(splitArray[i].equals("MAPPER")) {	//Once we find the "MAPPER" term
	    			   			
	    			Identifier val = Identifier.make(splitArray[i + 1]);
					sensorMapper = parserHelper.getSymbolTableMapper().get(val);
	    		}
	    	}
	    	
	    	//build final constructor here
	    	//sensor(sensorName, sensorGroups, sensorReporters, sensorWatchdogs, sensorMapper)
	    	if(sensorMapper != null) {
	    		//There is a Mapper
	    		MySensor s = new MySensor(sensorName, sensorGroups, sensorReporters, sensorWatchdogs, sensorMapper);
		    	System.out.println("Sensor s3 Created");
		    	parserHelper.getSymbolTableSensor().add(sensorName, s);
		    	System.out.println("Sensor s3 Added to Table");
	    	}
	    	else {
	    		//There is no Mapper
	    		MySensor s = new MySensor(sensorName, sensorGroups, sensorReporters, sensorWatchdogs);
		    	System.out.println("Sensor s2 Created");
		    	parserHelper.getSymbolTableSensor().add(sensorName, s);
		    	System.out.println("Sensor s2 Added to Table");
	    	}
	    }
	}	    
}