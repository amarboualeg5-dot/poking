package com.pokefi;

import android.net.wifi.ScanResult;

public class Monster {

    public String name;
    public String rarity;
    public String element;
    public String emoji;

    // ── Public factory ─────────────────────────────────────────────────────────

    public static Monster fromScanResult(ScanResult r) {
        String ssid      = (r.SSID == null || r.SSID.isEmpty()) ? "Hidden Network" : r.SSID;
        int    signal    = r.level;                        // dBm, negative
        String caps      = r.capabilities == null ? "" : r.capabilities.toUpperCase();
        int    freq      = r.frequency;                    // MHz
        return build(ssid, signal, caps, freq);
    }

    public static Monster fromFake(String ssid, int signal, String security, int freq) {
        return build(ssid, signal, security.toUpperCase(), freq);
    }

    // ── Core logic ─────────────────────────────────────────────────────────────

    private static Monster build(String ssid, int signal, String caps, int freq) {
        Monster m = new Monster();
        m.element = pickElement(ssid, caps, freq);
        m.rarity  = pickRarity(signal, caps, ssid);
        m.name    = buildName(ssid, m.element, m.rarity, caps);
        m.emoji   = emojiFor(m.element);
        return m;
    }

    private static String pickElement(String ssid, String caps, int freq) {
        String s = ssid.toLowerCase();
        // SSID keyword checks first
        if (s.contains("sky") || s.contains("star") || s.contains("galaxy")) return "Cosmic";
        if (s.contains("fire") || s.contains("hot") || s.contains("inferno")) return "Fire";
        if (s.contains("ice") || s.contains("frost") || s.contains("snow"))   return "Ice";
        if (s.contains("dark") || s.contains("shadow") || s.contains("void")) return "Shadow";
        if (s.contains("bt-") || s.contains("bt_") || s.contains("btHub"))    return "Thunder";
        if (s.contains("cafe") || s.contains("coffee") || s.contains("bucks"))return "Nature";
        if (s.contains("direct") || s.contains("printer") || s.contains("hp"))return "Steel";
        if (s.contains("guest") || s.contains("public") || s.contains("free"))return "Wind";
        if (s.contains("home") || s.contains("house") || s.contains("family"))return "Earth";
        // Security / frequency fallback
        if (caps.contains("WPA3"))             return "Cosmic";
        if (caps.contains("WPA2"))             return "Thunder";
        if (caps.isEmpty() || caps.equals("")) return "Wind";   // open network
        if (freq >= 5000)                      return "Psychic";
        return "Water";
    }

    private static String pickRarity(int signal, String caps, String ssid) {
        int score = 0;
        // Strong signal → higher score
        if (signal >= -50) score += 3;
        else if (signal >= -65) score += 2;
        else if (signal >= -75) score += 1;
        // Security
        if (caps.contains("WPA3")) score += 3;
        else if (caps.contains("WPA2")) score += 1;
        // Hidden/special names
        if (ssid.equals("Hidden Network")) score += 2;
        if (ssid.length() > 20) score += 1;

        if (score >= 6) return "★★★★ Legendary";
        if (score >= 4) return "★★★ Rare";
        if (score >= 2) return "★★ Uncommon";
        return "★ Common";
    }

    private static String buildName(String ssid, String element, String rarity, String caps) {
        // Derive a creature syllable from the SSID
        String seed = ssid.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        if (seed.isEmpty()) seed = "phantom";

        // Pick a base syllable (first 3 chars, padded)
        seed = (seed + "zzz").substring(0, 3);
        char c0 = seed.charAt(0), c1 = seed.charAt(1), c2 = seed.charAt(2);

        // Map vowel-ness to suffix pool
        String[] suffixes = {"ix", "eon", "ara", "ux", "yn", "os", "ith", "ax", "ora", "ys"};
        int idx = ((c0 + c1 + c2) & 0xFF) % suffixes.length;
        String creatureSyllable = capitalise("" + c0 + c1 + c2) + suffixes[idx];

        // Prefix from element
        String prefix = elementPrefix(element);

        // Legendary gets "Mega Ancient" prefix
        String legendary = rarity.startsWith("★★★★") ? "Mega Ancient " : "";

        return legendary + prefix + creatureSyllable;
    }

    private static String elementPrefix(String element) {
        switch (element) {
            case "Fire":    return "Igni";
            case "Ice":     return "Cryo";
            case "Thunder": return "Volt";
            case "Water":   return "Aqua";
            case "Nature":  return "Sylva";
            case "Shadow":  return "Umbra";
            case "Cosmic":  return "Astro";
            case "Wind":    return "Zeph";
            case "Earth":   return "Terra";
            case "Steel":   return "Ferro";
            case "Psychic": return "Psi";
            default:        return "Lumi";
        }
    }

    private static String emojiFor(String element) {
        switch (element) {
            case "Fire":    return "🔥";
            case "Ice":     return "❄️";
            case "Thunder": return "⚡";
            case "Water":   return "💧";
            case "Nature":  return "🌿";
            case "Shadow":  return "🌑";
            case "Cosmic":  return "✨";
            case "Wind":    return "🌬️";
            case "Earth":   return "🌍";
            case "Steel":   return "⚙️";
            case "Psychic": return "🔮";
            default:        return "💫";
        }
    }

    private static String capitalise(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
