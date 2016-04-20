//David Donaldson
//Player
//This code actually runs the Music Player
package com.example.thefirstorder.mozart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;

import android.graphics.Color;
import android.widget.TextView;

import org.w3c.dom.Text;
//import javax.*;
/*import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;*/

public class Player extends AppCompatActivity implements View.OnClickListener
{

    private final static char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    private final static char[] HEX_LOWER_CASE_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    int position;
    Uri u;
    SeekBar sb;
    Button btPlay, btFF, btRW, btNxt, btPrv, btMA;
    TextView timePos, timeDur, info;
    ImageView albumArt;
    String songName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btPlay = (Button) findViewById(R.id.btPlay);
        btFF = (Button) findViewById(R.id.btFF);
        btRW = (Button) findViewById(R.id.btRW);
        btNxt = (Button) findViewById(R.id.btNxt);
        btPrv = (Button) findViewById(R.id.btPrv);
        btMA = (Button) findViewById(R.id.btMA);
        sb = (SeekBar) findViewById(R.id.seekBar);
        info = (TextView) findViewById(R.id.info);
        timePos = (TextView) findViewById(R.id.timePos);
        timeDur = (TextView) findViewById(R.id.timeDur);
        albumArt = (ImageView) findViewById(R.id.albumArt);

        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btRW.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPrv.setOnClickListener(this);
        btMA.setOnClickListener(this);

        if(mp != null)
        {
            mp.stop();
            mp.release();
        }


        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songList");
        position = b.getInt("pos", 0);
        //songName = stripName(u.toString());
        u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.start();
        info.setText(stripName(u.toString()));
        albumArt.setImageURI(artCheck(stripName(u.toString())));
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                mp.seekTo(seekBar.getProgress());
            }
        });


        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleWithFixedDelay(new Runnable()
        {
            @Override
            public void run() {
                monitorHandler.sendMessage(monitorHandler.obtainMessage());
            }},
            200, //initialDelay
            200, //delay
            TimeUnit.MILLISECONDS);
        }

    private Uri artCheck(String artName)
    {

        return null;
    }


    Handler monitorHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                mediaPlayerMonitor();
            }
        };


        private void mediaPlayerMonitor()
        {
            if (mp == null)
            {
                sb.setVisibility(View.INVISIBLE);
            }
            else
            {
                if(mp.isPlaying())
                {
                    sb.setVisibility(View.VISIBLE);

                    int mediaDuration = mp.getDuration();
                    int mediaPosition = mp.getCurrentPosition();
                    sb.setMax(mediaDuration);
                    sb.setProgress(mediaPosition);
                    timePos.setText(playTime((float)mediaPosition/1000));
                    timeDur.setText(playTime((float)mediaDuration/1000));
                }
            }
        }

    private String stripName(String songName)
    {
        String filePath = Environment.getExternalStorageDirectory().toString() + "/Music/";
        if(songName.contains(filePath))
        {
            songName = songName.replace(filePath, "");
            songName = songName.replace(".mp3", "");
            return songName;
        }
        return "Error";
    }

    //Code for the buttons
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btPlay: {
                if (mp.isPlaying()) {
                    btPlay.setText(">");
                    mp.pause();
                } else {
                    mp.start();
                    btPlay.setText("||");
                }
                break;
            }
            case R.id.btFF: {
                mp.seekTo(mp.getCurrentPosition() + 5000);
                break;
            }
            case R.id.btRW: {
                mp.seekTo(mp.getCurrentPosition() - 5000);
                break;
            }
            case R.id.btNxt: {
                mp.stop();
                mp.release();
                position = (position + 1) % mySongs.size();
                Uri u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                break;
            }
            case R.id.btPrv: {
                mp.stop();
                mp.release();
                position = ((position - 1) < 0) ? mySongs.size() - 1 : position - 1;
                Uri u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                break;
            }
            //This is where the special stuff goes
            case R.id.btMA: {
                File mp3ToConvert = new File(u.toString());
                // BufferedInputStream inStream;
                StringBuilder bArray = new StringBuilder();
                try (BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(mp3ToConvert))){

                    for(int b; (b = inStream.read()) != -1;)
                    {
                        String s = Integer.toHexString(b).toUpperCase();
                        if(s.length() == 1)
                        {
                            bArray.append('1');
                        }
                    }
                    System.out.println("bArray1 = " + bArray.toString());
                    start(bArray);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                //Resolution size is needed
                //
                break;
            }
            case R.id.seekBar:
            {

            }
            default: {

            }
        }
    }

