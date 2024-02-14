package com.example.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.testandroidapp.IMyAidlInterface
import java.util.*

class MainActivity : AppCompatActivity() {
    var iMyAidlInterface:IMyAidlInterface?=null
    private var serviceConnection: ServiceConnection =object :ServiceConnection{
        override fun onServiceConnected(p0: ComponentName, iBinder: IBinder) {
            iMyAidlInterface= IMyAidlInterface.Stub.asInterface(iBinder)

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            iMyAidlInterface=null
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent=Intent("MyAIDLService")
        intent.setPackage("com.example.testandroidapp")
        val explicitIntent = convertImplicitIntentToExplicitIntent(this,intent)
        if(explicitIntent!=null){
            bindService(explicitIntent,serviceConnection, BIND_AUTO_CREATE)
        }
        val button: Button =findViewById(R.id.button)
        button.setOnClickListener {
            try {
                val random = Random()
                val color1 =
                    Color.argb(255, random.nextInt(256), random.nextInt(257), random.nextInt(258))
                Log.i("TAG", "getColor$color1")
                val color=iMyAidlInterface!!.color
                it.setBackgroundColor(color)
            }catch (e:java.lang.Exception){
                e.printStackTrace()
            }
        }

    }
    fun convertImplicitIntentToExplicitIntent(ct: Context, implicitIntent: Intent): Intent? {
        val pm = ct.packageManager
        val resolveInfoList = pm.queryIntentServices(implicitIntent, 0)
        if (resolveInfoList == null || resolveInfoList.size != 1) {
            return null
        }
        val serviceInfo = resolveInfoList[0]
        val component = ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name)
        val explicitIntent = Intent(implicitIntent)
        explicitIntent.component = component
        return explicitIntent
    }


    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()

    }

}