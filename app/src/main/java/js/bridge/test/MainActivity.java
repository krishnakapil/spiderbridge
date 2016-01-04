package js.bridge.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import js.bridge.test.bridge.ExampleBridge;

public class MainActivity extends AppCompatActivity {
    final String[] viewNames = {"loginBtn", "signupBtn", "logoutBtn", "profileImg"};

    private Button mStartEventsBtn;
    private Button mStopEventsBtn;
    private TextView mTextView;

    private boolean mKeepFiringEvents;

    private FireEventsTask mFireEventsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mStartEventsBtn = (Button) findViewById(R.id.start_events_btn);
        mStopEventsBtn = (Button) findViewById(R.id.stop_events_btn);
        mTextView = (TextView) findViewById(R.id.events_text);

        mStartEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKeepFiringEvents = true;
                mFireEventsTask = new FireEventsTask();
                mFireEventsTask.execute();
            }
        });

        mStopEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKeepFiringEvents = false;
                if (mFireEventsTask != null) {
                    mFireEventsTask.cancel(true);
                }
            }
        });

        findViewById(R.id.container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.v(MainActivity.class.getSimpleName(), "Touched " + motionEvent.toString());
                return false;
            }
        });
    }

    private int getRandomNumber(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private class FireEventsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ExampleBridge.getBridge().releaseThread();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ExampleBridge.getBridge().acquireThread();
            long i = 0;
            while (mKeepFiringEvents) {
                int rand1 = getRandomNumber(0, viewNames.length - 1);
                int rand2 = getRandomNumber(1, 5000);

                if (rand2 % 3 == 0) {
                    ExampleBridge.getBridge().notifySwipe(viewNames[rand1]);
                } else if (rand2 % 2 == 0) {
                    ExampleBridge.getBridge().notifyTouch(viewNames[rand1]);
                } else {
                    ExampleBridge.getBridge().notifyClick(viewNames[rand1]);
                }

                i++;

                final long finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText("No. Events Fired : - " + finalI);
                    }
                });

            }

            ExampleBridge.getBridge().releaseThread();
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            ExampleBridge.getBridge().acquireThread();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ExampleBridge.getBridge().acquireThread();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ExampleBridge.getBridge().destroyBridge();
    }
}
