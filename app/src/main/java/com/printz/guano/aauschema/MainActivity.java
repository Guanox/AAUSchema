package com.printz.guano.aauschema;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView schemaListView;
    private String _HTMLContent;
    private final String SCHEMA_URL = "https://www.moodle.aau.dk/calmoodle/public/?sid=2269";
    private SchemaDownloader mSchemaDownloader;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        schemaListView = (ListView) findViewById(R.id.lstv_schema);
        mSchemaDownloader = new SchemaDownloader();
        mSchemaDownloader.execute(SCHEMA_URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            new SchemaDownloader().execute(SCHEMA_URL);
            Log.d(TAG, "Updated schema");
        }

        return super.onOptionsItemSelected(item);
    }

    private class SchemaDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            _HTMLContent = downloadData(params[0]);

            if (_HTMLContent == null) {
                Log.e(TAG, "Error downloading XML");
            }

            return _HTMLContent;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            HTMLParser HTMLParser = new HTMLParser(_HTMLContent);
            HTMLParser.process();
            ArrayAdapter<SchemaDay> arrayAdapter = new ArrayAdapter<SchemaDay>(
                    MainActivity.this, R.layout.list_item, HTMLParser.getSchemaDays()
            );
            schemaListView.setAdapter(arrayAdapter);
        }

        private String downloadData(String urlPath) {
            StringBuffer stringBuffer = new StringBuffer();

            try {
                URL url = new URL(urlPath);
                Log.d(TAG, "Formed URL: " + url.toString());

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                int response = httpURLConnection.getResponseCode();
                Log.d(TAG, "HTTP reponse was: " + response);
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int charsRead;
                char[] buffer = new char[256];

                while (true) {
                    charsRead = inputStreamReader.read(buffer);
                    if (charsRead <= 0) {
                        break;
                    }
                    stringBuffer.append(String.copyValueOf(buffer, 0, charsRead));
                }

                return stringBuffer.toString();

            } catch (IOException e) {
                Log.e(TAG, "IO exception");
            } catch (SecurityException e) {
                Log.d(TAG, "Security permissions missing.");
            }

            return null;
        }
    }
}
