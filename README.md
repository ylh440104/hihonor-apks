# hihonor-apks

Decompile workbench for a set of HiView / Honor system APKs dumped from an Honor device (Android 14 / MagicOS, security patch 2026-05). Study of preinstalled components and their behavior.

## apks/

- `apks/AutoRegSms_15.1.0.100.apk`
- `apks/com.hihonor.hiviewpush.common.HiViewPushApplication_15.0.0.034.apk`
- `apks/hiview_15.0.0.036.apk`
- `apks/SystemMessage_15.0.0.011.apk`
- `apks/HiViewTunnel_15.0.0.034.apk`

## decompile pipeline

`.github/workflows/decompile.yml` runs on push and can also be started manually via workflow_dispatch:

1. jadx - full Java decompile (`out/jadx`)
2. raw unzip - AndroidManifest.xml (binary), resources.arsc, assets/, lib/ (`out/unpacked`)
3. apktool - smali + decoded resources (`out/apktool`)

All outputs are packed into a single artifact: `decompile-out`.
