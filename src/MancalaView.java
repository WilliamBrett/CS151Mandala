import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MancalaView extends JFrame implements ChangeListener 
{
	private MancalaModel theModel;
	
	MancalaView(MancalaModel r_model)
	{
		theModel = r_model;
	}
	public MancalaModel getModel()
	{
		return theModel;
	}
	public void setModel(MancalaModel r_model)
	{
		theModel = r_model;
	}
	@Override
	public void stateChanged(ChangeEvent c) 
	{
			
	}

}
