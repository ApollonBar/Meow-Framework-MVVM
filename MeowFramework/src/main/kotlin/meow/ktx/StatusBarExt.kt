@file:Suppress("unused")

/*
 * Copyright (C) 2020 Hamidreza Etebarian & Ali Modares.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package meow.ktx

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.etebarian.meowframework.R
import meow.controller
import meow.core.ui.MeowActivity
import meow.core.ui.MeowFragment

/**
 * Status Bar Extensions.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-21
 */

@TargetApi(Build.VERSION_CODES.M)
fun MeowActivity<*>.updateStatusBarByTheme(
    isDarkIcon: Boolean = !controller.isNightMode
) = avoidException {
    sdkNeed(Build.VERSION_CODES.M) {
        var flags = window.decorView.systemUiVisibility
        flags = if (isDarkIcon) {
            flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.decorView.systemUiVisibility = flags
        window.statusBarColor = getColorCompat(R.color.meow_status_bar)
    }

    setMIUIStatusBarDarkIcon(isDarkIcon)
    setMeizuStatusBarDarkIcon(isDarkIcon)
}

fun MeowActivity<*>.updateNavigationBarColorByTheme(
    color: Int = getColorCompat(R.color.meow_nav_bar)
) = avoidException {
    sdkNeed(21) {
        window.navigationBarColor = color
    }
}

@TargetApi(Build.VERSION_CODES.M)
fun MeowFragment<*>.updateStatusBarByTheme(
    isDarkIcon: Boolean
) = activity().updateStatusBarByTheme(isDarkIcon)

@SuppressLint("PrivateApi")
private fun MeowActivity<*>.setMIUIStatusBarDarkIcon(darkIcon: Boolean) = avoidException(false) {
    val clazz: Class<out Window?> = window.javaClass
    val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
    val field =
        layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
    val darkModeFlag = field.getInt(layoutParams)
    val extraFlagField = clazz.getMethod(
        "setExtraFlags",
        Int::class.javaPrimitiveType,
        Int::class.javaPrimitiveType
    )
    extraFlagField.invoke(window, if (darkIcon) darkModeFlag else 0, darkModeFlag)
}

private fun MeowActivity<*>.setMeizuStatusBarDarkIcon(darkIcon: Boolean) =
    avoidException(false) {
        val lp = window.attributes
        val darkFlag =
            WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
        val meizuFlags =
            WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
        darkFlag.isAccessible = true
        meizuFlags.isAccessible = true
        val bit = darkFlag.getInt(null)
        var value = meizuFlags.getInt(lp)
        value = if (darkIcon) {
            value or bit
        } else {
            value and bit.inv()
        }
        meizuFlags.setInt(lp, value)
        window.attributes = lp
    }

fun Window.setStatusBackgroundColor(color: Int) {
    sdkNeed(21) {
        statusBarColor = color
    }
}