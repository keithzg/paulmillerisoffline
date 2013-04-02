package ca.keithzg.paulmiller.is.offline;

import java.util.TimerTask;

public class Count extends TimerTask{

	private Long remSec;
	public Count(Long sec)
	{
		this.remSec = sec;
	}
	
	@Override
	public void run() {

		Long seconds = remSec % 60 ;
		Long minutes = ((remSec / 60) % 60);
		Long hours   = ((remSec / (60*60)) % 24);
		Long days = ((remSec / (60*60*24)));
		
		MainActivity.paulTimeLeft = String.format(" %d days\n %d hours\n %d minutes\n %d seconds\n",
				MainActivity.days = days,
				MainActivity.hours = hours,
				MainActivity.minutes = minutes,
				MainActivity.seconds = seconds
		);
		remSec--;
	}
	
}
