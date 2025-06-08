package com.futo.platformplayer.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.futo.platformplayer.R
import com.futo.platformplayer.Settings
import com.futo.platformplayer.api.media.PlatformID
import com.futo.platformplayer.constructs.Event0
import com.futo.platformplayer.constructs.Event1
import com.futo.platformplayer.constructs.TaskHandler
import com.futo.platformplayer.dp
import com.futo.platformplayer.logging.Logger
import com.futo.platformplayer.models.Subscription
import com.futo.platformplayer.selectBestImage
import com.futo.platformplayer.states.StateApp
import com.futo.platformplayer.toHumanTimeIndicator
import com.futo.platformplayer.views.others.CreatorThumbnail
import com.futo.platformplayer.views.platform.PlatformIndicator
import com.futo.polycentric.core.toURLInfoSystemLinkUrl

class SubscriptionViewHolder : ViewHolder {
    private val _layoutSubscription: LinearLayout;
    private val _textName: TextView;
    private val _creatorThumbnail: CreatorThumbnail;
    private val _buttonTrash: ImageButton;
    private val _buttonSettings: ImageButton;
    private val _platformIndicator : PlatformIndicator;
    private val _textMeta: TextView;

    var subscription: Subscription? = null
        private set;

    var onClick = Event1<Subscription>();
    var onTrash = Event0();
    var onSettings = Event1<Subscription>();

    constructor(viewGroup: ViewGroup) : super(LayoutInflater.from(viewGroup.context).inflate(R.layout.list_subscription, viewGroup, false)) {
        _layoutSubscription = itemView.findViewById(R.id.layout_subscription);
        _textName = itemView.findViewById(R.id.text_name);
        _textMeta = itemView.findViewById(R.id.text_meta);
        _creatorThumbnail = itemView.findViewById(R.id.creator_thumbnail);
        _buttonTrash = itemView.findViewById(R.id.button_trash);
        _buttonSettings = itemView.findViewById(R.id.button_settings);
        _platformIndicator = itemView.findViewById(R.id.platform);

        _layoutSubscription.setOnClickListener {
            val sub = subscription;
            if (sub != null) {
                onClick.emit(sub);
            }
        };

        _buttonTrash.setOnClickListener {
            onTrash.emit();
        };
        _buttonSettings.setOnClickListener {
            subscription?.let {
                onSettings.emit(it);
            };
        }
    }

    fun bind(sub: Subscription) {
        this.subscription = sub;

        _creatorThumbnail.setThumbnail(sub.channel.thumbnail, false);
        _textName.text = sub.channel.name;
        bindViewMetrics(sub);
        _platformIndicator.setPlatformFromClientID(sub.channel.id.pluginId);
    }

    fun bindViewMetrics(sub: Subscription?) {
        if(sub == null || !Settings.instance.subscriptions.showWatchMetrics)
            _textMeta.text = "";
        else
            _textMeta.text = listOf(
                if(sub.playbackViews > 0) "${sub.playbackViews} view" + (if(sub.playbackViews > 1) "s" else "") else null,
                if(sub.playbackSeconds > 0) sub.playbackSeconds.toHumanTimeIndicator() else null
            ).filterNotNull().joinToString(" · ");
    }

    companion object {
        private const val TAG = "SubscriptionViewHolder"
    }
}