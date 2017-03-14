
// GUI-related imports

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// File-related imports

import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.util.Arrays;

public class ProcessManagement extends Frame implements ActionListener
{
	// File Parameters
	String DataFilePath = null;
	String DataFileName = null;
		
	//Array
	Font font1 = new Font("Calibri",Font.BOLD, 16);
	Font font2 = new Font("Calibri",Font.BOLD, 12);
	
	int[] ProcessTime = new int[1000];
	int[] ArrivalTime = new int[1000];
	int[] WaitTime = new int[1000];
	int[] TurnaroundTime = new int[1000];
	int[] Processed   = new int[1000];
		// number of data items and keys
	 
	int time;
	boolean considerArrivalTime;
	boolean considerSwitchTime;
	int NumberOfDataItems=0;
	
	// Statistics
	
	float FCFSaverageWaitTime     = 0;
	
	float SJNaverageWaitTime      = 0;
	
	float RRaverageWaitTime       = 0;
	
	
	float FCFSaverageTRTime     = 0;
	
	float SJNaverageTRTime      = 0;
	
	float RRaverageTRTime       = 0;
	
	// Retrieved command code
	
	String command = "";
		
	public static void main(String[] args)
	{
		Frame frame = new ProcessManagement();
		
			
		frame.setResizable(false);
		frame.setSize(1150,800);
		frame.setVisible(true);
		
		Font f1= new Font("Calibri",Font.BOLD, 12);
		frame.setFont(f1);
		
	}
	
	public ProcessManagement()
	{
		setTitle("Process Management Algorithms");
		
		// Create Menu Bar
		   			
		MenuBar mb = new MenuBar();
		setMenuBar(mb);
		
		// Create Menu Group Labeled "File"
		
		Menu FileMenu = new Menu("File");
		
		// Add it to Menu Bar
		
		mb.add(FileMenu);
		
		// Create Menu Items
		// Add action Listener 
		// Add to "File" Menu Group
		
		MenuItem miOpen = new MenuItem("Open Data File");
		miOpen.addActionListener(this);
		FileMenu.add(miOpen);
						
		MenuItem miExit = new MenuItem("Exit");
		miExit.addActionListener(this);
		FileMenu.add(miExit);

		// Create Menu Group Labeled "File"
		
		Menu AlgMenu = new Menu("Algorithms");
		
		// Add it to Menu Bar
		
		mb.add(AlgMenu);
		
		// Create Menu Items
		// Add action Listener 
		// Add to "Search" Menu Group
		
		MenuItem miFCFS = new MenuItem("First Come First Served");
		miFCFS.addActionListener(this);
		AlgMenu.add(miFCFS);
						
		MenuItem miSJN = new MenuItem("Shortest Job Next");
		miSJN.addActionListener(this);
		AlgMenu.add(miSJN);
		
		MenuItem miRR = new MenuItem("Round Robin");
		miRR.addActionListener(this);
		AlgMenu.add(miRR);	
		
		MenuItem miAll = new MenuItem("Run All");
		miAll.addActionListener(this);
		AlgMenu.add(miAll);	
		
		MenuItem miBonus = new MenuItem("Bonus");
		miBonus.addActionListener(this);
		AlgMenu.add(miBonus);	
		
		
        
		WindowListener l = new WindowAdapter()
		{
						
			public void windowClosing(WindowEvent ev)
			{
				System.exit(0);
			}
			
			public void windowActivated(WindowEvent ev)
			{
				repaint();
			}
			
			public void windowStateChanged(WindowEvent ev)
			{
				repaint();
			}
		
		};
		
		ComponentListener k = new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e) 
			{
        		repaint();           
    		}
		};
		
		// register listeners
			
		this.addWindowListener(l);
		this.addComponentListener(k);

	}
	
