package pt.novais.daniel.splitthebill;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class ContactActivity extends AppCompatActivity {

    private AdView adView;
    private static final String MY_AD_UNIT_ID = "ca-app-pub-9369931784095626/8925003191";
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        setTitle("Contact");

        // Criar o anúncio intersticial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(MY_AD_UNIT_ID);

        // Criar a solicitação de anúncio.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("7500A3C8C475F70D26EBAD88C85F3D02")
                .build();

        // Começar a carregar o anúncio intersticial.
        interstitial.loadAd(adRequest);

        Button ad = findViewById(R.id.adButton);
        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInterstitial();
            }
        });

        adView = new AdView(this);
        adView.setAdUnitId(MY_AD_UNIT_ID);
        adView.setAdSize(AdSize.LARGE_BANNER);

        LinearLayout layout = findViewById(R.id.adsLayoutContact);
        layout.addView(adView);

        adView.loadAd(adRequest);
    }

    // Chamar displayInterstitial() quando você estiver pronto para exibir um intersticial.

    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
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
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_accept) {
            Intent myIntent = new Intent(ContactActivity.this, MainActivity.class);
            ContactActivity.this.startActivity(myIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
