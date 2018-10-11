package com.culture.santos.adapter

import com.culture.santos.culture.MapsActivity
import com.culture.santos.culture.R
import com.github.amlcurran.showcaseview.ShowcaseView
import com.github.amlcurran.showcaseview.targets.ViewTarget

/**
 * Created by Ricar on 06/10/2016.
 */
class TutorialAdapter(private val ctx: MapsActivity) {
    private val ADD_EVENT_TITLE = "Adicionando um evento."
    private val ADD_EVENT_CONTENT_TEXT = "Clique no ponto no mapa aonde você deseja adicionar o Evento."

    internal var mapViewTarget: ViewTarget
    internal var auxShowAddEventTutorialShowcaseView: ShowcaseView? = null

    init {
        mapViewTarget = ViewTarget(R.id.map, ctx)
    }

    fun showAddEventTutorial() {
        auxShowAddEventTutorialShowcaseView = ShowcaseView.Builder(ctx)
                .setTarget(mapViewTarget)
                .setContentTitle(ADD_EVENT_TITLE)
                .setContentText(ADD_EVENT_CONTENT_TEXT)
                .hideOnTouchOutside()
                .build()

        auxShowAddEventTutorialShowcaseView!!.hideButton()
    }

    fun hideAddEventTutorial() {
        if (auxShowAddEventTutorialShowcaseView != null) auxShowAddEventTutorialShowcaseView!!.hide()
    }
}