//******************************************************************************
//  called by windows manager whenever the application window performs an action
//  (select a menu item, close, resize, ....
//******************************************************************************

	public void actionPerformed (ActionEvent ev)
		{
			// figure out which command was issued
			
			command = ev.getActionCommand();
			
			// take action accordingly
						
			if("Open Data File".equals(command))
			{
				
				DataFilePath = null;
				DataFileName = null;
				
				  JFileChooser chooser = new JFileChooser();
				  chooser.setDialogType(JFileChooser.OPEN_DIALOG );
				  chooser.setDialogTitle("Open Data File");
			      
			      int returnVal = chooser.showOpenDialog(null);
			      if( returnVal == JFileChooser.APPROVE_OPTION) 
			      	{
			          DataFilePath = chooser.getSelectedFile().getPath();
			          DataFileName = chooser.getSelectedFile().getName();
			        }
			      // read data file and copy it to original array
			      try
			      {
			    	  NumberOfDataItems = ReadFileIntoArray(DataFilePath,"Original");
			      }
			      catch (IOException ioe)
			      {
			    	  System.exit(0); 
			      }
				repaint();		
			}
			
			else
				if("Exit".equals(command))
				{
					System.exit(0);
				}
			else
				if("First Come First Served".equals(command))
				{
					Initialize();
					FCFS();
					repaint();
				}
			
			else
				if("Shortest Job Next".equals(command))
				{
					Initialize();
					SJN();
					repaint();
				}
			else
					
			if("Round Robin".equals(command))
			{
				Initialize();
				RoundRobin(100);			
				repaint();
			}
			
			else	
			if("Run All".equals(command))
			{
				Initialize();
				FCFS();
		
				Initialize();
				SJN();
				
				Initialize();
				RoundRobin(100);
				
				repaint();
			}
			else
				if("Bonus".equals(command))
			{
				repaint();
			}
				
			
		}
//********************************************************
// called by repaint() to redraw the screen
//********************************************************
		
		public void paint(Graphics g)
		{
			GraphicsEnvironment e =
					GraphicsEnvironment.getLocalGraphicsEnvironment();
					String[] fontnames = e.getAvailableFontFamilyNames();
			/*		for (int i = 0; i < fontnames.length; i++)
					System.out.println(fontnames[i]);*/
									
			if("Open Data File".equals(command))
			{
				// Acknowledge that file was opened
				if (DataFileName != null)
				{
					g.drawString("File --  "+DataFileName+"  -- was successfully opened", 300, 200);
					g.drawString("Number of Data Items = "+Integer.toString(NumberOfDataItems), 330, 250);
				}
				else
				{
					g.drawString("NO Data File is Open", 300, 200);
				}
				
				return;	
			}
			
			if("First Come First Served".equals(command) )
			{
				DisplayResults(g, "First Come First Served (FCFS)", FCFSaverageWaitTime, FCFSaverageTRTime);
			}
			
			if("Shortest Job Next".equals(command) )
			{
				DisplayResults(g, "Shortest Job Next (SJN)", SJNaverageWaitTime, SJNaverageTRTime);
					
			}
			else
			
			if("Round Robin".equals(command) )
			{
				DisplayResults(g, "Round Robin (RR)", RRaverageWaitTime, RRaverageTRTime);
			}
			
			if("Run All".equals(command) )
			{
				RunAllResults(g, FCFSaverageWaitTime, FCFSaverageTRTime, 
						SJNaverageWaitTime, SJNaverageTRTime, 
						RRaverageWaitTime, RRaverageTRTime);
			}	
			if("Bonus".equals(command) )
			{
				BonusResults(g, "Round Robin (RR)");
			}
		}
		
