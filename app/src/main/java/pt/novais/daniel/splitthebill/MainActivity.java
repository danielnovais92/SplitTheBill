package pt.novais.daniel.splitthebill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    private Map<String, Person> people;
    private ArrayList<LinearLayout> entryList = new ArrayList<LinearLayout>();
    private ScrollView scrlv;
    private LinearLayout root;
    private AdView adView;
    private static final String MY_AD_UNIT_ID = "ca-app-pub-9369931784095626/8925003191";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adView = new AdView(this);
        adView.setAdUnitId(MY_AD_UNIT_ID);
        adView.setAdSize(AdSize.BANNER);

        LinearLayout layout = (LinearLayout) findViewById(R.id.adsLayoutMain);
        layout.addView(adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("7500A3C8C475F70D26EBAD88C85F3D02")
                .build();

        adView.loadAd(adRequest);

        people = new LinkedHashMap<String, Person>();

        scrlv = (ScrollView)findViewById(R.id.scrollV);

        root = (LinearLayout) findViewById(R.id.root);

        newEntry(root);
        newEntry(root);

    }

    private void newEntry (LinearLayout root) {
        LayoutInflater mInf = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = mInf.inflate(R.layout.name_entry, root, false);
        LinearLayout entry = (LinearLayout)v.findViewById(R.id.entry);
        entryList.add(entry);
        root.addView(entry);
        scrlv.fullScroll(View.FOCUS_DOWN);
    }

    private void error () {
        Toast.makeText(getApplicationContext(), "Something's wrong! Check your " +
                        "values and names. Remember that the payments must add up to the total!",
                Toast.LENGTH_LONG).show();
        people.clear();
    }

    private void submit() {

        EditText nameET, paidET, totalET;
        String name;
        Double total, each;
        total = 0.0;
        for (LinearLayout l : entryList) {
            nameET = (EditText) l.findViewById(R.id.nameET);
            name = nameET.getText().toString();

            if (!name.matches("")) {
                paidET = (EditText) l.findViewById(R.id.paidET);
                if (!paidET.getText().toString().matches(""))
                    each = Double.parseDouble(paidET.getText().toString());
                else
                    each = 0.0;

                if (people.containsKey(name))
                    error();
                else
                    people.put(name, new Person(each, 0.0));

                total += each;
            }

        }
        totalET = (EditText) findViewById(R.id.totalPaidET);
        if (totalET.getText().toString().matches("") ||
                total.compareTo(Double.parseDouble(totalET.getText().toString())) != 0 ||
                people.size() < 2)
            error();
        else {
            Gson gson = new Gson();
            String ppl = gson.toJson(people);
            Intent myIntent = new Intent(MainActivity.this, ListProductsActivity.class);
            myIntent.putExtra("peeps", ppl);
            myIntent.putExtra("tots", total);
            MainActivity.this.startActivity(myIntent);

        }
    }

    private void openPrevious () {

        SharedPreferences sp = getApplicationContext().getSharedPreferences("BillSave", 0);
        String save = sp.getString("saves", null);

        Gson gson = new Gson();
        Type entityType = new TypeToken<ArrayList<Save>>(){}.getType();
        final ArrayList<Save> saves = gson.fromJson(save, entityType);

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Select Bill by Date:");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.select_dialog_singlechoice);

        if (saves != null) {
            for (Save s : saves) {
                arrayAdapter.add(s.getDate().toString().substring(0,19));
            }
        }

        builderSingle.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(MainActivity.this, ResultActivity.class);
                        assert saves != null;
                        myIntent.putExtra("save", saves.get(which));
                        MainActivity.this.startActivity(myIntent);

                    }
                });
        builderSingle.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        people.clear();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_new:
                if (entryList.size() < 16)
                    newEntry(root);
                return true;

            case R.id.action_remove:
                if (entryList.size() > 2) {
                    root.removeView(entryList.get(entryList.size() - 1));
                    entryList.remove(entryList.size() - 1);
                    scrlv.fullScroll(View.FOCUS_DOWN);
                }
                return true;

            case R.id.action_accept:
                submit();
                return true;

            case R.id.action_about:
                Intent myIntent = new Intent(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(myIntent);
                return true;

            case R.id.action_load:
                openPrevious();
                return true;

            case R.id.action_contact:
                Intent myIntent2 = new Intent(MainActivity.this, ContactActivity.class);
                MainActivity.this.startActivity(myIntent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
