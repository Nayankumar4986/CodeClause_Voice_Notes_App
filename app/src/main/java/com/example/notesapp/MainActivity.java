//package com.example.notesapp;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import java.util.ArrayList;
//
//public class MainActivity extends AppCompatActivity {
//
//    Button btncreate;
//    RecyclerView recyclernote;
//    FloatingActionButton fabadd;
//
//    DatabaseHelper databaseHelper;
//
//    LinearLayout llnotes;
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        initVar();
//
//        shownotes();
//
//        fabadd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Dialog dialog = new Dialog(MainActivity.this);
//                dialog.setContentView(R.layout.dialog_note);
//
//                EditText edttitle,edtcontent;
//                Button btnadd;
//
//                edttitle = dialog.findViewById(R.id.edittitle);
//                edtcontent = dialog.findViewById(R.id.editcontent);
//                btnadd = dialog.findViewById(R.id.btnadd);
//
//
//
//
//                btnadd.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String title = edttitle.getText().toString();
//                        String content = edtcontent.getText().toString();
//
//                        if (!content.equals("")){
//
//                            //data ko add krege
//
//                            databaseHelper.noteDao().addNote(new Note(title,content));
//                            shownotes();
//                            dialog.dismiss();
//
//                        }else {
//                            Toast.makeText(MainActivity.this, "Please Enter Something!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//                dialog.show();
//            }
//        });
//
//
//        btncreate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fabadd.performClick();
//            }
//        });
//    }
//
//
//
//
//    public void shownotes() {
//
//       ArrayList<Note> arrnotes = (ArrayList<Note>) databaseHelper.noteDao().getNotes();
//
//       if (arrnotes.size()>0){
//           recyclernote.setVisibility(View.VISIBLE);
//           llnotes.setVisibility(View.GONE);
//
//           recyclernote.setAdapter(new recyclerNotesAdapter(this,arrnotes,databaseHelper));
//
//       }else {
//           llnotes.setVisibility(View.VISIBLE);
//           recyclernote.setVisibility(View.GONE);
//       }
//    }
//
//    private void initVar() {
//
//        btncreate = findViewById(R.id.btncreatenote);
//        fabadd = findViewById(R.id.fabadd);
//        recyclernote = findViewById(R.id.recyclernotes);
//        llnotes=findViewById(R.id.llnotes);
//
//        recyclernote.setLayoutManager(new GridLayoutManager(this,2));
//
//        databaseHelper = DatabaseHelper.getInstance(this);
//
//
//    }
//}
//


package com.example.notesapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btncreate;
    RecyclerView recyclernote;
    FloatingActionButton fabadd;

    DatabaseHelper databaseHelper;

    LinearLayout llnotes;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVar();

        shownotes();

        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabadd.performClick();
            }
        });
    }

    private void showDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_note);

        EditText edttitle, edtcontent;
        Button btnadd;
        ImageView micImage;

        edttitle = dialog.findViewById(R.id.edittitle);
        edtcontent = dialog.findViewById(R.id.editcontent);
        btnadd = dialog.findViewById(R.id.btnadd);
        micImage = dialog.findViewById(R.id.micImage);

        micImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechRecognition(edtcontent);
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edttitle.getText().toString();
                String content = edtcontent.getText().toString();

                if (!content.equals("")) {
                    //data ko add krege
                    databaseHelper.noteDao().addNote(new Note(title, content));
                    shownotes();
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Please Enter Something!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void startSpeechRecognition(EditText editText) {
        if (isSpeechRecognitionPermissionGranted()) {
            String previousText = editText.getText().toString().trim(); // Store the previous text
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {

                @Override
                public void onReadyForSpeech(Bundle bundle) {}

                @Override
                public void onBeginningOfSpeech() {}

                @Override
                public void onRmsChanged(float v) {}

                @Override
                public void onBufferReceived(byte[] bytes) {}

                @Override
                public void onEndOfSpeech() {}

                @Override
                public void onError(int i) {}

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        String spokenText = matches.get(0);
                        String newText = previousText + " " + spokenText; // Append recognized text to previous text
                        editText.setText(newText);
                    }
                }



                @Override
                public void onPartialResults(Bundle bundle) {}

                @Override
                public void onEvent(int i, Bundle bundle) {}
            });



            Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

            speechRecognizer.startListening(speechIntent);
        } else {
            requestSpeechRecognitionPermission();
        }
    }

    private boolean isSpeechRecognitionPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestSpeechRecognitionPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                Toast.makeText(this, "Speech recognition permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Rest of the code...


    public void shownotes() {

       ArrayList<Note> arrnotes = (ArrayList<Note>) databaseHelper.noteDao().getNotes();

       if (arrnotes.size()>0){
           recyclernote.setVisibility(View.VISIBLE);
           llnotes.setVisibility(View.GONE);

           recyclernote.setAdapter(new recyclerNotesAdapter(this,arrnotes,databaseHelper));

       }else {
           llnotes.setVisibility(View.VISIBLE);
           recyclernote.setVisibility(View.GONE);
       }
    }

    private void initVar() {

        btncreate = findViewById(R.id.btncreatenote);
        fabadd = findViewById(R.id.fabadd);
        recyclernote = findViewById(R.id.recyclernotes);
        llnotes=findViewById(R.id.llnotes);

        recyclernote.setLayoutManager(new GridLayoutManager(this,2));

        databaseHelper = DatabaseHelper.getInstance(this);


    }
}
