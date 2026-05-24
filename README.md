# 🧭 PokéFi — WiFi Monster Catcher

Turn every WiFi network around you into a collectable monster!

## How to Build (No Android Studio needed)

### Step 1 — Create a GitHub repo
1. Go to [github.com](https://github.com) → **New repository**
2. Name it `pokefi`, set it to **Public** (free Actions minutes), click **Create repository**

### Step 2 — Upload all these files
Upload the entire contents of this folder into your new repo (drag-and-drop works in the GitHub web UI). Make sure the folder structure matches exactly.

### Step 3 — Get your APK
1. Click the **Actions** tab in your repo
2. Click **Build PokéFi APK** in the left sidebar
3. Click **Run workflow** → **Run workflow** (green button)
4. Wait ~3-4 minutes for the build to finish ✅
5. Click on the completed workflow run → scroll down to **Artifacts** → download **PokeFi-debug**
6. Unzip it — you'll find `app-debug.apk`

### Step 4 — Install on your phone
1. Enable **Install unknown apps** in your phone settings (Settings → Security → Install unknown apps)
2. Transfer the APK to your phone (email it to yourself, AirDrop, Google Drive, USB — anything works)
3. Tap the APK file on your phone to install

---

## How It Works

| WiFi Property | What it affects |
|---|---|
| SSID keywords (fire, sky, coffee…) | Monster element type |
| Signal strength (dBm) | Rarity tier |
| Security (WPA2 / WPA3 / Open) | Rarity bonus |
| Frequency (2.4GHz vs 5GHz) | Element fallback |

### Rarity tiers
- ⭐ Common — weak signal, open network
- ⭐⭐ Uncommon — medium signal or WPA2
- ⭐⭐⭐ Rare — strong signal + security
- ⭐⭐⭐⭐ Legendary — WPA3 + strong signal + special name

### Elements: 🔥 Fire · ❄️ Ice · ⚡ Thunder · 💧 Water · 🌿 Nature · 🌑 Shadow · ✨ Cosmic · 🌬️ Wind · 🌍 Earth · ⚙️ Steel · 🔮 Psychic

---

## Permissions
- `ACCESS_FINE_LOCATION` — required by Android to read WiFi scan results (no location data is stored or sent anywhere)
- `ACCESS_WIFI_STATE` — to read scan results
- No internet permission — fully offline!

> **Note:** On Android 9+, the app uses cached scan results. Android limits apps to 4 scans per 2 minutes to save battery, so results are usually instant from the system cache.
