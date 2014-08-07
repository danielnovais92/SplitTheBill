package pt.novais.daniel.splitthebill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class ProductActivity extends ActionBarActivity {

    private Product product;
    private ArrayList<String> prodNames;
    private boolean upd;
    private HashMap<String,LinearLayout> nameLst;

    //@SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent myIntent = getIntent();
        product = (Product)myIntent.getSerializableExtra("updProd");

        String ppl = myIntent.getStringExtra("peeps");
        Gson gson = new Gson();
        Type entityType = new TypeToken<LinkedHashMap<String, Person>>(){}.getType();
        Map<String, Person> people = gson.fromJson(ppl, entityType);
        upd = false;

        prodNames = (ArrayList<String>)myIntent.getSerializableExtra("pNames");

        LinearLayout root = (LinearLayout) findViewById(R.id.mainLayout);
        nameLst = new HashMap<String,LinearLayout>();
        for (String name : people.keySet()) {
            nameLst.put(name, newEntry(root));
        }

        if (product != null) {
            setTitle(product.getName());
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            populateUpd(product, nameLst);
            upd = true;
        }
        else {
            populate(nameLst);
            setTitle("New Product");
        }

    }

    private void populate(HashMap<String,LinearLayout> nameLst) {
        CheckBox checkbox;
        SeekBar seekBar;
        for (String name : nameLst.keySet()) {
            LinearLayout layout = nameLst.get(name);

            checkbox = (CheckBox)layout.findViewById(R.id.checkBox);
            seekBar = (SeekBar)layout.findViewById(R.id.seekBar);

            checkbox.setText(name);

            final SeekBar seekBar2 = seekBar;
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    seekBar2.setEnabled(b);
                    if (b) {
                        seekBar2.setProgress(50);
                    }
                    else {
                        seekBar2.setProgress(0);
                    }
                }
            });

            final int step = 25;
            final CheckBox checkbox2 = checkbox;
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    i = Math.round(i/step ) * step;
                    if (i==0 && checkbox2.isChecked()) i = 25;
                    if (i==100) i = 75;
                    seekBar.setProgress(i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }

    }

    private void populateUpd(Product p, HashMap<String,LinearLayout> nameLst) {
        EditText productName = (EditText) findViewById(R.id.productNameET);
        EditText productCost = (EditText) findViewById(R.id.productCostET);

        productName.setText(p.getName());
        productCost.setText(p.getCost().toString());

        CheckBox checkbox;
        SeekBar seekBar;
        for (String name : nameLst.keySet()) {
            LinearLayout layout = nameLst.get(name);

            checkbox = (CheckBox)layout.findViewById(R.id.checkBox);
            seekBar = (SeekBar)layout.findViewById(R.id.seekBar);

            checkbox.setText(name);
            checkbox.setChecked(p.getChecks().get(name));
            seekBar.setProgress(p.getSeeks().get(name));

            final SeekBar seekBar2 = seekBar;
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    seekBar2.setEnabled(b);
                    if (!b)
                        seekBar2.setProgress(0);
                    else
                        seekBar2.setProgress(50);
                }
            });

            final int step = 25;
            final CheckBox finalCheckbox = checkbox;
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    i = Math.round(i/step ) * step;
                    if (i==0 && finalCheckbox.isChecked()) i = 25;
                    if (i==100) i = 75;
                    seekBar.setProgress(i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }

    }

    private boolean newProduct (HashMap<String,LinearLayout> nameLst) {

        EditText productName = (EditText) findViewById(R.id.productNameET);
        EditText productCost = (EditText) findViewById(R.id.productCostET);

        if (!upd) {
            if (prodNames.contains(productName.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Product already specified.", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        CheckBox checkBox;
        SeekBar seekBar;
        HashMap<String,Boolean> check = new HashMap<String,Boolean>();
        HashMap<String,Integer> seek = new HashMap<String,Integer>();
        for (String name : nameLst.keySet()) {
            LinearLayout layout = nameLst.get(name);

            checkBox = (CheckBox) layout.findViewById(R.id.checkBox);
            seekBar = (SeekBar) layout.findViewById(R.id.seekBar);
            check.put(name,checkBox.isChecked());
            seek.put(name,seekBar.getProgress());

        }

        if (!test(check)) {
            Toast.makeText(getApplicationContext(), "It seems like no one " +
                    "wants to pay for this.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (
                !productName.getText().toString().equals("") &&
                !productCost.getText().toString().equals("")
           ) {
            product = new Product(productName.getText().toString(),
                    Double.parseDouble(productCost.getText().toString()),
                    check,
                    seek);
            return true;
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill all fields.", Toast.LENGTH_LONG).show();
            return false;
        }


    }

    private boolean test (HashMap<String,Boolean> check) {
        int aux = 0;
        for (String name : check.keySet()){
            if (check.get(name)) aux++;
        }
        return aux != 0;
    }

    private LinearLayout newEntry (LinearLayout rootView) {
        LayoutInflater mInf = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = mInf.inflate(R.layout.amount_entry, rootView, false);
        LinearLayout entry = (LinearLayout)v.findViewById(R.id.amountEntrie);
        rootView.addView(entry);
        return entry;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_new:
                if (newProduct(nameLst)) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", product);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
                return true;

            case R.id.action_remove:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("R.E.M.",true);
                setResult(RESULT_OK, returnIntent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
