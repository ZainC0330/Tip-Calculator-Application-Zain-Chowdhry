package com.zchow.tipper

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.ceil

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var etSplit: EditText
    private lateinit var choose15: Button
    private lateinit var choose20: Button
    private lateinit var choose30: Button
    private lateinit var tvTotalPreSplitLabel: TextView
    private lateinit var tvTotalPreSplitAmount: TextView
    private lateinit var tvSplitNumber: TextView
    private lateinit var tvTheWordTip: TextView
    private lateinit var DollarSignTPP: TextView
    private lateinit var DollarSignBA: TextView
    private lateinit var DollarSignTA: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        etSplit = findViewById(R.id.etSplit)
        choose15 = findViewById(R.id.choose15)
        choose20 = findViewById(R.id.choose20)
        choose30 = findViewById(R.id.choose30)
        tvTotalPreSplitLabel = findViewById(R.id.tvTotalPreSplitLabel)
        tvTotalPreSplitAmount = findViewById(R.id.tvTotalPreSplitAmount)
        tvSplitNumber = findViewById(R.id.tvSplitNumber)
        tvTheWordTip = findViewById(R.id.tvTheWordTip)
        DollarSignTPP = findViewById(R.id.DollarSignTPP)
        DollarSignBA = findViewById(R.id.DollarSignBA)
        DollarSignTA = findViewById(R.id.DollarSignTA)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercentLabel.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                if(etSplit.text.isNullOrEmpty() || etSplit.text.toString().toInt() == 0){
                    tvSplitNumber.text = "1"
                }
                else{
                    tvSplitNumber.text = etSplit.text.toString().toInt().toString()
                }
                computeTipAndTotal()
            }
        })

        etSplit.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                if(etSplit.text.isNullOrEmpty() || etSplit.text.toString().toInt() == 0){
                    tvSplitNumber.text = "1"
                }
                else{
                    tvSplitNumber.text = etSplit.text.toString().toInt().toString()
                }
                computeTipAndTotal()
            }
        })

        choose15.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                seekBarTip.progress = INITIAL_TIP_PERCENT
                tvTipPercentLabel.text = "10%"
                seekBarTip.progress = 10
                if(etSplit.text.isNullOrEmpty() || etSplit.text.toString().toInt() == 0){
                    tvSplitNumber.text = "1"
                }
                else{
                    tvSplitNumber.text = etSplit.text.toString().toInt().toString()
                }
                computeTipAndTotal()
                updateTipDescription(10)

            }
        })

        choose20.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                seekBarTip.progress = INITIAL_TIP_PERCENT
                tvTipPercentLabel.text = "20%"
                seekBarTip.progress = 20
                if(etSplit.text.isNullOrEmpty() || etSplit.text.toString().toInt() == 0){
                    tvSplitNumber.text = "1"
                }
                else{
                    tvSplitNumber.text = etSplit.text.toString().toInt().toString()
                }
                computeTipAndTotal()
                updateTipDescription(20)

            }
        })

        choose30.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                seekBarTip.progress = INITIAL_TIP_PERCENT
                tvTipPercentLabel.text = "30%"
                seekBarTip.progress = 30
                if(etSplit.text.isNullOrEmpty() || etSplit.text.toString().toInt() == 0){
                    tvSplitNumber.text = "1"
                }
                else{
                    tvSplitNumber.text = etSplit.text.toString().toInt().toString()
                }
                computeTipAndTotal()
                updateTipDescription(30)

            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription
        when (tipPercent){
            in 0..9 -> tvTipDescription.setTextColor(Color.parseColor("#FF1100"))
            in 10..14 -> tvTipDescription.setTextColor(Color.parseColor("#FF8800"))
            in 15..19 -> tvTipDescription.setTextColor(Color.parseColor("#d4c200"))
            in 20..24 -> tvTipDescription.setTextColor(Color.parseColor("#4DFF00"))
            else -> tvTipDescription.setTextColor(Color.parseColor("#00D9FF"))
        }
    }

    private fun computeTipAndTotal() {
        if(etBaseAmount.text.isEmpty()){
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            tvTotalPreSplitAmount.text = ""
            DollarSignTPP.text = ""
            DollarSignBA.text = ""
            DollarSignTA.text = ""
            return
        }

        if(etBaseAmount.text.toString().toDouble()>999999999.99){
            etBaseAmount.setText("999999999.99")
        }

        var baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        var tipAmount = baseAmount*tipPercent/100
        var totalAmount = (baseAmount + tipAmount)
        if(etSplit.text.isEmpty()==false && etSplit.text.toString().toInt() != 0){
            totalAmount = totalAmount/etSplit.text.toString().toDouble()

            if(totalAmount < 0.01 && totalAmount > 0){
                totalAmount=0.01
            }
        }
        else {
            totalAmount = totalAmount
        }

        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
        tvTotalPreSplitAmount.text = "%.2f".format(baseAmount)
        DollarSignTPP.text = "$"
        DollarSignBA.text = "$"
        DollarSignTA.text = "$"
    }
}