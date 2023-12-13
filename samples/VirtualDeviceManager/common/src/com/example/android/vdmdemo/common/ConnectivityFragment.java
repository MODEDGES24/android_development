/*
 * Copyright (C) 2023 The Android Open Source Project
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

package com.example.android.vdmdemo.common;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import dagger.hilt.android.AndroidEntryPoint;

import java.util.Objects;

import javax.inject.Inject;

/** Fragment that holds the connectivity status UI. */
@AndroidEntryPoint(Fragment.class)
public final class ConnectivityFragment extends Hilt_ConnectivityFragment {

    @Inject ConnectionManager mConnectionManager;

    private TextView mStatus = null;
    private int mDefaultBackgroundColor;

    private final ConnectionManager.ConnectionCallback mConnectionCallback =
            new ConnectionManager.ConnectionCallback() {
                @Override
                public void onConnecting(String remoteDeviceName) {
                    updateStatus(mDefaultBackgroundColor, R.string.connecting, remoteDeviceName);
                }

                @Override
                public void onConnected(String remoteDeviceName) {
                    updateStatus(Color.GREEN, R.string.connected, remoteDeviceName);
                }

                @Override
                public void onDisconnected() {
                    updateStatus(mDefaultBackgroundColor, R.string.disconnected);
                }

                @Override
                public void onError(String message) {
                    updateStatus(Color.RED, R.string.error, message);
                }
            };

    public ConnectivityFragment() {
        super(R.layout.connectivity_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);

        mStatus = Objects.requireNonNull(getActivity()).requireViewById(R.id.connection_status);

        TypedValue background = new TypedValue();
        getActivity()
                .getTheme()
                .resolveAttribute(android.R.attr.windowBackground, background, true);
        mDefaultBackgroundColor = background.isColorType() ? background.data : Color.WHITE;

        CharSequence currentTitle = getActivity().getTitle();
        String localEndpointId = ConnectionManager.getLocalEndpointId();
        String title = getActivity().getString(R.string.this_device, currentTitle, localEndpointId);
        getActivity().setTitle(title);

        ConnectionManager.ConnectionStatus connectionStatus =
                mConnectionManager.getConnectionStatus();
        if (connectionStatus.connected) {
            mConnectionCallback.onConnected(connectionStatus.remoteDeviceName);
        } else if (connectionStatus.remoteDeviceName != null) {
            mConnectionCallback.onConnecting(connectionStatus.remoteDeviceName);
        } else {
            mConnectionCallback.onDisconnected();
        }

        mConnectionManager.addConnectionCallback(mConnectionCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mConnectionManager.removeConnectionCallback(mConnectionCallback);
    }

    private void updateStatus(int backgroundColor, int resId, Object... formatArgs) {
        Activity activity = Objects.requireNonNull(getActivity());
        activity.runOnUiThread(() -> {
            mStatus.setText(activity.getString(resId, formatArgs));
            mStatus.setBackgroundColor(backgroundColor);
        });
    }
}