//    public static String toHexString(byte[] array, int offset, int length, boolean upperCase)
//    {
//        char[] digits = upperCase ? HEX_DIGITS : HEX_LOWER_CASE_DIGITS;
//        char[] buf = new char[length * 2];
//
//        int bufIndex = 0;
//        for (int i = offset ; i < offset + length; i++)
//        {
//            byte b = array[i];
//            buf[bufIndex++] = digits[(b >>> 4) & 0x0F];
//            buf[bufIndex++] = digits[b & 0x0F];
//        }
//
//        return new String(buf);
//    }

    public String playTime(float timeInSeconds)
    {
        if(timeInSeconds < 1)
            return "0:00";
        else
        {
            int timeInMinutes = (int) timeInSeconds / 60;
            //If 0, then we haven't gotten to a minute yet
            if(timeInMinutes == 0)
            {
                if((timeInSeconds % 60) < 10)
                    return ("0:0" + (int)timeInSeconds);
                else
                    return("0:" + (int)(timeInSeconds % 60));
            }
            else
            {
                if((timeInSeconds % 60) < 10)
                    return (timeInMinutes + ":0" + (int)(timeInSeconds % 60));
                else
                    return(timeInMinutes + ":" + (int)(timeInSeconds % 60));
            }
        }
    }


    public static void start(StringBuilder tobeConverted)
    {
        //This converts the passed byte array to a string of capitol hex digits
        String musicString = tobeConverted.toString();

        //Changes Start
        //This is to just set the MAX_BYTES depending on the resolution (EX. 256 x 256) and multiplied by 6
        //Just for testing purposes
        int MAX_BYTE = 60;
        int MAX_BIT_SIZE = 393216;
        int res = 0;

        String bitstring = ""; //This will hold each hex one at a time
        //BufferedImage img = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        int width = 480;
        int height = 720;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(width, height, conf);
        Canvas canvas = new Canvas(bmp);

        int i = 0;
        int j = 0;
        int j2 = 0;
        int j3 = 0;
        int j4 = 0;
        int j5 = 0;
        int j6 = 0;
        int j7 = 0;
        int j8 = 0;
        int j9 = 0;
        int j10 = 0;
        int k = 0;
        int l = 0;

        res = 480;


        //This is just to randomly generate the hex values and store them into "bitstring" for testing purposes
		while(MAX_BYTE != 0){
			Random rand = new Random();
			for(int m = 0; m < 6; m++){
			String value = "" + rand.nextInt(15);
			if(value.equals("10"))
				value = "A";
			else if(value.equals("11"))
				value = "B";
			else if(value.equals("12"))
				value = "C";
			else if(value.equals("13"))
				value = "D";
			else if(value.equals("14"))
				value = "E";
			else if(value.equals("15"))
				value = "F";

			musicString += value;
			}
			MAX_BYTE -= 6;
		}

		/*
		 * The Colors being used are:
		 * FFFFFF: White (0 - 31) = w
		 * 808080: Gray	 (32 - 63) = g
		 * 000000: Black (64 - 95) = bl
		 * FF0000: Red   (96 - 127) = r
		 * FFFF00: Yellow (128 - 159) = y
		 * 00FF00: Lime  (160 - 191) = li
		 * 0000FF: Blue  (192 - 223) =
		 * FF00FF: Fuchsia (224-255)
		 *
		 */

        char rValue = 'a';
        char gValue = 'b';
        char bValue = 'c';

        int rg = 0;
        int gr = 0;
        int rb = 0;
        int br = 0;
        int gb = 0;
        int bg = 0;

        //
        int white = 0;
        int gray = res / 10;
        int black = (2 * res) / 10;
        int red = (3 * res) / 10;
        int yellow = (4 * res) / 10;
        int lime = (5 * res) / 10;
        int aqua = (6 * res) / 10;
        int blue = (7 * res) / 10;
        int fuchsia = (8 * res) / 10;
        int purple = (9 * res) / 10;

        bitstring = "D385A4";
        System.out.println(bitstring);
        System.out.println("byteArray = " + musicString);

        //Algorithm for reading in every 6th value
        for (k = 0; k < musicString.length(); k += 6) {
            bitstring = "";
            for (l = k; l < k + 6; l++) {
                bitstring += musicString.charAt(l);
            }

            //System.out.println(bitstring);


            //Algorithm for organizing the colors
            rValue = bitstring.charAt(0);
            gValue = bitstring.charAt(2);
            bValue = bitstring.charAt(4);

            rg = Character.getNumericValue(rValue) -
                    Character.getNumericValue(gValue);

            gr = Character.getNumericValue(gValue) -
                    Character.getNumericValue(rValue);

            rb = Character.getNumericValue(rValue) -
                    Character.getNumericValue(bValue);

            br = Character.getNumericValue(bValue) -
                    Character.getNumericValue(rValue);

            gb = Character.getNumericValue(gValue) -
                    Character.getNumericValue(bValue);

            bg = Character.getNumericValue(bValue) -
                    Character.getNumericValue(gValue);

//			System.out.println("RGB Values");
//			System.out.println(rValue);
//			System.out.println(gValue);
//			System.out.println(bValue);
//			System.out.println("Subtract Values");
//			System.out.println(Math.abs(rg));
//			System.out.println(Math.abs(rb));
//			System.out.println(Math.abs(bg));

            for(int x = 0; x < 256; x++){
                for(int y = 0; y < 256; y++){
                    int pixel = Color.parseColor("#"+bitstring);
                    bmp.setPixel(i, j, pixel);//int color);
                }
            }


            /*
            //Generate White
            if (rValue >= 'D' && gValue >= 'D' && bValue >= 'D') {
                bitstring = "0x" + bitstring;

                int bColor = Color.parseColor(bitstring);

                img.setRGB(white, j, bColor.getRGB());
                white++;
                if (white == (res / 10) - 1) {
                    white = 0;
                    j++;
                }
                if (j == 720)
                    j = 0;

                bitstring = "";
                //System.out.println("Printed white");

            }


            //Generate Gray
            else if (rValue >= '3' && gValue >= '3' && bValue >= '3' && (Math.abs(rg) <= 1 && Math.abs(rb) <= 1 && Math.abs(bg) <= 1)) {
                bitstring = "0x" + bitstring;
                Color bColor = Color.parseColor(bitstring);

                img.setRGB(gray, j2, bColor.getRGB());
                gray++;
                if (gray == ((2 * res) / 10) - 1) {
                    gray = (res / 10);
                    j2++;
                }
                if (j2 == 720)
                    j2 = 0;

                bitstring = "";
                //System.out.println("Printed gray");

            }

            //Generate Black
            else if (rValue <= '3' && gValue <= '3' && bValue <= '3') {
                bitstring = "0x" + bitstring;
                Color bColor = Color.parseColor(bitstring);

                img.setRGB(black, j3, bColor.getRGB());
                black++;
                if (black == ((3 * res) / 10) - 1) {
                    black = (2 * res) / 10;
                    j3++;
                }
                if (j3 == 720)
                    j3 = 0;

                bitstring = "";
                //System.out.println("Printed black");

            }


            //Generate Yellow
            else if (rValue > bValue && gValue > bValue && rValue >= '5' && gValue >= '5' && Math.abs(rg) <= 5 && (Math.abs(rb) >= 3 || Math.abs(gb) >= 3)) {
                bitstring = "0x" + bitstring;
                Color bColor = Color.parseColor(bitstring);

                img.setRGB(yellow, j4, bColor.getRGB());
                yellow++;
                if (yellow == ((5 * res) / 10) - 1) {
                    yellow = (4 * res) / 10;
                    j4++;
                }
                if (j4 == 720)
                    j4 = 0;

                bitstring = "";
                //System.out.println("Printed yellow");

            }

            //Generate Aqua
            else if (bValue > rValue && gValue > rValue && bValue >= '9' && gValue >= '9' && Math.abs(bg) <= 5 && (Math.abs(gr) >= 3 || Math.abs(br) >= 3)) {
                bitstring = "0x" + bitstring;
                Color bColor = Color.parseColor(bitstring);

                img.setRGB(aqua, j10, bColor.getRGB());
                aqua++;
                if (aqua == ((7 * res) / 10) - 1) {
                    aqua = (6 * res) / 10;
                    j10++;
                }
                if (j10 == 720)
                    j10 = 0;

                bitstring = "";
                //System.out.println("Printed yellow");

            }


            //Generate Purple
            else if (rValue > gValue && bValue > gValue && rValue >= '9' && bValue >= '9' && Math.abs(rb) <= 5 && (Math.abs(rg) >= 3 || Math.abs(gb) >= 3)) {
                bitstring = "0x" + bitstring;
                Color bColor = Color.parseColor(bitstring);

                img.setRGB(fuchsia, j5, bColor.getRGB());
                fuchsia++;
                if (fuchsia == ((9 * res) / 10) - 1) {
                    fuchsia = (8 * res) / 10;
                    j5++;
                }
                if (j5 == 720)
                    j5 = 0;

                bitstring = "";
                //System.out.println("Printed fuchsia");

            }

            //Print Darker purple
            else if (rValue > gValue && bValue > gValue && rValue <= '8' && bValue <= '8' && Math.abs(rb) <= 5 && (Math.abs(rg) >= 3 || Math.abs(gb) >= 3)) {
                bitstring = "0x" + bitstring;
                Color bColor = Color.parseColor(bitstring);

                img.setRGB(purple, j9, bColor.getRGB());
                purple++;
                if (purple == res - 1) {
                    purple = (9 * res) / 10;
                    j9++;
                }
                if (j9 == 720)
                    j9 = 0;

                bitstring = "";
                //System.out.println("Printed purple");

            }


            //Generate Red
            else if (rValue >= gValue && rValue >= bValue && (Math.abs(rg) >= 4 || Math.abs(rb) >= 4)) {
                bitstring = "0x" + bitstring;
                Color bColor = Color.parseColor(bitstring);

                img.setRGB(red, j6, bColor.getRGB());
                red++;
                if (red == ((4 * res) / 10) - 1) {
                    red = (3 * res) / 10;
                    j6++;
                }
                if (j6 == 720)
                    j6 = 0;

                bitstring = "";
                //System.out.println("Printed red");

            }


            //Green is the highest
            else if (gValue >= rValue && gValue >= bValue) {// && (Math.abs(rg) >= 4 || Math.abs(gb) >= 4)){//&& gValue >= '4'
                bitstring = "0x" + bitstring;
                Color bColor = Color.parseColor(bitstring);

                img.setRGB(lime, j7, bColor.getRGB());
                lime++;
                if (lime >= ((6 * res) / 10) - 1) {
                    lime = ((5 * res) / 10);
                    j7++;
                }
                if (j7 == 720)
                    j7 = 0;

                bitstring = "";
                //System.out.println("Printed green");
            }


            //Blue is the highest
            else if (bValue >= gValue && bValue >= rValue && (Math.abs(rb) >= 4 && Math.abs(gb) >= 4)) {
                bitstring = "0x" + bitstring;
                Color bColor = Color.parseColor(bitstring);

                img.setRGB(blue, j8, bColor.getRGB());
                blue++;
                if (blue >= ((8 * res) / 10) - 1) {
                    blue = ((7 * res) / 10);
                    j8++;
                }
                if (j8 == 720)
                    j8 = 0;

                bitstring = "";
                //System.out.println("Printed blue");
            }

*/
            //This is the Original color setter
//			bitstring = "0x" + bitstring;
//			Color bColor =  Color.decode(bitstring);
//
//			img.setRGB(i, j, bColor.getRGB());
//			i++;
//			if( i == 256){
//				i = 0;
//				j++;
//			}
//			if (j == 256)
//				j = 0;
//
//			bitstring = "";
            //}


            //This was just for randomly generated hex strings
//		while (MAX_BYTE != 0){
//			Random rand = new Random();
//			for(int m = 0; m < 6; m++){
//			String value = "" + rand.nextInt(15);
//			if(value.equals("10"))
//				value = "A";
//			else if(value.equals("11"))
//				value = "B";
//			else if(value.equals("12"))
//				value = "C";
//			else if(value.equals("13"))
//				value = "D";
//			else if(value.equals("14"))
//				value = "E";
//			else if(value.equals("15"))
//				value = "F";
//
//			bitstring += value;
//			}
//
//
//			bitstring = "0x" + bitstring;
//			Color bColor =  Color.decode(bitstring);
//
//
//		//The following code works!!!
//			img.setRGB(i, j, bColor.getRGB());
//			i++;
//			if( i == 256){
//				i = 0;
//				j++;
//			}
//			if (j == 256)
//				j = 0;
//
//			bitstring = "";
//			MAX_BYTE = MAX_BYTE - 6;
//		}

//            File f = new File("redImage.jpg");
//            ImageIO.write(img, "JPEG", f);

            /*
            File file = test;
            OutputStream out = null;
            try {
                out = new BufferedOutputStream(new FileOutputStream(test));
            }
            finally {
                if (out != null){
                    out.close();
                }
            }
            */
            canvas.drawBitmap(bmp, 0, 0, null);
            //bmp.compress(Bitmap.CompressFormat.JPEG,100,test);
        }

    }
}
