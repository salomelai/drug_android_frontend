package com.junting.drug_android_frontend

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException


class WelcomePageActivity : AppCompatActivity() {

    private lateinit var mSlideViewPager: ViewPager
    private lateinit var mDotLayout: LinearLayout
    private lateinit var backbtn: Button
    private lateinit var nextbtn:Button
    private lateinit var changebtn:Button
    private lateinit var dots: Array<TextView>
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    var btnText = intArrayOf(
        R.string.btn_one,
        R.string.btn_one,
        R.string.btn_two
    )

//  google one tap
    private lateinit var oneTapClient: SignInClient
    private lateinit var signUpRequest: BeginSignInRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)

        backbtn = findViewById<Button>(R.id.wel_backBtn)
        nextbtn = findViewById<Button>(R.id.wel_nextBtn)
        changebtn = findViewById<Button>(R.id.wel_changeBtn)

        mSlideViewPager = findViewById<ViewPager>(R.id.wel_slideViewPager)
        mDotLayout = findViewById<LinearLayout>(R.id.wel_indicator_layout)

        viewPagerAdapter = ViewPagerAdapter(this)

        mSlideViewPager.adapter = viewPagerAdapter

        setUpindicator(0)
        mSlideViewPager.addOnPageChangeListener(viewListener)


        backbtn.setOnClickListener {
            if (getitem(0) > 0) {
                mSlideViewPager.setCurrentItem(getitem(-1), true)
            }
        }

        nextbtn.setOnClickListener {
            if (getitem(0) < 3) {
                mSlideViewPager.setCurrentItem(getitem(1), true)
            } else {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }
//      google one tap start
        oneTapClient = Identity.getSignInClient(this)
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true) // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id)) // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
        val activityResultLauncher = registerForActivityResult<IntentSenderRequest, ActivityResult>(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    val idToken = credential.googleIdToken
                    if (idToken != null) {
                        val receiveIntent = Intent(this, MainActivity::class.java)
                        receiveIntent.putExtra("googleToken", idToken)
                        startActivity(receiveIntent)
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }
        }
//      google one tap end

        changebtn.setOnClickListener {
            if (getitem(0) ==1) {
                oneTapClient.beginSignIn(signUpRequest)
                    .addOnSuccessListener(
                        this
                    ) { result ->
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                        activityResultLauncher.launch(intentSenderRequest)
                    }
                    .addOnFailureListener(
                        this
                    ) { e -> // No Google Accounts found. Just continue presenting the signed-out UI.
                        Log.d("TAG", e.localizedMessage)
                    }
            }
            else if (getitem(0) ==2){

            }
        }


    }

    fun setUpindicator(position: Int) {
        dots = Array<TextView>(3) { TextView(this) }
        mDotLayout.removeAllViews()

        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i].text = HtmlCompat.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_LEGACY)
            dots[i].textSize = 35f
            dots[i].setTextColor(getColor(R.color.inactive))
            mDotLayout.addView(dots[i])
        }

        dots[position].setTextColor(getColor(R.color.active))
    }



    var viewListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            setUpindicator(position)
            if (position == 0) {
                backbtn.visibility = View.INVISIBLE
                changebtn.visibility = View.INVISIBLE

            }
            else if(position == 1){
                backbtn.visibility = View.VISIBLE
                changebtn.visibility = View.VISIBLE
                changebtn.setText(btnText[position])

            }
            else if(position == 2){
                backbtn.visibility = View.VISIBLE
                changebtn.visibility = View.VISIBLE
                changebtn.setText(btnText[position])

            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    private fun getitem(i: Int): Int {
        return mSlideViewPager.currentItem + i
    }
}