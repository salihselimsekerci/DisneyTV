package com.salihselimsekerci.disneyfilmkotlin.ui.main.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.salihselimsekerci.disneyfilmkotlin.R
import com.salihselimsekerci.disneyfilmkotlin.util.Singleton

class MainActivity : AppCompatActivity() {
    //Çıkış için kullanacağım alertdialog nesnemi lateinit ettim
    private lateinit var mAlert: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //İlgili actionbarın textcolorunu deiştirdim burada
        supportActionBar?.let {
            it.setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"))
        }
    }

    //Çıkış dialogu için yazdığım fonksiyon
    private fun showExitDialog(){
        mAlert = AlertDialog.Builder(this, R.style.AlertDialogCustom)
        mAlert.setIcon(R.mipmap.ic_disney_logo)
        mAlert.setTitle(getString(R.string.app_name))
        mAlert.setMessage(resources.getString(R.string.mainAlertMessage))
        mAlert.setPositiveButton(resources.getString(R.string.mainAlertPositive)
        ) { dialog, which ->
            dialog.dismiss()
        }
        mAlert.setNegativeButton(resources.getString(R.string.mainAlertNegative)
        ) { dialog, which ->
            dialog.dismiss()
            //bunun altındaki 3 satır kod uygulamadan kökten çıkmaya yarıyor
            moveTaskToBack(true)
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        }

        mAlert.show()
    }

    //Geriye basıldığında olacak işlemleri burada belirledim
    override fun onBackPressed() {
        if (Singleton.mainScreenState)
            showExitDialog()
        else
            super.onBackPressed()
    }
}