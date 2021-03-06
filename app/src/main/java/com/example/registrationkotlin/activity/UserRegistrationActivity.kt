package com.example.registrationkotlin.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.registrationkotlin.MainActivity
import com.example.registrationkotlin.R
import com.example.registrationkotlin.adapter.RegisterUserAdapter
import com.example.registrationkotlin.database.AppDatabase
import com.example.registrationkotlin.database.model.RegisterUsers
import com.example.registrationkotlin.delegate.ItemListDelegate
import kotlinx.android.synthetic.main.activity_user_registration.*

class UserRegistrationActivity : AppCompatActivity(),   ItemListDelegate {
    override fun sendID(userID: String) {
        var intent= Intent(this,AddNewUserActivity::class.java)
        intent.putExtra("SelectUserID",userID)
        intent.putExtra("type",type)
        startActivity(intent)
        finish()    }

    var type=""
    var registerList=ArrayList<RegisterUsers>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var mUserID:String?=null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)
        supportActionBar?.title="User Registration"

        val sharedPreferences=getSharedPreferences("userID", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("userID")){
            mUserID=sharedPreferences.getString("userID",null)

        }

        val database= AppDatabase.getDatabase(this)

        type = if (rdo_teacher.isChecked){
            "Teacher"
        } else{
            "Student"
        }
        linearLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        user_registration_recycler.layoutManager = linearLayoutManager

        btn_add_new_user.setOnClickListener {

            var intent= Intent(this,AddNewUserActivity::class.java)
            intent.putExtra("type",type)
            intent.putExtra("SelectUserID","")
            startActivity(intent)
            finish()
        }
        rdo_group.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                type= "${radio.text}"
                if (mUserID!=null){
                    registerList= database.getRegisterUserDao().getAllUsersByType(type, mUserID!!) as ArrayList<RegisterUsers>
                }
                val adapter = RegisterUserAdapter(registerList,this)
                user_registration_recycler.adapter=adapter

            })

    }


    override fun onBackPressed() {

            var intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
    }
}