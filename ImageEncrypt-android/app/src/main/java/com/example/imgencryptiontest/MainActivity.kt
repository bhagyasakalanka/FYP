package com.example.imgencryptiontest

import android.Manifest
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var Dir : File

    companion object {
        private val FILE_NAME_ENC = "img"
        private val FILE_NAME_DEC = "img.png"

        private  val key = "z3K8ptUsyCLq1ENu"
        private val specString = "ClEkfAH1i31eU7Ba"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        Dexter.withActivity(this)
            .withPermissions(*arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ))
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    enc_button.isEnabled = true
                    dec_button.isEnabled = true
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    Toast.makeText(this@MainActivity,"Please give the permission", Toast.LENGTH_SHORT).show()
                }

            }).check()

        val root = Environment.getExternalStorageDirectory().toString()
        Dir = File("$root/save_images")
        if (!Dir.exists()){
            Dir.mkdirs()
        }

        enc_button.setOnClickListener {
            val drawable = ContextCompat.getDrawable(this,R.drawable.img)
            val bitmapDrawable = drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
            val  input = ByteArrayInputStream(stream.toByteArray())

            val outputFileEnc = File(Dir, FILE_NAME_ENC)

            try {
                Encrypter.encryptToFile(key, specString,input,FileOutputStream(outputFileEnc))

                Toast.makeText(this@MainActivity, "Encrypted", Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        

    }
}