public int ReadFileIntoArray(String filePath , String type) throws IOException
{
	if (filePath != null)
    {
  	  int index = 0;
  	  Scanner integerTextFile = new Scanner(new File(filePath));	 
  	  while (integerTextFile.hasNext())
  	  {
  		  int i = integerTextFile.nextInt();
  		  ProcessTime[index] = integerTextFile.nextInt();
  		  ArrivalTime[index] = integerTextFile.nextInt();
  		  index++;
  	  }
			//  end of file detected
  	  integerTextFile.close();
  	  return index ;

    }
	else
		return 0;
}
public void FCFS() 
{
	int startTime = 0;
	int endTime = ProcessTime[0];
	int contextSwitch = 5;
	
	for (int i=0; i<NumberOfDataItems; i++)
	{
		WaitTime[i] = startTime - ArrivalTime[i];
		TurnaroundTime[i] = endTime - ArrivalTime[i];
		startTime = startTime + ProcessTime[i] + contextSwitch;
		endTime = startTime + ProcessTime[i+1];
		
		FCFSaverageWaitTime = FCFSaverageWaitTime + WaitTime[i];
		FCFSaverageTRTime = FCFSaverageTRTime + TurnaroundTime[i];
		time = time + ProcessTime[i] + contextSwitch;
	}
	FCFSaverageWaitTime = FCFSaverageWaitTime / NumberOfDataItems;
	FCFSaverageTRTime = FCFSaverageTRTime / NumberOfDataItems;
	
	FCFSaverageWaitTime = Math.round(FCFSaverageWaitTime);
	FCFSaverageTRTime = Math.round(FCFSaverageTRTime);
}

public void SJN() 
{
	int startTime = 0;
	int endTime = ProcessTime[0];
	int contextSwitch = 5;

	WaitTime[0] = startTime - ArrivalTime[0];
	TurnaroundTime[0] = endTime - ArrivalTime[0];
	
	int[] tempArrayProcess = new int[NumberOfDataItems-1];
	int[] tempArrayArrival = new int[NumberOfDataItems-1];
	for (int i=0; i<NumberOfDataItems-1; i++)
	{
		tempArrayProcess[i] = ProcessTime[i+1];
		tempArrayArrival[i] = ArrivalTime[i+1];
	}
	//Sort the temp arrays 
	int temp;
    for (int i = 0; i<tempArrayProcess.length; i++)
    {
        for (int j = 0; j<tempArrayProcess.length; j++)
        {
            if (tempArrayProcess[i] < tempArrayProcess[j])
            {
                temp = tempArrayProcess[i];
                tempArrayProcess[i] = tempArrayProcess[j];
                tempArrayProcess[j] = temp;
                temp = tempArrayArrival[i];
	            tempArrayArrival[i] = tempArrayArrival[j];
	            tempArrayArrival[j] = temp;
            }
        }
    }
    for (int i = 0; i<tempArrayProcess.length; i++)
    {
        for (int j = 0; j<tempArrayProcess.length; j++)
        {
            if (tempArrayProcess[i] == tempArrayProcess[j] && tempArrayArrival[i] < tempArrayArrival[j])
            {
                temp = tempArrayProcess[i];
                tempArrayProcess[i] = tempArrayProcess[j];
                tempArrayProcess[j] = temp;
                temp = tempArrayArrival[i];
	            tempArrayArrival[i] = tempArrayArrival[j];
	            tempArrayArrival[j] = temp;
            }
        }
    }
    //for (int i=0; i<tempArrayProcess.length; i++)
    //{System.out.println(tempArrayProcess[i] + " -Process..." + tempArrayArrival[i] + " -Arrival");}

	startTime = endTime + contextSwitch;
	for (int i=0; i<NumberOfDataItems-1; i++)
	{
		for (int j=1; j<NumberOfDataItems; j++)
		{
			//System.out.println(ProcessTime[j] + "\t" + ArrivalTime[j]);
			if (tempArrayProcess[i] == ProcessTime[j] && tempArrayArrival[i] == ArrivalTime[j]
					&& startTime >= ArrivalTime[j] && Processed[j] != ArrivalTime[j])
			{
				WaitTime[j] = startTime - ArrivalTime[j];
				Processed[j] = ArrivalTime[j];
				startTime = startTime + ProcessTime[j] + contextSwitch;
				System.out.println(j + " - " + ProcessTime[j] + " down / new start time: "+startTime);
				i = 0;
			}	
		}
	}
	
	for (int i = 0; i<NumberOfDataItems; i++)
	{
		TurnaroundTime[i] = ProcessTime[i] + WaitTime[i];
		SJNaverageWaitTime = SJNaverageWaitTime + WaitTime[i];
		SJNaverageTRTime = SJNaverageTRTime + TurnaroundTime[i];
		time = time + ProcessTime[i] + contextSwitch;
	}
	SJNaverageWaitTime = SJNaverageWaitTime / NumberOfDataItems;
	SJNaverageTRTime = SJNaverageTRTime / NumberOfDataItems;
	
	SJNaverageWaitTime = Math.round(SJNaverageWaitTime);
	SJNaverageTRTime = Math.round(SJNaverageTRTime);
}

