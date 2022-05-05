package com.nextcloud.talk.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nextcloud.talk.R;
import com.nextcloud.talk.activities.CallActivity;
import com.nextcloud.talk.utils.DisplayUtils;

import org.webrtc.MediaStream;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.Map;

public class ParticipantsAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private static final String TAG = "ParticipantsAdapter";

    private final CallActivity mContext;
    private final ArrayList<ParticipantDisplayItem> participantDisplayItems;
    private final RelativeLayout gridViewWrapper;
    private final LinearLayout callInfosLinearLayout;
    private final int columns;
    private final boolean isVoiceOnlyCall;
    private final SurfaceViewRenderer focusVideoSurfaceView;
    private final GridView gridView;

    public ParticipantsAdapter(CallActivity mContext,
                               Map<String, ParticipantDisplayItem> participantDisplayItems,
                               RelativeLayout gridViewWrapper,
                               LinearLayout callInfosLinearLayout,
                               int columns,
                               boolean isVoiceOnlyCall,
                               SurfaceViewRenderer focusVideoSurfaceView,
                               GridView gridView) {
        this.mContext = mContext;
        this.gridViewWrapper = gridViewWrapper;
        this.callInfosLinearLayout = callInfosLinearLayout;
        this.columns = columns;
        this.isVoiceOnlyCall = isVoiceOnlyCall;

        this.participantDisplayItems = new ArrayList<>();
        this.participantDisplayItems.addAll(participantDisplayItems.values());

        this.focusVideoSurfaceView = focusVideoSurfaceView;
        this.gridView = gridView;
    }


    @Override
    public int getCount() {
        return participantDisplayItems.size();
    }

    @Override
    public ParticipantDisplayItem getItem(int position) {
        return participantDisplayItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ParticipantDisplayItem participantDisplayItem = getItem(position);

        SurfaceViewRenderer surfaceViewRenderer;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.call_item, parent, false);
            convertView.setVisibility(View.VISIBLE);

            surfaceViewRenderer = convertView.findViewById(R.id.surface_view);
            try {
                Log.d(TAG, "hasSurface: " + participantDisplayItem.getRootEglBase().hasSurface());

                surfaceViewRenderer.setMirror(false);
                surfaceViewRenderer.init(participantDisplayItem.getRootEglBase().getEglBaseContext(), null);
                surfaceViewRenderer.setZOrderMediaOverlay(false);
                // disabled because it causes some devices to crash
                surfaceViewRenderer.setEnableHardwareScaler(false);
                surfaceViewRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
            } catch (Exception e) {
                Log.e(TAG, "error while initializing surfaceViewRenderer", e);
            }
        } else {
            surfaceViewRenderer = convertView.findViewById(R.id.surface_view);
        }

        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        layoutParams.height = scaleGridViewItemHeight();
        convertView.setLayoutParams(layoutParams);

        TextView nickTextView = convertView.findViewById(R.id.peer_nick_text_view);
        SimpleDraweeView imageView = convertView.findViewById(R.id.avatarImageView);

        MediaStream mediaStream = participantDisplayItem.getMediaStream();
        if (hasVideoStream(participantDisplayItem, mediaStream)) {
            VideoTrack videoTrack = mediaStream.videoTracks.get(0);
            videoTrack.addSink(surfaceViewRenderer);
            imageView.setVisibility(View.INVISIBLE);
            surfaceViewRenderer.setVisibility(View.VISIBLE);
            nickTextView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            surfaceViewRenderer.setVisibility(View.INVISIBLE);

            if (mContext.isInPipMode) {
                nickTextView.setVisibility(View.GONE);
            } else {
                nickTextView.setVisibility(View.VISIBLE);
                nickTextView.setText(participantDisplayItem.getNick());
            }

            imageView.setController(null);
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setOldController(imageView.getController())
                    .setImageRequest(DisplayUtils.getImageRequestForUrl(participantDisplayItem.getUrlForAvatar(), null))
                    .build();
            imageView.setController(draweeController);
        }

        ImageView audioOffView = convertView.findViewById(R.id.remote_audio_off);
        if (!participantDisplayItem.isAudioEnabled()) {
            audioOffView.setVisibility(View.VISIBLE);
        } else {
            audioOffView.setVisibility(View.INVISIBLE);
        }

        return convertView;

    }

    private boolean hasVideoStream(ParticipantDisplayItem participantDisplayItem, MediaStream mediaStream) {
        return mediaStream != null && mediaStream.videoTracks != null && mediaStream.videoTracks.size() > 0 && participantDisplayItem.isStreamEnabled();
    }

    private int scaleGridViewItemHeight() {
        int headerHeight = 0;
        int callControlsHeight = 0;
        if (callInfosLinearLayout.getVisibility() == View.VISIBLE && isVoiceOnlyCall) {
            headerHeight = callInfosLinearLayout.getHeight();
        }
        if (isVoiceOnlyCall) {
            callControlsHeight = Math.round(mContext.getResources().getDimension(R.dimen.call_controls_height));
        }
        int itemHeight = (gridViewWrapper.getHeight() - headerHeight - callControlsHeight) / getRowsCount(getCount());
        int itemMinHeight = Math.round(mContext.getResources().getDimension(R.dimen.call_grid_item_min_height));
        if (itemHeight < itemMinHeight) {
            itemMinHeight = itemHeight;
        }
//
        return itemMinHeight;

//defining the display metrics
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        mContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//
//
//        float aspect_ratio = width / height;
//        float itemHeight = (1 / aspect_ratio ) * width;
//        return (int) itemHeight;
    }


    private int getRowsCount(int items) {
//        int rows = (int) Math.ceil((double) items / (double) columns);
        int rows = (int) Math.ceil((double) items / (double) columns);
        if (rows == 0) {
            rows = 1;
        }
        return rows;
    }

    // Module: Follow video
    public void handleFocusVideo(int position) {
//        View convertView;
//        ViewGroup parent;
        ParticipantDisplayItem participantDisplayItem = getItem(position);

        SurfaceViewRenderer surfaceViewRenderer;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.call_item, parent, false);
