package com.bluevolume.wearlorcanaloretracker.complication

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.bluevolume.wearlorcanaloretracker.R
import com.bluevolume.wearlorcanaloretracker.presentation.MainActivity

class MainComplicationService : SuspendingComplicationDataSourceService() {

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        if (type != ComplicationType.SMALL_IMAGE) return null
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
        SmallImageComplicationData.Builder(
            smallImage = SmallImage.Builder(
                image = Icon.createWithResource(this, R.mipmap.ic_launcher_foreground),
                type = SmallImageType.PHOTO
            ).build(),
            contentDescription = PlainComplicationText.Builder("Open Lorcana Lore Tracker").build()
        ).apply {
            tapAction?.let { setTapAction(it) }
        }.build()
}
