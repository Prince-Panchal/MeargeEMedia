package com.meargedemo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.meargedemo.common.CommonMethods
import com.meargedemo.databinding.ActivityMainBinding
import com.meargedemo.factory.Factory
import com.meargedemo.permissions.Permissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var factory: Factory
    private lateinit var vm: MainActivityVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        factory = Factory(this)
        vm = ViewModelProvider(this, factory).get(MainActivityVM::class.java)
        binding.vm = vm
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            2 -> {
                if (resultCode == Activity.RESULT_OK) {
                    vm.pathOne.set(CommonMethods.getPath(data!!.data!!, this))
                    tvPathOne.text = "Recorded Path : ${CommonMethods.getPath(data.data!!, this)}"
                }
            }

            6 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val path = CommonMethods.getPath(data!!.data!!, this)
                    vm.pathTwo.set(path)
                    tvPathTwo.text =  "Gallery Path : ${CommonMethods.getPath(data.data!!, this)}"
                }
            }
        }
    }
}