//            convertView.setVisibility(View.VISIBLE);

//            surfaceViewRenderer = convertView.findViewById(R.id.surface_view);
        surfaceViewRenderer = focusVideoSurfaceView;
        surfaceViewRenderer.setVisibility(View.VISIBLE);

        gridView.setVisibility(View.GONE);
        try {
            Log.d(TAG, "handlefocusvideo " + participantDisplayItem.getRootEglBase().hasSurface());

            surfaceViewRenderer.setMirror(false);
            surfaceViewRenderer.init(participantDisplayItem.getRootEglBase().getEglBaseContext(), null);
            surfaceViewRenderer.setZOrderMediaOverlay(false);
            // disabled because it causes some devices to crash
            surfaceViewRenderer.setEnableHardwareScaler(false);
            surfaceViewRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
        } catch (Exception e) {
            Log.e(TAG, "error while initializing surfaceViewRenderer", e);
        }
//        } else {
//            surfaceViewRenderer = convertView.findViewById(R.id.surface_view);
//        }

        //adding the scale to the surface view renderer by getting parameters
        ViewGroup.LayoutParams layoutParams = surfaceViewRenderer.getLayoutParams();
         layoutParams.height = scaleGridViewItemHeight();
        layoutParams.height = 300;



//        TextView nickTextView = convertView.findViewById(R.id.peer_nick_text_view);
//        SimpleDraweeView imageView = convertView.findViewById(R.id.avatarImageView);
//
//        MediaStream mediaStream = participantDisplayItem.getMediaStream();
//        if (hasVideoStream(participantDisplayItem, mediaStream)) {
//            VideoTrack videoTrack = mediaStream.videoTracks.get(0);
//            videoTrack.addSink(surfaceViewRenderer);
//            imageView.setVisibility(View.INVISIBLE);
//            surfaceViewRenderer.setVisibility(View.VISIBLE);
//            nickTextView.setVisibility(View.GONE);
//        } else {
//            imageView.setVisibility(View.VISIBLE);
//            surfaceViewRenderer.setVisibility(View.INVISIBLE);
//
//            if (((CallActivity) mContext).isInPipMode) {
//                nickTextView.setVisibility(View.GONE);
//            } else {
//                nickTextView.setVisibility(View.VISIBLE);
//                nickTextView.setText(participantDisplayItem.getNick());
//            }
//
//            imageView.setController(null);
//            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
//                .setOldController(imageView.getController())
//                .setImageRequest(DisplayUtils.getImageRequestForUrl(participantDisplayItem.getUrlForAvatar(), null))
//                .build();
//            imageView.setController(draweeController);
//        }
//
//        ImageView audioOffView = convertView.findViewById(R.id.remote_audio_off);
//        if (!participantDisplayItem.isAudioEnabled()) {
//            audioOffView.setVisibility(View.VISIBLE);
//        } else {
//            audioOffView.setVisibility(View.INVISIBLE);
//        }

//        return convertView;

    }

    // Module: END Follow video

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        handleFocusVideo(position);
    }
}
