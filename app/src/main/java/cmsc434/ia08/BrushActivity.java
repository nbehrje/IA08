package cmsc434.ia08;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class BrushActivity extends Activity {
    final static private int RESULT_SAVE = 1;
    int red;
    int green;
    int blue;
    int alpha;
    SeekBar rSeekBar;
    SeekBar gSeekBar;
    SeekBar bSeekBar;
    SeekBar aSeekBar;
    float size;
    SeekBar sizeSeekBar;
    TextView sizeNum;
    Button saveButton;
    Button cancelButton;
    ImageView preview;
    SharedPreferences prefs;
    CheckBox checkBox;
    Boolean vTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush);

        sizeSeekBar = (SeekBar) findViewById(R.id.sizeSeekBar);
        sizeNum = (TextView) findViewById(R.id.sizeNum);
        sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sizeNum.setText(Integer.toString(i+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                size = seekBar.getProgress() + 1;
            }
        });

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sizeSeekBar.setEnabled(!b);

            }
        });

        preview = (ImageView) findViewById(R.id.preview);
        rSeekBar = (SeekBar) findViewById(R.id.rSeekBar);
        rSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                preview.setBackgroundColor(Color.argb(alpha, i, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                red = seekBar.getProgress();
            }
        });
        gSeekBar = (SeekBar) findViewById(R.id.gSeekBar);
        gSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                preview.setBackgroundColor(Color.argb(alpha, red, i, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                green = seekBar.getProgress();
            }
        });
        bSeekBar = (SeekBar) findViewById(R.id.bSeekBar);
        bSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                preview.setBackgroundColor(Color.argb(alpha, red, green, i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                blue = seekBar.getProgress();
            }
        });
        aSeekBar = (SeekBar) findViewById(R.id.aSeekBar);
        aSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                preview.setBackgroundColor(Color.argb(i, red, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                alpha = seekBar.getProgress();
            }
        });

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat("size", size);
                editor.putInt("red", red);
                editor.putInt("green", green);
                editor.putInt("blue", blue);
                editor.putInt("alpha", alpha);
                editor.putBoolean("vTracker", checkBox.isChecked());
                editor.commit();
                Intent intent = new Intent();
                intent.putExtra("size", size);
                intent.putExtra("red", red);
                intent.putExtra("green", green);
                intent.putExtra("blue", blue);
                intent.putExtra("alpha", alpha);
                intent.putExtra("vTracker", checkBox.isChecked());
                setResult(RESULT_SAVE, intent);
                finish();
            }
        });

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        prefs = getPreferences(Context.MODE_PRIVATE);
        size = prefs.getFloat("size", 50);
        sizeSeekBar.setProgress((int) size - 1);
        red = prefs.getInt("red", 0);
        blue = prefs.getInt("blue", 0);
        green = prefs.getInt("green", 0);
        alpha = prefs.getInt("alpha", 255);
        vTracker = prefs.getBoolean("vTracker", false);
        rSeekBar.setProgress(red);
        gSeekBar.setProgress(green);
        bSeekBar.setProgress(blue);
        aSeekBar.setProgress(alpha);
        preview.setBackgroundColor(Color.argb(alpha, red, green, blue));
        checkBox.setChecked(vTracker);
    }

}
