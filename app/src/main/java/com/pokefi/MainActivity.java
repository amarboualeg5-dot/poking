package com.pokefi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 1;
    private ListView listView;
    private Button scanButton;
    private TextView titleText;
    private List<Monster> monsters = new ArrayList<>();
    private MonsterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleText = findViewById(R.id.titleText);
        scanButton = findViewById(R.id.scanButton);
        listView = findViewById(R.id.listView);

        adapter = new MonsterAdapter();
        listView.setAdapter(adapter);

        scanButton.setOnClickListener(v -> checkPermissionsAndScan());

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Monster m = monsters.get(position);
            Intent intent = new Intent(this, CaptureActivity.class);
            intent.putExtra("name", m.name);
            intent.putExtra("rarity", m.rarity);
            intent.putExtra("element", m.element);
            intent.putExtra("emoji", m.emoji);
            startActivity(intent);
        });
    }

    private void checkPermissionsAndScan() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                 Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST);
        } else {
            doScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doScan();
            } else {
                Toast.makeText(this, "Location permission needed to scan WiFi!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void doScan() {
        WifiManager wifiManager = (WifiManager) getApplicationContext()
                .getSystemService(WIFI_SERVICE);

        if (wifiManager == null || !wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "Please enable WiFi first!", Toast.LENGTH_SHORT).show();
            return;
        }

        scanButton.setText("⚡ Scanning...");
        scanButton.setEnabled(false);

        // Use cached results (works without triggering a new scan on modern Android)
        List<ScanResult> results = wifiManager.getScanResults();

        monsters.clear();
        if (results == null || results.isEmpty()) {
            // Generate demo monsters if no WiFi available (emulator / no results)
            monsters.add(Monster.fromFake("HomeNetwork_5G", -45, "WPA2", 5180));
            monsters.add(Monster.fromFake("Starbucks WiFi", -72, "", 2412));
            monsters.add(Monster.fromFake("BT-Hub-A3F2", -60, "WPA3", 5745));
            monsters.add(Monster.fromFake("DIRECT-Printer", -80, "WPA2", 2437));
            monsters.add(Monster.fromFake("SKY12345", -55, "WPA2", 5180));
            Toast.makeText(this, "Using demo data – no WiFi results found", Toast.LENGTH_SHORT).show();
        } else {
            for (ScanResult r : results) {
                monsters.add(Monster.fromScanResult(r));
            }
        }

        adapter.notifyDataSetChanged();
        titleText.setText("⚔️ " + monsters.size() + " Monsters Found!");
        scanButton.setText("🔍 Scan Again");
        scanButton.setEnabled(true);
    }

    // ── Inner adapter ──────────────────────────────────────────────────────────

    class MonsterAdapter extends ArrayAdapter<Monster> {
        MonsterAdapter() {
            super(MainActivity.this, R.layout.item_monster, monsters);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_monster, parent, false);
            }
            Monster m = monsters.get(position);
            ((TextView) convertView.findViewById(R.id.monsterEmoji)).setText(m.emoji);
            ((TextView) convertView.findViewById(R.id.monsterName)).setText(m.name);
            ((TextView) convertView.findViewById(R.id.monsterDetails))
                    .setText(m.element + "  •  " + m.rarity);

            // Rarity colour
            TextView rarityView = convertView.findViewById(R.id.monsterDetails);
            int color;
            switch (m.rarity) {
                case "★★★★ Legendary": color = 0xFFFFD700; break;
                case "★★★ Rare":       color = 0xFFAA44FF; break;
                case "★★ Uncommon":    color = 0xFF44AAFF; break;
                default:               color = 0xFF88CC88; break;
            }
            rarityView.setTextColor(color);
            return convertView;
        }
    }
}
