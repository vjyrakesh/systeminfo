package com.rkasibhatla.systeminfo

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.BatteryManager
import androidx.core.content.ContextCompat.getSystemService


object SystemInfo {


    private val systemInfoList: ArrayList<ListItem> = ArrayList()
    private val sensorTypeMap: HashMap<Int, String> = HashMap()

    init {
        sensorTypeMap.put(1, "Accelerometer")
        sensorTypeMap.put(35, "Accelerometer Uncalibrated")
        sensorTypeMap.put(2, "Magnetometer")
        sensorTypeMap.put(13, "Ambient Temperature")
        sensorTypeMap.put(9, "Gravity")
        sensorTypeMap.put(4, "Gyroscope")
        sensorTypeMap.put(31, "Heart Beat")
        sensorTypeMap.put(21, "Heart Rate monitor")
        sensorTypeMap.put(5, "Ambient Light")
        sensorTypeMap.put(10, "Linear Acceleration")
        sensorTypeMap.put(30, "Motion Detection")
        sensorTypeMap.put(6, "Pressure")
        sensorTypeMap.put(8, "Proximity")
        sensorTypeMap.put(17, "Step Counter")
        sensorTypeMap.put(18, "Step Counter")
        sensorTypeMap.put(19, "Step Counter")
        sensorTypeMap.put(13, "Ambient Temperature")
        sensorTypeMap.put(11, "Rotation Vector")
        sensorTypeMap.put(14, "Magnetometer Uncalibrated")
        sensorTypeMap.put(15, "Game Rotation")
        sensorTypeMap.put(16, "Gyroscope Uncalibrated")
        sensorTypeMap.put(27, "Display Rotate")
        sensorTypeMap.put(29, "Stationary Detection")
    }

    fun getSystemInfo(context: Context): List<ListItem> {
        if(systemInfoList.size > 0)
            systemInfoList.clear()
        systemInfoList.addAll(getBuildInfo())
        systemInfoList.addAll(getCpuInfo())
        systemInfoList.addAll(getBatteryInfo(context))
        //systemInfoList.addAll(getSensorInfo(context))
        systemInfoList.addAll(getWifiInfo(context))
        return systemInfoList
    }

    private fun getBuildInfo(): List<ListItem> {
        val buildInfoList: ArrayList<ListItem> = ArrayList()
        buildInfoList.add(HeaderListItem("Build Info"))
        buildInfoList.add(ChildListItem("Brand", android.os.Build.BRAND.capitalize()))
        buildInfoList.add(ChildListItem("Manufacturer", android.os.Build.MANUFACTURER.capitalize()))
        buildInfoList.add(ChildListItem("Model", android.os.Build.MODEL.capitalize()))
        buildInfoList.add(ChildListItem("OS Version", android.os.Build.VERSION.RELEASE))
        return buildInfoList
    }

    private fun getBatteryInfo(context: Context): List<ListItem> {
        val batteryInfoList: ArrayList<ListItem> = ArrayList()
        batteryInfoList.add(HeaderListItem("Battery Info"))
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }

        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL
        batteryInfoList.add(ChildListItem("Charging", isCharging.toString()))

        val batteryPct: Float? = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }
        batteryInfoList.add(ChildListItem("Level", batteryPct?.toInt().toString() + "%"))
        return batteryInfoList
    }

    private fun getCpuInfo(): List<ListItem> {
        val cpuInfoList: ArrayList<ListItem> = ArrayList()
        val cpuInfoHeaderListItem = HeaderListItem("CPU Info")
        cpuInfoList.add(cpuInfoHeaderListItem)

        val cpuVendor = ChildListItem("Vendor", "Qualcomm")
        cpuInfoList.add(cpuVendor)

        return cpuInfoList
    }

    private fun getSensorInfo(context: Context): List<ListItem> {
        val sensorList: ArrayList<ListItem> = ArrayList()
        val sensorNameSet: HashSet<String> = HashSet()
        sensorList.add(HeaderListItem("On-device Sensors"))
        val sensorManager = getSystemService(context, SensorManager::class.java) as SensorManager
        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)
        for(sensor:Sensor in sensors) {
            if(sensorTypeMap.containsKey(sensor.type))
                sensorNameSet.add(sensorTypeMap[sensor.type]!!)
            else {
                if(sensor.name.contains(" Wakeup"))
                    sensorNameSet.add(sensor.name.removeSuffix(" Wakeup"))
                else if(sensor.name.contains(" Non-wakeup"))
                    sensorNameSet.add(sensor.name.removeSuffix(" Non-wakeup"))
                else
                    sensorNameSet.add(sensor.name)
            }
        }
        for(sensorName: String in sensorNameSet)
            sensorList.add(ChildListItem(sensorName, ""))

        return sensorList
    }

    private fun getWifiInfo(context: Context): List<ListItem> {
        val wifiInfoList: ArrayList<ListItem> = ArrayList()
        wifiInfoList.add(HeaderListItem("Wifi Info"))
        val wifiManager = getSystemService(context, WifiManager::class.java) as WifiManager
        wifiInfoList.add(ChildListItem("Enabled", wifiManager.isWifiEnabled.toString()))
        val wifiInfo = wifiManager.connectionInfo
        val freq = wifiInfo.frequency / 1000.0
        wifiInfoList.add(ChildListItem("Frequency", "$freq GHz"))
        wifiInfoList.add(ChildListItem("Link Speed", "${wifiInfo.linkSpeed} ${WifiInfo.LINK_SPEED_UNITS}"))
        wifiInfoList.add(ChildListItem("SSID", "${wifiInfo.bssid}"))
        
        return wifiInfoList
    }
}