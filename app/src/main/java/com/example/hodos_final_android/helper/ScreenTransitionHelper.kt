package com.example.hodos_final_android.helper

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.hodos_final_android.R

object ScreenTransitionHelper {

    fun startActivityWithAnimationBottomToTop(currentActivity: Activity, targetActivity: Class<*>) {
        val intent = Intent(currentActivity, targetActivity)
        currentActivity.startActivity(intent)
        currentActivity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
    }
    fun startActivityWithAnimationRightToLeft(currentActivity: Activity, targetActivity: Class<*>) {
        val intent = Intent(currentActivity, targetActivity)
        currentActivity.startActivity(intent)
        currentActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    fun startActivityWithAnimationLeftToRight(currentActivity: Activity, targetActivity: Class<*>) {
        val intent = Intent(currentActivity, targetActivity)
        currentActivity.startActivity(intent)
        currentActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun finishActivityWithAnimationRightToLeft(activity: Activity) {
        activity.finish()
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    fun finishActivityWithAnimationLeftToRight(activity: Activity) {
        activity.finish()
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }


    fun finishActivityWithAnimation(activity: Activity) {
        activity.finish()
        activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
    }


    fun replaceFragmentWithAnimation(
        activity: FragmentActivity,
        fragment: Fragment,
        containerId: Int
    ) {
        val fragmentManager: FragmentManager = activity.supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        transaction.replace(containerId, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
