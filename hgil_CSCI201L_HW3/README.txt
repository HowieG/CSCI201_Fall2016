Image file paths:

images/blue.png
images/dark_blue.png
images/gray.gif

I commented out the following code from TeamGUIComponents because it was giving me an odd error when showing the FJ pane:

		fjBetSlider.addChangeListener(new ChangeListener() {
		
					@Override
					public void stateChanged(ChangeEvent e)
					{
		
						changeBet(fjBetSlider.getValue());
					}
				});