public void RoundRobin(int timeQuantum) 
{
	int startTime = 0;
	int contextSwitch = 5;

	
	int[] tempArrayProcess = new int[NumberOfDataItems];
	for (int i=0; i<NumberOfDataItems; i++)
	{
		tempArrayProcess[i] = ProcessTime[i];
	}
	
	//1st iteration
	int count = 0;
	for (int i=0; i<NumberOfDataItems; i++)
	{
		WaitTime[i] = startTime - ArrivalTime[i];
		if (tempArrayProcess[i] <= timeQuantum && startTime >= ArrivalTime[i])
		{
			startTime = startTime + ProcessTime[i] + contextSwitch;
			TurnaroundTime[i] = startTime - ArrivalTime[i] - contextSwitch;
			tempArrayProcess[i] = 0;
			count++;
			System.out.println("OK " + i + " is processed / new start: " + startTime);
		} else {
			tempArrayProcess[i] = tempArrayProcess[i] - timeQuantum;
			startTime = startTime + timeQuantum + contextSwitch;
			System.out.println("SKIP " +i+ "....Now "+tempArrayProcess[i] + " ...new start: " + startTime);
		}
		
	}
	//All other iterations
	while (count < NumberOfDataItems){
		for (int i=0; i<NumberOfDataItems; i++)
		{
			if (tempArrayProcess[i] <= timeQuantum && startTime >= ArrivalTime[i]
					&& tempArrayProcess[i] != 0)
			{
				startTime = startTime + tempArrayProcess[i] + contextSwitch;
				TurnaroundTime[i] = startTime - ArrivalTime[i] - contextSwitch;
				tempArrayProcess[i] = 0;
				count++;
				System.out.println("OK " + i + " is processed / new start: " + startTime);
			} else if (tempArrayProcess[i] == 0)
			{
				continue;
			} else {
				tempArrayProcess[i] = tempArrayProcess[i] - timeQuantum;
				startTime = startTime + timeQuantum + contextSwitch;
				System.out.println("SKIP " +i+ "....Now "+tempArrayProcess[i] + " ...new start: " + startTime);
			}
		}
	}

	for (int i=0; i<NumberOfDataItems; i++)
	{
		RRaverageWaitTime = RRaverageWaitTime + WaitTime[i];
		RRaverageTRTime = RRaverageTRTime + TurnaroundTime[i];
	}
	RRaverageWaitTime = RRaverageWaitTime / NumberOfDataItems;
	RRaverageTRTime = RRaverageTRTime / NumberOfDataItems;
	time = startTime;
	
	RRaverageWaitTime = Math.round(RRaverageWaitTime);
	RRaverageTRTime = Math.round(RRaverageTRTime);
}


