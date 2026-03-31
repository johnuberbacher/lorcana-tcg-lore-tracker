package com.example.lorcanatcgloretracker.complication

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.example.lorcanatcgloretracker.R
import com.example.lorcanatcgloretracker.presentation.MainActivity

class MainComplicationService : SuspendingComplicationDataSourceService() {

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        if (type != ComplicationType.SHORT_TEXT) return null
        return buildComplicationData(tapAction = null)
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {
        return buildComplicationData(tapAction = createLaunchIntent())
    }

    private fun createLaunchIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun buildComplicationData(tapAction: PendingIntent?) =
        ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder("Lore").build(),
            contentDescription = PlainComplicationText.Builder("Open Lorcana Lore Tracker").build()
        ).apply {
            setMonochromaticImage(
                MonochromaticImage.Builder(
                    Icon.createWithResource(this@MainComplicationService, R.drawable.lore_gold)
                ).build()
            )
            tapAction?.let { setTapAction(it) }
        }.build()
}
