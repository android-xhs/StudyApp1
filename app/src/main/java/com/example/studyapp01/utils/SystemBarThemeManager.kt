package com.example.studyapp01.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.example.studyapp01.R

object SystemBarThemeManager {
    /**
     * 根据主题设置系统栏（推荐使用）
     * @param isNightMode 是否为夜间模式
     * @param edgeToEdge 是否启用边到边显示
     */
    fun applySystemBarTheme(
        activity: Activity,
        isNightMode: Boolean,
        edgeToEdge: Boolean = false
    ) {
        val window = activity.window
        val context = activity

        // 1. 获取对应主题的颜色
        val themeColors = getThemeColors(context, isNightMode, edgeToEdge)

        // 2. 设置背景颜色（兼容性处理）
        setBackgroundColors(window, themeColors)

        // 3. 设置图标外观（亮色/暗色）
        setIconsAppearance(window, isNightMode)

        // 4. 配置边到边显示
        if (edgeToEdge) {
            setupEdgeToEdge(window, themeColors.isTransparent)
        }
    }

    /**
     * 获取主题相关颜色
     */
    private fun getThemeColors(
        context: Context,
        isNightMode: Boolean,
        edgeToEdge: Boolean
    ): ThemeColors {
        return if (edgeToEdge) {
            // 边到边模式：使用透明或半透明颜色
            ThemeColors(
                statusBarColor = Color.TRANSPARENT,
                navBarColor = Color.TRANSPARENT,
                isTransparent = true
            )
        } else {
            // 普通模式：根据主题选择颜色
            val statusBarColor = if (isNightMode) {
                ContextCompat.getColor(context, R.color.main_bg_color)
            } else {
                ContextCompat.getColor(context, R.color.main_bg_color)
            }

            val navBarColor = if (isNightMode) {
                ContextCompat.getColor(context, R.color.main_bg_color)
            } else {
                ContextCompat.getColor(context, R.color.main_bg_color)
            }

            ThemeColors(statusBarColor, navBarColor, false)
        }
    }

    /**
     * 设置背景颜色
     */
    private fun setBackgroundColors(window: Window, colors: ThemeColors) {
        // 使用传统的 window 方法设置颜色（为了兼容性）
        window.statusBarColor = colors.statusBarColor
        window.navigationBarColor = colors.navBarColor

        // 尝试使用新的 API（如果可用）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            setBackgroundColorsModern(window.insetsController, colors)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setBackgroundColorsModern(
        controller: WindowInsetsController?,
        colors: ThemeColors
    ) {
        controller?.let {
            // API 34+ 提供了更现代的方法
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                // 7. 设置导航栏分隔线颜色（如果有）
//                window.navigationBarDividerColor = colors.navigationBarDividerColor
                it.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        }
    }
}

/**
 * 设置系统栏图标外观
 */
private fun setIconsAppearance(window: Window, isNightMode: Boolean) {
    val controller = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController
    } else {
        ViewCompat.getWindowInsetsController(window.decorView)
    }

    controller?.let {
        // 根据背景颜色决定图标颜色
        val isLightBackground = !isNightMode // 假设浅色主题用浅色背景

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+：使用 WindowInsetsController
            setIconsAppearanceModern(it as WindowInsetsController, isLightBackground)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6.0-10：使用 systemUiVisibility
            setIconsAppearanceLegacy(window, isLightBackground)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
private fun setIconsAppearanceModern(
    controller: WindowInsetsController,
    isLightBackground: Boolean
) {
    // 设置状态栏图标
    val statusBarAppearance = if (isLightBackground) {
        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
    } else {
        0
    }

    // 设置导航栏图标
    val navBarAppearance = if (isLightBackground) {
        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
    } else {
        0
    }

    // 应用设置
    controller.setSystemBarsAppearance(
        statusBarAppearance,
        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
    )

    controller.setSystemBarsAppearance(
        navBarAppearance,
        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
    )
}

private fun setIconsAppearanceLegacy(window: Window, isLightBackground: Boolean) {
    var systemUiVisibility = window.decorView.systemUiVisibility

    // 状态栏图标（Android 6.0+）
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        systemUiVisibility = if (isLightBackground) {
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }

    // 导航栏图标（Android 8.0+）
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        systemUiVisibility = if (isLightBackground) {
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        }
    }

    window.decorView.systemUiVisibility = systemUiVisibility
}

/**
 * 配置边到边显示
 */
private fun setupEdgeToEdge(window: Window, transparent: Boolean) {
    val decorView = window.decorView

    // 让内容延伸到系统栏下方
    var systemUiVisibility = decorView.systemUiVisibility
    systemUiVisibility = systemUiVisibility or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        systemUiVisibility = systemUiVisibility or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    decorView.systemUiVisibility = systemUiVisibility

    // 设置透明系统栏
    if (transparent) {
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
    }

    // 配置系统栏行为
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.systemBarsBehavior =
            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

/**
 * 获取当前系统栏配置
 */
@RequiresApi(Build.VERSION_CODES.R)
fun getCurrentSystemBarAppearance(window: Window): SystemBarAppearance {
    val controller = window.insetsController ?: return SystemBarAppearance()

    return SystemBarAppearance(
        statusBarColor = window.statusBarColor,
        navBarColor = window.navigationBarColor,
        isLightStatusBar = controller.systemBarsAppearance and
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS != 0,
        isLightNavBar = controller.systemBarsAppearance and
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS != 0
    )
}

data class ThemeColors(
    val statusBarColor: Int,
    val navBarColor: Int,
    val isTransparent: Boolean
)

data class SystemBarAppearance(
    val statusBarColor: Int = Color.TRANSPARENT,
    val navBarColor: Int = Color.TRANSPARENT,
    val isLightStatusBar: Boolean = false,
    val isLightNavBar: Boolean = false
)