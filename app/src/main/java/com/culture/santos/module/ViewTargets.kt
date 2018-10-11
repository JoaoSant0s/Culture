package com.culture.santos.module

import android.support.v7.widget.Toolbar
import android.view.View

import com.github.amlcurran.showcaseview.targets.ViewTarget


/**
 * A collection of not-officially supported ViewTargets. Use them at your own risk!
 */
object ViewTargets {

    /*
    *   Copyright and Licensing
    *   Copyright Alex Curran (@amlcurran) © 2012-2014. All rights reserved.
    *   This library is distributed under an Apache 2.0 License.
    *   https://github.com/amlcurran/ShowcaseView
    */

    /**
     * Highlight the navigation button (the Up or Navigation drawer button) in a Toolbar
     * @param toolbar The toolbar to search for the view in
     * @return the [ViewTarget] to supply to a [com.github.amlcurran.showcaseview.ShowcaseView]
     * @throws MissingViewException when the view couldn't be found. Raise an issue on Github if you get this!
     */
    @Throws(ViewTargets.MissingViewException::class)
    fun navigationButtonViewTarget(toolbar: Toolbar): ViewTarget {
        try {
            val field = Toolbar::class.java.getDeclaredField("mNavButtonView")
            field.isAccessible = true
            val navigationView = field.get(toolbar) as View
            return ViewTarget(navigationView)
        } catch (e: NoSuchFieldException) {
            throw MissingViewException(e)
        } catch (e: IllegalAccessException) {
            throw MissingViewException(e)
        }

    }

    class MissingViewException(throwable: Throwable) : Exception(throwable)
}