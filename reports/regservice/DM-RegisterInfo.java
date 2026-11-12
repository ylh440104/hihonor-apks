package cn.richinfo.dm.bean;

import android.content.Context;
import cn.richinfo.dm.DMConfig;
import cn.richinfo.dm.constance.PrivateConstance;
import cn.richinfo.dm.util.BusinessUtils;
import cn.richinfo.dm.util.DMDeviceInfoCollection;
import cn.richinfo.dm.util.LogUtils;
import cn.richinfo.dm.util.VerifyUtil;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class RegisterInfo {
    private static final String TAG = "自注册";
    private String bluetoothMac;
    private String brand;
    private String carrierAggregation;
    private String cellId;
    private String cellId2;
    private String channel;
    private String cpu;
    private String currentRam;
    private String currentRom;
    private String dataCard;
    private String dataCard2;
    private String firmwareVer;
    private String foreignRoaming;
    private String hardware700M;
    private String hardwareSA;
    private String iccid1;
    private String iccid2;
    private String imei1;
    private String imei2;
    private String imsi1;
    private String imsi2;
    private String inType;
    private String lac;
    private String lac2;
    private String mac;
    private String masterStatus;
    private String model;
    private String netCard;
    private String networkMode5g1;
    private String networkMode5g2;
    private String phoneNumber1;
    private String phoneNumber2;
    private String r165G1;
    private String r165G2;
    private String ram;
    private String rom;
    private String sdkVersion;
    private String sendTime;
    private String smsSwitch5g1;
    private String smsSwitch5g2;
    private String software700M;
    private String softwareSA;
    private String soltNetwork1;
    private String soltNetwork2;
    private String soltQuantity;
    private String status5g1;
    private String status5g2;
    private String systemVer;
    private String tempValue1;
    private String tempValue2;
    private String type;
    private String verify;
    private String voNRPlus;
    private String voiceCard;
    private String vonrSwitch1;
    private String vonrSwitch2;

    private static boolean cfgCheck(String str, JSONObject jSONObject) {
        return BusinessUtils.ifCollect(jSONObject, str);
    }

    public static String getMd5String(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(DMConfig.SDK_VERSION).append(DMDeviceInfoCollection.imei1(context)).append("|").append(DMDeviceInfoCollection.imei2(context)).append("|").append(DMDeviceInfoCollection.getBrand(context)).append("|").append(DMDeviceInfoCollection.getModel(context)).append("|").append(DMDeviceInfoCollection.firmwareVer(context)).append("|").append(DMDeviceInfoCollection.systemVer(context)).append("|").append(DMDeviceInfoCollection.type(context)).append("|").append(DMDeviceInfoCollection.iccid1(context)).append("|").append(DMDeviceInfoCollection.iccid2(context)).append("|").append(DMDeviceInfoCollection.imsi1(context)).append("|").append(DMDeviceInfoCollection.imsi2(context)).append("|").append(DMDeviceInfoCollection.masterStatus(context));
        return stringBuffer.toString();
    }

    public JSONObject asJsonObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("sdkVersion", this.sdkVersion);
            jSONObject.put("imei1", this.imei1);
            jSONObject.put("imei2", this.imei2);
            jSONObject.put("brand", this.brand);
            jSONObject.put("model", this.model);
            jSONObject.put("firmwareVer", this.firmwareVer);
            jSONObject.put("systemVer", this.systemVer);
            jSONObject.put("cpu", this.cpu);
            jSONObject.put("rom", this.rom);
            jSONObject.put("ram", this.ram);
            jSONObject.put("type", this.type);
            jSONObject.put("iccid1", this.iccid1);
            jSONObject.put("iccid2", this.iccid2);
            jSONObject.put("imsi1", this.imsi1);
            jSONObject.put("imsi2", this.imsi2);
            jSONObject.put("mac", this.mac);
            jSONObject.put("cellId", this.cellId);
            jSONObject.put("lac", this.lac);
            jSONObject.put("channel", this.channel);
            jSONObject.put("dataCard", this.dataCard);
            jSONObject.put("masterStatus", this.masterStatus);
            jSONObject.put("sendTime", this.sendTime);
            jSONObject.put("soltQuantity", this.soltQuantity);
            jSONObject.put("dataCard2", this.dataCard2);
            jSONObject.put("soltNetwork1", this.soltNetwork1);
            jSONObject.put("soltNetwork2", this.soltNetwork2);
            jSONObject.put("cellId2", this.cellId2);
            jSONObject.put("lac2", this.lac2);
            jSONObject.put("inType", this.inType);
            jSONObject.put("verify", this.verify);
            jSONObject.put("phoneNumber1", this.phoneNumber1);
            jSONObject.put("phoneNumber2", this.phoneNumber2);
            jSONObject.put("status5g1", this.status5g1);
            jSONObject.put("status5g2", this.status5g2);
            jSONObject.put("currentRam", this.currentRam);
            jSONObject.put("currentRom", this.currentRom);
            jSONObject.put("networkMode5g1", this.networkMode5g1);
            jSONObject.put("networkMode5g2", this.networkMode5g2);
            jSONObject.put("smsSwitch5g1", this.smsSwitch5g1);
            jSONObject.put("smsSwitch5g2", this.smsSwitch5g2);
            jSONObject.put("vonrSwitch1", this.vonrSwitch1);
            jSONObject.put("vonrSwitch2", this.vonrSwitch2);
            jSONObject.put("software700M", this.software700M);
            jSONObject.put("hardware700M", this.hardware700M);
            jSONObject.put("softwareSA", this.softwareSA);
            jSONObject.put("hardwareSA", this.hardwareSA);
            jSONObject.put("netCard", this.netCard);
            jSONObject.put("voiceCard", this.voiceCard);
            jSONObject.put("tempValue1", this.tempValue1);
            jSONObject.put("tempValue2", this.tempValue2);
            jSONObject.put("carrierAggregation", this.carrierAggregation);
            jSONObject.put("foreignRoaming", this.foreignRoaming);
            jSONObject.put("voNRPlus", this.voNRPlus);
            jSONObject.put("R16_5G1", this.r165G1);
            jSONObject.put("R16_5G2", this.r165G2);
            jSONObject.put("bluetoothMac", this.bluetoothMac);
        } catch (Exception unused) {
            LogUtils.err("自注册", "上报字段拼接异常");
        }
        return jSONObject;
    }

    public void getConfigRegisterMsg(Context context, JSONObject jSONObject) {
        this.sdkVersion = DMConfig.SDK_VERSION;
        this.imei1 = DMDeviceInfoCollection.imei1(context);
        this.imei2 = DMDeviceInfoCollection.imei2(context);
        this.sendTime = System.currentTimeMillis() + "";
        boolean cfgCheck = cfgCheck("brand", jSONObject);
        String str = PrivateConstance.DM_UNCOLLECT;
        this.brand = cfgCheck ? DMDeviceInfoCollection.getBrand(context) : PrivateConstance.DM_UNCOLLECT;
        this.model = cfgCheck("model", jSONObject) ? DMDeviceInfoCollection.getModel(context) : PrivateConstance.DM_UNCOLLECT;
        this.firmwareVer = cfgCheck("firmwareVer", jSONObject) ? DMDeviceInfoCollection.firmwareVer(context) : PrivateConstance.DM_UNCOLLECT;
        this.systemVer = cfgCheck("systemVer", jSONObject) ? DMDeviceInfoCollection.systemVer(context) : PrivateConstance.DM_UNCOLLECT;
        this.cpu = cfgCheck("cpu", jSONObject) ? DMDeviceInfoCollection.cpu(context) : PrivateConstance.DM_UNCOLLECT;
        this.rom = cfgCheck("rom", jSONObject) ? DMDeviceInfoCollection.rom(context) : PrivateConstance.DM_UNCOLLECT;
        this.ram = cfgCheck("ram", jSONObject) ? DMDeviceInfoCollection.ram(context) : PrivateConstance.DM_UNCOLLECT;
        this.type = cfgCheck("type", jSONObject) ? DMDeviceInfoCollection.type(context) : PrivateConstance.DM_UNCOLLECT;
        this.iccid1 = cfgCheck("iccid1", jSONObject) ? DMDeviceInfoCollection.iccid1(context) : PrivateConstance.DM_UNCOLLECT;
        this.imsi2 = cfgCheck("imsi2", jSONObject) ? DMDeviceInfoCollection.imsi2(context) : PrivateConstance.DM_UNCOLLECT;
        this.lac2 = cfgCheck("lac2", jSONObject) ? DMDeviceInfoCollection.lac2(context) : PrivateConstance.DM_UNCOLLECT;
        this.cellId2 = cfgCheck("cellId2", jSONObject) ? DMDeviceInfoCollection.cellId2(context) : PrivateConstance.DM_UNCOLLECT;
        this.iccid2 = cfgCheck("iccid2", jSONObject) ? DMDeviceInfoCollection.iccid2(context) : PrivateConstance.DM_UNCOLLECT;
        this.dataCard2 = cfgCheck("dataCard2", jSONObject) ? DMDeviceInfoCollection.dataCard2(context) : PrivateConstance.DM_UNCOLLECT;
        this.soltNetwork2 = cfgCheck("soltNetwork2", jSONObject) ? DMDeviceInfoCollection.soltNetwork2(context) : PrivateConstance.DM_UNCOLLECT;
        this.phoneNumber2 = cfgCheck("phoneNumber2", jSONObject) ? DMDeviceInfoCollection.phoneNumber2(context) : PrivateConstance.DM_UNCOLLECT;
        this.imsi1 = cfgCheck("imsi1", jSONObject) ? DMDeviceInfoCollection.imsi1(context) : PrivateConstance.DM_UNCOLLECT;
        this.mac = cfgCheck("mac", jSONObject) ? DMDeviceInfoCollection.macAddress(context) : PrivateConstance.DM_UNCOLLECT;
        this.cellId = cfgCheck("cellId", jSONObject) ? DMDeviceInfoCollection.cellId1(context) : PrivateConstance.DM_UNCOLLECT;
        this.lac = cfgCheck("lac", jSONObject) ? DMDeviceInfoCollection.lac1(context) : PrivateConstance.DM_UNCOLLECT;
        this.channel = cfgCheck("channel", jSONObject) ? DMDeviceInfoCollection.getMobileNetType(context) : PrivateConstance.DM_UNCOLLECT;
        this.dataCard = cfgCheck("dataCard", jSONObject) ? DMDeviceInfoCollection.dataCard(context) : PrivateConstance.DM_UNCOLLECT;
        this.masterStatus = cfgCheck("masterStatus", jSONObject) ? DMDeviceInfoCollection.masterStatus(context) : PrivateConstance.DM_UNCOLLECT;
        this.soltQuantity = cfgCheck("soltQuantity", jSONObject) ? DMDeviceInfoCollection.soltQuantity(context) : PrivateConstance.DM_UNCOLLECT;
        this.soltNetwork1 = cfgCheck("soltNetwork1", jSONObject) ? DMDeviceInfoCollection.soltNetwork1(context) : PrivateConstance.DM_UNCOLLECT;
        this.inType = cfgCheck("inType", jSONObject) ? DMDeviceInfoCollection.inType(context) : PrivateConstance.DM_UNCOLLECT;
        this.verify = cfgCheck("verify", jSONObject) ? VerifyUtil.getVerifyInfo(context) : PrivateConstance.DM_UNCOLLECT;
        this.phoneNumber1 = cfgCheck("phoneNumber1", jSONObject) ? DMDeviceInfoCollection.phoneNumber1(context) : PrivateConstance.DM_UNCOLLECT;
        this.status5g1 = cfgCheck("status5g1", jSONObject) ? DMDeviceInfoCollection.get5GState1(context) : PrivateConstance.DM_UNCOLLECT;
        this.status5g2 = cfgCheck("status5g2", jSONObject) ? DMDeviceInfoCollection.get5GState2(context) : PrivateConstance.DM_UNCOLLECT;
        this.currentRam = cfgCheck("currentRam", jSONObject) ? DMDeviceInfoCollection.getCurrentRam(context) : PrivateConstance.DM_UNCOLLECT;
        this.currentRom = cfgCheck("currentRom", jSONObject) ? DMDeviceInfoCollection.getCurrentRom(context) : PrivateConstance.DM_UNCOLLECT;
        this.netCard = cfgCheck("netCard", jSONObject) ? DMDeviceInfoCollection.netCard(context) : PrivateConstance.DM_UNCOLLECT;
        this.voiceCard = cfgCheck("voiceCard", jSONObject) ? DMDeviceInfoCollection.voiceCard(context) : PrivateConstance.DM_UNCOLLECT;
        this.software700M = cfgCheck("software700M", jSONObject) ? DMDeviceInfoCollection.software700M(context) : PrivateConstance.DM_UNCOLLECT;
        this.hardware700M = cfgCheck("hardware700M", jSONObject) ? DMDeviceInfoCollection.hardware700M(context) : PrivateConstance.DM_UNCOLLECT;
        this.softwareSA = cfgCheck("softwareSA", jSONObject) ? DMDeviceInfoCollection.softwareSA(context) : PrivateConstance.DM_UNCOLLECT;
        this.hardwareSA = cfgCheck("hardwareSA", jSONObject) ? DMDeviceInfoCollection.hardwareSA(context) : PrivateConstance.DM_UNCOLLECT;
        this.smsSwitch5g1 = cfgCheck("smsSwitch5g1", jSONObject) ? DMDeviceInfoCollection.smsSwitch5g1(context) : PrivateConstance.DM_UNCOLLECT;
        this.smsSwitch5g2 = cfgCheck("smsSwitch5g2", jSONObject) ? DMDeviceInfoCollection.smsSwitch5g2(context) : PrivateConstance.DM_UNCOLLECT;
        this.vonrSwitch1 = cfgCheck("vonrSwitch1", jSONObject) ? DMDeviceInfoCollection.vonrSwitch1(context) : PrivateConstance.DM_UNCOLLECT;
        this.vonrSwitch2 = cfgCheck("vonrSwitch2", jSONObject) ? DMDeviceInfoCollection.vonrSwitch2(context) : PrivateConstance.DM_UNCOLLECT;
        this.networkMode5g1 = cfgCheck("networkMode5g1", jSONObject) ? DMDeviceInfoCollection.get5gNetworkMode1(context) : PrivateConstance.DM_UNCOLLECT;
        this.networkMode5g2 = cfgCheck("networkMode5g2", jSONObject) ? DMDeviceInfoCollection.get5gNetworkMode2(context) : PrivateConstance.DM_UNCOLLECT;
        this.tempValue1 = cfgCheck("tempValue1", jSONObject) ? DMDeviceInfoCollection.tempValue1(context) : PrivateConstance.DM_UNCOLLECT;
        this.tempValue2 = cfgCheck("tempValue2", jSONObject) ? DMDeviceInfoCollection.tempValue2(context) : PrivateConstance.DM_UNCOLLECT;
        this.carrierAggregation = cfgCheck("carrierAggregation", jSONObject) ? DMDeviceInfoCollection.supportCarrierAggregation(context) : PrivateConstance.DM_UNCOLLECT;
        this.foreignRoaming = cfgCheck("foreignRoaming", jSONObject) ? DMDeviceInfoCollection.supportForeignRoaming(context) : PrivateConstance.DM_UNCOLLECT;
        this.voNRPlus = cfgCheck("voNRPlus", jSONObject) ? DMDeviceInfoCollection.supportVoNRPlus(context) : PrivateConstance.DM_UNCOLLECT;
        this.r165G1 = cfgCheck("R16_5G1", jSONObject) ? DMDeviceInfoCollection.supportR165G1(context) : PrivateConstance.DM_UNCOLLECT;
        this.r165G2 = cfgCheck("R16_5G2", jSONObject) ? DMDeviceInfoCollection.supportR165G2(context) : PrivateConstance.DM_UNCOLLECT;
        if (cfgCheck("bluetoothMac", jSONObject)) {
            str = DMDeviceInfoCollection.bluetoothMac(context);
        }
        this.bluetoothMac = str;
    }

    public String toString() {
        return "{\"sdkVersion\":'" + this.sdkVersion + "', \"imei1\":'" + this.imei1 + "', \"imei2\":'" + this.imei2 + "', \"brand\":'" + this.brand + "', \"model\":'" + this.model + "', \"firmwareVer\":'" + this.firmwareVer + "', \"systemVer\":'" + this.systemVer + "', \"cpu\":'" + this.cpu + "', \"rom\":'" + this.rom + "', \"ram\":'" + this.ram + "', \"type\":'" + this.type + "', \"iccid1\":'" + this.iccid1 + "', \"iccid2\":'" + this.iccid2 + "', \"imsi1\":'" + this.imsi1 + "', \"imsi2\":'" + this.imsi2 + "', \"mac\":'" + this.mac + "', \"cellId\":'" + this.cellId + "', \"lac\":'" + this.lac + "', \"channel\":'" + this.channel + "', \"dataCard\":'" + this.dataCard + "', \"masterStatus\":'" + this.masterStatus + "', \"sendTime\":'" + this.sendTime + "', \"soltQuantity\":'" + this.soltQuantity + "', \"dataCard2\":'" + this.dataCard2 + "', \"soltNetwork1\":'" + this.soltNetwork1 + "', \"soltNetwork2\":'" + this.soltNetwork2 + "', \"cellId2\":'" + this.cellId2 + "', \"lac2\":'" + this.lac2 + "', \"inType\":'" + this.inType + "', \"phoneNumber1\":'" + this.phoneNumber1 + "', \"phoneNumber2\":'" + this.phoneNumber2 + "', \"status5g1\":'" + this.status5g1 + "', \"status5g2\":'" + this.status5g2 + "', \"currentRam\":'" + this.currentRam + "', \"currentRom\":'" + this.currentRom + "', \"networkMode5g1\":'" + this.networkMode5g1 + "', \"networkMode5g2\":'" + this.networkMode5g2 + "', \"smsSwitch5g1\":'" + this.smsSwitch5g1 + "', \"smsSwitch5g2\":'" + this.smsSwitch5g2 + "', \"vonrSwitch1\":'" + this.vonrSwitch1 + "', \"vonrSwitch2\":'" + this.vonrSwitch2 + "', \"software700M\":'" + this.software700M + "', \"hardware700M\":'" + this.hardware700M + "', \"softwareSA\":'" + this.softwareSA + "', \"hardwareSA\":'" + this.hardwareSA + "', \"netCard\":'" + this.netCard + "', \"voiceCard\":'" + this.voiceCard + "', \"tempValue1\":'" + this.tempValue1 + "', \"tempValue2\":'" + this.tempValue2 + "', \"carrierAggregation\":'" + this.carrierAggregation + "', \"foreignRoaming\":'" + this.foreignRoaming + "', \"voNRPlus\":'" + this.voNRPlus + "', \"r165G1\":'" + this.r165G1 + "', \"r165G2\":'" + this.r165G2 + "', \"bluetoothMac\":'" + this.bluetoothMac + "'}";
    }
}
