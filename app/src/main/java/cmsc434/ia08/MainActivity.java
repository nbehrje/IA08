package cmsc434.ia08;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    ImageButton brushButton;
    ImageButton clearButton;
    ImageButton undoButton;
    DoodleView dv;
    static private final int BRUSH_REQUEST = 1;
    static private final int RESULT_SAVE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dv = (DoodleView) findViewById(R.id.doodleView);

        brushButton = (ImageButton) findViewById(R.id.brushButton);
        brushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BrushActivity.class);
                startActivityForResult(intent, BRUSH_REQUEST);
            }
        });

        clearButton = (ImageButton) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                alert.setMessage("Do you want to clear the drawing?");
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dv.clear();
                                dialogInterface.dismiss();
                            }
                        });
                alert.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                alert.show();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_SAVE) {
            dv.changeBrush(intent);
        }
    }

}