public void Initialize()
{
	time=0;
	RRaverageWaitTime = 0;
	RRaverageTRTime = 0;
	
	for (int i=0; i<NumberOfDataItems; i++)
	{
		Processed[i] = 0;
		WaitTime[i]=0;
		TurnaroundTime[i]=0;
	}
	
}
public int FindNextShortest()
{
	//System.out.println("find sjn");
    int index = 0;
    
    return index;
}
public void DisplayResults(Graphics g, String title, float avgWait, float avgTrnd)
{
	Font font1= new Font("Calibri", Font.BOLD, 16);
	g.setFont(font1);
	g.setColor(Color.red);
	g.drawString("Number Of Processes  = "+Integer.toString(NumberOfDataItems),500, 80);
	g.drawString("Total Period of Time = "+Integer.toString(time),500, 100);
	g.drawString("Time Quantum: 100 ", 500, 120);
	g.drawString("Context Switching Overhead: 5", 500, 140);
	int k = (this.getWidth()- (title.length()+20)*6)/2;
	g.drawString("Scheduling Policy: "+title,k, 160);
	Font font2 = new Font ("Calibri", Font.BOLD, 12);
	g.setFont(font2);
	g.setColor(Color.BLACK);
	int x = 50;
	int y = 200;
	for (int i=0; i<3; i++)
	{
		g.drawString("ID", x, y);
		g.drawString("PTime", x+40, y);
		g.drawString("ArrTime", x+100, y);
		g.drawString("Wait Time", x+165, y);
		g.drawString("TurnAround Time", x+230, y);
		y=y+15;
		g.drawLine(x, y, x+320, y);
		
		for (int j=0; j<30; j++)
		{
			y=y+15;
			g.drawString(Integer.toString(i*30+j), x, y);
			g.drawString(Integer.toString(ProcessTime[i*30+j]), x+40, y);
			g.drawString(Integer.toString(ArrivalTime[i*30+j]), x+100, y);
			g.drawString(Integer.toString(WaitTime[i*30+j]), x+170, y);
			g.drawString(Integer.toString(TurnaroundTime[i*30+j]), x+250, y);
		}
		x=x+360;
		y=200;
	}
	g.setFont(font1);
	g.setColor(Color.RED);
	g.drawString("Average Wait Time ", 450, 730);
	g.drawString("Average TurnAround Time", 600, 730);
	g.drawLine(400, 745, 800, 745);
	g.setColor(Color.BLACK);
	g.drawString(Float.toString(avgWait), 480, 760);
	g.drawString(Float.toString(avgTrnd), 650, 760);	
}
public void RunAllResults(Graphics g, float FCFSavgWait, float FCFSavgTrnd, 
		float SJNavgWait, float SJNavgTrnd, float RRavgWait, float RRavgTrnd)
{
	Font font1= new Font("Calibri", Font.BOLD, 16);
	g.setFont(font1);
	g.setColor(Color.red);
	g.drawString("Number Of Processes  = "+Integer.toString(NumberOfDataItems),500, 80);
	g.drawString("Total Period of Time = "+Integer.toString(time),500, 100);
	g.drawString("Time Quantum: 100 ", 500, 120);
	g.drawString("Context Switching Overhead: 5", 500, 140);
	
	g.setColor(Color.BLACK);
	g.drawString("Average Wait Time ", 550, 200);
	g.drawString("Average TurnAround Time", 750, 200);
	g.drawLine(100, 230, 1000, 230);
	g.drawString("First Come First Served (FCFS) ", 160, 250); 
		g.drawString(Float.toString(FCFSavgWait), 590, 250);
		g.drawString(Float.toString(FCFSavgTrnd), 800, 250);
	g.drawString("Shortest Job Next (SJN) ", 160, 275);
		g.drawString(Float.toString(SJNavgWait), 590, 275);
		g.drawString(Float.toString(SJNavgTrnd), 800, 275);
	g.drawString("Round Robin (RR) ", 160, 300);
		g.drawString(Float.toString(RRavgWait), 590, 300);
		g.drawString(Float.toString(RRavgTrnd), 800, 300);
}
public void BonusResults(Graphics g, String title)
{
	Font font1= new Font("Calibri", Font.BOLD, 16);
	g.setFont(font1);
	g.setColor(Color.red);
	g.drawString("Number Of Processes  = "+Integer.toString(NumberOfDataItems),500, 100);
	g.drawString("Context Switching Overhead: 5", 500, 140);
	g.drawString("Scheduling Policy: "+title,500, 120);
	
	g.setColor(Color.BLACK);
	g.drawString("Time Slice ", 250, 200);
	g.drawString("Average Wait Time ", 550, 200);
	g.drawString("Average TurnAround Time", 750, 200);
	g.drawLine(100, 230, 1000, 230);
	int x = 280; 
	int y = 250;
	for (int i=50; i<=250; i+=25)
	{
		Initialize();
		RoundRobin(i);
		g.drawString(Integer.toString(i), x, y);
		g.drawString(Float.toString(RRaverageWaitTime), 590, y);
		g.drawString(Float.toString(RRaverageTRTime), 800, y);
		y=y+25;
	}
}
}