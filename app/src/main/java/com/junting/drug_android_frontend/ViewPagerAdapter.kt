package com.junting.drug_android_frontend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient


class ViewPagerAdapter(var context: Context) : PagerAdapter() {

//  google one tap
    lateinit var oneTapClient: SignInClient
    lateinit var signUpRequest: BeginSignInRequest

    var images = intArrayOf(
        R.drawable.app_image,
        R.drawable.app_image,
        R.drawable.app_image
    )
    var headings = intArrayOf(
        R.string.heading_one,
        R.string.heading_two,
        R.string.heading_three
    )
    var description = intArrayOf(
        R.string.description_one,
        R.string.description_two,
        R.string.description_three
    )
//    var btnText = intArrayOf(
//        R.string.btn_one,
//        R.string.btn_one,
//        R.string.btn_two
//    )

    override fun getCount(): Int {
//      want to know how many layout to present
        return headings.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.slider_layout, container, false)
        val slidetitleimage = view.findViewById<View>(R.id.wel_titleImage) as ImageView
        val slideHeading = view.findViewById<View>(R.id.wel_textTitle) as TextView
        val slideDescription = view.findViewById<View>(R.id.wel_textDescription) as TextView
//        val slideBtn = view.findViewById<View>(R.id.wel_changeBtn) as Button
        slidetitleimage.setImageResource(images[position])
        slideHeading.setText(headings[position])
        slideDescription.setText(description[position])
//        slideBtn.setText(btnText[position])
//        if (position == 0) {
//            slideBtn.visibility = View.INVISIBLE
//        }
//
////      google one tap page
//        else if (position == 1) {
//            slideBtn.visibility = View.VISIBLE
//
//        }
//
////      drug box pairing page
//        else {
//            slideBtn.visibility = View.VISIBLE
//        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}