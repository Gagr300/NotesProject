package com.notesproject.fragments

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.notesproject.LoginActivity
import com.notesproject.R


class SettingsFragmentPro (): PreferenceFragmentCompat() {

    private lateinit var auth: FirebaseAuth
    //private val btnSignOut = findPreference<Preference>(getString(R.string.sign_out))

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    lateinit var notifManager: NotificationManager
    lateinit var audioManager: AudioManager

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        auth = FirebaseAuth.getInstance()
        when (preference.key) {
            context?.getString(R.string.pref_sign_out) -> {
                updateUI()
            }
            context?.getString(R.string.pref_notifications) -> {
                audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                var currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                val sharedPreferences = activity?.getSharedPreferences(getString(R.string.pref_volume_notifications), Context.MODE_PRIVATE)
                sharedPreferences?.getInt("@string/pref_volume_notifications", 0)?.let {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, it, 0)
                }

            }
            context?.getString(R.string.pref_volume_notifications) -> {
                notifManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notifManager.cancelAll()
            }
        }
        return true
    }


    private fun updateUI() {
        val intent = Intent(getActivity(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}