package edu.gatech.ppl.cycleatlanta;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.provider.Settings;

public class Feedback extends Activity
{
	final  Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	final int go = 700;
	final int pause = 200;
	final long[] pattern = {0, go, pause, go, pause, go};
	final MediaPlayer mp = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);

	public void giveFeedback()
	{
		vibe.vibrate(pattern, -1);
		mp.start();
	}

}
