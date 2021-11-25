package com.project.utils.ui

import android.content.Context
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty


class SharedPreferencesDelegate<T>(appContext: Context, val settingsType: String){

    companion object {
        val APP_PREFERENCES = "mysettings"
        val APP_PREFERENCES_LAST_WORD = "lastword" // последнее искомое слово
    }

    val sharedPreferences = appContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()


    operator fun getValue(thisRef: T?, property: KProperty<*>): T? {
        var value: T? = null
        if(!sharedPreferences.contains(settingsType)) println("No such settings is Shared Preferences")
        else when (settingsType){
            APP_PREFERENCES_LAST_WORD -> {
                value = sharedPreferences.getString(
                    APP_PREFERENCES_LAST_WORD, "") as T
            }
        }
        return value
    }

    operator fun setValue(
        thisRef: T?,
        property: KProperty<*>,
        value: T?) {
        when (value){
            is String -> {
                editor.putString(settingsType, value as String)
                editor.apply()
            }
        }
    }
}

fun <String> Fragment.getLastWord(): SharedPreferencesDelegate<kotlin.String>{
    return SharedPreferencesDelegate(this.requireActivity(), SharedPreferencesDelegate.APP_PREFERENCES_LAST_WORD)
}

fun Fragment.saveLastWord(word: String){
    var lastWord by SharedPreferencesDelegate<String>(this.requireActivity(), SharedPreferencesDelegate.APP_PREFERENCES_LAST_WORD)
    lastWord = word
}