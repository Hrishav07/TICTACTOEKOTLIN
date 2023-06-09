package com.example.tictactoeapp

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_code.*

var isCodeMaker = true;
var code = "null";
var codeFound = false
var checkTemp = true
var keyValue: String = "null"

@Suppress("DEPRECATION")
class CodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)
        Create.setOnClickListener {
            code = "null";
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = GameCode.text.toString()
            Create.visibility = View.GONE
            Paint.Join.visibility = View.GONE
            GameCode.visibility = View.GONE
            textView4.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            if (code != "null" && code != null && code != "") {

                isCodeMaker = true;
                FirebaseDatabase.getInstance().reference.child("codes")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            var check = isValueAvailable(snapshot, code)

                            Handler().postDelayed({
                                if (check == true) {
                                    Create.visibility = View.VISIBLE
                                    Paint.Join.visibility = View.VISIBLE
                                    GameCode.visibility = View.VISIBLE
                                    textView4.visibility = View.VISIBLE
                                    progressBar.visibility = View.GONE

                                } else {
                                    FirebaseDatabase.getInstance().reference.child("codes").push()
                                        .setValue(code)
                                    isValueAvailable(snapshot, code)
                                    checkTemp = false
                                    Handler().postDelayed({
                                        accepted()
                                        errorMsg("Please don't go back")
                                    }, 300)

                                }
                            }, 2000)


                        }

                    })
            } else {
                Create.visibility = View.VISIBLE
                Paint.Join.visibility = View.VISIBLE
                GameCode.visibility = View.VISIBLE
                textView4.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                errorMsg("Enter Code Properly")
            }
        }
        Paint.Join.setOnClickListener {
            code = "null";
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = GameCode.text.toString()
            if (code != "null" && code != null && code != "") {
                Create.visibility = View.GONE
                Paint.Join.visibility = View.GONE
                GameCode.visibility = View.GONE
                textView4.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                isCodeMaker = false;
                FirebaseDatabase.getInstance().reference.child("codes")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            var data: Boolean = isValueAvailable(snapshot, code)

                            Handler().postDelayed({
                                if (data == true) {
                                    codeFound = true
                                    accepted()
                                    Create.visibility = View.VISIBLE
                                    Paint.Join.visibility = View.VISIBLE
                                    GameCode.visibility = View.VISIBLE
                                    textView4.visibility = View.VISIBLE
                                    progressBar.visibility = View.GONE
                                } else {
                                    Create.visibility = View.VISIBLE
                                    Paint.Join.visibility = View.VISIBLE
                                    GameCode.visibility = View.VISIBLE
                                    textView4.visibility = View.VISIBLE
                                    progressBar.visibility = View.GONE
                                    errorMsg("Invalid Code")
                                }
                            }, 2000)


                        }


                    })

            } else {
                errorMsg("Enter Code Properly")
            }
        }

    }

    fun accepted() {
        startActivity(Intent(this, ThirdPage::class.java));
        Create.visibility = View.VISIBLE
        Paint.Join.visibility = View.VISIBLE
        GameCode.visibility = View.VISIBLE
        textView4.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

    }

    fun errorMsg(value: String) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
    }

    fun isValueAvailable(snapshot: DataSnapshot, code: String): Boolean {
        var data = snapshot.children
        data.forEach {
            var value = it.getValue().toString()
            if (value == code) {
                keyValue = it.key.toString()
                return true;
            }
        }
        return false
    }
}
